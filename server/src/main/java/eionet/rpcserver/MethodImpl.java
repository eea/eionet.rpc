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
import eionet.definition.Value;

import eionet.definition.Element;

import java.lang.reflect.InvocationTargetException;

import java.util.StringTokenizer;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;
import eionet.acl.AppUser;


/**
 * Class implements method in a service service.
 */
class MethodImpl implements UITMethodIF {

    private Value _valueDescr;
    private Service _service;
    private String _providerName;
    private String _name;

    private boolean _auth;


    MethodImpl(UITServiceIF srv, Method method)  {
        _name = method.getName();
        _valueDescr = method.getValue();
        _auth = (method.hasAuth() ? method.getAuth() : false);
        _providerName = srv.getProvider();
    }

    /**
     * Checks if the returned object fits to the process definition.
     */
    private void validateValue(Object value) throws ServiceException {

        //void method
        // XML / RPC does not support null values
        /*
        if (value == null && _valueDescr.getType() == null)
          return;
        */


        try {

            HashMap _valueTypes = TypeMappings.valueTypes;

            //type in description
            String descType = _valueDescr.getType();

            //the actual type returned from the method
            String valueType = value.getClass().getName();

            //System.out.println("================================== desc " + descType);
            //System.out.println("================================== value " + valueType);

            if (!valueType.equals((String)_valueTypes.get(descType)))
              throw new ServiceException(" The value type is not the same as described in the "
                  + "deployment descriptor, \n"
                  + " returned " + valueType + ", must be " + descType);

            // check, if the content of the structure (Hash) is correct
            // struct does not have to be described
            if (descType.equals("STRUCT") && _valueDescr.getStruct() != null) {

                //value is Hashtable by mappings
                Hashtable h = (Hashtable)value;

                //elements by the description
                Element[] elems = _valueDescr.getStruct().getElement();

                int elemCount = elems.length;
                int valuesCount = h.size();

                if (elemCount != valuesCount)
                  throw new ServiceException("** Count of members in the structure is not the same "
                      + " in the returned hash and in the description");

                //check, if the element types are same in the Hash and in the description
                for (int i = 0; i < elems.length; i++) {
                    String dMemberName = elems[i].getName();
                    String dMemberType = elems[i].getType();

                    //check, if there is a member with the key as required by the description
                    if (!h.containsKey(dMemberName))
                        throw new ServiceException("No such Member in the returned structure " + dMemberName);
                    else {
                        //check, if the member is of the correct type
                        String  vMemberType = (String)h.get(dMemberName).getClass().getName();
                        if (!vMemberType.equals((String)_valueTypes.get(dMemberType)))
                          throw new ServiceException("Wrong type of Member " + dMemberName + "\n"
                              + " returned " + vMemberType + " must be " + dMemberType);
                    }
                }
            }


            if (descType.equals("ARRAY") && _valueDescr.getArray() != null) {

                //Vector, returned by the API method
                Vector v = (Vector)value;


        //->
              //Check Type of all elements in array, if ARRAY Type is specified
                String arrayElemsType = _valueDescr.getArray().getType();
                if (arrayElemsType != null) {
                  for (int i = 0; i < v.size(); i++) {
                  if (!v.elementAt(i).getClass().getName().equals(_valueTypes.get(arrayElemsType)))
                      throw new ServiceException("Wrong type in result array, position " + (i +1)
                          + " needed " + arrayElemsType);
                  }
               }
        //<-


                //Elements, needed by the description
                Element[] elems = _valueDescr.getArray().getElement();

                //check, if the real and described element count are the same

                //check, if there are elements described, if not, the length is not limited
                if (_valueDescr.getArray().getElement().length > 0) {
                    int elemCount = elems.length;
                    int valuesCount = v.size();


                    if (elemCount != valuesCount) {
                        throw new ServiceException("** Count of members in the array is not the same ");
                    }
                }
                //check, if each member is of the correct type
                for (int i = 0; i < elems.length; i++) {
                  String  vMember = v.elementAt(i).getClass().getName();
                  String  dMember = elems[i].getType();

                  if (!vMember.equals(_valueTypes.get(dMember)))
                      throw new ServiceException("Wrong type in result array, position " + (i + 1)
                          + " needed " + dMember + ", method returned " + vMember);

                  //check, if all elements are of required type

                }
            }
        } catch (Exception e) {
            throw new ServiceException("Exception thrown " + e.toString());
        }


    }


