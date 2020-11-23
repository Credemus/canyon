package de.objectcode.canyon.spiImpl.parser;

import org.apache.commons.digester.AbstractObjectCreationFactory;
import org.xml.sax.Attributes;

import de.objectcode.canyon.model.data.BasicType;

/**
 * @author    junglas
 * @created   24. November 2003
 */
public class BasicTypeFactory extends AbstractObjectCreationFactory
{
  /**
   * @param attrs          Description of the Parameter
   * @return               Description of the Return Value
   * @exception Exception  Description of the Exception
   * @see                  org.apache.commons.digester.ObjectCreationFactory#createObject(org.xml.sax.Attributes)
   */
  public Object createObject( Attributes attrs )
    throws Exception
  {
    return BasicType.fromString( attrs.getValue( "Type" ) );
  }

}
