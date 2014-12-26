/*
 * This class was automatically generated with 
 * <a href="http://castor.exolab.org">Castor 0.9.3.9+</a>, using an
 * XML Schema.
 * $Id: ServiceType.java,v 1.1 2003/04/24 12:58:33 te-ee Exp $
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
 * @version $Revision: 1.1 $ $Date: 2003/04/24 12:58:33 $
**/
public abstract class ServiceType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    private java.lang.String _name;

    private java.lang.String _provider;

    private Methods _methods;


      //----------------/
     //- Constructors -/
    //----------------/

    public ServiceType() {
        super();
    } //-- eionet.definition.ServiceType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'methods'.
     * 
     * @return the value of field 'methods'.
    **/
    public Methods getMethods()
    {
        return this._methods;
    } //-- Methods getMethods() 

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
     * Returns the value of field 'provider'.
     * 
     * @return the value of field 'provider'.
    **/
    public java.lang.String getProvider()
    {
        return this._provider;
    } //-- java.lang.String getProvider() 

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
     * Sets the value of field 'methods'.
     * 
     * @param methods the value of field 'methods'.
    **/
    public void setMethods(Methods methods)
    {
        this._methods = methods;
    } //-- void setMethods(Methods) 

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
     * Sets the value of field 'provider'.
     * 
     * @param provider the value of field 'provider'.
    **/
    public void setProvider(java.lang.String provider)
    {
        this._provider = provider;
    } //-- void setProvider(java.lang.String) 

    /**
    **/
    public void validate()
        throws org.exolab.castor.xml.ValidationException
    {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } //-- void validate() 

}
