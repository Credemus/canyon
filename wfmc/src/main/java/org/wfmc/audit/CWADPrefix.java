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

import java.io.Serializable;
import java.util.Date;

/**
 * Object representation of a WfMC audit prefix.
 * <p><b>Note:</b> All ids are limited to 64 characters.
 *
 * @author Anthony Eden
 */
public class CWADPrefix implements Serializable {
    static final long serialVersionUID = -8136551872480997981L;

    private String initialProcessInstanceId;
    private String currentProcessInstanceId;
    private String activityInstanceId;
    private String processState;
    private WMAEventCode eventCode;
    private String domainId;
    private String nodeId;
    private String userId;
    private String roleId;
    private Date timestamp;
    private String informationId;

    public CWADPrefix() {
    }

    /**
     *
     * @param initialProcessInstanceId
     * @param currentProcessInstanceId
     * @param activityInstanceId
     * @param processState
     * @param eventCode
     * @param domainId
     * @param nodeId
     * @param userId
     * @param roleId
     * @param timestamp
     * @param informationId
     */
    public CWADPrefix(String initialProcessInstanceId,
        String currentProcessInstanceId, String activityInstanceId,
        String processState, WMAEventCode eventCode, String domainId,
        String nodeId, String userId, String roleId,
        Date timestamp, String informationId) {

        this.initialProcessInstanceId = initialProcessInstanceId;
        this.currentProcessInstanceId = currentProcessInstanceId;
        this.activityInstanceId = activityInstanceId;
        this.processState = processState;
        this.eventCode = eventCode;
        this.domainId = domainId;
        this.nodeId = nodeId;
        this.userId = userId;
        this.roleId = roleId;
        this.timestamp = timestamp;
        this.informationId = informationId;
    }

    /**
     * Get the initial (root) process instance Id.
     * @return The initial process instance Id
     */
    public String getInitialProcessInstanceId() {
        return initialProcessInstanceId;
    }

    /**
     * Set the initial (root) process instance Id.
     * @param initialProcessInstanceId The initial process instance Id
     */
    public void setInitialProcessInstanceId(
        String initialProcessInstanceId) {

        this.initialProcessInstanceId = initialProcessInstanceId;
    }

    /**
     * Get the current process instance Id.  The string is limited to 64
     * characters according to the WfMC Interface 5 specification.
     * @return The current process instance Id
     */
    public String getCurrentProcessInstanceId() {
        return currentProcessInstanceId;
    }

    /**
     * Set the current process instance Id.
     *
     * @param currentProcessInstanceId The current process instance Id
     */
    public void setCurrentProcessInstanceId(
        String currentProcessInstanceId) {

        this.currentProcessInstanceId = currentProcessInstanceId;
    }

    /**
     * Get the activity instance Id.  The activity instance Id is optional so
     * this method may return null.
     *
     * @return The activity instance Id
     */
    public String getActivityInstanceId() {
        return activityInstanceId;
    }

    /**
     * Set the activity instance Id.  The value can be null if the activity
     * instance Id is not specified.
     *
     * @param activityInstanceId The activity instance Id
     */
    public void setActivityInstanceId(String activityInstanceId) {
        this.activityInstanceId = activityInstanceId;
    }

    /**
     * Get the process state.
     *
     * @return The process state
     */
    public String getProcessState() {
        return processState;
    }

    /**
     * Set the process state.
     * @param processState The process state
     */
    public void setProcessState(String processState) {
        this.processState = processState;
    }

    /**
     * Get the event code.
     *
     * @return The event code
     */
    public WMAEventCode getEventCode() {
        return eventCode;
    }

    /**
     * Set the event code.
     *
     * @param eventCode The new event code
     */
    public void setEventCode(WMAEventCode eventCode) {
        this.eventCode = eventCode;
    }

    public String getDomainId() {
        return domainId;
    }

    public void setDomainId(String domainId) {
        this.domainId = domainId;
    }

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getInformationId() {
        return informationId;
    }

    public void setInformationId(String informationId) {
        this.informationId = informationId;
    }

    public String toString() {
        return "CWADPrefix@" + System.identityHashCode(this) + "{" +
            "initialProcessInstanceId=" + initialProcessInstanceId +
            ", currentProcessInstanceId=" + currentProcessInstanceId +
            ", activityInstanceId=" + activityInstanceId +
            ", processState='" + processState + "'" +
            ", eventCode=" + eventCode +
            ", domainId=" + domainId +
            ", nodeId=" + nodeId +
            ", userId=" + userId +
            ", roleId=" + roleId +
            ", timestamp=" + timestamp +
            ", informationId=" + informationId +
            "}";
    }
}