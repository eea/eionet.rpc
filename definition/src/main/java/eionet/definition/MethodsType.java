/*
 * This class was automatically generated with 
 * <a href="http://castor.exolab.org">Castor 0.9.3.9+</a>, using an
 * XML Schema.
 * $Id: MethodsType.java,v 1.1 2003/04/24 12:58:47 te-ee Exp $
 */

package eionet.definition;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import java.util.Enumeration;
import java.util.Vector;
import org.exolab.castor.xml.*;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;

/**
 * 
 * 
 * @version $Revision: 1.1 $ $Date: 2003/04/24 12:58:47 $
**/
public abstract class MethodsType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    private java.util.Vector _methodList;


      //----------------/
     //- Constructors -/
    //----------------/

    public MethodsType() {
        super();
        _methodList = new Vector();
    } //-- eionet.definition.MethodsType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * 
     * 
     * @param vMethod
    **/
    public void addMethod(Method vMethod)
        throws java.lang.IndexOutOfBoundsException
    {
        _methodList.addElement(vMethod);
    } //-- void addMethod(Method) 

    /**
     * 
     * 
     * @param index
     * @param vMethod
    **/
    public void addMethod(int index, Method vMethod)
        throws java.lang.IndexOutOfBoundsException
    {
        _methodList.insertElementAt(vMethod, index);
    } //-- void addMethod(int, Method) 

    /**
    **/
    public java.util.Enumeration enumerateMethod()
    {
        return _methodList.elements();
    } //-- java.util.Enumeration enumerateMethod() 

    /**
     * 
     * 
     * @param index
    **/
    public Method getMethod(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _methodList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (Method) _methodList.elementAt(index);
    } //-- Method getMethod(int) 

    /**
    **/
    public Method[] getMethod()
    {
        int size = _methodList.size();
        Method[] mArray = new Method[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (Method) _methodList.elementAt(index);
        }
        return mArray;
    } //-- Method[] getMethod() 

    /**
    **/
    public int getMethodCount()
    {
        return _methodList.size();
    } //-- int getMethodCount() 

    /**
    **/
    public boolean isValid()
    {
        try {
            validate();
        }
        catch (org.exolab.castor.xml.ValidationException vex) {
            return false;
        }
        return true;
    } //-- boolean isValid() 

    /**
     * 
     * 
     * @param out
    **/
    public abstract void marshal(java.io.Writer out)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException;

    /**
     * 
     * 
     * @param handler
    **/
    public abstract void marshal(org.xml.sax.ContentHandler handler)
        throws java.io.IOException, org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException;

    /**
    **/
    public void removeAllMethod()
    {
        _methodList.removeAllElements();
    } //-- void removeAllMethod() 

    /**
     * 
     * 
     * @param index
    **/
    public Method removeMethod(int index)
    {
        java.lang.Object obj = _methodList.elementAt(index);
        _methodList.removeElementAt(index);
        return (Method) obj;
    } //-- Method removeMethod(int) 

    /**
     * 
     * 
     * @param index
     * @param vMethod
    **/
    public void setMethod(int index, Method vMethod)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _methodList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _methodList.setElementAt(vMethod, index);
    } //-- void setMethod(int, Method) 

    /**
     * 
     * 
     * @param methodArray
    **/
    public void setMethod(Method[] methodArray)
    {
        //-- copy array
        _methodList.removeAllElements();
        for (int i = 0; i < methodArray.length; i++) {
            _methodList.addElement(methodArray[i]);
        }
    } //-- void setMethod(Method) 

    /**
    **/
    public void validate()
        throws org.exolab.castor.xml.ValidationException
    {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } //-- void validate() 

}
