package org.wfmc.audit;

/**
 * Create/Start Process/Subprocess Instance Audit Data.
 * <p><b>Note:</b> All ids are limited to 64 characters
 * @author Antony Lodge
 */
public class CreateProcessInstanceData extends AuditBase {
    static final long serialVersionUID = 3625765091841098523L;

    private String processDefinitionId;
    private String processDefinitionBusinessName;

    /**
     *
     */
    public CreateProcessInstanceData() {
    }

    /**
     *
     * @param cwadPrefix
     * @param cwadSuffix
     * @param processDefinitionId
     * @param processDefinitionBusinessName
     */
    public CreateProcessInstanceData(CWADPrefix cwadPrefix,
        CWADSuffix cwadSuffix, String processDefinitionId,
        String processDefinitionBusinessName) {

        super(cwadPrefix, cwadSuffix);
        this.processDefinitionId = processDefinitionId;
        this.processDefinitionBusinessName = processDefinitionBusinessName;
    }

    public CreateProcessInstanceData(CWADPrefix cwadPrefix,
        CWADSuffix cwadSuffix) {
        super(cwadPrefix, cwadSuffix);
    }

    /**
     *
     * @return Process Definition Id identifying the definition used for
     * creating this process instance
     */
    public String getProcessDefinitionId() {
        return processDefinitionId;
    }

    /**
     *
     * @param processDefinitionId Process Definition Id identifying the
     * definition used for creating this process instance
     */
    public void setProcessDefinitionId(String processDefinitionId) {
        this.processDefinitionId = processDefinitionId;
    }

    /**
     *
     * @return Business name of the process definition relevant to the business
     */
    public String getProcessDefinitionBusinessName() {
        return processDefinitionBusinessName;
    }

    /**
     *
     * @param processDefinitionBusinessName Business name of the process
     * definition relevant to the business
     */
    public void setProcessDefinitionBusinessName(
        String processDefinitionBusinessName) {
        this.processDefinitionBusinessName = processDefinitionBusinessName;
    }

    public String toString() {
        return "CreateProcessInstanceData@" +
            System.identityHashCode(this) + "[" +
            " cwadPrefix=" + getCwadPrefix() +
            ", processDefinitionId=" + processDefinitionId +
            ", processDefinitionBusinessName='" +
            processDefinitionBusinessName + "'" +
            ", cwadSuffix=" + getCwadSuffix() +
            "]";
    }
}