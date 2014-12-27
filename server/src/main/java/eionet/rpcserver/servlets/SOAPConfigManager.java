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

package eionet.rpcserver.servlets;

import java.util.*;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import org.apache.soap.*;
import org.apache.soap.rpc.*;
import org.apache.soap.server.*;
import org.apache.soap.server.http.*;
import org.apache.soap.util.*;


/**
* Class extending the default config manager.
* Not used by default
* If used, not *.ds file is written in the disk, but performance is slower
*
* must be specified in the soap.xml file
*/
public class SOAPConfigManager extends BaseConfigManager   implements ConfigManager {

  /** The name of the deployment file. */
  protected String filename = "eionet.ds";


  /**
   * This method sets the configuration options (usually
   * read from the config file).
   */
  public void setOptions( Hashtable options ) {
    if ( options == null ) return;

    String value = (String) options.get( "filename" );
    if ( value != null && !"".equals(value) )
      filename = value;
  }

  /**
   * Loads the descriptors from the underlying registry file, which
   * contains a serialized Hashtable.
   */

  public void loadRegistry() throws SOAPException {
    //empty implementation
  }

  /**
   * Saves currently deployed descriptors to the underlying registry file,
   * as a serialized Hashtable.
   */
  public void saveRegistry() throws SOAPException {
    //emtpty implementation
  }
  
    private static void _log(String s ) {
        System.out.println("================== " + s);
    }
}
