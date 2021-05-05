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

import java.io.Serializable;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/** A block of activities.

    <p><b>Note:</b> This class is deprecated.  Inline blocks have
    been replaced by activity sets in the XPDL specification.</p>

    @author Anthony Eden
    @deprecated
*/

public class InlineBlock implements Serializable {
    static final long serialVersionUID = 3576144905643811049L;

    private String blockName;
    private String begin;
    private String end;
    private String description;
    private URL documentation;
    private URL icon;
    private Map<String, Object> extendedAttributes;

    /** Construct a new InlineBlock.

        @param begin The begining activity block
        @param end The ending activity block
    */

    public InlineBlock(String begin, String end){
        setBegin(begin);
        setEnd(end);

        extendedAttributes = new HashMap<String, Object>();
    }

    /** Get the block's name.

        @return The block name
    */

    public String getBlockName(){
        return blockName;
    }

    /** Set the block's name.

        @param blockName The new block name
    */

    public void setBlockName(String blockName){
        this.blockName = blockName;
    }

    /** Get the id of the first activity in the block.

        @return The begin activity
    */

    public String getBegin(){
        return begin;
    }

    /** Set the ID of the first activity in the block.

        @param The new begin activity
    */

    public void setBegin(String begin){
        if(begin == null){
            throw new IllegalArgumentException("InlineBlock begin required");
        }
        this.begin = begin;
    }

    /** Get the id of the last activity in the block.

        @return The end activity
    */

    public String getEnd(){
        return end;
    }

    /** Set the id of the last activity in the block.

        @param end The new end activity ID
    */

    public void setEnd(String end){
        if(end == null){
            throw new IllegalArgumentException("InlineBlock end required");
        }
        this.end = end;
    }

    /** Get a description of the InlineBlock.

        @return A description of the InlineBlock
    */

    public String getDescription(){
        return description;
    }

    /** Set a description of the InlineBlock.

        @param description The new description
    */

    public void setDescription(String description){
        this.description = description;
    }

    /** Get a URL for the documentation of the InlineBlock.

        @return The documentation URL
    */

    public URL getDocumentation(){
        return documentation;
    }

    /** Get a URL for the documentation of the InlineBlock.

        @param documentation The documentation URL
    */

    public void setDocumentation(URL documentation){
        this.documentation = documentation;
    }

    /** Get an icon URL for the InlineBlock.

        @return The icon
    */

    public URL getIcon(){
        return icon;
    }

    /** Get the icon URL for the InlineBlock.

        @param icon The icon URL
    */

    public void setIcon(URL icon){
        this.icon = icon;
    }

    /** Get a Map of extended attributes.

        @return A Map of extended attributes
    */

    public Map getExtendedAttributes(){
        return extendedAttributes;
    }

}
