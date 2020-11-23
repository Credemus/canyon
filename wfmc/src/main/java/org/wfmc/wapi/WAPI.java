/*--

 Copyright (C) 2002 Anthony Eden, Adrian Price.
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

 For more information on OBE, please see <http://www.openbusinessengine.org/>.

 */

package org.wfmc.wapi;

/**
 * Enables client applications to connect to and interact with a workflow
 * engine.
 * <p>
 * This interface is based on the WfMC's Interface 2 Client WAPI
 * specification.  Some of the methods have been modified from the original
 * specification to fit within the normal design of Java applications.  For
 * instance, the WfMC specification functions always return an error object
 * (even for success) and uses out parameters to return values.  This
 * interface returns the value and throws an exception when an error occurs.
 * If no error occurs then an exception is not thrown.  The C WAPI uses
 * query handles and iterator functions to retrieve collections; this Java
 * binding uses {@link WMIterator}.
 * @author Anthony Eden
 * @author Adrian Price
 */
public interface WAPI {
    /**
     * Connects to a workflow service.
     *
     * @param connectInfo The connection info.
     * @throws WMWorkflowException Workflow client exception.
     */
    void connect(WMConnectInfo connectInfo) throws WMWorkflowException;

    /**
     * Disconnects from the workflow service.
     *
     * @throws WMWorkflowException Workflow client exception.
     */
    void disconnect() throws WMWorkflowException;

    /**
     * Opens a list of process definitions.  The items in the list can be
     * retrieved sequentially in a typesafe way by calling the iterator's
     * <code>tsNext()</code> method.
     *
     * @param filter The filter or null.
     * @param countFlag True to return count value.
     * @return An iterator to access the {@link WMProcessDefinition} objects.
     * @throws WMWorkflowException Workflow client exception.
     */
    WMProcessDefinitionIterator listProcessDefinitions(WMFilter filter,
        boolean countFlag) throws WMWorkflowException;

    /**
     * Opens a list of process definition states.  The items in the state list
     * can be retrieved sequentially in a typesafe way by calling the iterator's
     * <code>tsNext()</code> method.
     *
     * @param procDefId The unique process definition ID.
     * @param filter The filter or null.
     * @param countFlag True to return count value.
     * @return An iterator to access the {@link WMProcessDefinitionState}
     * objects.
     * @throws WMWorkflowException Workflow client exception.
     */
    WMProcessDefinitionStateIterator listProcessDefinitionStates(
        String procDefId, WMFilter filter, boolean countFlag)
        throws WMWorkflowException;

    /**
     * Changes the process definition state.
     *
     * @param processDefinitionId The process definition id.
     * @param newState The new process definition state.
     * @throws WMWorkflowException Workflow client exception.
     */
    void changeProcessDefinitionState(String processDefinitionId,
        WMProcessDefinitionState newState) throws WMWorkflowException;

    /**
     * Creates a new process instance for the given process definition.
     *
     * @param processDefinitionId The process definition id.
     * @param processInstanceName The name of the process instance.
     * @return The process instance id.
     * @throws WMWorkflowException Workflow client exception.
     */
    String createProcessInstance(String processDefinitionId,
        String processInstanceName) throws WMWorkflowException;

    /**
     * Starts a process instance.  The process instance id is retrieved
     * from a prior call to <code>createProcessInstance()</code>
     *
     * @param processInstanceId The process instance id retrieved in a prior
     * call to <code>createProcessInstance()</code>.
     * @return The new process instance id (which may be the same as the old).
     * @throws WMWorkflowException Workflow client exception.
     */
    String startProcess(String processInstanceId) throws WMWorkflowException;

    /**
     * Terminates a process instance.
     *
     * @param processInstanceId The process instance id.
     * @throws WMWorkflowException Workflow client exception.
     */
    void terminateProcessInstance(String processInstanceId)
        throws WMWorkflowException;

