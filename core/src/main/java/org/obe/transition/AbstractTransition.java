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

package org.obe.transition;

import org.obe.AbstractWFElement;
import org.obe.activity.Activity;
import org.obe.activity.ExecutionType;
import org.obe.condition.Condition;

/**
 * Abstract implementation of a transition.
 *
 * @author Anthony Eden
 * @author Adrian Price
 */
public abstract class AbstractTransition extends AbstractWFElement
    implements Transition {

    static final long serialVersionUID = -6489182908053659185L;

    private Activity toActivity;
    private Activity fromActivity;
    private Event event;
    private ExecutionType executionType;
    private Condition condition;
    private String from;
    private String to;

    /**
     * Construct a new AbstractTransition.
     *
     * @param id The transition ID
     * @param name The transition name
     * @param from The activity ID where the transition starts
     * @param to The activity ID where the transition ends
     */
    public AbstractTransition(String id, String name, String from, String to) {
        super(id, name);

        setFrom(from);
        setTo(to);
    }

    /**
     * Get the condition which is used to determine whether or not the
     * transition should be executed.
     *
     * @return The condition
     */
    public Condition getCondition() {
        return condition;
    }

    /**
     * Set the condition which is used to determine whether or not the
     * transition should be executed.
     *
     * @param condition The condition
     */
    public void setCondition(Condition condition) {
        this.condition = condition;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    /**
     * Get the start activity ID
     *
     * @return The start activity ID
     */
    public String getFrom() {
        return from;
    }

    /**
     * Set the start activity ID.
     *
     * @param from The new start activity ID
     */
    public void setFrom(String from) {
        if (from == null)
            throw new IllegalArgumentException("'from' attribute required");
        if (!from.equals(this.from)) {
            this.from = from;
            this.fromActivity = null;
        }
    }

    /**
     * Get the end activity ID.
     *
     * @return The end activity ID
     */
    public String getTo() {
        return to;
    }

    /**
     * Set the end activity ID.
     *
     * @param to The end activty ID
     */
    public void setTo(String to) {
        if (to == null)
            throw new IllegalArgumentException("'to' attribute required");
        if (!to.equals(this.to)) {
            this.to = to;
            this.toActivity = null;
        }
    }

    public Activity getToActivity() {
        return toActivity;
    }

    public void setToActivity(Activity toActivity) {
        this.toActivity = toActivity;
        this.to = toActivity.getId();
    }

    public Activity getFromActivity() {
        return fromActivity;
    }

    public void setFromActivity(Activity fromActivity) {
        this.fromActivity = fromActivity;
        this.from = fromActivity.getId();
    }

    public ExecutionType getExecution() {
        return executionType;
    }

    public void setExecutionType(ExecutionType executionType) {
        this.executionType = executionType;
    }

    public String toString() {
        return "Transition[id='" + getId() +
            "', executionType=" + executionType +
            ", condition=" + condition +
            ", from='" + from + "'" +
            ", to='" + to + "'" +
            ']';
    }
}