package org.wfmc.audit;

/**
 * Change Process Instance State Audit Data.
 *
 * @author Antony Lodge
 */
public class ChangeWorkItemStateData extends AuditBase {
    static final long serialVersionUID = 7025075993712917031L;

    private String activityInstanceId;
    private String workItemId;
    private String workItemState;

    /**
     *
     */
    public ChangeWorkItemStateData() {
    }

    /**
     *
     * @param cwadPrefix
     * @param cwadSuffix
     */
    public ChangeWorkItemStateData(CWADPrefix cwadPrefix,
        CWADSuffix cwadSuffix) {

        super(cwadPrefix, cwadSuffix);
    }

    /**
     *
     * @param cwadPrefix
     * @param cwadSuffix
     * @param activityInstanceId
     * @param workItemId
     * @param workItemState
     */
    public ChangeWorkItemStateData(CWADPrefix cwadPrefix, CWADSuffix cwadSuffix,
        String activityInstanceId, String workItemId,
        String workItemState) {

        super(cwadPrefix, cwadSuffix);
        this.activityInstanceId = activityInstanceId;
        this.workItemId = workItemId;
        this.workItemState = workItemState;
    }

    /**
     *
     * @return State of the work item
     */
    public String getWorkItemState() {
        return workItemState;
    }

    /**
     *
     * @param workItemState State of the work item
     */
    public void setWorkItemState(String workItemState) {
        this.workItemState = workItemState;
    }

    /**
     *
     * @return Unique Id for the current Activity Instance
     */
    public String getActivityInstanceId() {
        return activityInstanceId;
    }

    /**
     *
     * @param activityInstanceId Unique Id for the current Activity Instance
     */
    public void setActivityInstanceId(String activityInstanceId) {
        this.activityInstanceId = activityInstanceId;
    }

    /**
     *
     * @return Unique Id for the work item
     */
    public String getWorkItemId() {
        return workItemId;
    }

    /**
     *
     * @param workItemId Unique Id for the work item
     */
    public void setWorkItemId(String workItemId) {
        this.workItemId = workItemId;
    }

    public String toString() {
        return "ChangeWorkItemStateData@" +
            System.identityHashCode(this) + "[" +
            " cwadPrefix=" + getCwadPrefix() +
            ", activityInstanceId=" + activityInstanceId +
            ", workItemId=" + workItemId +
            ", workItemState='" + workItemState + "'" +
            ", cwadSuffix=" + getCwadSuffix() +
            "]";
    }
}