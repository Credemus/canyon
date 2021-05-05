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
 * The ImplementationType class is used to represent how an activity
 * is implemented.
 *
 * @author Anthony Eden
 * @author Adrian Price
 */
public final class ImplementationType implements Serializable {
    static final long serialVersionUID = 8184059437925889311L;

    /** Integer representing the NO implementation type. */
    public static final int NO_INT = 0;
    /** Integer representing the TOOL implementation type. */
    public static final int TOOL_INT = 1;
    /** Integer representing the SUBFLOW implementation type. */
    public static final int SUBFLOW_INT = 2;

    /** ImplementationType which represents an empty implementation. */
    public static final ImplementationType NO =
        new ImplementationType(NO_INT);
    /** ImplementationType which represents a tool implementation. */
    public static final ImplementationType TOOL =
        new ImplementationType(TOOL_INT);
    /** ImplementationType which represents a subflow implementation. */
    public static final ImplementationType SUBFLOW =
        new ImplementationType(SUBFLOW_INT);

    private static final String[] TAGS = {
        "No",
        "Tool",
        "SubFlow"
    };
    private static final ImplementationType[] VALUES = {
        NO,
        TOOL,
        SUBFLOW
    };
    private static final HashMap<String, ImplementationType> tagMap = new HashMap<String, ImplementationType>();

    static {
        for (int i = 0; i < TAGS.length; i++) {
            tagMap.put(TAGS[i], VALUES[i]);
        }
    }

    private final int _value;

    /**
     * Convert the specified String to an ImplementationType object.  If there
     * no matching ImplementationType for the given String then this method
     * returns null.
     *
     * @param tag The String
     * @return The ImplementationType object
     */
    public static ImplementationType fromString(String tag) {
        ImplementationType implementationType = tagMap.get(tag);
        if (implementationType == null && tag != null)
            throw new IllegalArgumentException(tag);
        return implementationType;
    }

    /**
     * Construct a new ImplementationType instance.
     *
     * @param value The value
     */
    private ImplementationType(int value) {
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
     * Return a String representation of the object.
     *
     * @return The string representation
     */
    public String toString() {
        return TAGS[_value];
    }

    public Object readResolve() {
        return VALUES[_value];
    }
}