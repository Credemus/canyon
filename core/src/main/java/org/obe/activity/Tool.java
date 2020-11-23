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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Implementation which specifies a particular tool which should be
 * used for the activity.  The tool will either be an application or
 * a procedure.
 *
 * @author Anthony Eden
 */
public class Tool implements Serializable {
    static final long serialVersionUID = -6478999523224995636L;

    private String id;
    private String description;
    private List actualParameters;
    private Map extendedAttributes;
    private ToolType toolType;

    /** Construct a new Tool object with the given id.

        @param id The tool ID
    */

    public Tool(String id){
        this.id = id;

        this.extendedAttributes = new HashMap();
        this.actualParameters = new ArrayList();
    }

    /** Get the tool ID.

        @return The tool ID
    */

    public String getId(){
        return id;
    }

    /** Get a list of actual parameters.  Actual parameters are just
        Strings which are names of fields in the workflow relevant data.

        @return A List of actual parameters
    */

    public List getActualParameters(){
        return actualParameters;
    }

    /** Get a description of the tool.

        @return A description of the tool
    */

    public String getDescription(){
        return description;
    }

    /** Set a description of the tool.

        @param description The new description of the tool
    */

    public void setDescription(String description){
        this.description = description;
    }

    /** Get a Map of extended attributes.

        @return A Map of extended attributes
    */

    public Map getExtendedAttributes(){
        return extendedAttributes;
    }

    /** Get the tool type, for example an application or function.

        @return The ToolType
    */

    public ToolType getToolType(){
        return toolType;
    }

    /** Set the tool type.

        @param toolType The tool type
    */

    public void setToolType(ToolType toolType){
        this.toolType = toolType;
    }

}
