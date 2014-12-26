/*
 * This class was automatically generated with 
 * <a href="http://castor.exolab.org">Castor 0.9.3.9+</a>, using an
 * XML Schema.
 * $Id: ServicesType.java,v 1.1 2003/04/24 12:58:34 te-ee Exp $
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
 * @version $Revision: 1.1 $ $Date: 2003/04/24 12:58:34 $
**/
public abstract class ServicesType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    private java.util.Vector _serviceList;


      //----------------/
     //- Constructors -/
    //----------------/

    public ServicesType() {
        super();
        _serviceList = new Vector();
    } //-- eionet.definition.ServicesType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * 
     * 
     * @param vService
    **/
    public void addService(Service vService)
        throws java.lang.IndexOutOfBoundsException
    {
        _serviceList.addElement(vService);
    } //-- void addService(Service) 

    /**
     * 
     * 
     * @param index
     * @param vService
    **/
    public void addService(int index, Service vService)
        throws java.lang.IndexOutOfBoundsException
    {
        _serviceList.insertElementAt(vService, index);
    } //-- void addService(int, Service) 

    /**
    **/
    public java.util.Enumeration enumerateService()
    {
        return _serviceList.elements();
    } //-- java.util.Enumeration enumerateService() 

    /**
     * 
     * 
     * @param index
    **/
    public Service getService(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _serviceList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (Service) _serviceList.elementAt(index);
    } //-- Service getService(int) 

    /**
    **/
    public Service[] getService()
    {
        int size = _serviceList.size();
        Service[] mArray = new Service[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (Service) _serviceList.elementAt(index);
        }
        return mArray;
    } //-- Service[] getService() 

    /**
    **/
    public int getServiceCount()
    {
        return _serviceList.size();
    } //-- int getServiceCount() 

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
    public void removeAllService()
    {
        _serviceList.removeAllElements();
    } //-- void removeAllService() 

    /**
     * 
     * 
     * @param index
    **/
    public Service removeService(int index)
    {
        java.lang.Object obj = _serviceList.elementAt(index);
        _serviceList.removeElementAt(index);
        return (Service) obj;
    } //-- Service removeService(int) 

    /**
     * 
     * 
     * @param index
     * @param vService
    **/
    public void setService(int index, Service vService)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _serviceList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _serviceList.setElementAt(vService, index);
    } //-- void setService(int, Service) 

    /**
     * 
     * 
     * @param serviceArray
    **/
    public void setService(Service[] serviceArray)
    {
        //-- copy array
        _serviceList.removeAllElements();
        for (int i = 0; i < serviceArray.length; i++) {
            _serviceList.addElement(serviceArray[i]);
        }
    } //-- void setService(Service) 

    /**
    **/
    public void validate()
        throws org.exolab.castor.xml.ValidationException
    {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } //-- void validate() 

}
