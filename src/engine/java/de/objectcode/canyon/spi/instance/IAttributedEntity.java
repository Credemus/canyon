package de.objectcode.canyon.spi.instance;

import java.util.Map;

import de.objectcode.canyon.spi.RepositoryException;
import de.objectcode.canyon.spi.process.IProcessDefinitionID;

/**
 * @author    junglas
 * @created   17. Oktober 2003
 */
public interface IAttributedEntity
{
  /**
   * Gets the entityId attribute of the IAttributedEntity object
   *
   * @return   The entityId value
   */
  public String getEntityId();


  public IProcessDefinitionID getProcessDefinitionId();


  /**
   * Gets the attributeInstances attribute of the IAttributedEntity object
   *
   * @return                         The attributeInstances value
   * @exception RepositoryException  Description of the Exception
   */
  public Map getAttributeInstances()
    throws RepositoryException;


  /**
   * Returns the named attribute for the entity, if one exists.
   *
   * @param attributeName             The name of the attribute.
   * @return                          The requested attribute.
   * @throws ObjectNotFoundException  if the attribute could not be found.
   * @throws RepositoryException      if some other exception occurred.
   */
  public IAttributeInstance getAttributeInstance( String attributeName )
    throws RepositoryException;


  /**
   * Adds a feature to the AttributeInstance attribute of the IAttributedEntity object
   *
   * @param attributeName            The feature to be added to the AttributeInstance attribute
   * @param attributeType            The feature to be added to the AttributeInstance attribute
   * @param attributeValue           The feature to be added to the AttributeInstance attribute
   * @return                         Description of the Return Value
   * @exception RepositoryException  Description of the Exception
   */
  public IAttributeInstance addAttributeInstance( String attributeName,
      int attributeType, Object attributeValue )
    throws RepositoryException;
}
