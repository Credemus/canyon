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
package de.objectcode.canyon.spi.event;

import org.wfmc.audit.WMAEventCode;

import de.objectcode.canyon.model.transition.Transition;
import de.objectcode.canyon.spi.instance.IActivityInstance;

/**
 * Delivers transition event notifications.
 *
 * @author Adrian Price
 */
public final class TransitionEvent extends WorkflowEvent {
    static final long serialVersionUID = -5960088342111172827L;
    /** The transition fired. */
    public static final int FIRED = 0;
    private static final String[] EVENT_TYPES = {
        "TransitionFired"
    };

    private transient Transition _definition;

    /**
     * Constructs a new transition event.
     *
     * @param activityInstance
     * @param id
     * @param broker
     * @param definition
     */
    public TransitionEvent(IActivityInstance activityInstance, int id,
        IWorkflowEventBroker broker, Transition definition) {

        super(activityInstance, id, String.class, EVENT_TYPES[id],
            definition.getId(), broker);

        _definition = definition;
    }

    /**
     * Returns the transition ID.
     *
     * @return The ID of the transition that fired the event.
     */
    public String getTransitionId() {
        return (String)getKeys()[0];
    }

    /**
     * Returns the target activity instance.
     *
     * @return Target activity instance.
     */
    public IActivityInstance getActivityInstance() {
        return (IActivityInstance)source;
    }

    /**
     * Returns the definition of the transition.
     *
     * @return The transition definition from the workflow process definition.
     */
    public Transition getTransition() {
        return _definition;
    }

    public WMAEventCode getWMAEventCode() {
        return null;
    }

    public String toString() {
        return EVENT_TYPES[_id] + "[source=" + source + ", definition=" +
            _definition + ']';
    }
}