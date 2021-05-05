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

package org.obe.activity;

import org.obe.data.ActualParameter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/** A SubFlow represents another workflow process which is executed within
 the context of the current workflow process.

 @author Anthony Eden
 */

public class SubFlow implements Implementation, Serializable {
    static final long serialVersionUID = -1475378210472793406L;

    /** Constant representing the default execution type (synchronous). */

    public static final ExecutionType DEFAULT_EXECUTION =
        ExecutionType.SYNCHRONOUS;

    private String id;
    private ExecutionType execution = DEFAULT_EXECUTION;
    private List<ActualParameter> actualParameters;

    /** Construct a new SubFlow which represents the specified workflow
     process ID.

     @param id The workflow process id
     */

    public SubFlow(String id) {
        setId(id);

        this.actualParameters = new ArrayList();
    }

    /** Get the workflow process id.

     @return The workflow process id
     */

    public String getId() {
        return id;
    }

    /** Set the workflow process id.

     @param id The new ID
     */

    public void setId(String id) {
        if (id == null) {
            throw new IllegalArgumentException("SubFlow ID can not be null");
        }
        this.id = id;
    }

    /** Get the execution type (either synchronous or asynchronous).

     @return The ExecutionType
     */

    public ExecutionType getExecution() {
        return execution;
    }

    /** Set the execution type.  If the execution parameter is null then
     the default execution type (synchronous) is used.

     @param execution The execution type
     */

    public void setExecution(ExecutionType execution) {
        if (execution == null) {
            execution = DEFAULT_EXECUTION;
        }
        this.execution = execution;
    }

    /** Get a list of actual parameters.  Actual parameters are just
     Strings which are names of fields in the workflow relevant data.

     @return A List of actual parameters
     */

    public List<ActualParameter> getActualParameters() {
        return actualParameters;
    }

}
