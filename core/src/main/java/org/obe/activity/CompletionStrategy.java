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
 * Represents how an assigned auto-finish activity should end.  There are two
 * strategies: {@link #ALL} (the default mode) will result in the automatic
 * finish of an activity when all of its work items are complete; the
 * {@link #ANY} strategy completes the activity when any one of its work items
 * is completed.
 *
 * @author Adrian Price
 */
public final class CompletionStrategy implements Serializable {
    static final long serialVersionUID = -7262752929327795028L;
    /**	Integer representing the ALL strategy. */
    public static final int ALL_INT = 0;
    /**	Integer representing the ANY strategy. */
    public static final int ANY_INT = 1;
    /**	Completes the activity when all of its work item are complete. */
    public static final CompletionStrategy ALL =
        new CompletionStrategy(ALL_INT);
    /** Completes the activity when any one of its work item is completed. */
    public static final CompletionStrategy ANY =
        new CompletionStrategy(ANY_INT);
    private static final String[] TAGS = {
        "ALL",
        "ANY"
    };
    private static final CompletionStrategy[] VALUES = {
        ALL,
        ANY
    };
    private static final HashMap<String, CompletionStrategy> tagMap = new HashMap<String, CompletionStrategy>();

    private final int _value;

    static {
        for (int i = 0; i < TAGS.length; i++) {
            tagMap.put(TAGS[i], VALUES[i]);
        }
    }

    /**
     * Converts the specified string to a CompletionStrategy object.  If there
     * is no matching CompletionStrategy for the given string then this method
     * returns null.
     *
     * @param tag The string
     * @return The CompletionStrategy object
     */
    public static CompletionStrategy fromString(String tag) {
        CompletionStrategy completionStrategy = tagMap.get(tag);
        if (completionStrategy == null && tag != null)
            throw new IllegalArgumentException(tag);
        return completionStrategy;
    }

    /**
     * Constructs a new CompletionStrategy instance.
     *
     * @param value The value.
     */
    private CompletionStrategy(int value) {
        _value = value;
    }

    /**
     * Gets the ordinal value.  This value can be used in switch statements in
     * conjunction with the xxx_INT contants defined in this class.
     *
     * @return The ordinal value.
     */
    public int getValue() {
        return _value;
    }

    /**
     * Returns a string representation of this object.
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