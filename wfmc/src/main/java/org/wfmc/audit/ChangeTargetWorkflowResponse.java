package org.wfmc.audit;

/**
 *
 * @author Antony Lodge
 */
public class ChangeTargetWorkflowResponse extends SourceWorkflow {
    static final long serialVersionUID = 5631448975632770827L;

    private String targetProcessDefinitionId;
    private String targetState;
    private String previousState;

    public ChangeTargetWorkflowResponse() {
    }

    /**
     *
     * @param cwadPrefix
     * @param messageId
     * @param extensionNumber
     * @param extensionType
     * @param sourceConversationId
     * @param targetConversationId
     * @param sourceActivityDefinitionBusinessName
     * @param targetProcessInstanceId
     * @param targetProcessDefinitionBusinessName
     * @param targetNodeId
     * @param targetUserId
     * @param targetRoleId
     */
    public ChangeTargetWorkflowResponse(CWADPrefix cwadPrefix,
        String messageId, short extensionNumber, String extensionType,
        String sourceConversationId, String targetConversationId,
        String sourceActivityDefinitionBusinessName,
        String targetProcessInstanceId,
        String targetProcessDefinitionBusinessName, String targetNodeId,
        String targetUserId, String targetRoleId) {

        super(cwadPrefix, messageId, extensionNumber, extensionType,
            sourceConversationId, targetConversationId,
            sourceActivityDefinitionBusinessName, targetProcessInstanceId,
            targetProcessDefinitionBusinessName, targetNodeId, targetUserId,
            targetRoleId);
    }

    /**
     *
     * @param cwadPrefix
     * @param messageId
     * @param extensionNumber
     * @param extensionType
     * @param sourceConversationId
     * @param targetConversationId
     * @param sourceActivityDefinitionBusinessName
     * @param targetProcessInstanceId
     * @param targetProcessDefinitionBusinessName
     * @param targetNodeId
     * @param targetUserId
     * @param targetRoleId
     * @param targetProcessDefinitionId
     * @param targetState
     * @param previousState
     */
    public ChangeTargetWorkflowResponse(CWADPrefix cwadPrefix,
        String messageId, short extensionNumber, String extensionType,
        String sourceConversationId, String targetConversationId,
        String sourceActivityDefinitionBusinessName,
        String targetProcessInstanceId,
        String targetProcessDefinitionBusinessName, String targetNodeId,
        String targetUserId, String targetRoleId,
        String targetProcessDefinitionId, String targetState,
        String previousState) {

        super(cwadPrefix, messageId, extensionNumber, extensionType,
            sourceConversationId, targetConversationId,
            sourceActivityDefinitionBusinessName, targetProcessInstanceId,
            targetProcessDefinitionBusinessName, targetNodeId, targetUserId,
            targetRoleId);

        this.targetProcessDefinitionId = targetProcessDefinitionId;
        this.targetState = targetState;
        this.previousState = previousState;
    }

    /**
     *
     * @return
     */
    public String getTargetProcessDefinitionId() {
        return targetProcessDefinitionId;
    }

    /**
     *
     * @param targetProcessDefinitionId
     */
    public void setTargetProcessDefinitionId(
        String targetProcessDefinitionId) {

        this.targetProcessDefinitionId = targetProcessDefinitionId;
    }

    /**
     *
     * @return
     */
    public String getTargetState() {
        return targetState;
    }

    /**
     *
     * @param targetState
     */
    public void setTargetState(String targetState) {
        this.targetState = targetState;
    }

    /**
     *
     * @return
     */
    public String getPreviousState() {
        return previousState;
    }

    /**
     *
     * @param previousState
     */
    public void setPreviousState(String previousState) {
        this.previousState = previousState;
    }

    public String toString() {
        return "ChangeTargetWorkflowResponse@" +
            System.identityHashCode(this) + "[" +
            " cwadPrefix=" + getCwadPrefix() +
            ", messageId=" + getMessageId() +
            ", sourceActivityDefinitionBusinessName='" +
            getSourceActivityDefinitionBusinessName() + "'" +
            ", targetProcessDefinitionId=" + targetProcessDefinitionId +
            ", targetProcessInstanceId=" + getTargetProcessInstanceId() +
            ", targetProcessDefinitionBusinessName='" +
            getTargetProcessDefinitionBusinessName() + "'" +
            ", targetNodeId=" + getTargetNodeId() +
            ", targetUserId=" + getTargetUserId() +
            ", targetRoleId=" + getTargetRoleId() +
            ", targetState=" + targetState +
            ", previousState=" + previousState +
            ", extensionNumber=" + getExtensionNumber() +
            ", extensionType='" + getExtensionType() + "'" +
            ", sourceConversationId='" + getSourceConversationId() + "'" +
            ", targetConversationId='" + getTargetConversationId() + "'" +
            "]";
    }
}