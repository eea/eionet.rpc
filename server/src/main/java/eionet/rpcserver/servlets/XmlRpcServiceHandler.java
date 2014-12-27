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
 * The Original Code is "EINRC-5 / UIT project".
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

import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;

import org.apache.xmlrpc.XmlRpcHandler;
import org.apache.xmlrpc.AuthenticatedXmlRpcHandler;

import eionet.rpcserver.ServiceException;
import eionet.rpcserver.UITServiceRoster;
import eionet.rpcserver.UITServiceIF;
import eionet.rpcserver.UITMethodIF;

import java.util.Vector;
import eionet.acl.AccessController;
import eionet.acl.SignOnException;
//import eionet.rpcserver.UITUser;
import eionet.acl.AppUser;

import java.io.*;

/**
 * Class handles a service for the XML/Rpc protocol.
 */
class XmlRpcServiceHandler   implements AuthenticatedXmlRpcHandler  {

    private String _srvName ;

    protected XmlRpcServiceHandler() {}

    XmlRpcServiceHandler(String srvName) {
        _srvName = srvName;
    }

    /**
     * @param method
     * @param parameters Handler input parameters.
     * @return The value
     */
    public Object execute(String methodName, Vector parameters)  throws Exception    {

        //value of method
        Object value = null;
        //System.out.println("========== execute !!!!!!!!!!!!!!!!!!! " + methodName);
        try {

            //get Service
            UITServiceIF srv = UITServiceRoster.getService(_srvName);

            //System.out.println("========== service ok " + _srvName);

            if (srv == null)
                throw new ServiceException("**No such service " + _srvName);

            //get Method
            UITMethodIF mthd = srv.getMethod(methodName);
            //System.out.println("========== mthd ok " + methodName);

            if (mthd == null) {
                //System.out.println("========== bad 1");
                throw new ServiceException("Method " + methodName + " does not exist in the deployment descriptor");
            }

            if (!mthd.isPublic() && UITServiceRoster.getUser()==null) {
                //System.out.println("========== bad 2");
                throw new ServiceException("Not authenticated");
            }


            try {
                //System.out.println("========== lets go to get value of" + methodName);
                value = mthd.getValue(parameters);
                //System.out.println("========== value ok " + methodName);

            } catch (ServiceException se ) {
                throw new ServiceException("Error, getting value for the method \n" +
                  se.toString());
            }

            if (value==null)
                throw new ServiceException("Method returned null");

        } catch (Exception e ) {
            throw new ServiceException(e,  e.toString());
        }

        return value;
    }

    public Object execute (String methodName, Vector parameters, String user, String pwd )   throws Exception  {

        if (user != null) {
            //System.out.println("========== user " + user);
            AppUser usr = new AppUser();
            usr.authenticate(user,pwd);
            UITServiceRoster.setUser(usr);
        } else
            UITServiceRoster.setUser(null);

        //System.out.println("========== execute !!!!!!!!!!!!!!!!!!! " + methodName);

        // JH150705 - Apache's XML-RPC 2.0 passes methodName like 'serviceName.methodName' while
        // as the older 1.1 passes it like simply 'methodName'. Since TE XML/RPC framework expects
        // the latter case, I'm removing the ''serviceName.' part by force
        int i = methodName==null ? -1 : methodName.indexOf('.');
        if (i >= 0 && i<(methodName.length()-1)) methodName = methodName.substring(i+1);

        return execute (methodName, parameters);

    }

}
