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

package eionet.rpcserver;

import java.util.HashMap;
import java.util.ResourceBundle;

import java.io.File;
import java.io.FileReader;
import java.io.FileNotFoundException;

import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;

import eionet.definition.Service;
import eionet.definition.Services;
import eionet.acl.AppUser;

/**
 * Roster, containing all the services in the server.
 */
public class UITServiceRoster {

    // JH161205
    public static final String RESOURCE_BUNDLE_NAME = "uit";
    public static final String PROP_XMLRPC_ENCODING = "xmlrpc.encoding";

    private static HashMap _services;

    private static AppUser _user;
    private static String fileName = "";
    //static String rpcExecutePrm = "";

    /**
     * Security Context.
     */
    public static AppUser getUser() {
        return _user;
    }

    public static void setUser(AppUser user) {
        _user = user;
    }

    private static void init() throws ServiceException {
        if (_services == null) {
            _services = new HashMap();
            File file = null;
            try {

                ResourceBundle props = ResourceBundle.getBundle(RESOURCE_BUNDLE_NAME);

                //fileName=props.getString("services.definition.file");

                //acl admin & help admin services included
                boolean aclAdmin = false;
                try {
                    fileName = props.getString("services.definition.file");
                } catch(Exception e) {
                    //e.printStackTrace(System.out);
                }

                try {
                    aclAdmin = new Boolean(props.getString("acl.admin")).booleanValue();
                } catch(Exception e) {
                    //e.printStackTrace(System.out);
                }


                if (!fileName.equals("")) {
                    file = new File(fileName);

                    FileReader reader = new FileReader(file);
                    Services srvs = Services.unmarshal(reader);
                    Service srv[] = srvs.getService();

                    for (int i = 0; i < srv.length; i++) {
                        String srvName = srv[i].getName();

                        if (srvName != null) {
                            _services.put(srv[i].getName(), new ServiceImpl(srv[i]));
                        }
                    }
                }

                // load component services
                _services.putAll(new ComponentServices(props));

                // this to support the old hardcoded way of loading XService
                if (aclAdmin)
                    _services.put("XService", new XServiceImpl());


            } catch (FileNotFoundException fe) {
                fe.printStackTrace(System.out);
                throw new ServiceException(fe, "File " + file + " not found.");
            } catch (MarshalException me) {
                throw new ServiceException(me, "Error reading services from file " + file);
            } catch (ValidationException ve) {
                throw new ServiceException(ve, "Validation exception " + ve.toString());
            } catch (Exception e) {
                throw new ServiceException(e,"Exception " + e.toString());
            }
        }
    }

    /**
     * Returns service by name.
     */
    public static UITServiceIF getService(String id) throws ServiceException {
        init();
        return (UITServiceIF)_services.get(id);
    }

    /**
     * Returns all services.
     *
     * @return HashMap of services
     */
    public static HashMap getServices() throws ServiceException {
      init();
      return _services;
    }

}
