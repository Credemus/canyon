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

package org.obe.transition;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.obe.AbstractWFElement;
import org.obe.data.ExternalReference;

/**
 * Describes an application event type, referenced by an event-type transition.
 * This is an OBE XPDL-1.0 extension.
 *
 * @author Adrian Price
 */
public final class EventType extends AbstractWFElement {
    static final long serialVersionUID = -6721041421989416656L;
    private List _formalParameters = new ArrayList();
    private ExternalReference _externalReference;

    public EventType(String id, String name) {
        super(id, name);
    }

    public List getFormalParameters() {
        return _formalParameters;
    }

    public void setFormalParameters(List formalParameters) {
        _formalParameters = formalParameters;
    }

    /**
     * <em>Unsupported operation.  Do not call.</em>
     */
    public Map getExtendedAttributes() {
        throw new UnsupportedOperationException("getExtendedAttributes");
    }

    public ExternalReference getExternalReference() {
        return _externalReference;
    }

    public void setExternalReference(ExternalReference externalReference) {
        _externalReference = externalReference;
    }
}