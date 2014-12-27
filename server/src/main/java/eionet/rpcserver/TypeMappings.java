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

/**
 * Static hashes of mappings between java objects, descriptive types.
 */
class TypeMappings {

    public static HashMap classTypes;
    public static HashMap valueTypes;
  
    static { 
          
        classTypes = new HashMap();
        valueTypes = new HashMap();
          
        classTypes.put("java.lang.Integer", Integer.TYPE );
        classTypes.put("java.lang.Boolean", Boolean.TYPE );    
        classTypes.put("java.lang.Double", Double.TYPE );    
          
        valueTypes.put("STRING", "java.lang.String");
        valueTypes.put("INT", "java.lang.Integer");
        valueTypes.put("DOUBLE", "java.lang.Double");
        valueTypes.put("STRUCT", "java.util.Hashtable");
        valueTypes.put("ARRAY", "java.util.Vector");
        valueTypes.put("BOOLEAN", "java.lang.Boolean");
        valueTypes.put("DATE", "java.sql.Date");
        valueTypes.put("BASE64", "[B"); //KL 030905
          
    }
}
