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
 * The Original Code is "EINRC-7 / AIT project".
 *
 * The Initial Developer of the Original Code is TietoEnator.
 * The Original Code code was developed for the European
 * Environment Agency (EEA) under the IDA/EINRC framework contract.
 *
 * Copyright (C) 2000-2004 by European Environment Agency.  All
 * Rights Reserved.
 *
 * Original Code: Jaaus Heinlaid (TietoEnator)
 */

package eionet.rpcserver;

import java.util.*;

public class ComponentServices extends HashMap {
    
    public ComponentServices(ResourceBundle props){
        super();
        init(props);
    }
    
    private void init(ResourceBundle props){
        
        if (props==null) return;
        
        String services = props.getString("componentservices");
        if (services==null) return;
        
        StringTokenizer st = new StringTokenizer(services, ",");
        while (st.hasMoreTokens()) {
            String service = st.nextToken().trim();
            String provider = props.getString("componentservices." + service + ".provider");
            if (provider!=null){
                try{
                    CompServiceImpl impl = new CompServiceImpl(provider);
                    put(service, impl);
                }
                catch (ServiceException se){}
            }
        }
    }
}