    /**
     * Opens a list of process instance states.  The items in the state list
     * can be retrieved sequentially in a typesafe way by calling the iterator's
     * <code>tsNext()</code> method.
     *
     * @param processInstanceId The unique process instance ID.
     * @param filter The filter or null.
     * @param countFlag True to return count value.
     * @return An iterator to access the {@link WMProcessInstanceState} objects.
     * @throws WMWorkflowException Workflow client exception.
     */
    WMProcessInstanceStateIterator listProcessInstanceStates(
        String processInstanceId, WMFilter filter, boolean countFlag)
        throws WMWorkflowException;

    /**
     * Changes the state of a process instance.
     *
     * @param processInstanceId The process instance id.
     * @param newState The new process instance state.
     * @throws WMWorkflowException Workflow client exception.
     */
    void changeProcessInstanceState(String processInstanceId,
        WMProcessInstanceState newState) throws WMWorkflowException;

    /**
     * Opens a list of process instance attributes.  The items in the
     * attribute list can be retrieved sequentially in a typesafe way by calling
     * the iterator's <code>tsNext()</code> method.
     *
     * @param filter The filter or null.
     * @param countFlag True to return count value.
     * @return An iterator to access the {@link WMAttribute} objects.
     * @throws WMWorkflowException Workflow client exception.
     */
    WMAttributeIterator listProcessInstanceAttributes(String processInstanceId,
        WMFilter filter, boolean countFlag) throws WMWorkflowException;

    /**
     * Gets the specified process instance attribute value.
     *
     * @param processInstanceId The process instance id.
     * @param attributeName The attribute name.
     * @return The attribute.
     * @throws WMWorkflowException Workflow client exception.
     */
    WMAttribute getProcessInstanceAttributeValue(String processInstanceId,
        String attributeName) throws WMWorkflowException;

    /**
     * Sets the specified process instance attribute value.
     *
     * @param processInstanceId The process instance id.
     * @param attributeName The attribute name.
     * @param attributeValue The attribute value.
     * @throws WMWorkflowException Workflow client exception.
     */
    void assignProcessInstanceAttribute(String processInstanceId,
        String attributeName, Object attributeValue) throws WMWorkflowException;

    /**
     * Opens a list of process instance states.  The items in the state list
     * can be retrieved sequentially in a typesafe way by calling the iterator's
     * <code>tsNext()</code> method.
     *
     * @param processInstanceId The process instance id.
     * @param activityInstanceId The activity instance id.
     * @param filter The filter or null.
     * @param countFlag True to return count value.
     * @return An iterator to access the {@link WMActivityInstanceState} objects.
     * @throws WMWorkflowException Workflow client exception.
     */
    WMActivityInstanceStateIterator listActivityInstanceStates(
        String processInstanceId, String activityInstanceId, WMFilter filter,
        boolean countFlag) throws WMWorkflowException;

    /**
     * Changes the state of an activity instance.
     *
     * @param processInstanceId The process instance id.
     * @param activityInstanceId The activity instance id.
     * @param newState The new activity instance state.
     * @throws WMWorkflowException Workflow client exception.
     */
    void changeActivityInstanceState(String processInstanceId,
        String activityInstanceId, WMActivityInstanceState newState)
        throws WMWorkflowException;

    /**
     * Opens a list of activity instance attributes.  The items in the
     * attribute list can be retrieved sequentially in a typesafe way by calling
     * the iterator's <code>tsNext()</code> method.
     *
     * @param processInstanceId The process instance id.
     * @param activityInstanceId The activity instance id.
     * @param filter The filter or null.
     * @param countFlag True to return count value.
     * @return An iterator to access the {@link WMAttribute} objects.
     * @throws WMWorkflowException Workflow client exception.
     */
    WMAttributeIterator listActivityInstanceAttributes(String processInstanceId,
        String activityInstanceId, WMFilter filter, boolean countFlag)
        throws WMWorkflowException;

