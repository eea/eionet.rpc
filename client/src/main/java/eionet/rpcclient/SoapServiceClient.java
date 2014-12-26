/**
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

import java.util.Vector;

import java.net.URL;
import java.net.MalformedURLException;

import org.apache.soap.rpc.Call;
import org.apache.soap.Constants;
import org.apache.soap.Fault;
import org.apache.soap.SOAPException;

import org.apache.soap.rpc.Call;
import org.apache.soap.rpc.Response;
import org.apache.soap.rpc.Parameter;


import org.apache.soap.encoding.SOAPMappingRegistry;

import org.apache.soap.transport.http.SOAPHTTPConnection;

/**
* Implementation of service client for the SOAP protocol
*/

class SoapServiceClient implements ServiceClientIF {

    /**
     * Inner class for handling SOAPCall
     */
    class SOAPCall {

        private Response response;
        private Call call;
        private SOAPHTTPConnection conn;

        private URL soapSrvUrl;

        /**
         * Creates a new SOAP Call
         */
        //SOAPCall(String soapSrvUrl, String serviceName, String methodName, Vector parameters)
        SOAPCall(String serviceName, String urlName) throws ServiceClientException {

      //this.soapSrvUrl = soapSrvUrl;
        try {
        
            this.soapSrvUrl = new URL(urlName);
       
            conn = new SOAPHTTPConnection();
            call = new Call();

            call.setEncodingStyleURI(Constants.NS_URI_SOAP_ENC);
            call.setSOAPTransport(conn);

            call.setTargetObjectURI(serviceName);
            //_log("target set " + serviceName);

        } catch (MalformedURLException mfe) {
            throw new ServiceClientException("Bad url " + urlName);
        } catch (Exception e) {
            throw new ServiceClientException("Bad connection " + e.toString());
        }
   
        //call.setMethodName(methodName);

        //SOAP CAll do not have to have parameters
        /*if (parameters != null)
          call.setParams(parameters);
        */

    }

    /**
    */
    void invoke() throws ServiceClientException {
      try {
         response = call.invoke(soapSrvUrl, null);

      } catch (SOAPException soae) {
        soae.printStackTrace(System.out);
        throw new ServiceClientException("Error getting value from SOAP server = " + soae.toString());      
      }
    }
    void setParameters(Vector parameters) {
      if (parameters != null) {
        convParams(parameters);
        call.setParams(parameters);
      }
    }

    void setMethodName(String methodName) {
        call.setMethodName(methodName);
    }

    private void convParams(Vector params) {
        for (int i=0; i< params.size(); i++) {
            Object prmValue = params.elementAt(i);
            String prmName = "prm" + i;
            Class prmClass = prmValue.getClass();
            Parameter param = new Parameter(prmName, prmClass, prmValue, null);
            params.setElementAt(param, i);
            //params.addElement(new org.apache.soap.rpc.Parameter("xroleId", String.class, "eionet-nrc", null));
        }
    }

//->

       private Object getValue() throws ServiceClientException {
            Object value=null;
            if (!response.generatedFault()) {
                              Parameter retval = response.getReturnValue();
            value = retval.getValue();
                            }
          else {
                              Fault fault = response.getFault();
                                            throw new ServiceClientException("SOAP Fault Code :" + fault.getFaultCode() + "\n" +
                                                     "Fault : " + fault.getFaultString());
                      }

                            return value;
        }

    
    } //SOAPCall


    private SOAPCall call;
    private String srvName;
    private String srvUrl;
  
    /**
     * Constructor.
     */
    SoapServiceClient(String srvName, String srvUrl) throws ServiceClientException {
      this.srvName     = "urn:" + srvName;
      this.srvUrl     = srvUrl;    

      this.call = initSOAPCall(this.srvName, this.srvUrl);
      
    }

    private SOAPCall initSOAPCall(String srvName, String srvUrl) throws ServiceClientException  {
        return new SOAPCall(srvName, srvUrl);
    }

    /**
     * Returns value of the method
     */
    public Object getValue(String methodName, Vector parameters) throws ServiceClientException {

        Object value = null;
      
        if (this.call == null)
            call = initSOAPCall(srvName, srvUrl);

        try {
            //URL url = call.soapSrvUrl;
            call.setMethodName(methodName);
            call.setParameters(parameters);

            //_log("method = " + methodName);      

            call.invoke();

            value = call.getValue();

        } catch (Exception e) {
            throw new ServiceClientException("Error getting values from SOAP server: " + e.toString());
        } 
      
        return value;   
    }


    public void setCredentials (String userName, String pwd) throws ServiceClientException {
        call.conn.setUserName(userName);   
        call.conn.setPassword(pwd);   
    }

    private static void _log(String s) {
        System.out.println("********** " + s);
    }

}
