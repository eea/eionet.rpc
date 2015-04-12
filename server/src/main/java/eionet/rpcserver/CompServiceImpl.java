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

/**
 * Introspect a class to get its declared RPC methods. The class must have two
 * methods called <code>methodNames</code> and <code>valueTypes</code>. These
 * must return an array of String.
 */
package eionet.rpcserver;

import java.util.HashMap;
import eionet.definition.Method;
import eionet.definition.Value;

class CompServiceImpl implements UITServiceIF {

    private HashMap _methods;
    private String[] methodNames = null;
    private String[] valueTypes = null;

    /** Class name to introspect. */
    private String provider = null;

    public CompServiceImpl(String provider) throws ServiceException {
        this.provider = provider;
        init();
    }

    public String getProvider() {
        return provider;
    }

    public UITMethodIF getMethod(String name) {
        return (UITMethodIF) _methods.get(name);
    }

    public String[] getMethodNames() {
        return methodNames;
    }

    private void init() throws ServiceException {

        if (provider == null)
            throw new ServiceException("Provider class name missing!");

        // get the method names and value types from provider

        try {
            Class c = Class.forName(provider);
            java.lang.reflect.Method m = c.getMethod("methodNames", null);
            methodNames = (String[]) m.invoke(null, null);
            m = c.getMethod("valueTypes", null);
            valueTypes = (String[]) m.invoke(null, null);
        } catch (Exception e) {
            throw new ServiceException(e.toString());
        }

        // init provider methods
        initMethods();
    }

    private void initMethods() {

        _methods = new HashMap();
        for (int i = 0; i < methodNames.length; i++ ) {
            eionet.definition.Method method = new eionet.definition.Method();
            Value value = new Value();
            value.setType(valueTypes[i]);
            method.setValue(value);
            method.setName(methodNames[i]);
            method.setAuth(true);
            UITMethodIF m = new MethodImpl(this, method);
            _methods.put(methodNames[i], m);
        }
    }

}
