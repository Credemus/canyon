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

import de.objectcode.canyon.CanyonRuntimeException;
import de.objectcode.canyon.model.process.DataField;
import de.objectcode.canyon.model.process.WorkflowProcess;
import de.objectcode.canyon.spi.RepositoryException;
import de.objectcode.canyon.spi.instance.IAttributeInstance;
import de.objectcode.canyon.spi.process.IProcessRepository;

/**
 * Delivers attribute instance event notifications.
 *
 * @author Adrian Price
 */
public final class AttributeInstanceEvent extends WorkflowEvent {
    static final long serialVersionUID = -4732580987634759448L;
    /** The attribute was created. */
    public static final int CREATED = 0;
    /** The attribute was deleted. */
    public static final int DELETED = 1;
    /** The attribute was updated. */
    public static final int UPDATED = 2;
    private static final WMAEventCode[] IF5_EVENT_CODES = {
        null,
        null,
        WMAEventCode.ASSIGNED_PROCESS_INSTANCE_ATTRIBUTE
    };
    private static final String[] EVENT_TYPES = {
        "AttributeInstanceCreated",
        "AttributeInstanceDeleted",
        "AttributeInstanceUpdated"
    };
    private final Object _oldValue;
    private transient DataField _definition;

    // Caution!  The event framework requires this constructor to be the first.
    public AttributeInstanceEvent(IAttributeInstance source, int id,
        IWorkflowEventBroker broker, DataField definition) {

        this(source, id, broker, null, definition);
    }

    // TODO: extend event framework to handle attribute updated events fully.
    public AttributeInstanceEvent(IAttributeInstance source, int id,
        IWorkflowEventBroker broker, Object oldValue, DataField definition) {

        super(source, id, IAttributeInstance.class, EVENT_TYPES[id],
            source.getName(), broker);
        _oldValue = oldValue;
        _definition = definition;
    }

    /**
     * Returns the source attribute instance entity.
     *
     * @return The attribute instance that fired the event.
     */
    public IAttributeInstance getAttributeInstance() {
        return (IAttributeInstance)source;
    }

    /**
     * Returns the definition of the source attribute.
     *
     * @return The data field definition from the workflow process definition
     * or package.
     */
    public DataField getDataField() {
        if (_definition == null) {
            IProcessRepository processRepository =
                _broker.getServiceManager().getProcessRepository();
            try {
                IAttributeInstance attr = getAttributeInstance();
                WorkflowProcess workflow =
                    processRepository.findWorkflowProcess(
                        attr.getOwner().getProcessDefinitionId());
                _definition = workflow.findDataField(
                    attr.getName());
            } catch (RepositoryException e) {
                throw new CanyonRuntimeException(e);
            }
        }
        return _definition;
    }

    /**
     * Returns the value of the attribute prior to this update. <i>N.B. This
     * method has not yet been implemented.</i>
     *
     * @return Old attribute value.
     */
    public Object getOldValue() {
        return _oldValue;
    }

    /**
     * Returns the new value of the attribute.
     *
     * @return New attribute value.
     */
    public Object getNewValue() {
        return getAttributeInstance().getValue();
    }

    public WMAEventCode getWMAEventCode() {
        // N.B. This only works correctly for process instance attributes.
        return IF5_EVENT_CODES[_id];
    }

    public String toString() {
        return EVENT_TYPES[_id] + "[source=" + source + ", definition=" +
            _definition + ']';
    }
}