    /**
     * Gets the value of an activity instance attribute.
     *
     * @param processInstanceId The process instance id.
     * @param activityInstanceId The activity instance id.
     * @param attributeName The attribute name.
     * @return The attribute.
     * @throws WMWorkflowException Workflow client exception.
     */
    WMAttribute getActivityInstanceAttributeValue(String processInstanceId,
        String activityInstanceId, String attributeName)
        throws WMWorkflowException;

    /**
     * Sets the value of an activity instance attribute.
     *
     * @param processInstanceId The process instance id.
     * @param activityInstanceId The activity instance id.
     * @param attributeName The attribute name.
     * @param attributeValue The attribute value.
     * @throws WMWorkflowException Workflow client exception.
     */
    void assignActivityInstanceAttribute(String processInstanceId,
        String activityInstanceId, String attributeName, Object attributeValue)
        throws WMWorkflowException;

    /**
     * Opens a list of process instances.  The items in the list
     * can be retrieved sequentially in a typesafe way by calling the iterator's
     * <code>tsNext()</code> method.
     *
     * @param filter The filter or null.
     * @param countFlag True to return count value.
     * @return An iterator to access the {@link WMProcessInstance} objects.
     * @throws WMWorkflowException Workflow client exception.
     */
    WMProcessInstanceIterator listProcessInstances(WMFilter filter,
        boolean countFlag) throws WMWorkflowException;

    /**
     * Retrieves a process instance.
     *
     * @param processInstanceId The process instance id.
     * @return The process instance.
     * @throws WMWorkflowException Workflow client exception.
     */
    WMProcessInstance getProcessInstance(String processInstanceId)
        throws WMWorkflowException;

    /**
     * Opens a list of activity instances.  The items in the list can be
     * retrieved sequentially in a typesafe way by calling the iterator's
     * <code>tsNext()</code> method.
     *
     * @param filter The filter or null.
     * @param countFlag True to return count value.
     * @return An iterator to access the {@link WMActivityInstance} objects.
     * @throws WMWorkflowException Workflow client exception.
     */
    WMActivityInstanceIterator listActivityInstances(WMFilter filter,
        boolean countFlag) throws WMWorkflowException;

    /**
     * Retrieves an activity instance.
     *
     * @param processInstanceId The process instance id.
     * @param activityInstanceId The activity instance id.
     * @return The activity instance.
     * @throws WMWorkflowException Workflow client exception.
     */
    WMActivityInstance getActivityInstance(String processInstanceId,
        String activityInstanceId) throws WMWorkflowException;

    /**
     * Opens a worklist.  The items in the list can be retrieved
     * sequentially using the iterator's <code>tsNext()</code> method.
     *
     * @param filter The filter or null.
     * @param countFlag True to return count value.
     * @return An iterator to access the {@link WMWorkItem} objects.
     * @throws WMWorkflowException Workflow client exception.
     */
    WMWorkItemIterator listWorkItems(WMFilter filter, boolean countFlag)
        throws WMWorkflowException;

    /**
     * Retrieves a work item.
     *
     * @param processInstanceId The process instance id.
     * @param workItemId The work item id.
     * @return The work item.
     * @throws WMWorkflowException Workflow client exception.
     */
    WMWorkItem getWorkItem(String processInstanceId, String workItemId)
        throws WMWorkflowException;

    /**
     * Completes the specified work item.
     *
     * @param processInstanceId The process instance id.
     * @param workItemId The work item id.
     * @throws WMWorkflowException Workflow client exception.
     */
    void completeWorkItem(String processInstanceId, String workItemId)
        throws WMWorkflowException;

