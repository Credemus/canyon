package org.wfmc.wapi;


/**
 * Indicates that an attribute does not exist, or is read-only.
 *
 * @author Adrian Price
 */
public class WMInvalidAttributeException extends WMInvalidObjectException {
	static final long serialVersionUID = -7607744446178994346L;
	
	public WMInvalidAttributeException(String attributeName) {
        super(WMError.WM_INVALID_ATTRIBUTE, attributeName);
    }
}