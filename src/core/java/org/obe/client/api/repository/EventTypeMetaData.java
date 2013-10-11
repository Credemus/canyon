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

import org.obe.condition.Condition;

/**
 * Describes a user-defined event type.
 *
 * @author Adrian Price
 */
public class EventTypeMetaData extends AbstractMetaData {
    private String _contentType;
    private String _schema;
    private Condition _condition;
    private String[] _keyExpressions;
    private String _scriptType;

    /**
     * Constructs event type meta-data.
     */
    public EventTypeMetaData() {
    }

    public EventTypeMetaData(String eventType, String displayName,
        String description, String docUrl, String author, String contentType,
        String schema, Condition condition, String scriptType,
        String[] keyExpressions) {

        super(null, null, eventType, displayName, description, docUrl, author);
        _contentType = contentType;
        _schema = schema;
        _condition = condition;
        _keyExpressions = keyExpressions;
        _scriptType = scriptType;
    }

    public String getContentType() {
        return _contentType;
    }

    public void setContentType(String contentType) {
        _contentType = contentType;
    }

    public String getSchema() {
        return _schema;
    }

    public void setSchema(String schema) {
        _schema = schema;
    }

    public Condition getCondition() {
        return _condition;
    }

    public void setCondition(Condition condition) {
        _condition = condition;
    }

    public String[] getKeyExpressions() {
        return _keyExpressions;
    }

    public void setKeyExpressions(String[] keyExpressions) {
        _keyExpressions = keyExpressions;
    }

    public String getScriptType() {
        return _scriptType;
    }

    public void setScriptType(String scriptType) {
        _scriptType = scriptType;
    }

    public Object createImplementation() throws RepositoryException {
        // TODO: understand what 'implementation' maps to in the case of events.
        return "implementation";
    }
}