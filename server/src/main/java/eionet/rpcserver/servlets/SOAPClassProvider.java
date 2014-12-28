/*
 * The contents of this file are subject to the Mozilla Public
 * License Version 1.1 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of
 * the License at http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS
 * IS" basis, WITHOUT WARRANTY OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * rights and limitations under the License.
 *
 * The Original Code is "EINRC-6 / UIT project".
 *
 * The Initial Developer of the Original Code is TietoEnator.
 * The Original Code code was developed for the European
 * Environment Agency (EEA) under the IDA/EINRC framework contract.
 *
 * Copyright (C) 2000-2002 by European Environment Agency.  All
 * Rights Reserved.
 *
 * Original Code: Kaido Laine (TietoEnator)
 */

package eionet.rpcserver.servlets;


/**
 * Description: This class must be specified as user defined provider type
 * for all Java services that need to be deployed on Apache SOAP with user
 * authentication. <p> Instead of using the default Java provider, use this one
 * to provide a basic
 * authentication scheme and check against authorization rules.
 *  http://www.soapuser.com
 */


import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;


import org.apache.soap.util.Provider;
import org.apache.soap.*;
import org.apache.soap.rpc.*;
import org.apache.soap.server.*;
import org.apache.soap.encoding.soapenc.Base64;

import org.apache.soap.server.http.*;
import org.apache.soap.util.*;
import org.apache.soap.providers.*;
import eionet.rpcserver.UITServiceRoster;
import eionet.rpcserver.ServiceException;
import eionet.acl.AppUser;
import eionet.acl.SignOnException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


public class SOAPClassProvider implements Provider  {

    protected DeploymentDescriptor dd;
    protected Envelope             envelope;
    protected Call                 call;
    protected String               methodName;
    protected String               targetObjectURI;
    protected HttpServlet          servlet;
    protected HttpSession          session;

    protected Object               targetObject;

    public void locate(DeploymentDescriptor dd, Envelope env, Call call, String methodName,
                          String targetObjectURI,
                          SOAPContext reqContext) throws SOAPException {

        HttpServlet servlet = (HttpServlet) reqContext.getProperty(Constants.BAG_HTTPSERVLET);
        HttpSession session = (HttpSession) reqContext.getProperty(Constants.BAG_HTTPSESSION);
        HttpServletRequest rq = (HttpServletRequest) reqContext.getProperty(Constants.BAG_HTTPSERVLETREQUEST);

        //cut "urn:"
        String srvName = dd.getID().substring(4);

        boolean isPublic;

        try {
            isPublic = UITServiceRoster.getService(srvName).getMethod(methodName).isPublic();
        } catch (ServiceException se) {
            isPublic = false;
            //throw new SOAPException(Constants.FAULT_CODE_PROTOCOL, se.toString());
        }

        AppUser user = null;
        //if method is not public, perform the auth
        if (!isPublic) {
            String auth = rq.getHeader("Authorization");
            if (auth == null)
              throw new SOAPException(Constants.FAULT_CODE_PROTOCOL, "Not authenticated");

            auth = auth.substring(auth.indexOf(" "));
            // Decoding is as simple as this...
            String decoded = new String(Base64.decode(auth));

            // decoded now contains <<username:password>> in plain text.
            int i = decoded.indexOf(":");
            // so we take the username from it ( everything until the ':' )
            String username = decoded.substring(0, i);
            // and the password
            String pwd = decoded.substring(i + 1, decoded.length());


            user = new AppUser();
            try {
              user.authenticate(username, pwd);
            } catch (SignOnException se) {
                throw new SOAPException(Constants.FAULT_CODE_PROTOCOL, "Authentication failed");
            }

            UITServiceRoster.setUser(user);

            /*System.out.println("Request received from " + username);
            System.out.println("for service: " + srvName);
            System.out.println("for method: " + methodName); */

        }

        this.dd              = dd;
        this.envelope        = env;
        this.call            = call;
        this.methodName      = methodName;
        this.targetObjectURI = targetObjectURI;
        this.servlet         = servlet;
        this.session         = session;

        ServletConfig  config  = null;
        ServletContext context = null;

        if (servlet != null) config  = servlet.getServletConfig();
        if (config != null) context = config.getServletContext();


        ServiceManager serviceManager = ServerHTTPUtils.getServiceManagerFromContext(context);

          // Default processing for 'java' and 'script' providers
          // call on a valid method name?
        if (!RPCRouter.validCall(dd, call)) {
            throw new SOAPException(Constants.FAULT_CODE_SERVER,
                              "Method '" + call.getMethodName() + "' is not supported.");
        }
             // get at the target object
       targetObject = getTargetObject(serviceManager, dd, targetObjectURI, servlet, session,  reqContext, context, user);

    };


    public void invoke(SOAPContext reqContext, SOAPContext resContext) throws SOAPException {
        try {
            Response resp = RPCRouter.invoke(dd, call, targetObject, reqContext, resContext);
            Envelope env = resp.buildEnvelope();
            StringWriter  sw = new StringWriter();
            env.marshall(sw, call.getSOAPMappingRegistry(), resContext);
            resContext.setRootPart(sw.toString(), Constants.HEADERVAL_CONTENT_TYPE_UTF8);
        } catch (Exception e) {
            if (e instanceof SOAPException)
                throw (SOAPException) e;
            throw new SOAPException(Constants.FAULT_CODE_SERVER, e.toString());
        }
   }


