package org.wfmc.wapi;


public class WMLockedException extends WMWorkflowException {
	static final long serialVersionUID = -7936326033418137067L;
	
	public WMLockedException() {
        super(new WMError(WMError.WM_LOCKED));
    }
}
