/**
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

package eionet.rpcserver.servlets;
  
import org.apache.xmlrpc.*;
//import org.apache.xmlrpc.XmlRpcServer;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.servlet.ServletException;
import javax.servlet.ServletConfig;

import java.util.HashMap;
import java.util.Iterator;
import java.util.ResourceBundle;
import java.util.StringTokenizer;

import java.io.IOException;
import java.io.OutputStream;
import java.io.File;


import eionet.rpcserver.UITServiceRoster;
import eionet.rpcserver.ServiceException;

import eionet.acl.AppUser;
import eionet.acl.SignOnException;

/**
 * Class router services for XML/RPC protocol.
 */
public class XmlRpcRouter extends HttpServlet {

  public XmlRpcServer xmlrpc;

  /**
   * gets the service defs from the roster.
   */
  public void init(ServletConfig config) throws ServletException {

    super.init(config);
    xmlrpc = new XmlRpcServer();

     try {
      HashMap services = UITServiceRoster.getServices();

      Iterator iter = services.keySet().iterator();
      while (iter.hasNext()) {
        String srvName = (String)iter.next();
        
        xmlrpc.addHandler (srvName, new XmlRpcServiceHandler(srvName));
        //Logger.log("** srv = " + srvName);
      }
  
     } catch (ServiceException se) {
        throw new ServletException(se);
     } catch (Exception e) {
        throw new ServletException(e);
   }

}


  /**
  * Standard doPost implementation.
  *
  */
  public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

  /*
    System.out.println("=============================");
    System.out.println("============POST ============");
    System.out.println("=============================");

    */
    byte[] result = null;
    //authorization here!

    // JH161205 -->
    String encoding = null;
    try{
        ResourceBundle props = ResourceBundle.getBundle(UITServiceRoster.RESOURCE_BUNDLE_NAME);
        if (props != null) encoding = props.getString(UITServiceRoster.PROP_XMLRPC_ENCODING);
    }
    catch (Exception e) {
    }
    
    if (encoding != null) {
        req.setCharacterEncoding(encoding);
        XmlRpc.setEncoding(encoding);
    }
    // <--- JH161205

    //get authorization header from request
    String auth=req.getHeader("Authorization");
    if (auth != null) {

    if (!auth.toUpperCase().startsWith("BASIC")) {
        throw new ServletException("wrong kind of authorization!");
    }
 
    //get encoded username and password    
    String userPassEncoded=auth.substring(6);
 
    //create a base64 decoder using Sun utility class
    sun.misc.BASE64Decoder dec = new sun.misc.BASE64Decoder();
    String userPassDecoded = new String(dec.decodeBuffer(userPassEncoded));
 
    //split decoded username and password
    StringTokenizer userAndPass = new StringTokenizer(userPassDecoded,":");
    String username = userAndPass.nextToken();
    String password = userAndPass.nextToken();
    
    result = xmlrpc.execute(req.getInputStream(), username, password);
    } else {
      //log("================ 2 ");    
      result = xmlrpc.execute(req.getInputStream());
    }
    
    res.setContentType("text/xml");
    res.setContentLength(result.length);
    OutputStream output = res.getOutputStream();
    output.write(result);
    output.flush();

    //req.getSession().invalidate(); //???????????????
  }

  /**
   * Warning, if someone's trying to use GET.
   */
  public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

    res.setContentType("text/html");
    res.getWriter().write("<html><head><title>XML/RPC RPC Router</title></head>");
    res.getWriter().write("<body><h1>XML/RPC Router</h1>");
    res.getWriter().write("<p>Sorry, I don't speak via HTTP GET- you have to use HTTP POST to talk to me</p></body></html>");
   }

}
