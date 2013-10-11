package de.objectcode.canyon.spiImpl.parser;

import java.util.HashMap;
import java.util.Map;

import de.objectcode.canyon.CanyonRuntimeException;
import de.objectcode.canyon.spi.ObjectAlreadyExistsException;
import de.objectcode.canyon.spi.ObjectNotFoundException;
import de.objectcode.canyon.spi.parser.IParser;
import de.objectcode.canyon.spi.parser.IParserFactory;
import de.objectcode.canyon.spi.parser.ISerializer;

/**
 * @author    junglas
 * @created   16. Oktober 2003
 */
public class DefaultParserFactory implements IParserFactory
{
  private final  Map  m_parsers      = new HashMap();
  private final  Map  m_serializers  = new HashMap();


  /**
   *Constructor for the DefaultParserFactory object
   */
  public DefaultParserFactory()
  {
    try {
//      registerParser( WMClient.XPDL, Dom4JXPDLParser.class.getName(),
//          Dom4JXPDLSerializer.class.getName() );
      registerParser( XPDL, DefaultXPDLParser.class.getName(), DefaultTxtSerializer.class.getName());
    }
    catch ( ObjectAlreadyExistsException e ) {
      throw new CanyonRuntimeException( e );
    }
  }


  /**
   * Description of the Method
   *
   * @param contentType                  Description of the Parameter
   * @return                             Description of the Return Value
   * @exception ObjectNotFoundException  Description of the Exception
   */
  public IParser createParser( String contentType )
    throws ObjectNotFoundException
  {

    String  parserClass  = ( String ) m_parsers.get( contentType );
    if ( parserClass == null ) {
      throw new ObjectNotFoundException( contentType );
    }
    try {
      return ( IParser ) Class.forName( parserClass ).newInstance();
    }
    catch ( Exception e ) {
      throw new CanyonRuntimeException( e );
    }
  }


  /**
   * Description of the Method
   *
   * @param contentType                  Description of the Parameter
   * @return                             Description of the Return Value
   * @exception ObjectNotFoundException  Description of the Exception
   */
  public ISerializer createSerializer( String contentType )
    throws ObjectNotFoundException
  {

    String  serializerClass  = ( String ) m_serializers.get( contentType );
    if ( serializerClass == null ) {
      throw new ObjectNotFoundException( contentType );
    }
    try {
      return ( ISerializer ) Class.forName( serializerClass ).newInstance();
    }
    catch ( Exception e ) {
      throw new CanyonRuntimeException( e );
    }
  }


  /**
   * Description of the Method
   *
   * @param contentType                       Description of the Parameter
   * @param parserClass                       Description of the Parameter
   * @param serializerClass                   Description of the Parameter
   * @exception ObjectAlreadyExistsException  Description of the Exception
   */
  public void registerParser( String contentType, String parserClass,
      String serializerClass )
    throws ObjectAlreadyExistsException
  {

    if ( parserClass != null && m_parsers.containsKey( contentType ) ) {
      throw new ObjectAlreadyExistsException( contentType );
    }
    m_parsers.put( contentType, parserClass );
    if ( serializerClass != null && m_serializers.containsKey( contentType ) ) {
      throw new ObjectAlreadyExistsException( contentType );
    }
    m_serializers.put( contentType, serializerClass );
  }


  /**
   * Description of the Method
   *
   * @param contentType                  Description of the Parameter
   * @exception ObjectNotFoundException  Description of the Exception
   */
  public void unregisterParser( String contentType )
    throws ObjectNotFoundException
  {

    m_parsers.remove( contentType );
    m_serializers.remove( contentType );
  }
}
