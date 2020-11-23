package de.objectcode.canyon.model.data;

import java.io.Serializable;

/**
 * @author junglas
 */
public abstract class DataType implements Serializable
{
	static final long serialVersionUID = -3752828595517145329L;
	
	public abstract Class getValueClass ( );
}
