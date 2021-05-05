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
import java.util.HashMap;

/**
 * The ExecutionType is used to represent whether a workflow process
 * should execute synchronously or asynchronously.
 *
 * @author Anthony Eden
 * @author Adrian Price
 */
public final class ExecutionType implements Serializable {
    static final long serialVersionUID = 2635003088809664289L;

    /** Integer representing the SYNCHRONOUS type. */
    public static final int SYNCHRONOUS_INT = 0;
    /** Integer representing the ASYNCHRONOUS type. */
    public static final int ASYNCHRONOUS_INT = 1;
    /** ExecutionType representing a synchronous execution. */
    public static final ExecutionType SYNCHRONOUS =
        new ExecutionType(SYNCHRONOUS_INT);
    /** ExecutionType representing an asynchronous execution. */
    public static final ExecutionType ASYNCHRONOUS =
        new ExecutionType(ASYNCHRONOUS_INT);
    /** An array of all execution types. */
    public static final ExecutionType[] types = {SYNCHRONOUS, ASYNCHRONOUS};

    public static final String[] TAGS = {
        "SYNCHR",
        "ASYNCHR"
    };
    public static final ExecutionType[] VALUES = {
        SYNCHRONOUS,
        ASYNCHRONOUS
    };
    private static final HashMap<String, ExecutionType> tagMap = new HashMap<String, ExecutionType>();

    private final int _value;

    static {
        for (int i = 0; i < TAGS.length; i++)
            tagMap.put(TAGS[i], VALUES[i]);
    }

    /**
     * Convert the specified String to an ExecutionType object.  If there
     * no matching ExecutionType for the given String then this method
     * returns null.
     *
     * @param tag The String
     * @return The ExecutionType object
     */
    public static ExecutionType fromString(String tag) {
        ExecutionType executionType = tagMap.get(tag);
        if (executionType == null && tag != null)
            throw new IllegalArgumentException(tag);
        return executionType;
    }

    /**
     * Construct a new ExecutionType instance.
     *
     * @param value The value
     */
    private ExecutionType(int value) {
        _value = value;
    }

    /**
     * Get the value.  This value can be used in switch statements in
     * conjunction with the xxx_INT contants defined in this class.
     *
     * @return An int value
     */
    public int getValue() {
        return _value;
    }

    /**
     * Return a String representation of this class.
     *
     * @return The String representation
     */
    public String toString() {
        return TAGS[_value];
    }

    public Object readResolve() {
        return VALUES[_value];
    }
}