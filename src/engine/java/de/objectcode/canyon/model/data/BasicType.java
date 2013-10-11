package de.objectcode.canyon.model.data;

import java.util.Date;

import de.objectcode.canyon.model.participant.Participant;

/**
 * @author    junglas
 * @created   26. November 2003
 */
public class BasicType extends DataType
{

	static final long serialVersionUID = 1028290966697412929L;
	
  public final static   int          STRING_INT     = 0;
  public final static   int          FLOAT_INT      = 1;
  public final static   int          INTEGER_INT    = 2;
  public final static   int          REFERENCE_INT  = 3;
  public final static   int          DATETIME_INT   = 4;
  public final static   int          BOOLEAN_INT    = 5;
  public final static   int          PERFORMER_INT  = 6;
  public final static   BasicType    STRING         = new BasicType( STRING_INT, "STRING", String.class );
  public final static   BasicType    FLOAT          = new BasicType( FLOAT_INT, "FLOAT", Float.class );
  public final static   BasicType    INTEGER        = new BasicType( INTEGER_INT, "INTEGER", Integer.class );
  public final static   BasicType    REFERENCE      = new BasicType( REFERENCE_INT, "REFERENCE", Object.class );
  public final static   BasicType    DATETIME       = new BasicType( DATETIME_INT, "DATETIME", Date.class );
  public final static   BasicType    BOOLEAN        = new BasicType( BOOLEAN_INT, "BOOLEAN", Boolean.class );
  public final static   BasicType    PERFORMER      = new BasicType( PERFORMER_INT, "PERFORMER", Participant.class );

  private final static  BasicType[]  g_values       = {
      STRING,
      FLOAT,
      INTEGER,
      REFERENCE,
      DATETIME,
      BOOLEAN,
      PERFORMER
      };

  private final static  Object[][]   g_typeMap      = {
      {String.class, STRING},
      {double.class, FLOAT},
      {Double.class, FLOAT},
      {float.class, FLOAT},
      {Float.class, FLOAT},
      {int.class, INTEGER},
      {Integer.class, INTEGER},
      {Date.class, DATETIME},
      {boolean.class, BOOLEAN},
      {Boolean.class, BOOLEAN}
      };

  private final         int          m_value;
  private final         String       m_tag;
  private final         Class        m_valueClass;


  /**
   *Constructor for the BasicType object
   *
   * @param value  Description of the Parameter
   * @param tag    Description of the Parameter
   */
  private BasicType( int value, String tag, Class valueClass )
  {
    m_value = value;
    m_tag = tag;
    m_valueClass = valueClass;
  }


  /**
   * @return
   */
  public String getTag()
  {
    return m_tag;
  }


  /**
   * @return
   */
  public int getValue()
  {
    return m_value;
  }


  /**
   * Description of the Method
   *
   * @param tag  Description of the Parameter
   * @return     Description of the Return Value
   */
  public static BasicType fromString( String tag )
  {
    int  i;

    for ( i = 0; i < g_values.length; i++ ) {
      if ( g_values[i].getTag().equals( tag ) ) {
        return g_values[i];
      }
    }
    throw new IllegalArgumentException( tag );
  }


  /**
   * Description of the Method
   *
   * @param clazz  Description of the Parameter
   * @return       Description of the Return Value
   */
  public static BasicType fromClass( Class clazz )
  {
    int  i;

    for ( i = 0; i < g_typeMap.length; i++ ) {
      if ( ( ( Class ) g_typeMap[i][0] ).isAssignableFrom( clazz ) ) {
        return ( BasicType ) g_typeMap[i][1];
      }
    }
    throw new IllegalArgumentException( clazz.getName() );
  }

  public static BasicType fromInt ( int value )
  {
    int  i;

    for ( i = 0; i < g_values.length; i++ ) {
      if ( g_values[i].getValue() == value ) {
        return g_values[i];
      }
    }
    throw new IllegalArgumentException( String.valueOf(value) );
  }

  /**
   * Description of the Method
   *
   * @return   Description of the Return Value
   */
  public String toString()
  {
    return m_tag;
  }

  public Object readResolve() 
  {
    return fromInt ( m_value );
  }
  
  public Class getValueClass()
  {
    return m_valueClass;
  }
}
