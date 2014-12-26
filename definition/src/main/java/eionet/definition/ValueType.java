/*
 * This class was automatically generated with 
 * <a href="http://castor.exolab.org">Castor 0.9.3.9+</a>, using an
 * XML Schema.
 * $Id: ValueType.java,v 1.1 2003/04/24 12:58:28 te-ee Exp $
 */

package eionet.definition;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import org.exolab.castor.xml.*;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;

/**
 * 
 * 
 * @version $Revision: 1.1 $ $Date: 2003/04/24 12:58:28 $
**/
public abstract class ValueType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    private java.lang.String _type;

    private Struct _struct;

    private Array _array;


      //----------------/
     //- Constructors -/
    //----------------/

    public ValueType() {
        super();
    } //-- eionet.definition.ValueType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'array'.
     * 
     * @return the value of field 'array'.
    **/
    public Array getArray()
    {
        return this._array;
    } //-- Array getArray() 

    /**
     * Returns the value of field 'struct'.
     * 
     * @return the value of field 'struct'.
    **/
    public Struct getStruct()
    {
        return this._struct;
    } //-- Struct getStruct() 

    /**
     * Returns the value of field 'type'.
     * 
     * @return the value of field 'type'.
    **/
    public java.lang.String getType()
    {
        return this._type;
    } //-- java.lang.String getType() 

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
     * Sets the value of field 'array'.
     * 
     * @param array the value of field 'array'.
    **/
    public void setArray(Array array)
    {
        this._array = array;
    } //-- void setArray(Array) 

    /**
     * Sets the value of field 'struct'.
     * 
     * @param struct the value of field 'struct'.
    **/
    public void setStruct(Struct struct)
    {
        this._struct = struct;
    } //-- void setStruct(Struct) 

    /**
     * Sets the value of field 'type'.
     * 
     * @param type the value of field 'type'.
    **/
    public void setType(java.lang.String type)
    {
        this._type = type;
    } //-- void setType(java.lang.String) 

    /**
    **/
    public void validate()
        throws org.exolab.castor.xml.ValidationException
    {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } //-- void validate() 

}
