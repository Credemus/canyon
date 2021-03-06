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
 * @author Anthony Eden
 * @author Adrian Price
 */
public final class ToolType implements Serializable {
    static final long serialVersionUID = 5121160093995786673L;

    /** Integer representing an APPLICATION tool type. */
    public static final int APPLICATION_INT = 0;
    /** Integer representing a PROCEDURE tool type. */
    public static final int PROCEDURE_INT = 1;

    /**
     * ToolType representing an application.  An application is an executable
     * tool that executes in its own environment (i.e., not in the workflow
     * engine's JVM).
     */
    public static final ToolType APPLICATION = new ToolType(APPLICATION_INT);
    /**
     * ToolType representing a procedure.  A procedure is a tool that is
     * executed in the workflow engine's JVM.
     */
    public static final ToolType PROCEDURE = new ToolType(PROCEDURE_INT);

    private static final String[] TAGS = {
        "APPLICATION",
        "PROCEDURE"
    };
    private static final ToolType[] VALUES = {
        APPLICATION,
        PROCEDURE
    };
    private static final HashMap tagMap = new HashMap();

    private final int _value;

    static {
        for (int i = 0; i < TAGS.length; i++) {
            tagMap.put(TAGS[i], VALUES[i]);
        }
    }

    /**
     * Convert the specified String to an ToolType object.  If there
     * no matching ToolType for the given String then this method
     * returns null.
     *
     * @param tag The String
     * @return The ToolType object
     */
    public static ToolType fromString(String tag) {
        ToolType toolType = (ToolType)tagMap.get(tag);
        if (toolType == null && tag != null)
            throw new IllegalArgumentException(tag);
        return toolType;
    }

    /**
     * Construct a new ToolType instance.
     *
     * @param value The value
     */
    private ToolType(int value) {
        _value = value;
    }

    /**
     * Get the value.  This value can be used in switch statements in
     * conjunction with the xxx_VALUE contants defined in this class.
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