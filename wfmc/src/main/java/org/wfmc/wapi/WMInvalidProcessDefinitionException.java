package org.wfmc.wapi;

public class WMInvalidProcessDefinitionException
        extends WMInvalidObjectException {
	static final long serialVersionUID = -6619236362370390306L;
	
    public WMInvalidProcessDefinitionException(String processDefinitionId) {
        super(WMError.WM_INVALID_PROCESS_DEFINITION, processDefinitionId);
    }
}
