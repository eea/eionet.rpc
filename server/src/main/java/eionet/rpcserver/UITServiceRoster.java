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
 * Copyright (C) 2000-2015 by European Environment Agency.  All
 * Rights Reserved.
 *
 * Original Code: Kaido Laine (TietoEnator)
 * Contributor: Søren Roug, European Environment Agency
 */

package eionet.rpcserver;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Properties;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.Binding;
import javax.naming.NamingException;

import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;

import eionet.definition.Service;
import eionet.definition.Services;
import eionet.acl.AppUser;

/**
 * Roster, containing all the services in the server.
 */
public class UITServiceRoster {

    public static final String RESOURCE_BUNDLE_NAME = "rpc";
    public static final String PROP_XMLRPC_ENCODING = "xmlrpc.encoding";
    public static final String TOMCAT_CONTEXT = "java:comp/env/";

    private static HashMap _services;

    private static AppUser _user;
    /** Location of an XML file to parse for a services API. */
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

    /**
     * Initialise services.
     */
    private static void init() throws ServiceException {
        if (_services == null) {
            _services = new HashMap();
            Hashtable<Object, Object> props = loadProperties();

            fileName = (String) props.get("services.definition.file");
            if (fileName != null && !fileName.equals("")) {
                loadServicesFile(fileName);
            }
            try {
                //acl admin & help admin services included
                boolean aclAdmin = false;

                try {
                    Object aclAdminCandidate = props.get("acl.admin");
                    if (aclAdminCandidate == null) {
                        aclAdmin = false;
                    } else if (aclAdminCandidate instanceof Boolean) {
                        aclAdmin = ((Boolean)aclAdminCandidate).booleanValue();
                    } else {
                        aclAdmin = Boolean.valueOf(aclAdminCandidate.toString()).booleanValue();
                    }
                } catch(Exception e) {
                    aclAdmin = false;
                }

                // load component services
                _services.putAll(new ComponentServices(props));

                // this to support the old hardcoded way of loading XService
                if (aclAdmin)
                    _services.put("XService", new XServiceImpl());

            } catch (Exception e) {
                throw new ServiceException(e, "Exception " + e.toString());
            }
        }
    }

    /**
     * Load the API description from a services file.
     * If the file name contains no "/" or "\", then load from class path.
     * Otherwise load directly from file system. This makes it possible
     * to store the file in the JAR.
     *
     * @param servicesFileName - The file name of the services file.
     * @throws ServiceException if it can't load the file.
     */
    private static void loadServicesFile(String servicesFileName) throws ServiceException {
        if (servicesFileName != null && !servicesFileName.equals("")) {
            try {
                Reader reader = null;
                if (servicesFileName.indexOf('/') >= 0 || servicesFileName.indexOf('\\') >= 0) {
                    File file = new File(servicesFileName);
                    reader = new FileReader(file);
                } else {
                    InputStream inStream = UITServiceRoster.class.getResourceAsStream("/" + servicesFileName);
                    if (inStream == null) {
                        throw new FileNotFoundException(servicesFileName + " not found");
                    }
                    reader = new InputStreamReader(inStream);
                }
                Services srvs = Services.unmarshal(reader);
                Service srv[] = srvs.getService();

                for (Service service : srv) {
                    String srvName = service.getName();

                    if (srvName != null) {
                        _services.put(service.getName(), new ServiceImpl(service));
                    }
                }
            } catch (FileNotFoundException fe) {
                throw new ServiceException(fe, "Services file " + servicesFileName + " not found.");
            } catch (MarshalException me) {
                throw new ServiceException(me, "Error reading services from file " + servicesFileName);
            } catch (ValidationException ve) {
                throw new ServiceException(ve, "Validation exception " + ve.toString());
            }
        }
    }

    /**
     * Load the configuration. First try JNDI, then fall back to property files.
     *
     * @return The properties in a hashtable.
     * @throws ServiceException if unable to load the properties.
     */
    public static Hashtable<Object, Object> loadProperties() throws ServiceException {
        Hashtable<Object, Object> props = new Hashtable<Object, Object>();
        try {
            Context initContext = new InitialContext();
            if (initContext != null) {
                // Load from JNDI. Tomcat puts its stuff under java:comp/env:
                for (Enumeration<Binding> e = initContext.listBindings(TOMCAT_CONTEXT + RESOURCE_BUNDLE_NAME); e.hasMoreElements();) {
                    Binding binding = e.nextElement();
                    props.put(binding.getName(), binding.getObject());
                }
            }
        } catch (NamingException mre) {
            //throw new ServiceException("JNDI not configured properly");
        }

        // Load from properties file
        if (props.size() == 0 || props.containsKey("propertiesfile")) {
            try {
                Properties fileProps = new Properties();
                InputStream inStream = null;

                if (props.containsKey("propertiesfile")) {
                    try {
                        inStream = new FileInputStream((String) props.get("propertiesfile"));
                    } catch (Exception e) {
                        throw new ServiceException("Properties file not found");
                    }
                } else {
                    inStream = UITServiceRoster.class.getResourceAsStream("/" + RESOURCE_BUNDLE_NAME + ".properties");
                    if (inStream == null) {
                        throw new ServiceException("Properties file is not found in the classpath");
                    }
                }
                fileProps.load(inStream);
                inStream.close();
                props.putAll(fileProps);
            } catch (IOException mre) {
                throw new ServiceException("Properties file is not readable");
            }
        }
        return props;
    }

    /**
     * Returns service by name.
     *
     * @param id - the name of the service.
     */
    public static UITServiceIF getService(String id) throws ServiceException {
        init();
        return (UITServiceIF) _services.get(id);
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

    /**
     * Reset the singleton object for testing.
     */
    static void reset() {
        _services = null;
    }
}
