/*--

 Copyright (C) 2002 Anthony Eden.
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
    me@anthonyeden.com.

 4. Products derived from this software may not be called "OBE" or
    "Open Business Engine", nor may "OBE" or "Open Business Engine"
    appear in their name, without prior written permission from
    Anthony Eden (me@anthonyeden.com).

 In addition, I request (but do not require) that you include in the
 end-user documentation provided with the redistribution and/or in the
 software itself an acknowledgement equivalent to the following:
     "This product includes software developed by
      Anthony Eden (http://www.anthonyeden.com/)."

 THIS SOFTWARE IS PROVIdED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 DISCLAIMED.  IN NO EVENT SHALL THE AUTHOR(S) BE LIABLE FOR ANY DIRECT,
 INDIRECT, INCIdENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING
 IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 POSSIBILITY OF SUCH DAMAGE.

 For more information on OBE, please see <http://www.openbusinessengine.org/>.

 */

package org.wfmc.audit;

/**
 * Assign Process Instance Attribute Audit Data.
 *
 * @author Antony Lodge
 */
public class AssignProcessInstanceAttributeData extends AuditBase {
    static final long serialVersionUID = -6108514019431468721L;

    private String attributeName;
    private int attributeType;
    private int newAttributeLength;
    private String newAttributeValue;
    private int previousAttributeLength;
    private String previousAttributeValue;

    /**
     *
     */
    public AssignProcessInstanceAttributeData() {
    }

    /**
     *
     * @param cwadPrefix
     * @param cwadSuffix
     */
    public AssignProcessInstanceAttributeData(CWADPrefix cwadPrefix,
        CWADSuffix cwadSuffix) {

        super(cwadPrefix, cwadSuffix);
    }

    /**
     *
     * @param cwadPrefix
     * @param cwadSuffix
     * @param attributeName
     * @param attributeType
     * @param newAttributeLength
     * @param newAttributeValue
     * @param previousAttributeLength
     * @param previousAttributeValue
     */
    public AssignProcessInstanceAttributeData(CWADPrefix cwadPrefix,
        CWADSuffix cwadSuffix, String attributeName, int attributeType,
        int newAttributeLength, String newAttributeValue,
        int previousAttributeLength, String previousAttributeValue) {

        super(cwadPrefix, cwadSuffix);
        this.attributeName = attributeName;
        this.attributeType = attributeType;
        this.newAttributeLength = newAttributeLength;
        this.newAttributeValue = newAttributeValue;
        this.previousAttributeLength = previousAttributeLength;
        this.previousAttributeValue = previousAttributeValue;
    }

    /**
     *
     * @return
     */
    public String getAttributeName() {
        return attributeName;
    }

    /**
     *
     * @param attributeName
     */
    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    /**
     *
     * @return
     */
    public int getAttributeType() {
        return attributeType;
    }

    /**
     *
     * @param attributeType
     */
    public void setAttributeType(int attributeType) {
        this.attributeType = attributeType;
    }

    /**
     *
     * @return
     */
    public int getNewAttributeLength() {
        return newAttributeLength;
    }

    /**
     *
     * @param newAttributeLength
     */
    public void setNewAttributeLength(int newAttributeLength) {
        this.newAttributeLength = newAttributeLength;
    }

    /**
     *
     * @return
     */
    public String getNewAttributeValue() {
        return newAttributeValue;
    }

    /**
     *
     * @param newAttributeValue
     */
    public void setNewAttributeValue(String newAttributeValue) {
        this.newAttributeValue = newAttributeValue;
    }

    /**
     *
     * @return
     */
    public int getPreviousAttributeLength() {
        return previousAttributeLength;
    }

    /**
     *
     * @param previousAttributeLength
     */
    public void setPreviousAttributeLength(int previousAttributeLength) {
        this.previousAttributeLength = previousAttributeLength;
    }

    /**
     *
     * @return
     */
    public String getPreviousAttributeValue() {
        return previousAttributeValue;
    }

    /**
     *
     * @param previousAttributeValue
     */
    public void setPreviousAttributeValue(String previousAttributeValue) {
        this.previousAttributeValue = previousAttributeValue;
    }

    public String toString() {
        return "AssignProcessInstanceAttributeData@" +
            System.identityHashCode(this) + "[" +
            " cwadPrefix=" + getCwadPrefix() +
            ", attributeName='" + attributeName + "'" +
            ", attributeType=" + attributeType +
            ", newAttributeLength=" + newAttributeLength +
            ", newAttributeValue='" + newAttributeValue + "'" +
            ", previousAttributeLength=" + previousAttributeLength +
            ", previousAttributeValue='" + previousAttributeValue + "'" +
            ", cwadSuffix=" + getCwadSuffix() +
            "]";
    }
}