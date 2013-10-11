package org.wfmc.wapi;

/**
 * @author Adrian Price
 */
public abstract class WMInvalidObjectException extends WMWorkflowException {
    private Object object;

    protected WMInvalidObjectException(int errorCode, Object object) {
        super(new WMError(errorCode));
        this.object = object;
    }

    public Object getObject() {
        return object;
    }

    public String toString() {
        return getClass().getName() + '@' + System.identityHashCode(this)
                + "[object='" + object + "'"
                + ']';
    }
}