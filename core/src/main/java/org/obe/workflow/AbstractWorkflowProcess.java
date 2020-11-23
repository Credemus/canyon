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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.obe.AbstractWFElement;
import org.obe.AccessLevel;
import org.obe.Package;
import org.obe.RedefinableHeader;
import org.obe.activity.Activity;
import org.obe.activity.ActivitySet;
import org.obe.activity.BlockActivity;
import org.obe.transition.Transition;

public abstract class AbstractWorkflowProcess extends AbstractWFElement
    implements WorkflowProcess {

    static final long serialVersionUID = -2304468508983567539L;

    private Package pkg;
    private ProcessHeader processHeader;
    private RedefinableHeader redefinableHeader;
    private List formalParameters = new ArrayList();
    private List dataFields = new ArrayList();
    private List eventTypes = new ArrayList();
    private List participants = new ArrayList();
    private List applications = new ArrayList();
    private List activities = new ArrayList();
    private List activitySets = new ArrayList();
    private List transitions = new ArrayList();
    private AccessLevel accessLevel = AccessLevel.PUBLIC;
    private int state = 1; // (WMProcessDefinitionState.ENABLED_INT)

    /** Construct a new AbstractWorkflowProcess.

     @param id The id
     @param name The name
     @param processHeader The Workflow process header
     */

    public AbstractWorkflowProcess(String id, String name, Package pkg,
        ProcessHeader processHeader) {

        super(id, name);
        setProcessHeader(processHeader);
        this.pkg = pkg;
    }

    public String getPackageId() {
        return pkg == null ? null : pkg.getId();
    }

    public String getProcessDefinitionId() {
        return getId();
    }

    public Package getPackage() {
        return pkg;
    }

    public void setPackage(Package pkg) {
        this.pkg = pkg;
    }

    /** Get the workflow process access level.

     @return The access level
     */

    public AccessLevel getAccessLevel() {
        return accessLevel;
    }

    /** Set the workflow process access level.

     @param accessLevel The access level
     */

    public void setAccessLevel(AccessLevel accessLevel) {
        if (accessLevel != null) {
            this.accessLevel = accessLevel;
        }
    }

    /** Return the description specified in the process header.

     @return The process header description
     */

    public String getDescription() {
        return processHeader.getDescription();
    }

    public void setDescription(String description) {
        processHeader.setDescription(description);
    }

    public ProcessHeader getProcessHeader() {
        return processHeader;
    }

    public void setProcessHeader(ProcessHeader processHeader) {
        if (processHeader == null) {
            throw new IllegalArgumentException("ProcessHeader must not be null");
        }

        this.processHeader = processHeader;
    }

    public RedefinableHeader getRedefinableHeader() {
        return redefinableHeader;
    }

    public void setRedefinableHeader(RedefinableHeader redefinableHeader) {
        this.redefinableHeader = redefinableHeader;
    }

    public List getFormalParameters() {
        return formalParameters;
    }

    public List getDataFields() {
        return dataFields;
    }

    public List getParticipants() {
        return participants;
    }

    public List getApplications() {
        return applications;
    }

    public List getEventTypes() {
        return eventTypes;
    }

    public List getActivities() {
        return activities;
    }

    public List getActivitySets() {
        return activitySets;
    }

    public List getTransitions() {
        return transitions;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public void resolveReferences() {
        resolveActivitySets(getActivitySets(), getActivities(), true);
        resolveTransitions(getActivitySets(), getActivities(),
            getTransitions());
    }

    // 'Connects up' each BlockActivity to its ActivitySet.
    private void resolveActivitySets(List activitySets, List activities,
        boolean recursive) {

        // Resolve ActivitySet references from the activities collection.
        for (Iterator i = activities.iterator(); i.hasNext();) {
            Activity activity = (Activity)i.next();
            BlockActivity ba = activity.getBlockActivity();
            if (ba != null) {
                for (Iterator j = activitySets.iterator(); j.hasNext();) {
                    ActivitySet activitySet = (ActivitySet)j.next();
                    if (activitySet.getId().equals(ba.getBlockId())) {
                        ba.setActivitySet(activitySet);
                        break;
                    }
                }
                if (ba.getActivitySet() == null) {
                    throw new IllegalStateException("ActivitySet not found: " +
                        ba.getBlockId());
                }
            }
        }

        if (recursive) {
            // Resolve ActivitySet references from each of the ActivitySets.
            for (Iterator i = activitySets.iterator(); i.hasNext();) {
                ActivitySet activitySet = (ActivitySet)i.next();
                resolveActivitySets(activitySets, activitySet.getActivities(),
                    false);
            }
        }
    }

    // 'Connects up' each activity to its transitions.
    private void resolveTransitions(List activitySets, List activities,
        List transitions) {

        for (Iterator i = activities.iterator(); i.hasNext();) {
            Activity activity = (Activity)i.next();
            String activityId = activity.getId();
            Map afferentTransitions = activity.getAfferentTransitions();
            Map efferentTransitions = activity.getEfferentTransitions();
            afferentTransitions.clear();
            efferentTransitions.clear();
            for (Iterator j = transitions.iterator(); j.hasNext();) {
                Transition transition = (Transition)j.next();
                if (transition.getFrom().equals(activityId)) {
                    transition.setFromActivity(activity);
                    efferentTransitions.put(transition.getId(), transition);
                }
                if (transition.getTo().equals(activityId)) {
                    transition.setToActivity(activity);
                    afferentTransitions.put(transition.getId(), transition);
                }
            }
        }

        if (activitySets != null) {
            for (Iterator i = activitySets.iterator(); i.hasNext();) {
                ActivitySet activitySet = (ActivitySet)i.next();
                resolveTransitions(null, activitySet.getActivities(),
                    activitySet.getTransitions());
            }
        }
    }
}