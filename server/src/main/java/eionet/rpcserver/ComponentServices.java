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

import java.util.HashMap;
import java.util.Hashtable;
import java.util.StringTokenizer;

public class ComponentServices extends HashMap {

    public ComponentServices(Hashtable<Object, Object> props) {
        super();
        init(props);
    }

    /**
     * Load configuration from properties map.
     *
     * @param props - key,value map
     */
    private void init(Hashtable<Object, Object> props) {

        if (props == null) return;

        String services = (String) props.get("componentservices");
        if (services == null) return;

        StringTokenizer st = new StringTokenizer(services, ",");
        while (st.hasMoreTokens()) {
            String service = st.nextToken().trim();
            String provider = (String) props.get("componentservices." + service + ".provider");
            if (provider != null) {
                try {
                    CompServiceImpl impl = new CompServiceImpl(provider);
                    put(service, impl);
                } catch (ServiceException se){
                   // Ignore problems - should at least unconfigure and log.
                }
            }
        }
    }
}
