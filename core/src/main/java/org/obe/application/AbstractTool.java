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

package org.obe.application;

import java.util.ArrayList;
import java.util.List;

import org.obe.AbstractWFElement;
import org.obe.data.ExternalReference;
import org.obe.data.FormalParameter;

/**
 * Abstract base implementation of the tool metadata interface.
 *
 * @author Anthony Eden
 * @author Adrian Price
 */
public abstract class AbstractTool extends AbstractWFElement {
    static final long serialVersionUID = -273095181530777278L;

    private final List<FormalParameter> formalParameters;
    private ExternalReference externalReference;

    /** Construct a new AbstractTool.

        @param id The application id
        @param name The application name
    */

    public AbstractTool(String id, String name) {
        super(id, name);

        this.formalParameters = new ArrayList<FormalParameter>();
    }

    /** Return a List of all FormalParameters for the application.

        @return A List of FormalParameter objects
    */

    public List<FormalParameter> getFormalParameters(){
        return formalParameters;
    }

    /** Get an ExternalReference for the application.  This may be used
        if the application is accessible through a URI (for example, a
        web service).  This method may return null if the formal
        parameters are specified.

        @return The ExternalReference
    */

    public ExternalReference getExternalReference(){
        return externalReference;
    }

    /** Set an ExternalReference for the application.  This may be used
        if the application is accessible through a URI (for example, a
        web service).

        @param externalReference The ExternalReference
    */

    public void setExternalReference(ExternalReference externalReference){
        this.externalReference = externalReference;
    }

}
