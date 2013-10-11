package de.objectcode.canyon.jmx;

import javax.naming.InitialContext;
import javax.naming.Name;
import javax.naming.NamingException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.objectcode.canyon.jmx.async.JMSAsyncManager;
import de.objectcode.canyon.spi.ServiceManager;
import de.objectcode.canyon.worklist.WorklistEngine;

/**
 * @jmx.mbean name="canyon:service=ServiceManager" description="Service Manager"
 * @jboss.service servicefile="jboss"
 * @jboss.xmbean
 * @jboss.depends object-name="jboss.mq.destination:service=Queue,name=WSAsyncRequest"
 *
 * @author    junglas
 * @created   16. Oktober 2003
 */
public class WSServiceManager implements WSServiceManagerMBean
{
  private final static  Log             log                     = LogFactory.getLog( WSServiceManager.class );

  private               String          m_jndiName              = "java:/canyon/ServiceManager";
  private               String          m_asyncJndiName         = "queue/WSAsyncRequest";
  private               boolean         m_started;
  private               ServiceManager  m_svcMgr;
  private               boolean         m_jmsBasedAsyncManager  = true;


  /**
   * @jmx.managed-attribute access="read-write" description="JNDI name of the ServiceManager"
   *
   * @param jndiName       The new jndiName value
   * @exception Exception  Description of the Exception
   * @see                  de.neutrasoft.saints.core.obe.mbean.OBEServiceManagerMBean#setJndiName(java.lang.String)
   */
  public void setJndiName( String jndiName )
    throws Exception
  {
    String  oldName  = m_jndiName;
    m_jndiName = jndiName;
    if ( m_started ) {
      unbind( oldName );
      rebind();
    }
  }


  /**
   * @jmx.managed-attribute access="read-write" description="JNDI name of the async request queue"
   *
   * @param string
   */
  public void setAsyncJndiName( String string )
  {
    m_asyncJndiName = string;
  }


  /**
   * @jmx.managed-attribute access="read-write" description="true if a JMSAsyncManager should be used"
   *
   * @param jmsBasedAsyncManager  The jmsBasedAsyncManager to set.
   */
  public void setJmsBasedAsyncManager( boolean jmsBasedAsyncManager )
  {
    m_jmsBasedAsyncManager = jmsBasedAsyncManager;
  }


  /**
   * @jmx.managed-attribute access="read-write" description="JNDI name of the async request queue"
   *
   * @return   The jndiName value
   * @see      de.neutrasoft.saints.core.obe.mbean.OBEServiceManagerMBean#getJndiName()
   */
  public String getJndiName()
  {
    return m_jndiName;
  }


  /**
   * @jmx.managed-attribute access="read-write" description="JNDI name of the async request queue"
   *
   * @return
   */
  public String getAsyncJndiName()
  {
    return m_asyncJndiName;
  }


  /**
   * @jmx.managed-attribute access="read-write" description="true if a JMSAsyncManager should be used"
   *
   * @return   Returns the jmsBasedAsyncManager.
   */
  public boolean isJmsBasedAsyncManager()
  {
    return m_jmsBasedAsyncManager;
  }


  /**
   * @jmx.managed-operation
   *
   * @exception Exception  Description of the Exception
   * @see                  de.neutrasoft.saints.core.obe.mbean.OBEServiceManagerMBean#start()
   */
  public void start()
    throws Exception
  {
    log.info( "Starting ServiceManager" );

    m_svcMgr = new ServiceManager();

    if ( m_jmsBasedAsyncManager ) {
      JMSAsyncManager  asyncManager  = new JMSAsyncManager( m_asyncJndiName );
      m_svcMgr.setAsyncManager( asyncManager );
    }
    WorklistEngine  worklist  = new WorklistEngine( m_svcMgr );

    m_svcMgr.setWorklistEngine( worklist );

    m_started = true;
    rebind();
  }


  /**
   * @jmx.managed-operation
   *
   * @exception Exception  Description of the Exception
   * @see                  de.neutrasoft.saints.core.obe.mbean.OBEServiceManagerMBean#stop()
   */
  public void stop()
    throws Exception
  {
    log.info( "Stopping ServiceManager" );

    m_svcMgr = null;
    m_started = false;
    unbind( m_jndiName );
  }


  /**
   * Description of the Method
   *
   * @exception NamingException  Description of the Exception
   */
  private void rebind()
    throws NamingException
  {
    InitialContext  ctx       = new InitialContext();
    Name            fullName  = ctx.getNameParser( "" ).parse( m_jndiName );

    NonSerializableFactory.rebind( fullName, m_svcMgr, true );
  }


  /**
   * Description of the Method
   *
   * @param jndiName             Description of the Parameter
   * @exception NamingException  Description of the Exception
   */
  private void unbind( String jndiName )
    throws NamingException
  {
    InitialContext  ctx  = new InitialContext();

    ctx.unbind( m_jndiName );
    NonSerializableFactory.unbind( m_jndiName );
  }

}
