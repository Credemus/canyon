package de.objectcode.canyon.bpe.engine.correlation;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.Serializable;
import java.util.Arrays;

import org.dom4j.Element;

import de.objectcode.canyon.bpe.engine.EngineException;
import de.objectcode.canyon.bpe.engine.evaluator.IAssignableExpression;
import de.objectcode.canyon.bpe.engine.evaluator.IExpression;
import de.objectcode.canyon.bpe.util.HydrationContext;
import de.objectcode.canyon.bpe.util.IDomSerializable;
import de.objectcode.canyon.bpe.util.IStateHolder;

/**
 * @author    junglas
 * @created   15. Juni 2004
 */
public class Correlation implements Serializable, IDomSerializable, IStateHolder
{
  final static         long            serialVersionUID     = 7902062509879138951L;
  protected            CorrelationSet  m_correlationSet;
  protected transient  Object[]        m_correlationValues;

  public Correlation ( CorrelationSet correlationSet )
  {
    m_correlationSet = correlationSet;
  }

  /**
   *Constructor for the Correlation object
   *
   * @param correlationSet  Description of the Parameter
   * @param values          Description of the Parameter
   */
  public Correlation( CorrelationSet correlationSet, Object[] values )
  {
    m_correlationSet = correlationSet;
    m_correlationValues = values;
  }


  /**
   * Gets the name attribute of the Correlation object
   *
   * @return   The name value
   */
  public String getName()
  {
    return m_correlationSet.getName();
  }


  /**
   * @return   The elementName value
   * @see      de.objectcode.canyon.bpe.util.IDomSerializable#getElementName()
   */
  public String getElementName()
  {
    return "correlation";
  }


  /**
   * Description of the Method
   *
   * @param obj  Description of the Parameter
   * @return     Description of the Return Value
   */
  public boolean equals( Object obj )
  {
    if ( !( obj instanceof Correlation ) ) {
      return false;
    }
    Correlation  castObj  = ( Correlation ) obj;

    if ( !m_correlationSet.equals( castObj.m_correlationSet ) ) {
      return false;
    }

    return Arrays.equals( m_correlationValues, castObj.m_correlationValues );
  }


  /**
   * Description of the Method
   *
   * @param message              Description of the Parameter
   * @return                     Description of the Return Value
   * @exception EngineException  Description of the Exception
   */
  public boolean match( Message message )
    throws EngineException
  {
    int          i;
    IAssignableExpression  expressions[]  = m_correlationSet.getExpressions();

    for ( i = 0; i < expressions.length; i++ ) {
      Object  value  = expressions[i].eval( message );

      if ( !value.equals( m_correlationValues[i] ) ) {
        return false;
      }
    }
    return true;
  }

  public void correlate ( Message message )
  	throws EngineException
  {
    int          i;
    IAssignableExpression  expressions[]  = m_correlationSet.getExpressions();

    for ( i = 0; i < expressions.length; i++ ) {
      expressions[i].assign(message, m_correlationValues[i]);
    }
  }

  /**
   * @param element  Description of the Parameter
   * @see            de.objectcode.canyon.bpe.util.IDomSerializable#toDom(org.dom4j.Element)
   */
  public void toDom( Element element )
  {
    element.addAttribute( "name", getName() );

    IExpression  expressions[]  = m_correlationSet.getExpressions();
    int          i;

    for ( i = 0; i < expressions.length; i++ ) {
      Element  expressionElement  = element.addElement( expressions[i].getElementName() );

      expressions[i].toDom( expressionElement );

      Element  valueElement       = expressionElement.addElement( "value" );

      if ( m_correlationValues != null && m_correlationValues[i] != null ) {
        valueElement.addText( m_correlationValues[i].toString() );
      }
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
    int     i;
    int     size;
  	if (context.getSchema() == HydrationContext.CLASSIC_SCHEMA) {
			String name = in.readUTF();

			if (!m_correlationSet.getName().equals(name)) {
				throw new IOException("CorrelationSet name does not match: " + name
						+ " " + m_correlationSet.getName());
			}
		} else {
		}
  	
    size  = in.readInt();
  	if (context.getSchema() == HydrationContext.CLASSIC_SCHEMA
				|| size != -1) {

			m_correlationValues = new Object[size];
			try {
				for (i = 0; i < size; i++) {
					m_correlationValues[i] = in.readObject();
				}
			} catch (ClassNotFoundException e) {
				throw new IOException(e.toString());
			}
		}
  }


  /**
	 * @param out
	 *          Description of the Parameter
	 * @exception IOException
	 *              Description of the Exception
	 * @see de.objectcode.canyon.bpe.util.IStateHolder#dehydrate(java.io.ObjectOutput)
	 */
  public void dehydrate( HydrationContext context, ObjectOutput out )
    throws IOException
  {
  	if (context.getSchema() == HydrationContext.CLASSIC_SCHEMA) {
			out.writeUTF(m_correlationSet.getName());
			out.writeInt(m_correlationValues.length);
		} else {
			if (m_correlationValues==null) {
				out.writeInt(-1);
				return;
			} else 
				out.writeInt(m_correlationValues.length);
		}

    int  i;

    for ( i = 0; i < m_correlationValues.length; i++ ) {
      out.writeObject( m_correlationValues[i] );
    }
  }
  
  public boolean isEmpty() {
  	return m_correlationValues == null;
  }
}
