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
 * Change Activity Instance State Audit Data.
 *
 * @author Antony Lodge
 */
public class ChangeActivityInstanceStateData extends AuditBase {
    static final long serialVersionUID = 3105870611884980114L;

    private String activityInstanceId;
    private String activityDefinitionBusinessName;
    private String applicationId;
    private String newActivityState;
    private String previousActivityState;

    /**
     *
     */
    public ChangeActivityInstanceStateData() {
    }

    /**
     *
     * @param cwadPrefix
     * @param cwadSuffix
     */
    public ChangeActivityInstanceStateData(CWADPrefix cwadPrefix,
        CWADSuffix cwadSuffix) {

        super(cwadPrefix, cwadSuffix);
    }

    /**
     *
     * @param cwadPrefix
     * @param cwadSuffix
     * @param activityInstanceId
     * @param activityDefinitionBusinessName
     * @param applicationId
     * @param newActivityState
     * @param previousActivityState
     */
    public ChangeActivityInstanceStateData(CWADPrefix cwadPrefix,
        CWADSuffix cwadSuffix, String activityInstanceId,
        String activityDefinitionBusinessName, String applicationId,
        String newActivityState, String previousActivityState) {

        super(cwadPrefix, cwadSuffix);
        this.activityInstanceId = activityInstanceId;
        this.activityDefinitionBusinessName = activityDefinitionBusinessName;
        this.applicationId = applicationId;
        this.newActivityState = newActivityState;
        this.previousActivityState = previousActivityState;
    }

    /**
     *
     * @return Unique Id for the current Activity Instance
     */
    public String getActivityInstanceId() {
        return activityInstanceId;
    }

    /**
     *
     * @param activityInstanceId Unique Id for the current Activity Instance
     */
    public void setActivityInstanceId(String activityInstanceId) {
        this.activityInstanceId = activityInstanceId;
    }

    /**
     *
     * @return Business name of the Activity
     */
    public String getActivityDefinitionBusinessName() {
        return activityDefinitionBusinessName;
    }

    /**
     *
     * @param activityDefinitionBusinessName Business name of the Activity
     */
    public void setActivityDefinitionBusinessName(
        String activityDefinitionBusinessName) {

        this.activityDefinitionBusinessName = activityDefinitionBusinessName;
    }

    /**
     *
     * @return Id of the application associated with this activity
     */
    public String getApplicationId() {
        return applicationId;
    }

    /**
     *
     * @param applicationId Id of application associated with this activity
     */
    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    /**
     *
     * @return New Activity State
     */
    public String getNewActivityState() {
        return newActivityState;
    }

    /**
     *
     * @param newActivityState  New Activity State
     */
    public void setNewActivityState(String newActivityState) {
        this.newActivityState = newActivityState;
    }

    /**
     *
     * @return Previous Activity State
     */
    public String getPreviousActivityState() {
        return previousActivityState;
    }

    /**
     *
     * @param previousActivityState Previous Activity State
     */
    public void setPreviousActivityState(String previousActivityState) {
        this.previousActivityState = previousActivityState;
    }

    public String toString() {
        return "ChangeActivityInstanceStateData@" +
            System.identityHashCode(this) + "[" +
            " cwadPrefix=" + getCwadPrefix() +
            ", activityInstanceId=" + activityInstanceId +
            ", activityDefinitionBusinessName='" +
            activityDefinitionBusinessName + "'" +
            ", applicationId=" + applicationId +
            ", newActivityState='" + newActivityState + "'" +
            ", previousActivityState='" + previousActivityState + "'" +
            ", cwadSuffix=" + getCwadSuffix() +
            "]";
    }
}