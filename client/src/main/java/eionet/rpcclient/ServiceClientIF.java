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
import java.util.Vector;

/**
 * Interface for a service client, has got implementations for several communication
 * protocols: SOAP, XML/RPC.
 */
public interface ServiceClientIF {

    /**
     * Client type For XML/RPC services.
     */ 
    public static final int CLIENT_TYPE_XMLRPC = 0;

    /**
     * Client type For SOAP services.
     */ 
    public static final int CLIENT_TYPE_SOAP = 1;

    /**
     * Returns value, received from the XML/RPC server.
     * @param String methodName
     * @param Vector parameters
     * @return Object
     * @throw RpcClientException
     */
    public Object getValue(String methodName, Vector parameters) throws ServiceClientException;

    /**
     * Returns value, received from the XML/RPC server.
     * @param String username
     * @param String pwd
     * @throw RpcClientException
     */
    public void setCredentials(String userName, String wpd) throws ServiceClientException;


}
