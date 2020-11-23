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
import java.util.HashMap;

/**
 * Data type representing the basic workflow data types.
 *
 * @author Anthony Eden
 * @author Adrian Price
 */
public final class BasicType implements Type, Serializable {
    static final long serialVersionUID = -7894443551993705284L;

    public static final int STRING_INT = 0;
    public static final int FLOAT_INT = 1;
    public static final int INTEGER_INT = 2;
    public static final int REFERENCE_INT = 3;
    public static final int DATETIME_INT = 4;
    public static final int BOOLEAN_INT = 5;
    public static final int PERFORMER_INT = 6;
    public static final BasicType STRING = new BasicType(STRING_INT);
    public static final BasicType FLOAT = new BasicType(FLOAT_INT);
    public static final BasicType INTEGER = new BasicType(INTEGER_INT);
    public static final BasicType REFERENCE = new BasicType(REFERENCE_INT);
    public static final BasicType DATETIME = new BasicType(DATETIME_INT);
    public static final BasicType BOOLEAN = new BasicType(BOOLEAN_INT);
    public static final BasicType PERFORMER = new BasicType(PERFORMER_INT);

    private static final String[] TAGS = {
        "STRING",
        "FLOAT",
        "INTEGER",
        "REFERENCE",
        "DATETIME",
        "BOOLEAN",
        "PERFORMER"
    };
    private static final BasicType[] VALUES = {
        STRING,
        FLOAT,
        INTEGER,
        REFERENCE,
        DATETIME,
        BOOLEAN,
        PERFORMER
    };
    private static HashMap tagMap = new HashMap();

    static {
        for (int i = 0; i < TAGS.length; i++) {
            tagMap.put(TAGS[i], VALUES[i]);
        }
    }

    public static BasicType fromInt(int type) {
        return VALUES[type];
    }

    public static BasicType fromString(String tag) {
        BasicType basicType = (BasicType)tagMap.get(tag);
        if (basicType == null && tag != null)
            throw new IllegalArgumentException(tag);
        return basicType;
    }

    private final int _value;

    /**
     * Construct a new BasicType.
     *
     * @param value The int value
     */
    private BasicType(int value) {
        _value = value;
    }

    public int getValue() {
        return _value;
    }

    public String toString() {
        return TAGS[_value];
    }

    public Object readResolve() {
        return VALUES[_value];
    }
}