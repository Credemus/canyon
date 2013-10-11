package de.objectcode.canyon.model.data;

/**
 * @author junglas
 */
public class SchemaType extends DataType 
{
	static final long serialVersionUID = 7305820362725774301L;
	
	public Class getValueClass()
  {
    return Object.class;
  }
}
