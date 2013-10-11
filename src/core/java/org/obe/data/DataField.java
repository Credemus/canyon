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

package org.obe.data;

import org.obe.AbstractWFElement;

/** The DataField class represents a data field which is placed in the
    workflow relevant data when a workflow process executes.

    @author Anthony Eden
*/

public class DataField extends AbstractWFElement {
    static final long serialVersionUID = 8312139864854170366L;

    private DataType dataType;
    private String initialValue;
    private int length;
    private boolean isArray;

    /** Construct a new DataField.

        @param id The data field id
        @param name The data field name
        @param dataType The data type
    */

    public DataField(String id, String name, DataType dataType){
        super(id, name);
        setDataType(dataType);
    }

    /** Get the field's data type.

        @return The DataType
    */

    public DataType getDataType(){
        return dataType;
    }

    /** Set the field's data type.  The data type must not be null.

        @param dataType The data type
    */

    public void setDataType(DataType dataType){
        if(dataType == null){
            throw new IllegalArgumentException("Data type cannot be null");
        }
        this.dataType = dataType;
    }

    /** Get the field's initial value.

        @return The initial value
    */

    public String getInitialValue(){
        return initialValue;
    }

    /** Set the field's initial value.

        @param initialValue The new initial value
    */

    public void setInitialValue(String initialValue){
        this.initialValue = initialValue;
    }

    /** Get the length of the data field if the field represents an
        array or list.  If the field is a single value then this method
        should return 0.

        @return The field's length for arrays or 0
    */

    public int getLength(){
        return length;
    }

    /** Set the length of the data field.

        @param length The length
    */

    public void setLength(int length){
        this.length = length;
    }

    /** Return true if the data field is an array.

        @return True if the data field is an array
    */

    public boolean isArray(){
        return isArray;
    }

    /** Set the flag indicating that the field is an array.

        @param flag True to indicate that the field is an array
    */

    public void setArray(boolean flag){
        this.isArray = flag;
    }

}
