package de.objectcode.canyon.bpe.engine.activities;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import org.dom4j.Element;

import de.objectcode.canyon.bpe.engine.EngineException;
import de.objectcode.canyon.bpe.engine.ExtensibleElement;
import de.objectcode.canyon.bpe.engine.evaluator.ICondition;
import de.objectcode.canyon.bpe.util.HydrationContext;
import de.objectcode.canyon.bpe.util.IStateHolder;

/**
 * @author    junglas
 * @created   7. Juni 2004
 */
public class Link extends ExtensibleElement implements IStateHolder
{
  final static  long        serialVersionUID       = 8331982262005225225L;

  protected     LinkState   m_state;

  protected     String      m_name;
  protected     Activity    m_source;
  protected     Activity    m_target;
  protected     ICondition  m_transitionCondition;


  /**
   *Constructor for the Link object
   *
   * @param name    Description of the Parameter
   * @param source  Description of the Parameter
   * @param target  Description of the Parameter
   */
  Link( String name, Activity source, Activity target )
  {
    m_name = name;
    m_source = source;
    m_target = target;
    m_state = LinkState.UNKNOWN;
  }


  /**
   * @param state  The state to set.
   */
  public void setState( LinkState state )
  {
    m_state = state;
  }


  /**
   * @param transitionCondition  The transitionCondition to set.
   */
  public void setTransitionCondition( ICondition transitionCondition )
  {
    m_transitionCondition = transitionCondition;
  }


  /**
   * @return   Returns the name.
   */
  public String getName()
  {
    return m_name;
  }


  /**
   * @return   Returns the source.
   */
  public Activity getSource()
  {
    return m_source;
  }


  /**
   * @return   Returns the target.
   */
  public Activity getTarget()
  {
    return m_target;
  }


  /**
   * Gets the elementName attribute of the Link object
   *
   * @return   The elementName value
   */
  public String getElementName()
  {
    return "link";
  }


  /**
   * @return   Returns the state.
   */
  public LinkState getState()
  {
    return m_state;
  }


  /**
   *Constructor for the fail object
   *
   * @exception EngineException  Description of the Exception
   */
  public void fail()
    throws EngineException
  {
    if ( m_state != LinkState.FALSE ) {
      m_state = LinkState.FALSE;
      m_target.linkStateChanged( this );
    }
  }


  /**
   * Description of the Method
   *
   * @exception EngineException  Description of the Exception
   */
  void fire()
    throws EngineException
  {
    if ( m_state != LinkState.TRUE ) {
      if ( m_transitionCondition == null || m_transitionCondition.eval( m_source ) ) {
        m_state = LinkState.TRUE;
      } else {
        m_state = LinkState.FALSE;
      }
      m_target.linkStateChanged( this );
    }
  }


  /**
   * Description of the Method
   */
  void reset()
  {
    m_state = LinkState.UNKNOWN;
  }


  /**
   * Description of the Method
   *
   * @param element  Description of the Parameter
   */
  public void toDom( Element element )
  {
    element.addAttribute( "name", m_name );
    element.addAttribute( "state", m_state.getTag() );
    element.addAttribute( "source", m_source.getId() );
    element.addAttribute( "target", m_target.getId() );

    if ( m_transitionCondition != null ) {
      m_transitionCondition.toDom( element.addElement( m_transitionCondition.getElementName() ) );
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
				throw new IOException("Link name does not match: " + name + " "
						+ m_name);
			}
			m_state = LinkState.fromInt(in.readInt());
		} else {
			m_state = LinkState.fromInt(in.readByte());
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
			out.writeUTF(m_name);
			out.writeInt(m_state.getValue());
		} else {
			out.writeByte(m_state.getValue());
		}
  }
}
