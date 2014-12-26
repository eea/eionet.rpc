/*
 * This class was automatically generated with 
 * <a href="http://castor.exolab.org">Castor 0.9.3.9+</a>, using an
 * XML Schema.
 * $Id: MethodType.java,v 1.1 2003/04/24 12:58:45 te-ee Exp $
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
 * @version $Revision: 1.1 $ $Date: 2003/04/24 12:58:45 $
**/
public abstract class MethodType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    private java.lang.String _name;

    private java.lang.String _description;

    private boolean _auth;

    /**
     * keeps track of state for field: _auth
    **/
    private boolean _has_auth;

    private Parameters _parameters;

    private Value _value;


      //----------------/
     //- Constructors -/
    //----------------/

    public MethodType() {
        super();
    } //-- eionet.definition.MethodType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
    **/
    public void deleteAuth()
    {
        this._has_auth= false;
    } //-- void deleteAuth() 

    /**
     * Returns the value of field 'auth'.
     * 
     * @return the value of field 'auth'.
    **/
    public boolean getAuth()
    {
        return this._auth;
    } //-- boolean getAuth() 

    /**
     * Returns the value of field 'description'.
     * 
     * @return the value of field 'description'.
    **/
    public java.lang.String getDescription()
    {
        return this._description;
    } //-- java.lang.String getDescription() 

    /**
     * Returns the value of field 'name'.
     * 
     * @return the value of field 'name'.
    **/
    public java.lang.String getName()
    {
        return this._name;
    } //-- java.lang.String getName() 

    /**
     * Returns the value of field 'parameters'.
     * 
     * @return the value of field 'parameters'.
    **/
    public Parameters getParameters()
    {
        return this._parameters;
    } //-- Parameters getParameters() 

    /**
     * Returns the value of field 'value'.
     * 
     * @return the value of field 'value'.
    **/
    public Value getValue()
    {
        return this._value;
    } //-- Value getValue() 

    /**
    **/
    public boolean hasAuth()
    {
        return this._has_auth;
    } //-- boolean hasAuth() 

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
     * Sets the value of field 'auth'.
     * 
     * @param auth the value of field 'auth'.
    **/
    public void setAuth(boolean auth)
    {
        this._auth = auth;
        this._has_auth = true;
    } //-- void setAuth(boolean) 

    /**
     * Sets the value of field 'description'.
     * 
     * @param description the value of field 'description'.
    **/
    public void setDescription(java.lang.String description)
    {
        this._description = description;
    } //-- void setDescription(java.lang.String) 

    /**
     * Sets the value of field 'name'.
     * 
     * @param name the value of field 'name'.
    **/
    public void setName(java.lang.String name)
    {
        this._name = name;
    } //-- void setName(java.lang.String) 

    /**
     * Sets the value of field 'parameters'.
     * 
     * @param parameters the value of field 'parameters'.
    **/
    public void setParameters(Parameters parameters)
    {
        this._parameters = parameters;
    } //-- void setParameters(Parameters) 

    /**
     * Sets the value of field 'value'.
     * 
     * @param value the value of field 'value'.
    **/
    public void setValue(Value value)
    {
        this._value = value;
    } //-- void setValue(Value) 

    /**
    **/
    public void validate()
        throws org.exolab.castor.xml.ValidationException
    {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } //-- void validate() 

}
