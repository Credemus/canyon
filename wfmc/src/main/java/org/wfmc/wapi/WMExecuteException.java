package org.wfmc.wapi;


public class WMExecuteException extends WMWorkflowException {
	static final long serialVersionUID = -6582786780183780238L;
	
	public WMExecuteException(Exception e) {
        super(new WMError(WMError.WM_EXECUTE_FAILED), e);
    }
}
