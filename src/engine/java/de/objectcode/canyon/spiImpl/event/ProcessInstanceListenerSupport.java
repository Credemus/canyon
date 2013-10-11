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

import de.objectcode.canyon.bpe.repository.ProcessInstance;
import de.objectcode.canyon.model.process.WorkflowProcess;
import de.objectcode.canyon.spi.event.ApplicationEvent;
import de.objectcode.canyon.spi.event.IProcessInstanceListener;
import de.objectcode.canyon.spi.event.IWorkflowEventBroker;
import de.objectcode.canyon.spi.event.ProcessInstanceEvent;
import de.objectcode.canyon.spi.instance.IProcessInstance;

/**
 * Supports process instance listeners.  This class maintains a list of
 * {@link ProcessInstanceListener}s, and provides a set of
 * <code>fireProcessInstance&lt;Event&gt;({@link ProcessInstance} source,
 * {@link WorkflowProcess} definition)</code> methods to notify the listeners of
 * events.
 *
 * @author Adrian Price
 */
public final class ProcessInstanceListenerSupport
    extends AbstractListenerSupport {

    private static final Log _logger = LogFactory.getLog(
        ProcessInstanceListenerSupport.class);

    private static final String[] NOTIFICATION_METHODS = {
        "processInstanceAborted",
        "processInstanceCompleted",
        "processInstanceCreated",
        "processInstanceDeleted",
        "processInstanceResumed",
        "processInstanceStarted",
        "processInstanceSuspended",
        "processInstanceTerminated"
    };

    public ProcessInstanceListenerSupport(IWorkflowEventBroker eventBroker) {
        super(eventBroker, ProcessInstanceEvent.class,
            IProcessInstanceListener.class, NOTIFICATION_METHODS);
    }

    public void fireProcessInstanceEvent(IProcessInstance src, int id,
        WorkflowProcess defn) {

        fire(src, id, defn);
    }

    public void fireProcessInstanceAborted(IProcessInstance src,
        WorkflowProcess defn) {

        fireEvent(createProcessInstanceAborted(src,defn));
    }

    public ApplicationEvent createProcessInstanceAborted(IProcessInstance src,
        WorkflowProcess defn) {

        return createEvent(src, ProcessInstanceEvent.ABORTED, defn);
    }

    public void fireProcessInstanceCompleted(IProcessInstance src,
        WorkflowProcess defn) {

        fireEvent(createProcessInstanceCompleted(src,defn));
    }

    public ApplicationEvent createProcessInstanceCompleted(IProcessInstance src,
        WorkflowProcess defn) {

        return createEvent(src, ProcessInstanceEvent.COMPLETED, defn);
    }

    public void fireProcessInstanceCreated(IProcessInstance src,
        WorkflowProcess defn) {

        fireEvent(createProcessInstanceCreated(src,defn));
    }

    public ApplicationEvent createProcessInstanceCreated(IProcessInstance src,
        WorkflowProcess defn) {

        return createEvent(src, ProcessInstanceEvent.CREATED, defn);
    }

    public void fireProcessInstanceDeleted(IProcessInstance src,
        WorkflowProcess defn) {

        fireEvent(createProcessInstanceDeleted(src,defn));
    }

    public ApplicationEvent createProcessInstanceDeleted(IProcessInstance src,
        WorkflowProcess defn) {

        return createEvent(src, ProcessInstanceEvent.DELETED, defn);
    }

    public void fireProcessInstanceResumed(IProcessInstance src,
        WorkflowProcess defn) {

        fireEvent(createProcessInstanceResumed(src,defn));
    }

    public ApplicationEvent createProcessInstanceResumed(IProcessInstance src,
        WorkflowProcess defn) {

        return createEvent(src, ProcessInstanceEvent.RESUMED, defn);
    }

    public void fireProcessInstanceStarted(IProcessInstance src,
        WorkflowProcess defn) {

        fireEvent(createProcessInstanceStarted(src,defn));
    }

    public ApplicationEvent createProcessInstanceStarted(IProcessInstance src,
        WorkflowProcess defn) {

        return createEvent(src, ProcessInstanceEvent.STARTED, defn);
    }

    public void fireProcessInstanceSuspended(IProcessInstance src,
        WorkflowProcess defn) {

        fireEvent(createProcessInstanceSuspended(src,defn));
    }

    public ApplicationEvent createProcessInstanceSuspended(IProcessInstance src,
        WorkflowProcess defn) {

        return createEvent(src, ProcessInstanceEvent.SUSPENDED, defn);
    }

    public void fireProcessInstanceTerminated(IProcessInstance src,
        WorkflowProcess defn) {

        fireEvent(createProcessInstanceTerminated(src,defn));
    }

    public ApplicationEvent createProcessInstanceTerminated(IProcessInstance src,
        WorkflowProcess defn) {

        return createEvent(src, ProcessInstanceEvent.TERMINATED, defn);
    }

    public Log getLogger() {
        return _logger;
    }
}