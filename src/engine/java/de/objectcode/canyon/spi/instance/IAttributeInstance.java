package de.objectcode.canyon.spi.instance;

/**
 * @author    junglas
 * @created   17. Oktober 2003
 */
public interface IAttributeInstance
{
  /**
   * Gets the name attribute of the IAttributeInstance object
   *
   * @return   The name value
   */
  public String getName();


  /**
   * Gets the type attribute of the IAttributeInstance object
   *
   * @return   The type value
   */
  public int getType();


  /**
   * Sets the value attribute of the IAttributeInstance object
   *
   * @param type                            The new value value
   * @param value                           The new value value
   * @exception AttributeReadOnlyException  Description of the Exception
   */
  public void setValue( int type, Object value )
    throws AttributeReadOnlyException;


  /**
   * Gets the value attribute of the IAttributeInstance object
   *
   * @return   The value value
   */
  public Object getValue();


  /**
   * Gets the owner attribute of the IAttributeInstance object
   *
   * @return   The owner value
   */
  public IAttributedEntity getOwner();
}
