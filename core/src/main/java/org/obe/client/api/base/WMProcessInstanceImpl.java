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

package org.obe.client.api.base;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;

import org.obe.client.api.model.OBEProcessInstance;
import org.wfmc.wapi.WMParticipant;
import org.wfmc.wapi.WMProcessInstanceState;

public class WMProcessInstanceImpl
    implements OBEProcessInstance, Serializable {

    static final long serialVersionUID = -2212490072805582089L;
    private String _name;
    private String _id;
    private String _processDefinitionId;
    private WMProcessInstanceState _state;
    private int _priority;
    private WMParticipant[] _participants;
    private Date _completedDate;
    private Date _createdDate;
    private Date _dueDate;
    private Date _startedDate;

    public WMProcessInstanceImpl() {
    }

    public WMProcessInstanceImpl(String name, String id,
        String processDefinitionId, WMProcessInstanceState state,
        int priority, WMParticipant[] participants,
        Date completedDate, Date createdDate, Date dueDate, Date startedDate) {

        _name = name;
        _id = id;
        _processDefinitionId = processDefinitionId;
        _state = state;
        _priority = priority;
        _participants = participants;
        _completedDate = completedDate;
        _createdDate = createdDate;
        _dueDate = dueDate;
        _startedDate = startedDate;
    }

    public String getName() {
        return _name;
    }

    public String getId() {
        return _id;
    }

    public String getProcessDefinitionId() {
        return _processDefinitionId;
    }

    public WMProcessInstanceState getState() {
        return _state;
    }

    public int getPriority() {
        return _priority;
    }

    public WMParticipant[] getParticipants() {
        return _participants;
    }

    public Date getCompletedDate() {
        return _completedDate;
    }

    public Date getCreatedDate() {
        return _createdDate;
    }

    public Date getDueDate() {
        return _dueDate;
    }

    public String getProcessInstanceId() {
        return _id;
    }

    public Date getStartedDate() {
        return _startedDate;
    }

    protected void setParticipants(WMParticipant[] participants) {
        _participants = participants;
    }

    protected void setName(String name) {
        _name = name;
    }

    protected void setId(String id) {
        _id = id;
    }

    protected void setProcessDefinitionId(String processDefinitionId) {
        _processDefinitionId = processDefinitionId;
    }

    protected void setState(String state) {
        _state = WMProcessInstanceState.fromTag(state);
    }

    protected void setState(WMProcessInstanceState state) {
        _state = state;
    }

    public void setPriority(Integer priority) {
        _priority = priority.intValue();
    }

    public String toString() {
        return "WMProcessInstance[_completedDate=" + _completedDate
            + ", _name='" + _name + "'"
            + ", _id='" + _id + "'"
            + ", _processDefinitionId='" + _processDefinitionId + "'"
            + ", _state=" + _state
            + ", _priority=" + _priority
            + ", _participants=" + (_participants == null ? null : "length:" + _participants.length + Arrays.asList(_participants))
            + ", _createdDate=" + _createdDate
            + ", _dueDate=" + _dueDate
            + ", _startedDate=" + _startedDate
            + ']';
    }
}