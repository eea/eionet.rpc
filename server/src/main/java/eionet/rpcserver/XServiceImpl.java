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

package eionet.rpcserver;
import java.util.HashMap;
import eionet.definition.Method;
import eionet.definition.Value;
import eionet.acl.RemoteService;

class XServiceImpl implements UITServiceIF {
  private HashMap _methods;

//  private final String getMethodName="getAclsData";
//  private final String setMethodName="setAclsData";  

  private final String[] methodNames = RemoteService.methodNames();
  

  /*{"getAclEntries", "getLocalGroups", "getChildrenAcls",
    "getUserPermissions",    "getPermissionDescrs", "setAclEntries", "setLocalGroups"};*/

  private final String[] valueTypes = RemoteService.valueTypes();


  /*{"ARRAY", "STRUCT", "ARRAY", 
      "ARRAY", "STRUCT", "STRING", "STRING"};*/
  
  public XServiceImpl()  { 
    initMethods();
  }

  public String getProvider() {
    return "eionet.acl.RemoteService";
  }

  public UITMethodIF getMethod(String name) {
    return (UITMethodIF)_methods.get(name);

  }

  public String[] getMethodNames() {
    //String s [] = { getMethodName , setMethodName} ;
    //return s;
    return methodNames;
  }

 private void     initMethods() {
    //System.out.println("=============== init X Service"); 
    _methods=new HashMap();
    for (int i=0; i<methodNames.length; i++ ) {
      Method method = new Method();
      Value value = new Value();
      value.setType(valueTypes[i]);
      method.setValue(value);   
      method.setName(methodNames[i]);        
      method.setAuth(true);        
      UITMethodIF m = new MethodImpl( this, method );
      _methods.put(methodNames[i], m);        
    }

 }
    
}