    /**
     * Opens a list of work item states.  The items in the
     * work item states list can be retrieved sequentially in a typesafe way by
     * calling the iterator's <code>tsNext()</code> method.
     * <p/>
     * N.B. This function is poorly documented in the WfMC specification, which
     * contains several 'copy/paste' errors.
     * <p/>
     * N.B. The signature of this method differs from that described in the
     * WAPI2 specification, in that it has a processInstanceId parameter.  This
     * is because the specification's definition for this function is clearly in
     * error, having been copied badly from that for
     * WMOpenProcessDefinitionStatesList. The other WAPI functions that refer to
     * work items invariably require the processInstanceId parameter.
     *
     * @param processInstanceId The process instance id.
     * @param workItemId The process instance id.
     * @param filter The filter or null.
     * @param countFlag True to return count value.
     * @return An iterator to access the {@link WMWorkItemState} objects.
     * @throws WMWorkflowException Workflow client exception.
     */
    WMWorkItemStateIterator listWorkItemStates(String processInstanceId,
        String workItemId, WMFilter filter, boolean countFlag)
        throws WMWorkflowException;

    /**
     * Changes the state of a work item.
     * N.B. The signature of this method differs from that described in the
     * WAPI2 specification, in that it has a processInstanceId parameter.  This
     * is because the specification's definition for this function is clearly in
     * error, having been badly copied from that for
     * WMChangeDefinitionState. The other WAPI functions that refer to
     * work items invariably require the processInstanceId parameter.
     * @param processInstanceId The process instance id.
     * @param workItemId The work item id.
     * @param newState The new work item state.
     * @throws WMWorkflowException Workflow client exception.
     */
    void changeWorkItemState(String processInstanceId, String workItemId,
        WMWorkItemState newState)throws WMWorkflowException;

    /**
     * Reassigns a work item to another user.
     *
     * @param sourceUser The current user.
     * @param targetUser The new user.
     * @param processInstanceId The process instance id.
     * @param workItemId The work item id.
     * @throws WMWorkflowException Workflow client exception.
     */
    void reassignWorkItem(String sourceUser, String targetUser,
        String processInstanceId, String workItemId) throws WMWorkflowException;

    /**
     * Opens a list of work item attributes.  The items in the
     * attribute list can be retrieved sequentially in a typesafe way by calling
     * the iterator's <code>tsNext()</code> method.
     *
     * @param processInstanceId The process instance id.
     * @param workItemId The work item id.
     * @param filter The filter or null.
     * @param countFlag True to return count value.
     * @return An iterator to access the {@link WMAttribute} objects.
     * @throws WMWorkflowException Workflow client exception.
     */
    WMAttributeIterator listWorkItemAttributes(String processInstanceId,
        String workItemId, WMFilter filter, boolean countFlag)
        throws WMWorkflowException;

    /**
     * Retrieves the value of a work item attribute.
     *
     * @param processInstanceId The process instance id.
     * @param workItemId The work item id.
     * @param attributeName The attribute name.
     * @return The attribute.
     * @throws WMWorkflowException Workflow client exception.
     */
    WMAttribute getWorkItemAttributeValue(String processInstanceId,
        String workItemId, String attributeName) throws WMWorkflowException;

    /**
     * Sets the value of a work item attribute.
     *
     * @param processInstanceId The process instance id.
     * @param workItemId The work item id.
     * @param attributeName The attribute name.
     * @param attributeValue The attribute value.
     * @throws WMWorkflowException Workflow client exception.
     */
    void assignWorkItemAttribute(String processInstanceId, String workItemId,
        String attributeName, Object attributeValue) throws WMWorkflowException;

    /**
     * Changes the state of selected process instances.
     *
     * @param processDefinitionId The ID of the process definition for which
     * instances are to be changed.
     * @param filter A filter specification; can be <code>null</code>.
     * @param newState The new state to apply.
     * @throws WMWorkflowException Workflow client exception.
     */
    void changeProcessInstancesState(String processDefinitionId,
        WMFilter filter, WMProcessInstanceState newState)
        throws WMWorkflowException;

