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

import java.io.Serializable;

/** Data type which describes an array.  All items in the array must be
    of the same type.

    @author Anthony Eden
*/

public class ArrayType implements Type, Serializable {
    static final long serialVersionUID = -3322164316372325857L;

    private Type type;
    private int lowerIndex;
    private int upperIndex;

    /** Construct a new ArrayType object.

        @param type The array data type
        @param lowerIndex The lower index of the array
        @param upperIndex The upper index of the array
    */

    public ArrayType(Type type, String lowerIndex, String upperIndex){
        this(type, Integer.parseInt(lowerIndex), Integer.parseInt(upperIndex));
    }

    /** Construct a new ArrayType object.

        @param type The array data type
        @param lowerIndex The lower index of the array
        @param upperIndex The upper index of the array
    */

    public ArrayType(Type type, int lowerIndex, int upperIndex){
        setType(type);
        this.lowerIndex = lowerIndex;
        this.upperIndex = upperIndex;
    }

    /** Get the type for items in the array.

        @return The item types
    */

    public Type getType(){
        return type;
    }

    /** Set the type for items in the array.

        @param type The type
    */

    public void setType(Type type){
        if(type == null){
            throw new IllegalArgumentException("Type must not be null");
        }
        this.type = type;
    }

    public int getLowerIndex(){
        return lowerIndex;
    }

    public void setLowerIndex(int lowerIndex){
        this.lowerIndex = lowerIndex;
    }

    public void setLowerIndex(String lowerIndex){
        if(lowerIndex != null){
            setLowerIndex(Integer.parseInt(lowerIndex));
        }
    }

    public int getUpperIndex(){
        return upperIndex;
    }

    public void setUpperIndex(int upperIndex){
        this.upperIndex = upperIndex;
    }

    public void setUpperIndex(String upperIndex){
        if(upperIndex != null){
            setUpperIndex(Integer.parseInt(upperIndex));
        }
    }

}
