package de.objectcode.canyon.bpe.engine.variable;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Date;

import org.dom4j.Element;

import de.objectcode.canyon.bpe.util.HydrationContext;
import de.objectcode.canyon.model.data.BasicType;

/**
 * @author    junglas
 * @created   17. Juni 2004
 */
public class BasicVariable implements IVariable
{
  final static  long       serialVersionUID  = -7310513417668569614L;

  private       String     m_name;
  private       Object     m_value;
  private       BasicType  m_type;

  public BasicVariable ( String name, BasicType type )
  {
    m_name = name;
    m_type = type;
  }

  /**
   *Constructor for the BasicVariable object
   *
   * @param name   Description of the Parameter
   * @param type   Description of the Parameter
   * @param value  Description of the Parameter
   */
  public BasicVariable( String name, BasicType type, Object value )
  {
    m_name = name;
    m_type = type;

    setValue( value );
  }


  /**
   * Sets the value attribute of the BasicVariable object
   *
   * @param value  The new value value
   */
  public void setValue( Object value )
  {
    if ( value == null ) {
      m_value = null;
      return;
    }

    switch ( m_type.getValue() ) {
      case BasicType.BOOLEAN_INT:
        if ( value instanceof Boolean ) {
          m_value = value;
        } else {
          m_value = new Boolean( value.toString() );
        }
        break;
      case BasicType.STRING_INT:
        if ( value instanceof String ) {
          m_value = value;
        } else {
          m_value = value.toString();
        }
        break;
      case BasicType.DATETIME_INT:
        if ( value instanceof Date ) {
          m_value = new Date(((Date)value).getTime());
        } else if ( value instanceof Long ) {
          m_value = new Date(((Long)value).longValue());
        }
        break;
      case BasicType.FLOAT_INT:
        if ( value instanceof Number ) {
          m_value = value;
        } else {
          m_value = new Double( value.toString() );
        }
        break;
      case BasicType.INTEGER_INT:
        if ( value instanceof Integer ) {
          m_value = value;
        } else {
          m_value = new Integer( value.toString() );
        }
        break;
    }
  }


  /**
   * @return   The name value
   * @see      de.objectcode.canyon.bpe.engine.variable.IVariable#getName()
   */
  public String getName()
  {
    return m_name;
  }


  /**
   * @return   The value value
   * @see      de.objectcode.canyon.bpe.engine.variable.IVariable#getValue()
   */
  public Object getValue()
  {
    return m_value;
  }


  /**
   * Gets the valueClass attribute of the BasicVariable object
   *
   * @return   The valueClass value
   */
  public Class getValueClass()
  {
    switch ( m_type.getValue() ) {
      case BasicType.BOOLEAN_INT:
        return Boolean.class;
      case BasicType.STRING_INT:
        return String.class;
      case BasicType.DATETIME_INT:
        return Date.class;
      case BasicType.FLOAT_INT:
        return Double.class;
      case BasicType.INTEGER_INT:
        return Integer.class;
    }
    return Object.class;
  }


  /**
   * @return   The elementName value
   * @see      de.objectcode.canyon.bpe.engine.variable.IVariable#getElementName()
   */
  public String getElementName()
  {
    return "basic-variable";
  }


  /**
   * @param element  Description of the Parameter
   * @see            de.objectcode.canyon.bpe.engine.variable.IVariable#toDom(org.dom4j.Element)
   */
  public void toDom( Element element )
  {
    element.addAttribute( "name", m_name );
    element.addAttribute( "type", m_type.getTag() );
    if ( m_value != null ) {
      element.addAttribute( "value", m_value.toString() );
    }
  }


  /**
   * @param in               Description of the Parameter
   * @exception IOException  Description of the Exception
   * @see                    de.objectcode.canyon.bpe.util.IStateHolder#hydrate(java.io.ObjectInput)
   */
  public void hydrate( HydrationContext context, ObjectInput in )
    throws IOException
  {
  	if (context.getSchema() == HydrationContext.CLASSIC_SCHEMA) {
			String name = in.readUTF();

			if (!m_name.equals(name)) {
				throw new IOException("Variable name does not match: " + name + " "
						+ m_name);
			}
		}
  	
    try {
      m_value = in.readObject();
    }
    catch ( ClassNotFoundException e ) {
      throw new IOException( e.toString() );
    }
  }


  /**
   * @param out              Description of the Parameter
   * @exception IOException  Description of the Exception
   * @see                    de.objectcode.canyon.bpe.util.IStateHolder#dehydrate(java.io.ObjectOutput)
   */
  public void dehydrate( HydrationContext context, ObjectOutput out )
    throws IOException
  {
  	if (context.getSchema() == HydrationContext.CLASSIC_SCHEMA) {
			out.writeUTF(m_name);
		}
    out.writeObject( m_value );
  }
}
