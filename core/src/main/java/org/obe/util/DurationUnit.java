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

package org.obe.util;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Class which defines all duration units available.  This class defines
 * the following duration units:
 *
 * <ul>
 * <li><b>Y</b> - Year
 * <li><b>M</b> - Month
 * <li><b>D</b> - Day
 * <li><b>h</b> - Hour
 * <li><b>m</b> - Minute
 * <li><b>s</b> - Second
 * </ul>
 *
 * @author Anthony Eden
 * @author Adrian Price
 */
public final class DurationUnit implements Serializable {
    static final long serialVersionUID = 3955674746845399636L;

    public static final int YEAR_INT = 0;
    public static final int MONTH_INT = 1;
    public static final int DAY_INT = 2;
    public static final int HOUR_INT = 3;
    public static final int MINUTE_INT = 4;
    public static final int SECOND_INT = 5;
    public static final DurationUnit YEAR = new DurationUnit(YEAR_INT);
    public static final DurationUnit MONTH = new DurationUnit(MONTH_INT);
    public static final DurationUnit DAY = new DurationUnit(DAY_INT);
    public static final DurationUnit HOUR = new DurationUnit(HOUR_INT);
    public static final DurationUnit MINUTE = new DurationUnit(MINUTE_INT);
    public static final DurationUnit SECOND = new DurationUnit(SECOND_INT);

    private static final String[] TAGS = {
        "Y",
        "M",
        "D",
        "h",
        "m",
        "s"
    };
    private static final DurationUnit[] VALUES = {
        YEAR,
        MONTH,
        DAY,
        HOUR,
        MINUTE,
        SECOND
    };
    private static final HashMap<String, DurationUnit> tagMap = new HashMap<String, DurationUnit>();
    private static final HashMap<DurationUnit, Long> unitToMillisecondsMap = new HashMap<DurationUnit, Long>();

    private final int _value;

    static {
        for (int i = 0; i < TAGS.length; i++) {
            tagMap.put(TAGS[i], VALUES[i]);
        }

        unitToMillisecondsMap.put(SECOND, 1000L);
        unitToMillisecondsMap.put(MINUTE, (long) (1000 * 60));
        unitToMillisecondsMap.put(HOUR, (long) (1000 * 60 * 60));
        unitToMillisecondsMap.put(DAY, (long) (1000 * 60 * 60 * 24));
        unitToMillisecondsMap.put(MONTH,
                (long) (1000 * 60 * 60 * 24) * (long) 30);
        unitToMillisecondsMap.put(YEAR,
                (long) (1000 * 60 * 60 * 24) * (long) 365);
    }

    /**
     * Convert the specified type String to a DurationUnit object.  If the
     * String cannot be converted to a DurationUnit then the value null
     * is returned.
     *
     * @param tag The duration unit String
     * @return A DurationUnit or null
     */
    public static DurationUnit fromString(String tag) {
        DurationUnit durationUnit = tagMap.get(tag);
        if (durationUnit == null && tag != null)
            throw new IllegalArgumentException(tag);
        return durationUnit;
    }

    /**
     * Convert the specified DurationUnit to milliseconds.
     *
     * @param unit The DurationUnit
     * @return The number of milliseconds
     */
    public static long unitToMilliseconds(DurationUnit unit) {
        Long value = unitToMillisecondsMap.get(unit);
        if (value == null) {
            throw new IllegalArgumentException("Unknown duration unit: " +
                unit);
        } else {
            return value;
        }
    }

    /**
     * Construct a new duration unit.
     *
     * @param value An int value
     */
    private DurationUnit(int value) {
        _value = value;
    }

    /**
     * Get the int value of the DurationUnit constant.  This is useful
     * for use in <code>case</code> logic.
     *
     * @return The int value
     */
    public int getValue() {
        return _value;
    }

    /**
     * Return the String representation of the duration unit.
     *
     * @return String representation of the duration unit
     */
    public String toString() {
        return TAGS[_value];
    }

    /**
     * Convert the duration unit to milliseconds.
     *
     * @return Convert duration unit to milliseconds
     */
    public long toMilliseconds() {
        return unitToMilliseconds(this);
    }

    public Object readResolve() {
        return VALUES[_value];
    }
}