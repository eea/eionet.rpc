/*
 * This class was automatically generated with 
 * <a href="http://castor.exolab.org">Castor 0.9.3.9+</a>, using an
 * XML Schema.
 * $Id: ParamStructType.java,v 1.1 2003/04/24 12:58:38 te-ee Exp $
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
 * @version $Revision: 1.1 $ $Date: 2003/04/24 12:58:38 $
**/
public abstract class ParamStructType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    private java.util.Vector _memberList;


      //----------------/
     //- Constructors -/
    //----------------/

    public ParamStructType() {
        super();
        _memberList = new Vector();
    } //-- eionet.definition.ParamStructType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * 
     * 
     * @param vMember
    **/
    public void addMember(Member vMember)
        throws java.lang.IndexOutOfBoundsException
    {
        _memberList.addElement(vMember);
    } //-- void addMember(Member) 

    /**
     * 
     * 
     * @param index
     * @param vMember
    **/
    public void addMember(int index, Member vMember)
        throws java.lang.IndexOutOfBoundsException
    {
        _memberList.insertElementAt(vMember, index);
    } //-- void addMember(int, Member) 

    /**
    **/
    public java.util.Enumeration enumerateMember()
    {
        return _memberList.elements();
    } //-- java.util.Enumeration enumerateMember() 

    /**
     * 
     * 
     * @param index
    **/
    public Member getMember(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _memberList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (Member) _memberList.elementAt(index);
    } //-- Member getMember(int) 

    /**
    **/
    public Member[] getMember()
    {
        int size = _memberList.size();
        Member[] mArray = new Member[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (Member) _memberList.elementAt(index);
        }
        return mArray;
    } //-- Member[] getMember() 

    /**
    **/
    public int getMemberCount()
    {
        return _memberList.size();
    } //-- int getMemberCount() 

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
    public void removeAllMember()
    {
        _memberList.removeAllElements();
    } //-- void removeAllMember() 

    /**
     * 
     * 
     * @param index
    **/
    public Member removeMember(int index)
    {
        java.lang.Object obj = _memberList.elementAt(index);
        _memberList.removeElementAt(index);
        return (Member) obj;
    } //-- Member removeMember(int) 

    /**
     * 
     * 
     * @param index
     * @param vMember
    **/
    public void setMember(int index, Member vMember)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _memberList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _memberList.setElementAt(vMember, index);
    } //-- void setMember(int, Member) 

    /**
     * 
     * 
     * @param memberArray
    **/
    public void setMember(Member[] memberArray)
    {
        //-- copy array
        _memberList.removeAllElements();
        for (int i = 0; i < memberArray.length; i++) {
            _memberList.addElement(memberArray[i]);
        }
    } //-- void setMember(Member) 

    /**
    **/
    public void validate()
        throws org.exolab.castor.xml.ValidationException
    {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } //-- void validate() 

}
