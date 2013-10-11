package de.objectcode.canyon.wetdock.jmx;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.Name;
import javax.naming.NamingException;

import net.sf.hibernate.SessionFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.objectcode.canyon.jmx.NonSerializableFactory;
import de.objectcode.canyon.spi.ServiceManager;
import de.objectcode.canyon.wetdock.repository.participant.ParticipantRepository;
import de.objectcode.canyon.wetdock.user.persistent.UserManager;

/**
 * @author junglas
 */
public class WetdockRepositories implements WetdockRepositoriesMBean
{
  private final static Log log = LogFactory.getLog(WetdockRepositories.class);
  
  private String m_serviceManagerJndiName = "java:/canyon/ServiceManager";
  private String m_hibernateJndiName      = "java:/canyon/wetdock/HibernateFactory";
  private String m_userManagerJndiName    = "java:/canyon/wetdock/UserManager";

  private ParticipantRepository m_participantRepository;
  private UserManager m_userManager;
  
  /**
   * @return Returns the hibernateJndiName.
   */
  public String getHibernateJndiName()
  {
    return m_hibernateJndiName;
  }

  /**
   * @param hibernateJndiName
   *          The hibernateJndiName to set.
   */
  public void setHibernateJndiName( String hibernateJndiName )
  {
    m_hibernateJndiName = hibernateJndiName;
  }

  /**
   * @return Returns the serviceManagerJndiName.
   */
  public String getServiceManagerJndiName()
  {
    return m_serviceManagerJndiName;
  }

  /**
   * @param serviceManagerJndiName
   *          The serviceManagerJndiName to set.
   */
  public void setServiceManagerJndiName( String serviceManagerJndiName )
  {
    m_serviceManagerJndiName = serviceManagerJndiName;
  }

  /**
   * @return Returns the userManagerJndiName.
   */
  public String getUserManagerJndiName()
  {
    return m_userManagerJndiName;
  }

  /**
   * @param userManagerJndiName
   *          The userManagerJndiName to set.
   */
  public void setUserManagerJndiName( String userManagerJndiName )
  {
    m_userManagerJndiName = userManagerJndiName;
  }

  public void start()
      throws Exception
  {
    log.info( "Start Bep Repositories" );

    Context         ctx             = new InitialContext();
    ServiceManager  serviceManager  = ( ServiceManager ) ctx.lookup( m_serviceManagerJndiName );
    SessionFactory  factory         = ( SessionFactory ) ctx.lookup( m_hibernateJndiName );
    
    m_userManager = new UserManager(factory);
    m_participantRepository = new ParticipantRepository(m_userManager);    
    serviceManager.setParticipantRepository(m_participantRepository);
  
    rebind();
  }

  public void stop()
      throws Exception
  {
    unbind();
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
    Name            fullName  = ctx.getNameParser( "" ).parse( m_userManagerJndiName );

    NonSerializableFactory.rebind( fullName, m_userManager, true );
  }


  /**
   * Description of the Method
   *
   * @param name                 Description of the Parameter
   * @exception NamingException  Description of the Exception
   */
  private void unbind( )
    throws NamingException
  {
    InitialContext  ctx  = new InitialContext();

    ctx.unbind( m_userManagerJndiName );
    NonSerializableFactory.unbind( m_userManagerJndiName );
  }
}
