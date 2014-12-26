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

import eionet.definition.Method;
import eionet.definition.Service; 

import java.util.HashMap;
import java.util.Iterator;

/**
* Class implements SericeIF
*/

class ServiceImpl implements UITServiceIF  {
  private String _providerName;
  private HashMap _methods;
  
  ServiceImpl( Service srv ) {
    this._providerName = srv.getProvider();
    readMethods( srv );
  }

  public String[] getMethodNames() {

    String[] names = new String [ _methods.size()];
    int x = 0;
    for (Iterator i = _methods.keySet().iterator(); i.hasNext();) {
      names[x] = (String)i.next();
      x++;
    }

    return names;
  }
  
  /**
  * Returns nethod by name
  */
  public UITMethodIF getMethod (String methodName ) throws ServiceException {
    UITMethodIF method = null;
    try {
      method =  (UITMethodIF)_methods.get(methodName);

    } catch (NullPointerException n) {
      throw new ServiceException("No method " + methodName);

    }
    return method;
  }

  public String getProvider() {
    return _providerName;
  }

  /**
  * Methods to HashMap
  */
  private void readMethods(Service srv) {



    if (_methods== null) {
      _methods = new HashMap();
      Method[] methods = srv.getMethods().getMethod();

      for (int i=0; i<methods.length;i++){
        _methods.put( methods[i].getName(), new MethodImpl( this, methods[i])  );
         //Logger.log(" ** method " + methods[i].getName() ); }
      }   
    }
  }
}
