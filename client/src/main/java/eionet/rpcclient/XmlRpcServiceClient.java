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

import org.apache.xmlrpc.*;
import org.apache.xmlrpc.XmlRpcClient;
import org.apache.xmlrpc.XmlRpcClientLite;
import org.apache.xmlrpc.XmlRpcException;

import java.util.ResourceBundle;
import java.util.Vector;
import java.util.Hashtable;

import java.net.MalformedURLException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;


/**
 * Service Client implementation for the XML/RPC protocol.
 */
class XmlRpcServiceClient implements ServiceClientIF  {

    private static final String RESOURCE_BUNDLE_NAME = "uit";
    private static final String PROP_XMLRPC_ENCODING = "xmlrpc.encoding";

    //private static String rpcUrl; // = "http://localhost:8080/uit/rpcrouter/";
    private String srvName;
    private String rpcUrl;
    private XmlRpcClient rpcClient;


    XmlRpcServiceClient(String srvName, String srvUrl) throws ServiceClientException {
        this.srvName = srvName;
        this.rpcUrl = srvUrl;
        
        if (srvName==null) srvName = "";

        this.rpcClient = initRpcClient();
    }

    private XmlRpcClient initRpcClient() throws ServiceClientException {
        //XmlRpcClientLite xmlrpc = null;
        XmlRpcClient xmlrpc = null;
        try {
            xmlrpc = new XmlRpcClient(rpcUrl);
        } catch (MalformedURLException mfe) {
            throw new ServiceClientException("Bad URL " + rpcUrl);
        }
        return xmlrpc;
    }

    /**
     * Returns value of the method.
     */
    public Object getValue(String methodName, Vector params) throws ServiceClientException { 
        Object value = null;

        //remove me!
        if (rpcClient == null)
            throw new ServiceClientException("No Client "); 

        String methSrvName = srvName.length() != 0 ? srvName + "." : srvName;
	methSrvName = methSrvName + methodName;
	
	String encoding = null;
	try {
            ResourceBundle props = ResourceBundle.getBundle(RESOURCE_BUNDLE_NAME);
            if (props != null) encoding = props.getString(PROP_XMLRPC_ENCODING);
	} catch (Exception e) {
	}

        try {
            
            if (encoding!=null) XmlRpc.setEncoding("UTF-8");
            
            value = rpcClient.execute (methSrvName, params) ;
            
            // JH181205 - for some reason, when an exception is encountered, Apache's XML-RPC 2.0
            // returns that exception as the return value of XmlRpcClient.execute(String, Vector)
            // while it should simply through it. This might cause ClassCastException at the receiving
            // end of this getValue(String, Vector) method.
            // It was not like that with Apache's XML-RPC 1.1. Could also be my bug somewhere. But
            // since I don't have time right now to investigate, I'm quick-fixing this as follows.
            if (value != null) {
                if (Throwable.class.isInstance(value)) {
                    ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();							
                    ((Throwable)value).printStackTrace(new PrintStream(bytesOut));
                    String trace = bytesOut.toString();
                    throw new ServiceClientException(((Throwable)value).getMessage() + "\n" + trace);
                }
            }

        } catch (IOException ioe) {
            ioe.printStackTrace(System.out);
            throw new ServiceClientException("Error, getting value for " + methSrvName + "\n"
                    + ioe.toString());
        } catch (XmlRpcException xmle) {
            throw new ServiceClientException(" ** RPC Error rpc : " + xmle.toString());
        } catch (Exception e) {
            throw new ServiceClientException(e , "Error, getting value for " + methSrvName
                    + "\n" + e.toString());
        }

        return value;
  
    }

    public void setCredentials (String userName, String pwd) throws ServiceClientException {
        rpcClient.setBasicAuthentication(userName, pwd);
    }

}
