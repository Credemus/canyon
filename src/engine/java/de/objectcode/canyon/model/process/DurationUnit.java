package de.objectcode.canyon.model.process;

import de.objectcode.canyon.model.AbstractEnum;

/**
 * @author    junglas
 * @created   20. November 2003
 */
public class DurationUnit extends AbstractEnum
{
	static final long serialVersionUID = -2980931234330300327L;
	
  public final static   int             YEAR_INT    = 0;
  public final static   int             MONTH_INT   = 1;
  public final static   int             DAY_INT     = 2;
  public final static   int             HOUR_INT    = 3;
  public final static   int             MINUTE_INT  = 4;
  public final static   int             SECOND_INT  = 5;
  public final static   DurationUnit    YEAR        = new DurationUnit( YEAR_INT, "Y", 365L * 24L * 60L * 60L * 1000L );
  public final static   DurationUnit    MONTH       = new DurationUnit( MONTH_INT, "M", 30L * 24L * 60L * 60L * 1000L );
  public final static   DurationUnit    DAY         = new DurationUnit( DAY_INT, "D", 24L * 60L * 60L * 1000L );
  public final static   DurationUnit    HOUR        = new DurationUnit( HOUR_INT, "h", 60L * 60L * 1000L );
  public final static   DurationUnit    MINUTE      = new DurationUnit( MINUTE_INT, "m", 60L * 1000L );
  public final static   DurationUnit    SECOND      = new DurationUnit( SECOND_INT, "s", 1000L );

  private final static  DurationUnit[]  g_values    = {
      YEAR,
      MONTH,
      DAY,
      HOUR,
      MINUTE,
      SECOND
      };

  private final         long            m_millis;


  /**
   *Constructor for the DurationUnit object
   *
   * @param value   Description of the Parameter
   * @param tag     Description of the Parameter
   * @param millis  Description of the Parameter
   */
  private DurationUnit( int value, String tag, long millis )
  {
    super( value, tag );

    m_millis = millis;
  }


  /**
   * @return
   */
  public long getMillis()
  {
    return m_millis;
  }


  /**
   * Convert the specified type String to a DurationUnit object.  If the
   * String cannot be converted to a DurationUnit then the value null
   * is returned.
   *
   * @param tag  The duration unit String
   * @return     A DurationUnit or null
   */
  public static DurationUnit fromString( String tag )
  {
    int  i;

    for ( i = 0; i < g_values.length; i++ ) {
      if ( g_values[i].getTag().equals( tag ) ) {
        return g_values[i];
      }
    }
    throw new IllegalArgumentException( tag );
  }

  public static DurationUnit fromInt( int value )
  {
    int  i;

    for ( i = 0; i < g_values.length; i++ ) {
      if ( g_values[i].getValue() == value ) {
        return g_values[i];
      }
    }
    throw new IllegalArgumentException( String.valueOf( value ) );
  }
  
  public Object readResolve()
  {
    return fromInt ( m_value );
  }

}
