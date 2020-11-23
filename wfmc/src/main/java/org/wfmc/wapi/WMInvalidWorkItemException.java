package org.wfmc.wapi;

public class WMInvalidWorkItemException
        extends WMInvalidObjectException {
	static final long serialVersionUID = 8073398504788706532L;
	
    public WMInvalidWorkItemException(String workItemId) {
        super(WMError.WM_INVALID_WORKITEM, workItemId);
    }
}
