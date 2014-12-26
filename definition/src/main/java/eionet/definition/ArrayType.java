/*
 * This class was automatically generated with 
 * <a href="http://castor.exolab.org">Castor 0.9.3.9+</a>, using an
 * XML Schema.
 * $Id: ArrayType.java,v 1.1 2003/04/24 12:58:56 te-ee Exp $
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
 * @version $Revision: 1.1 $ $Date: 2003/04/24 12:58:56 $
**/
public abstract class ArrayType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    private java.lang.String _type;

    private java.util.Vector _elementList;


      //----------------/
     //- Constructors -/
    //----------------/

    public ArrayType() {
        super();
        _elementList = new Vector();
    } //-- eionet.definition.ArrayType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * 
     * 
     * @param vElement
    **/
    public void addElement(Element vElement)
        throws java.lang.IndexOutOfBoundsException
    {
        _elementList.addElement(vElement);
    } //-- void addElement(Element) 

    /**
     * 
     * 
     * @param index
     * @param vElement
    **/
    public void addElement(int index, Element vElement)
        throws java.lang.IndexOutOfBoundsException
    {
        _elementList.insertElementAt(vElement, index);
    } //-- void addElement(int, Element) 

    /**
    **/
    public java.util.Enumeration enumerateElement()
    {
        return _elementList.elements();
    } //-- java.util.Enumeration enumerateElement() 

    /**
     * 
     * 
     * @param index
    **/
    public Element getElement(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _elementList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (Element) _elementList.elementAt(index);
    } //-- Element getElement(int) 

    /**
    **/
    public Element[] getElement()
    {
        int size = _elementList.size();
        Element[] mArray = new Element[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (Element) _elementList.elementAt(index);
        }
        return mArray;
    } //-- Element[] getElement() 

    /**
    **/
    public int getElementCount()
    {
        return _elementList.size();
    } //-- int getElementCount() 

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
    **/
    public void removeAllElement()
    {
        _elementList.removeAllElements();
    } //-- void removeAllElement() 

    /**
     * 
     * 
     * @param index
    **/
    public Element removeElement(int index)
    {
        java.lang.Object obj = _elementList.elementAt(index);
        _elementList.removeElementAt(index);
        return (Element) obj;
    } //-- Element removeElement(int) 

    /**
     * 
     * 
     * @param index
     * @param vElement
    **/
    public void setElement(int index, Element vElement)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _elementList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _elementList.setElementAt(vElement, index);
    } //-- void setElement(int, Element) 

    /**
     * 
     * 
     * @param elementArray
    **/
    public void setElement(Element[] elementArray)
    {
        //-- copy array
        _elementList.removeAllElements();
        for (int i = 0; i < elementArray.length; i++) {
            _elementList.addElement(elementArray[i]);
        }
    } //-- void setElement(Element) 

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
