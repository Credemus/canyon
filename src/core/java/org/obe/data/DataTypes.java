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
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Anthony Eden
 * @author Adrian Price
 */
public final class DataTypes implements Serializable {
    static final long serialVersionUID = 8024954267852036171L;

    public static final int BOOLEAN_INT = 0;
    public static final int PERFORMER_INT = 1;
    public static final int STRING_INT = 2;
    public static final int FLOAT_INT = 3;
    public static final int INTEGER_INT = 4;
    public static final int REFERENCE_INT = 5;
    public static final int DATETIME_INT = 6;
    public static final int UNION_INT = 7;
    public static final int ENUMERATION_INT = 8;
    public static final int ARRAY_INT = 9;
    public static final int LIST_INT = 10;
    public static final int EXTERNAL_REFERENCE_INT = 11;
    public static final int SCHEMA_INT = 12;

    public static final DataTypes BOOLEAN = new DataTypes(BOOLEAN_INT);
    public static final DataTypes PERFORMER = new DataTypes(PERFORMER_INT);
    public static final DataTypes STRING = new DataTypes(STRING_INT);
    public static final DataTypes FLOAT = new DataTypes(FLOAT_INT);
    public static final DataTypes INTEGER = new DataTypes(INTEGER_INT);
    public static final DataTypes REFERENCE = new DataTypes(REFERENCE_INT);
    public static final DataTypes DATETIME = new DataTypes(DATETIME_INT);
    public static final DataTypes UNION = new DataTypes(UNION_INT);
    public static final DataTypes ENUMERATION = new DataTypes(ENUMERATION_INT);
    public static final DataTypes ARRAY = new DataTypes(ARRAY_INT);
    public static final DataTypes LIST = new DataTypes(LIST_INT);
    public static final DataTypes EXTERNAL_REFERENCE =
        new DataTypes(EXTERNAL_REFERENCE_INT);
    public static final DataTypes SCHEMA = new DataTypes(SCHEMA_INT);

    public static final String[] TAGS = {
        "BOOLEAN",
        "PERFORMER",
        "STRING",
        "FLOAT",
        "INTEGER",
        "REFERENCE",
        "DATETIME",
        "UNION",
        "ENUMERATION",
        "ARRAY",
        "LIST",
        "EXTERNAL_REFERENCE",
        "SCHEMA"
    };
    /** Array of all DataTypes. */
    public static final DataTypes[] VALUES = {
        DataTypes.BOOLEAN,
        DataTypes.PERFORMER,
        DataTypes.STRING,
        DataTypes.FLOAT,
        DataTypes.INTEGER,
        DataTypes.REFERENCE,
        DataTypes.DATETIME,
        DataTypes.UNION,
        DataTypes.ENUMERATION,
        DataTypes.ARRAY,
        DataTypes.LIST,
        DataTypes.EXTERNAL_REFERENCE,
        DataTypes.SCHEMA
    };
    public static final Map tagMap;

    private final int _value;

    static {
        HashMap map = new HashMap();
        for (int i = 0; i < TAGS.length; i++) {
            map.put(TAGS[i], VALUES[i]);
        }
        tagMap = Collections.unmodifiableMap(map);
    }

    public static DataTypes fromString(String tag) {
        DataTypes dataTypes = (DataTypes)tagMap.get(tag);
        if (dataTypes == null && tag != null)
            throw new IllegalArgumentException(tag);
        return dataTypes;
    }

    /**
     * Construct a new DataTypes object.
     *
     * @param value The int value
     */
    private DataTypes(int value) {
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