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

import java.util.Vector;

/**
 * Method in an external service.
 */
public interface UITMethodIF {

    /**
     * Returns value of the method.
     * @throw ServiceException
     * @param Vector parameters Vector of objects
     */
    public Object getValue( Vector parameters) throws ServiceException;


    /**
     * Returns value of the method.
     * @throw ServiceException
     * @param Vector parameters Vector of objects
     */
    public Object getValue( String parameters) throws ServiceException;

    /**
     * True, if the method does not require authentication.
     */
    public boolean isPublic();

}
