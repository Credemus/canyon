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

package org.obe;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/** The ExternalPackage class represents a package which is accessible on
    a remote server (through a URL).

    @author Anthony Eden
*/

public class ExternalPackage implements Serializable {
    static final long serialVersionUID = -9203167030242295684L;

    private String href;
    private Map extendedAttributes;
    private Package pkg;

    /** Construct a new ExternalPackage.

        @param href The remote address
        @param pkg The remote package object
    */

    public ExternalPackage(String href, Package pkg){
        // what happens if the href is not specified?
        // spec is unclear.  -AE
        this.href = href;
        this.pkg = pkg;

        this.extendedAttributes = new HashMap();
    }

    /** Get the URL.

        @return The URL
    */

    public String getHref(){
        return href;
    }

    /** Set the URL.

        @param href The URL
    */

    public void setHref(String href){
        this.href = href;
    }

    /** Get a Map of extended attributes.

        @return The extended attributes
    */

    public Map getExtendedAttributes(){
        return extendedAttributes;
    }

    /** Get the package object.

        @return The Package
    */

    public Package getPackage(){
        return pkg;
    }

}