    /**
     * Parameters
     * Object classes from Vector -to Class[]
     */
    private Class[] getParamTypes(Vector prms) {

        if (prms == null)
            return null;

        int prmCount = prms.size();

        Class[] paramTypes = new Class[prmCount];

        for (int i = 0; i < prmCount; i++) {
            Object prm = prms.elementAt(i);
            paramTypes[i]=getClass(prm);
        }
        return paramTypes;

    }

    /**
     * Class of the object, if not primitive wrapper
     * else Class.TYPE
     */
    private Class getClass(Object o) {
        HashMap types = TypeMappings.classTypes;

        Class c = o.getClass();
        String clName = c.getName();

        if (types.containsKey(clName))
            c = (Class)types.get(clName);

        return c;
    }

   /**
    * Returns Value of the method
    *
    * @throw ServiceException
    * @param Vector paramters
    * @author Kaido Laine
    */
    public Object getValue(String prms) throws ServiceException {

        Vector params = parsePrms(prms);

        return getValue(params);

    }

    /**
     * Returns Value of the method
     *
     * @throw ServiceException
     * @param Vector paramters
     * @author Kaido Laine
     */
    public Object getValue(Vector parameters) throws ServiceException {

        Object value = null;
        try {

          //provider class
          Class providerClass = Class.forName(_providerName);

          Class[] paramTypes = getParamTypes(parameters);


          //method in java class
          java.lang.reflect.Method method = providerClass.getMethod(_name, paramTypes);
          Object[] args = parameters.toArray();
          Object o = null;

          try {

              //if not public, we add a AppUser object in constructor
              if (!isPublic()) {
                  //System.out.println(" ====================== getValue3");
                  Class[] c = new Class[1];
                  AppUser user = UITServiceRoster.getUser();

                  //System.out.println(" ====================== user = " + user.getUserName());


                  if (user == null)
                      throw new ServiceException("User is not authenticated.");

                  Object[] p = new Object[1];
                  p[0]=user;
                  c[0] = user.getClass();
                  java.lang.reflect.Constructor constr = providerClass.getConstructor(c);
                  o = constr.newInstance(p);
                  //System.out.println(" ====================== getValue4");
              } else
                  o = providerClass.newInstance();


          } catch (InstantiationException ie) {
              System.out.println("Constructor error in the adapter class" + ie.toString());
          } catch (Exception e) {
              throw new ServiceException(e.toString());
          }

            value = method.invoke(o, args);


            try {
                validateValue(value);
            } catch (ServiceException se) {
                //Logger.log(se.toString());
                throw new ServiceException(se, " Value, returned by the API method, does not fit "
                  + " with the description. \n See log for details" + "\n" + se.toString());
            }

        } catch (ClassNotFoundException cnf) {
            throw new ServiceException(cnf, "No such class " + _providerName);
        } catch (InvocationTargetException ite) {
            ite.printStackTrace(System.out);
            throw new ServiceException(ite, "Exception thrown " + ite.getTargetException().toString());
        } catch (NoSuchMethodException nsme) {
            ///nsme.printStackTrace(System.out);
            throw new ServiceException(nsme, "No such method or incorrect signature "
              + _name + parameters + " in class " + _providerName);
        } catch (IllegalAccessException iae) {
            throw new ServiceException(iae, "Illegal access to the method " + _name);
        } catch (NullPointerException nue) {
            throw new ServiceException(nue, "NullPointerException. Method " + _name
                + " is probably not static in the API ");
        }
        return value;
    }

    private Vector parsePrms(String prms) {
        StringTokenizer str = new StringTokenizer(prms, ";");
        Vector v = new Vector();

        while (str.hasMoreTokens())
            v.add(str.nextToken());

        return v;
    }

    public boolean isPublic() {
        return !_auth;
    }
}
