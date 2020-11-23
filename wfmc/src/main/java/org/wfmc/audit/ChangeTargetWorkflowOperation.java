package org.wfmc.audit;

/**
 *
 *
 * @author Antony Lodge
 */
public class ChangeTargetWorkflowOperation extends RemoteAuditBase {
    static final long serialVersionUID = 6821617415591508535L;

    private String previousProcessState;
    private String newProcessState;

    public ChangeTargetWorkflowOperation() {
    }

    /**
     *
     * @param cwadPrefix
     * @param messageId
     * @param extensionNumber
     * @param extensionType
     * @param sourceConversationId
     * @param targetConversationId
     */
    public ChangeTargetWorkflowOperation(CWADPrefix cwadPrefix,
        String messageId, short extensionNumber, String extensionType,
        String sourceConversationId, String targetConversationId) {

        super(cwadPrefix, messageId, extensionNumber, extensionType,
            sourceConversationId, targetConversationId);
    }

    /**
     *
     * @param cwadPrefix
     * @param messageId
     * @param extensionNumber
     * @param extensionType
     * @param sourceConversationId
     * @param targetConversationId
     */
    public ChangeTargetWorkflowOperation(CWADPrefix cwadPrefix,
        String messageId, short extensionNumber, String extensionType,
        String sourceConversationId, String targetConversationId,
        String previousProcessState, String newProcessState) {

        super(cwadPrefix, messageId, extensionNumber, extensionType,
            sourceConversationId, targetConversationId);
        this.previousProcessState = previousProcessState;
        this.newProcessState = newProcessState;
    }

    /**
     *
     * @return State of process instance prior to change
     */
    public String getPreviousProcessState() {
        return previousProcessState;
    }

    /**
     *
     * @return New state of process instance
     */
    public String getNewProcessState() {
        return newProcessState;
    }

    /**
     *
     * @param previousProcessState  State of process instance prior to change
     */
    public void setPreviousProcessState(String previousProcessState) {
        this.previousProcessState = previousProcessState;
    }

    /**
     *
     * @param newProcessState New state of process instance
     */
    public void setNewProcessState(String newProcessState) {
        this.newProcessState = newProcessState;
    }

    public String toString() {
        return "ChangeTargetWorkflowOperation@" +
            System.identityHashCode(this) + "[" +
            " cwadPrefix=" + getCwadPrefix() +
            ", messageId=" + getMessageId() +
            ", previousProcessState=" + previousProcessState +
            ", newProcessState=" + newProcessState +
            ", extensionNumber=" + getExtensionNumber() +
            ", extensionType='" + getExtensionType() + "'" +
            ", sourceConversationId='" + getSourceConversationId() + "'" +
            ", targetConversationId='" + getTargetConversationId() + "'" +
            "]";
    }
}