    /**
     * Return the target object that services the service with the
     * given ID. Depending on the deployment information of the
     * service, the object's lifecycle is also managed here.
     * overwritten method because of AppUser constructor KL030312
     */
    private static Object getTargetObject(ServiceManager serviceManager,
                                 DeploymentDescriptor dd,
                                 String targetID,
                                 HttpServlet thisServlet,
                                 HttpSession session,
                                 SOAPContext ctxt,
                                 ServletContext context,
                                 AppUser user) throws SOAPException {


        final String SERVICE_MANAGER_ID = "serviceManager";
        final String SCRIPT_CLASS = "com.ibm.bsf.BSFManager";
        final String SCRIPT_INVOKER =  "org.apache.soap.server.InvokeBSF";
        final String SERVLET_CLASSLOADER = "servletClassLoader";


        int scope = dd.getScope();
        byte providerType = dd.getProviderType();
        String className;
        Object targetObject = null;
        if (providerType == DeploymentDescriptor.PROVIDER_JAVA
                || providerType == DeploymentDescriptor.PROVIDER_USER_DEFINED) {
            className = dd.getProviderClass();
        } else {
            // for scripts, we need a new BSF manager basically
            className = SCRIPT_CLASS;
        }

        // determine the scope and lock object to use to manage the lifecycle
        // of the service providing object
        Object scopeLock = null;
        if (scope == DeploymentDescriptor.SCOPE_REQUEST) {
            scopeLock = thisServlet; // no need to register .. create, use and dink
        } else if (scope == DeploymentDescriptor.SCOPE_SESSION) {
            scopeLock = session;
        } else if (scope == DeploymentDescriptor.SCOPE_APPLICATION) {
            scopeLock = context;
        } else {
            throw new SOAPException(Constants.FAULT_CODE_SERVER,
                                   "Service uses deprecated object scope "
                                   + "'page': inform provider of error");
        }

        // create the object if necessary
        boolean freshObject = false;

        // find the target object on which the requested method should
        // be invoked
        if (targetID.equals(ServerConstants.SERVICE_MANAGER_SERVICE_NAME)) {
          targetObject = serviceManager;
        } else {
            // locate (or create) the target object and invoke the method
            if (scopeLock == null) scopeLock = className; // Just pick something
            synchronized (scopeLock) {
                if (scopeLock == session) {
                    // targetObject = session.getAttribute(targetID);
                    targetObject = session.getValue(targetID);
                } else if (scopeLock == context) {
                    targetObject = context.getAttribute(targetID);
                } else {
                    targetObject = null;
                }
                if (targetObject == null) {
                    try {
                        Class c = ctxt.loadClass(className);

                        if (dd.getIsStatic()) {
                            targetObject = c;
                        } else {

                            if (user == null)
                                targetObject = c.newInstance();
                            else {
                               Class[] cl = new Class[1];
                               Object[] p = new Object[1];
                               p[0] = user;
                               cl[0] = user.getClass();
                               java.lang.reflect.Constructor constr = c.getConstructor(cl);
                               targetObject = constr.newInstance(p);
                            }

                        }
                        freshObject = true;

                        // remember the created instance if the scope is not REQUEST;
                        // in that case the object is to be thrown away after handling
                        // the request
                        if (scopeLock == session) {
                            session.putValue(targetID, targetObject);
                            // session.setAttribute(targetID, targetObject);
                        } else if (scopeLock == context) {
                            context.setAttribute(targetID, targetObject);
                        }
                    } catch (Exception e) {
                        String msg;
                        if (providerType == DeploymentDescriptor.PROVIDER_JAVA
                                || providerType == DeploymentDescriptor.PROVIDER_USER_DEFINED) {
                            msg = "Unable to resolve target object: " + e.getMessage();
                        } else {
                            msg = "Unable to load BSF: script services not available "
                                + "without BSF: " + e.getMessage();
                        }
                        throw new SOAPException(Constants.FAULT_CODE_SERVER_BAD_TARGET_OBJECT_URI, msg, e);
                    }
                }
            }
        }

        // if script provider type and first time to it, then load and
        // exec the script
        if ((providerType != DeploymentDescriptor.PROVIDER_JAVA
                && providerType != DeploymentDescriptor.PROVIDER_USER_DEFINED)
                && freshObject) {
            // find the class that provides the BSF services (done
            // this way via reflection to avoid a static dependency on BSF)
            Class bc = null;
            try {
                bc = ctxt.loadClass(SCRIPT_INVOKER);
            } catch (Exception e) {
                String msg = "Unable to load BSF invoker (" + SCRIPT_INVOKER + ")"
                    + ": script services not available without BSF: " + e.getMessage();
                throw new SOAPException(Constants.FAULT_CODE_SERVER, msg, e);
            }

            // get the script string to exec
            String script = dd.getScriptFilenameOrString();
            if (providerType == DeploymentDescriptor.PROVIDER_SCRIPT_FILE) {
                String fileName = context.getRealPath(script);
                try {
                    script = IOUtils.getStringFromReader(new FileReader(fileName));
                } catch (Exception e) {
                    String msg = "Unable to load script file (" + fileName + ")"
                        + ": " + e.getMessage();
                    throw new SOAPException(Constants.FAULT_CODE_SERVER, msg, e);
                }
            }

            // exec it
            Class[] sig = {DeploymentDescriptor.class,
                           Object.class,
                           String.class};
            try {
                Method m = MethodUtils.getMethod(bc, "init", sig, true);
                m.invoke(null, new Object[] {dd, targetObject, script});
            } catch (InvocationTargetException ite) {
                Throwable te = ite.getTargetException();
                if (te instanceof SOAPException)
                  throw (SOAPException) te;
                String msg = "Unable to invoke init method of script invoker: " + te;
                throw new SOAPException(Constants.FAULT_CODE_SERVER, msg, te);
            } catch (Exception e) {
                String msg = "Unable to invoke init method of script invoker: " + e;
                throw new SOAPException(Constants.FAULT_CODE_SERVER, msg, e);
            }
        }

        return targetObject;
    }

}
