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


package eionet.rpcclient;

import java.util.HashMap;
import java.io.File;
import java.io.FileReader;
import java.io.FileNotFoundException;

import java.util.ResourceBundle;

public class ServiceClients {

    private static HashMap _services;
    private static String fileName;


    /**
     * Returns ServiceClient by the ServiceName and service URL.
     */
    public static ServiceClientIF getServiceClient(String srvName, String srvUrl) throws ServiceClientException {
      //default is XML/RPC
        return getServiceClient( srvName, srvUrl, ServiceClientIF.CLIENT_TYPE_XMLRPC);
    }

    /**
     * Returns XML/RPC client for the service
     * @param String srvName, String srvUrl, int type
     */
    public static ServiceClientIF getServiceClient(String srvName, String srvUrl, int type)
          throws ServiceClientException {
        ServiceClientIF client = null;
        if (type == ServiceClientIF.CLIENT_TYPE_XMLRPC)    
            client = new XmlRpcServiceClient(srvName, srvUrl);
        else if (type == ServiceClientIF.CLIENT_TYPE_SOAP)    
            client = new SoapServiceClient( srvName, srvUrl);
        else
            throw new ServiceClientException("Not such type implemented " + type);
        
        return client;
      
   }

    /*
    public static void main(String s[]) {
      String service = "WebRODService";
    String url = "http://rod.eionet.eu.int/rpcrouter";
    //int type = ServiceClientIF.CLIENT_TYPE_XMLRPC;
    int type = ServiceClientIF.CLIENT_TYPE_SOAP;  


   try {
      ServiceClientIF srv = ServiceClients.getServiceClient(service,url,type);
      String m = "getActivities";
      java.util.Vector prms = new     java.util.Vector();
      
      Object v = srv.getValue(m, prms);
      _log("** " + v);
    } catch (Exception e) {
      _log("** error " + e.toString());
    }
  }

   private static void _log(String s) {
      System.out.println("******************** " + s);
    }
    
    */

}
