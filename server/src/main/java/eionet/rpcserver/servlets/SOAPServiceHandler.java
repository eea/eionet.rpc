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

import org.apache.soap.server.DeploymentDescriptor;
import org.apache.soap.server.ServiceManager;
import org.apache.soap.server.http.ServerHTTPUtils;

import eionet.rpcserver.UITServiceRoster;
import eionet.rpcserver.UITServiceIF;
import eionet.rpcserver.ServiceException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import java.util.HashMap;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.ServletConfig;
import org.apache.soap.server.http.RPCRouterServlet;
import org.apache.soap.util.ConfigManager;

/**
 * Class handles services, described in an xml-file.
 */
class SOAPServiceHandler extends HttpServlet { //BaseServletAC  {

    /**
     * gets definitions from the ServicesRoster,
     * creates deployment descriptors.
     */
    public void init(ServletConfig config) throws ServletException {

        super.init(config);

        try {

            ServiceManager serviceManager =  ServerHTTPUtils.getServiceManagerFromContext(config.getServletContext());

            HashMap services = UITServiceRoster.getServices();

            for (Iterator iter = services.keySet().iterator(); iter.hasNext();) {
                DeploymentDescriptor dd = new DeploymentDescriptor();
                String srvName = (String) iter.next();

                UITServiceIF srv = (UITServiceIF) services.get(srvName);

                //urn prefix needed by the xml schema?
                dd.setID("urn:" + srvName);
                dd.setScope(dd.SCOPE_REQUEST);

                String[] methods = srv.getMethodNames();
                dd.setMethods(methods);

                dd.setProviderType(DeploymentDescriptor.PROVIDER_USER_DEFINED);
                dd.setServiceClass("eionet.rpcserver.servlets.SOAPClassProvider");

                //dd.setProviderType(DeploymentDescriptor.PROVIDER_JAVA);
                dd.setProviderClass(srv.getProvider());

                dd.setIsStatic(false);

                serviceManager.deploy(dd);
            }

        } catch (Exception e) {
            throw new ServletException(e);
        }

    }
}
