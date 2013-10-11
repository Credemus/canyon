/*-- 

 Copyright (C) 2002-2003 Aetrion LLC.
 All rights reserved.
 
 Redistribution and use in source and binary forms, with or without
 modification, are permitted provided that the following conditions
 are met:
 
 1. Redistributions of source code must retain the above copyright
    notice, this list of conditions, and the following disclaimer.
 
 2. Redistributions in binary form must reproduce the above copyright
    notice, this list of conditions, and the disclaimer that follows 
    these conditions in the documentation and/or other materials 
    provided with the distribution.

 3. The names "OBE" and "Open Business Engine" must not be used to 
 	endorse or promote products derived from this software without prior 
 	written permission.  For written permission, please contact 
 	obe@aetrion.com.
 
 4. Products derived from this software may not be called "OBE" or
 	"Open Business Engine", nor may "OBE" or "Open Business Engine" 
 	appear in their name, without prior written permission from 
 	Aetrion LLC (obe@aetrion.com).

 THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 DISCLAIMED.  IN NO EVENT SHALL THE AUTHOR(S) BE LIABLE FOR ANY DIRECT, 
 INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES 
 (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR 
 SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) 
 HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, 
 STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING 
 IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE 
 POSSIBILITY OF SUCH DAMAGE.

 For more information on OBE, please see 
 <http://www.openbusinessengine.org/>.
 
 */

package org.obe.client.api.repository;

import java.io.Serializable;

/**
 * An abstract base class for object type descriptors.
 *
 * @author Adrian Price
 */
public class AbstractObjectType implements Serializable {
    static final long serialVersionUID = -6694757335807623985L;
    // TODO: Move OBE_HOME somewhere sensible.
    public static final String OBE_HOME =
        "Open Business Engine (http://www.openbusinessengine.org)";
    public static final String OBE_APIDOC_URL = OBE_HOME + "/docs/api/";

    protected final String _className;
    protected final String _displayName;
    protected final String _description;
    protected final String _docUrl;
    protected final String _vendor;

    public static String getDocUrl(Class clazz) {
        return OBE_APIDOC_URL + clazz.getName().replace('.', '/') + ".html";
    }

    protected AbstractObjectType(String className, String displayName,
        String description, String docUrl, String vendor) {

        _className = className;
        _displayName = displayName;
        _description = description;
        _docUrl = docUrl;
        _vendor = vendor;
    }

    /**
     * Returns the name of the procedure type implementation class.
     *
     * @return Fully qualified class name.
     */
    public String getClassName() {
        return _className;
    }

    /**
     * Returns the localized display name for this type of procedure.
     *
     * @return Localized display name.
     */
    public String getDisplayName() {
        return _displayName;
    }

    /**
     * Returns the URL of the documentation for this procedure type.
     *
     * @return URL for documentation.
     */
    public String getDocUrl() {
        return _docUrl;
    }

    /**
     * Returns the localized description of this type of procedure.
     *
     * @return Localized description name.
     */
    public String getDescription() {
        return _description;
    }

    /**
     * Returns the name of the vendor who provided this procedure type.
     *
     * @return Vendor name.
     */
    public String getVendor() {
        return _vendor;
    }
}