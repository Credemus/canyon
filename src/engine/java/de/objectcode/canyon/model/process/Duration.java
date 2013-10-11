package de.objectcode.canyon.model.process;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.objectcode.canyon.spiImpl.parser.Converter;

/**
 * @author    junglas
 * @created   20. November 2003
 */
public class Duration implements Serializable
{

  static final long serialVersionUID = -2206022371578069490L;

  private  int           m_value;
  private  DurationUnit  m_unit;
	private  String				 m_deadlineConditionExpr;
	private boolean		 m_absolute = false;

	private static Pattern NUMBER_PATTERN = Pattern.compile("^\\s*([\\d]+)\\s*$");
	private static Pattern NUMBER_UNIT_PATTERN = Pattern.compile("^\\s*([\\d]+)\\s*(Y|M|D|h|m|s)\\s*$");
	
  /**
   *Constructor for the Duration object
   *
   * @param value  Description of the Parameter
   * @param unit   Description of the Parameter
   */
  public Duration( int value, DurationUnit unit )
  {
	  this(value, unit, false);
  }

  public Duration( int value, DurationUnit unit, boolean absolute )
  {
	m_absolute = absolute;
    m_value = value;
    m_unit = unit;
  }

  /**
   * @param unit
   */
  public void setUnit( DurationUnit unit )
  {
    m_unit = unit;
  }


  /**
   * @param i
   */
  public void setValue( int i )
  {
    m_value = i;
  }


  /**
   * @return
   */
  public DurationUnit getUnit()
  {
    return m_unit;
  }


  /**
   * Gets the unit attribute of the Duration object
   *
   * @param defaultUnit  Description of the Parameter
   * @return             The unit value
   */
  public DurationUnit getUnit( DurationUnit defaultUnit )
  {
    if ( m_unit == null ) {
      return defaultUnit;
    }
    return m_unit;
  }


  /**
   * @return
   */
  public int getValue()
  {
    return m_value;
  }

  
  public boolean isStatic() {
  	return m_value != 0;
  }

  public boolean isDynamic() {
  	return m_value == 0;
  }

  /**
   * Gets the millis attribute of the Duration object
   *
   * @return   The millis value
   */
  public long getMillis()
  {
  	if (m_unit==null)
  		throw new IllegalStateException("Duration without unit!");
    return m_value * m_unit.getMillis();
  }


  /**
   * Gets the millis attribute of the Duration object
   *
   * @param unit  Description of the Parameter
   * @return      The millis value
   */
  public long getMillis( DurationUnit unit )
  {
  	if (m_unit!=null)
  		return getMillis();
  	if (unit==null)
  		throw new IllegalStateException("Duration without unit!");
    return m_value * unit.getMillis();
  }
  
	/**
	 * @return
	 */
	public String getDeadlineConditionExpr() {
		return m_deadlineConditionExpr;
	}

	/**
	 * @param string
	 */
	public void setDeadlineConditionExpr(String string) {
		m_deadlineConditionExpr = string;
	}

	/**
	 * 
	 * @param value
	 * @return
	 */
	public static final Duration parse( String value )
	{
		// first try absolute
		try {
			Date date = (Date) Converter.getConverter(Date.class).fromString(
					value);
			int secs = (int) (date.getTime() / 1000);
			Duration duration = new Duration(secs, DurationUnit.SECOND, true);
			return duration;
		} catch (Exception e) {
		}

		DurationUnit  unit       = null;
		String number = null;
	    Matcher m = NUMBER_UNIT_PATTERN.matcher(value);
	    if (m.find()) {
	      number = m.group(1);
			try {
				unit = DurationUnit.fromString( m.group(2) );					
			} catch (IllegalArgumentException iae) {
				//
			}
	    } else {
	      m = NUMBER_PATTERN.matcher(value);
	      if (m.find())
	        number = m.group(1);
	    }
		Duration result = null;
		if ( number==null ) {
			result = new Duration(0,unit);
		} else {
			result = new Duration(Integer.parseInt( number ), unit);
		}
		result.setDeadlineConditionExpr(value);
		return result;
	}


	private static DateFormat DATE_FORMAT = new SimpleDateFormat( "dd.MMM.yyyy HH:mm:ss" );
	
	public String toString() {
		try {
		if (isAbsolute()) {
			return DATE_FORMAT.format(new Date(getMillis())); 
		} else {
			if (m_unit!=null)
				return m_value + m_unit.getTag();
			else
				return String.valueOf(m_value);
		}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "???";
	}
	public boolean isAbsolute() {
		return m_absolute;
	}

	public static Duration getQualifiedDuration(Duration duration, DurationUnit defaultDurationUnit) {
		if (duration.getUnit()!=null)
			return duration;
		else
			return new Duration(duration.getValue(), defaultDurationUnit);
	}
	
}
