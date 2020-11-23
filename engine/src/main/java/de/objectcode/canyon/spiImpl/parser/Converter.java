package de.objectcode.canyon.spiImpl.parser;

import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.objectcode.canyon.model.GraphConformance;
import de.objectcode.canyon.model.PublicationStatus;
import de.objectcode.canyon.model.activity.ExecutionType;
import de.objectcode.canyon.model.activity.ToolType;
import de.objectcode.canyon.model.data.ParameterMode;
import de.objectcode.canyon.model.participant.ParticipantType;
import de.objectcode.canyon.model.process.AccessLevel;
import de.objectcode.canyon.model.process.Duration;
import de.objectcode.canyon.model.process.DurationUnit;
import de.objectcode.canyon.model.transition.ConditionType;
import de.objectcode.canyon.model.transition.JoinType;
import de.objectcode.canyon.model.transition.SplitType;

/**
 * @author    junglas
 * @created   24. November 2003
 */
public abstract class Converter
{
  private final static  Log  log           = LogFactory.getLog( Converter.class );
  private static        Map  g_converters  = new HashMap();


  /**
   * Gets the converter attribute of the Converter object
   *
   * @param type  Description of the Parameter
   * @return      The converter value
   */
  public static Converter getConverter( Class type )
  {
    return ( Converter ) g_converters.get( type.getName() );
  }


  /**
   * Description of the Method
   *
   * @param value  Description of the Parameter
   * @return       Description of the Return Value
   */
  public abstract Object fromString( String value );


  /**
   * Description of the Class
   *
   * @author    junglas
   * @created   24. November 2003
   */
  public static class NoConverter extends Converter
  {
    /**
     * Description of the Method
     *
     * @param value  Description of the Parameter
     * @return       Description of the Return Value
     */
    public Object fromString( String value )
    {
      return value;
    }
  }


  /**
   * Description of the Class
   *
   * @author    junglas
   * @created   24. November 2003
   */
  public static class EnumConverter extends Converter
  {
    Method  m_fromString;


    /**
     *Constructor for the EnumConverter object
     *
     * @param enumClass  Description of the Parameter
     */
    public EnumConverter( Class enumClass )
    {
      try {
        m_fromString = enumClass.getMethod( "fromString", new Class[]{String.class} );
      }
      catch ( Exception e ) {
        log.fatal( "Exception", e );
      }
    }


    /**
     * Description of the Method
     *
     * @param value  Description of the Parameter
     * @return       Description of the Return Value
     */
    public Object fromString( String value )
    {
      try {
        return m_fromString.invoke( null, new Object[]{value} );
      }
      catch ( Exception e ) {
        throw new IllegalArgumentException( e.toString() + " '" + value + "'" );
      }
    }
  }


  /**
   * Description of the Class
   *
   * @author    junglas
   * @created   24. November 2003
   */
  public static class DateConverter extends Converter
  {
    DateFormat  m_formats[]  = new DateFormat[]{
        new SimpleDateFormat( "yyyy-MM-dd'T'HH:mm:ss'Z'" ),
        new SimpleDateFormat( "yyyy-MM-dd'T'HH:mm:ssZ" ),
        new SimpleDateFormat( "yyyy-MM-dd'T'HH:mm:ssz" ),
        new SimpleDateFormat( "MM/dd/yyyy HH:mm:ss a" ),
        new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" ),
        new SimpleDateFormat( "dd.MM.yyyy HH:mm:ss" ),
        new SimpleDateFormat( "dd.MMM.yyyy HH:mm:ss" ),
        };


    /**
     * Description of the Method
     *
     * @param value  Description of the Parameter
     * @return       Description of the Return Value
     */
    public Object fromString( String value )
    {
      int  i;

      for ( i = 0; i < m_formats.length; i++ ) {
        try {
          return m_formats[i].parse( value );
        }
        catch ( ParseException e ) {
          // do nothing
        }
      }

      throw new IllegalArgumentException( "'" + value + "' is not a valid date" );
    }
  }


  /**
   * Description of the Class
   *
   * @author    junglas
   * @created   25. November 2003
   */
  public static class DurationConverter extends Converter
  {
    /**
     * Description of the Method
     *
     * @param value  Description of the Parameter
     * @return       Description of the Return Value
     */
    public Object fromString( String value )
    {
      return Duration.parse(value);
    }
  }

  static {
    g_converters.put( String.class.getName(), new NoConverter() );
    g_converters.put( "boolean", new NoConverter() );
    g_converters.put( "int", new NoConverter() );
    g_converters.put( Date.class.getName(), new DateConverter() );
    g_converters.put( Duration.class.getName(), new DurationConverter() );
    g_converters.put( PublicationStatus.class.getName(), new EnumConverter( PublicationStatus.class ) );
    g_converters.put( GraphConformance.class.getName(), new EnumConverter( GraphConformance.class ) );
    g_converters.put( ParameterMode.class.getName(), new EnumConverter( ParameterMode.class ) );
    g_converters.put( DurationUnit.class.getName(), new EnumConverter( DurationUnit.class ) );
    g_converters.put( SplitType.class.getName(), new EnumConverter( SplitType.class ));
    g_converters.put( JoinType.class.getName(), new EnumConverter( JoinType.class ));
    g_converters.put( ToolType.class.getName(), new EnumConverter ( ToolType.class ));
    g_converters.put( AccessLevel.class.getName(), new EnumConverter( AccessLevel.class ));
    g_converters.put( ParticipantType.class.getName(), new EnumConverter( ParticipantType.class));
    g_converters.put( ConditionType.class.getName(), new EnumConverter ( ConditionType.class ));
    g_converters.put( ExecutionType.class.getName(), new EnumConverter ( ExecutionType.class ));
  }
}
