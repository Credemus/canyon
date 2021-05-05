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

package org.obe.workflow;

import org.obe.AccessLevel;
import org.obe.Package;
import org.obe.RedefinableHeader;
import org.obe.WFElement;
import org.obe.activity.Activity;
import org.obe.activity.ActivitySet;
import org.obe.application.Application;
import org.obe.data.DataField;
import org.obe.data.FormalParameter;
import org.obe.participant.Participant;
import org.obe.transition.EventType;
import org.obe.transition.Transition;

import java.util.List;

public interface WorkflowProcess extends WFElement {
    String getPackageId();
    String getProcessDefinitionId();
    String getDescription();
    int getState();

    Package getPackage();
    void setPackage(Package pkg);

    AccessLevel getAccessLevel();
    void setAccessLevel(AccessLevel accessLevel);

    ProcessHeader getProcessHeader();
    void setProcessHeader(ProcessHeader processHeader);

    RedefinableHeader getRedefinableHeader();
    void setRedefinableHeader(RedefinableHeader redefinableHeader);

    List<FormalParameter> getFormalParameters();

    List<DataField> getDataFields();

    List<Participant> getParticipants();

    List<Application> getApplications();

    List<EventType> getEventTypes();

    List<Activity> getActivities();

    List<ActivitySet> getActivitySets();

    /**
     * Returns a list of transitions in the workflow.  <b>N.B. If adding a
     * transition to this list, be sure also to add it to the appropriate
     * efferent and afferent transition lists of the 'from' and 'to' activities
     * respectively.
     * @return Transition list.
     * @see org.obe.activity.Activity#getAfferentTransitions()
     * @see org.obe.activity.Activity#getEfferentTransitions()
     */
    List<Transition> getTransitions();
}