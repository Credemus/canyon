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

/**
 * Provides information about a tool type.  Each tool type uses a different
 * underlying technology to implement the tool invocation. For example,
 * distinct tool types are required call native executables, Java classes, EJBs,
 * scripts, etc.  Access to the set of installed tool types is required by
 * management consoles, in order to allow an administrator to define new tools
 * and update existing ones.
 *
 * @author Adrian Price
 */
public final class ToolType extends AbstractObjectType {
    static final long serialVersionUID = 2009683953098800431L;

    /**
     * Constructs a tool type.
     *
     * @param className Fully qualified procedure implementation class name.
     * @param displayName Localized display name.
     * @param description Localized description.
     * @param docUrl URL for documentation.
     * @param vendor Vendor's name.
     */
    public ToolType(String className, String displayName,
        String description, String docUrl, String vendor) {

        super(className, displayName, description, docUrl, vendor);
    }
}