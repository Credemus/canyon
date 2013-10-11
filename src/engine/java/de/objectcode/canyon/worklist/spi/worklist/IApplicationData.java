package de.objectcode.canyon.worklist.spi.worklist;

import de.objectcode.canyon.model.ExtendedAttribute;
import de.objectcode.canyon.spi.tool.Parameter;

/**
 * @author junglas
 */
public interface IApplicationData 
{
	public String getId();

  public String getName();
  
  public String getDescription();
  
  public ExtendedAttribute[] getExtendedAttributes();

  public Parameter[] getParameters ( );
  
  public void setParameterValue ( String formalName, Object value );
}
