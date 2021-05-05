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
import java.util.ArrayList;
import java.util.List;

/** A split represents a location in a workflow process where execution
    can split or change paths based on the outcome of a condition.  Two
    types of splits are supported: AND and XOR.  AND splits result in
    multiple threads executing at the same time.  XOR splits result in
    a continuation of the current thread, albeit on one of several possible
    paths.  Non-exclusive splits are also possible by using an AND split
    in conjuction with conditions.

    @author Anthony Eden
*/

public class Split implements Serializable {
    static final long serialVersionUID = 7654247142688505300L;

    private SplitType type;
    private List<String> transitionReferences;

    /** Construct a new Split. */

    public Split(){
        transitionReferences = new ArrayList();
    }

    /** Get the split type.

        @return The split type
    */

    public SplitType getType(){
        return type;
    }

    /** Set the split type.

        @param type The split type
    */

    public void setType(SplitType type){
        this.type = type;
    }

    /** Get a list of transition references.  Transition references are used
        in XOR splits to determine which transitions are part of the possible
        threads.

        @return The transition references
    */

    public List<String> getTransitionReferences(){
        return transitionReferences;
    }

}
