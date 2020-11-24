package de.objectcode.canyon.ejb;

import javax.ejb.EJBException;
import javax.ejb.MessageDrivenBean;
import javax.ejb.MessageDrivenContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @ejb.bean generate="false"
 * @ejb.home generate="false"
 * @ejb.interface generate="false"
 *
 * @author    junglas
 * @created   30. Oktober 2003
 */
public class BaseMessageBean implements MessageDrivenBean
{
	static final long serialVersionUID = -1106077740135821470L;
	
	protected transient  Log                   m_log;
  protected            MessageDrivenContext  m_ctx;


  /**
   * @param ctx               The new messageDrivenContext value
   * @exception EJBException  Description of the Exception
   * @see                     javax.ejb.MessageDrivenBean#setMessageDrivenContext(javax.ejb.MessageDrivenContext)
   */
  public void setMessageDrivenContext( MessageDrivenContext ctx )
    throws EJBException
  {
    m_ctx = ctx;

    if ( m_log == null ) {
      m_log = LogFactory.getLog( getClass() );
    }
  }


  /**
   * @see                     javax.ejb.MessageDrivenBean#ejbRemove()
   */
  public void ejbRemove()
  {
    m_log.debug( "Remove" );
  }

}
