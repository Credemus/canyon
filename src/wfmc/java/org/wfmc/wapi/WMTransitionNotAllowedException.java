package org.wfmc.wapi;

public class WMTransitionNotAllowedException extends WMWorkflowException {
	static final long serialVersionUID = -9001778228100390200L;
	
	private static final String[] ACTIONS = {
        "(illegal)",
        "(none)",
        "start",
        "stop",
        "suspend",
        "abort",
        "terminate",
        "complete"
    };
    private String _oldState;
    private String _newState;
    private int _action;

    public WMTransitionNotAllowedException(String oldState, String newState) {
        super(new WMError(WMError.WM_TRANSITION_NOT_ALLOWED));
        _oldState = oldState;
        _newState = newState;
        _action = WMObjectState.ILLEGAL_ACTION;
    }

    public WMTransitionNotAllowedException(String oldState, int action) {
        super(new WMError(WMError.WM_TRANSITION_NOT_ALLOWED));
        _oldState = oldState;
        _newState = "(invalid)";
        _action = action;
    }

    public String toString() {
        return getClass().getName() + '@' + System.identityHashCode(this) +
            "[from='" + _oldState +
            "', to='" + _newState +
            "', action='" + ACTIONS[_action + 2] +
            "']";
    }
}