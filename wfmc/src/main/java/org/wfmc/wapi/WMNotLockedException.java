package org.wfmc.wapi;


public class WMNotLockedException extends WMWorkflowException {
	static final long serialVersionUID = -7321557115925938780L;
	
	public WMNotLockedException() {
        super(new WMError(WMError.WM_NOT_LOCKED));
    }
}
