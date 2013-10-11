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

package de.objectcode.canyon.spiImpl.event;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.objectcode.canyon.model.activity.Activity;
import de.objectcode.canyon.spi.event.ActivityInstanceEvent;
import de.objectcode.canyon.spi.event.ApplicationEvent;
import de.objectcode.canyon.spi.event.IActivityInstanceListener;
import de.objectcode.canyon.spi.event.IWorkflowEventBroker;
import de.objectcode.canyon.spi.instance.IActivityInstance;

/**
 * Supports activity instance listeners.  This class maintains a list of
 * {@link org.obe.spi.event.ActivityInstanceListener}s, and provides a set of
 * <code>fireActivityInstance&lt;Event&gt;({@link org.obe.spi.model.ActivityInstance} source,
 * {@link Activity} definition)</code> methods to notify the listeners of
 * events.
 *
 * @author Adrian Price
 */
public final class ActivityInstanceListenerSupport
    extends AbstractListenerSupport {

    private static final Log _logger = LogFactory.getLog(
        ActivityInstanceListenerSupport.class);
    private static final String[] NOTIFICATION_METHODS = {
        "activityInstanceAborted",
        "activityInstanceCompleted",
        "activityInstanceCreated",
        "activityInstanceResumed",
        "activityInstanceStarted",
        "activityInstanceStopped",
        "activityInstanceSuspended",
        "activityInstanceTerminated"
    };

    public ActivityInstanceListenerSupport(IWorkflowEventBroker eventBroker) {
        super(eventBroker, ActivityInstanceEvent.class,
            IActivityInstanceListener.class, NOTIFICATION_METHODS);
    }

    public void fireActivityInstanceEvent(IActivityInstance src, int id,
        Activity defn) {

        fire(src, id, defn);
    }

    public void fireActivityInstanceAborted(IActivityInstance src,
        Activity defn) {

        fireEvent(createActivityInstanceAborted(src, defn));
    }

    public ApplicationEvent createActivityInstanceAborted(IActivityInstance src,
        Activity defn) {
        return createEvent(src, ActivityInstanceEvent.ABORTED, defn);
    }

    public void fireActivityInstanceCompleted(IActivityInstance src,
        Activity defn) {

        fireEvent(createActivityInstanceCompleted(src, defn));
    }

    public ApplicationEvent  createActivityInstanceCompleted(IActivityInstance src,
        Activity defn) {

        return createEvent(src, ActivityInstanceEvent.COMPLETED, defn);
    }

    public void fireActivityInstanceCreated(IActivityInstance src,
        Activity defn) {

        fireEvent(createActivityInstanceCreated(src, defn));
    }

    public ApplicationEvent createActivityInstanceCreated(IActivityInstance src,
        Activity defn) {

        return createEvent(src, ActivityInstanceEvent.CREATED, defn);
    }

    public void fireActivityInstanceResumed(IActivityInstance src,
        Activity defn) {

        fireEvent(createActivityInstanceResumed(src, defn));
    }

    public ApplicationEvent createActivityInstanceResumed(IActivityInstance src,
        Activity defn) {

        return createEvent(src, ActivityInstanceEvent.RESUMED, defn);
    }
    

    public void fireActivityInstanceStarted(IActivityInstance src,
        Activity defn) {

        fireEvent(createActivityInstanceStarted(src, defn));
    }

    public ApplicationEvent createActivityInstanceStarted(IActivityInstance src,
        Activity defn) {

        return createEvent(src, ActivityInstanceEvent.STARTED, defn);
    }

    
    public void fireActivityInstanceStopped(IActivityInstance src,
        Activity defn) {

        fireEvent(createActivityInstanceStopped(src, defn));
    }

    public ApplicationEvent createActivityInstanceStopped(IActivityInstance src,
        Activity defn) {

        return createEvent(src, ActivityInstanceEvent.STOPPED, defn);
    }

    public void fireActivityInstanceSuspended(IActivityInstance src,
        Activity defn) {

        fireEvent(createActivityInstanceSuspended(src, defn));
    }

    public ApplicationEvent createActivityInstanceSuspended(IActivityInstance src,
        Activity defn) {

        return createEvent(src, ActivityInstanceEvent.SUSPENDED, defn);
    }

    public void fireActivityInstanceTerminated(IActivityInstance src,
        Activity defn) {

        fireEvent(createActivityInstanceTerminated(src, defn));
    }

    public ApplicationEvent createActivityInstanceTerminated(IActivityInstance src,
        Activity defn) {

        return createEvent(src, ActivityInstanceEvent.TERMINATED, defn);
    }

    public Log getLogger() {
        return _logger;
    }
}