    /**
     * Changes the state of selected activity instances.
     *
     * @param processDefinitionId The ID of the process definition for which
     * activity instances are to be changed.
     * @param activityDefinitionId The ID of the activity definition for which
     * instances are to be changed.
     * @param filter A filter specification; can be <code>null</code>.
     * @param newState The new state to apply.
     * @throws WMWorkflowException Workflow client exception.
     */
    void changeActivityInstancesState(String processDefinitionId,
        String activityDefinitionId, WMFilter filter,
        WMActivityInstanceState newState) throws WMWorkflowException;

    /**
     * Terminates a group of process instances.
     *
     * @param processDefinitionId The ID of the process definition for which
     * instances are to be terminated.
     * @param filter A filter specification; can be <code>null</code>.
     * @throws WMWorkflowException Workflow client exception.
     */
    void terminateProcessInstances(String processDefinitionId, WMFilter filter)
        throws WMWorkflowException;

    /**
     * Assigns an attribute value for a group of process instances.
     * @param processDefinitionId The ID of the process definition for which
     * instance attributes are to be assigned.
     * @param filter A filter specification; can be <code>null</code>.
     * @param attributeName The attribute name.
     * @param attributeValue The attribute value.
     * @throws WMWorkflowException Workflow client exception.
     */
    void assignProcessInstancesAttribute(String processDefinitionId,
        WMFilter filter, String attributeName, Object attributeValue)
        throws WMWorkflowException;

    /**
     * Assigns an attribute value for a group of process instances.
     *
     * @param processDefinitionId The ID of the process definition for which
     * activity instance attributes are to be assigned.
     * @param activityDefinitionId The ID of the activity definition for which
     * instance attributes are to be assigned.
     * @param filter A filter specification; can be <code>null</code>.
     * @param attributeName The attribute name.
     * @param attributeValue The attribute value.
     * @throws WMWorkflowException Workflow client exception.
     */
    void assignActivityInstancesAttribute(String processDefinitionId,
        String activityDefinitionId, WMFilter filter, String attributeName,
        Object attributeValue) throws WMWorkflowException;

    /**
     * Aborts a group of process instances.
     *
     * @param processDefinitionId The ID of the process definition for which
     * instances are to be aborted.
     * @param filter A filter specification; can be <code>null</code>.
     * @throws WMWorkflowException Workflow client exception.
     */
    void abortProcessInstances(String processDefinitionId, WMFilter filter)
        throws WMWorkflowException;

    /**
     * Aborts a process instance.
     *
     * @param processInstanceId The ID of the process instance to abort.
     * @throws WMWorkflowException Workflow client exception.
     */
    void abortProcessInstance(String processInstanceId)
        throws WMWorkflowException;

    /**
     * Invokes a client-side application or tool.
     *
     * @param toolAgentHandle
     * @param applicationName The tag name of the application or tool.
     * @param processInstanceId The ID of the associated process instance.
     * @param workItemId The ID of the associated work item.
     * @param parameters Parameters to pass to the application.
     * @param appMode Application mode, one of:
     * @throws WMWorkflowException Workflow client exception.
     */
    void invokeApplication(int toolAgentHandle, String applicationName,
        String processInstanceId, String workItemId, Object[] parameters,
        int appMode) throws WMWorkflowException;

    /**
     * Requests the status of an invoked application.
     *
     * @param toolAgentHandle The application handle, returned by the prior call
     * to {@link #invokeApplication}.
     * @param processInstanceId The ID of the associated process instance.
     * @param workItemId The ID of the associated work item.
     * @param workflowRelevantData
     * @return
     * @throws WMWorkflowException Workflow client exception.
     */
    int requestAppStatus(int toolAgentHandle, String processInstanceId,
        String workItemId, WMAttribute[] workflowRelevantData)
        throws WMWorkflowException;

    /**
     * Terminates a running application.
     *
     * @param toolAgentHandle
     * @param processInstanceId The ID of the associated process instance.
     * @param workItemId The ID of the associated work item.
     * @throws WMWorkflowException Workflow client exception.
     */
    void terminateApp(int toolAgentHandle, String processInstanceId,
        String workItemId) throws WMWorkflowException;
}