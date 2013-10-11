package de.objectcode.canyon.persistent.jmx;

import java.util.Date;

import javax.management.MBeanRegistration;
import javax.management.MBeanServer;
import javax.management.Notification;
import javax.management.NotificationFilter;
import javax.management.NotificationListener;
import javax.management.ObjectName;
import javax.management.timer.TimerNotification;
import javax.naming.Context;
import javax.naming.InitialContext;

import net.sf.hibernate.SessionFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.objectcode.canyon.persistent.async.AsyncManager;
import de.objectcode.canyon.persistent.instance.InstanceRepository;
import de.objectcode.canyon.persistent.participant.ResolverRepository;
import de.objectcode.canyon.persistent.process.ProcessRepository;
import de.objectcode.canyon.persistent.worklist.WorklistRepository;
import de.objectcode.canyon.spi.ServiceManager;

/**
 * @author    junglas
 * @created   16. Oktober 2003
 */
public class WSRepositories implements WSRepositoriesMBean, MBeanRegistration
{
  private final static  Log                        log                       = LogFactory.getLog( WSRepositories.class );
  private               String                     m_serviceManagerJndiName  = "java:/canyon/ServiceManager";
  private               String                     m_hibernateJndiName       = "java:/canyon/HibernateFactory";
  private               boolean                    m_started;
  private               ProcessRepository          m_processRepository;
  private               InstanceRepository         m_instanceRepository;
  private               ResolverRepository         m_resolverRepository;
  private               WorklistRepository         m_worklistRepository;
  private               AsyncManager               m_asyncManager;
  private               MBeanServer                m_server;
  private               int                        m_timerNotification;
  private               int                        m_timerAsync;
  private               TimedNotificationListener  m_notificationListener;
  private               AsyncListener              m_asyncListener;
  private               ObjectName                 m_timerService;
  private               int                        m_notificationInterval    = 30;
  private               int                        m_asyncInterval           = 10;
  private               boolean                    m_timerBasedAsyncManager  = false;


  /**
   * @param string
   */
  public void setServiceManagerJndiName( String string )
  {
    m_serviceManagerJndiName = string;
  }


  /**
   * @param string
   */
  public void setHibernateJndiName( String string )
  {
    m_hibernateJndiName = string;
  }


  /**
   * @param name
   */
  public void setTimerService( ObjectName name )
  {
    m_timerService = name;
  }


  /**
   * @param asyncInterval            The asyncInterval to set.
   */
  public void setAsyncInterval( int asyncInterval )
  {
    m_asyncInterval = asyncInterval;
  }


  /**
   * @param notificationInterval            The notificationInterval to set.
   */
  public void setNotificationInterval( int notificationInterval )
  {
    m_notificationInterval = notificationInterval;
  }


  /**
   * @param timerBasedAsyncManager  The timerBasedAsyncManager to set.
   */
  public void setTimerBasedAsyncManager( boolean timerBasedAsyncManager )
  {
    m_timerBasedAsyncManager = timerBasedAsyncManager;
  }


  /**
   * @return
   */
  public String getServiceManagerJndiName()
  {
    return m_serviceManagerJndiName;
  }


  /**
   * @return
   */
  public String getHibernateJndiName()
  {
    return m_hibernateJndiName;
  }


  /**
   * @return
   */
  public ObjectName getTimerService()
  {
    return m_timerService;
  }


  /**
   * @return   Returns the asyncInterval.
   */
  public int getAsyncInterval()
  {
    return m_asyncInterval;
  }


  /**
   * @return   Returns the notificationInterval.
   */
  public int getNotificationInterval()
  {
    return m_notificationInterval;
  }


  /**
   * @return   Returns the timerBasedAsyncManager.
   */
  public boolean isTimerBasedAsyncManager()
  {
    return m_timerBasedAsyncManager;
  }


  /**
   * @exception Exception                Description of the Exception
   * @see                  de.objectcode.flowws.persistent.jmx.WSRepositoriesMBean#start()
   */
  public void start()
    throws Exception
  {
    log.info( "Starting Repositories" );
    Context         ctx             = new InitialContext();
    SessionFactory  factory         = ( SessionFactory ) ctx.lookup( m_hibernateJndiName );
    ServiceManager  serviceManager  = ( ServiceManager ) ctx.lookup( m_serviceManagerJndiName );
    m_processRepository = new ProcessRepository( factory );
    m_instanceRepository = new InstanceRepository( factory );
    m_resolverRepository = new ResolverRepository( serviceManager, factory );
    m_worklistRepository = new WorklistRepository( factory );
    serviceManager.setProcessRepository( m_processRepository );
    serviceManager.setInstanceRepository( m_instanceRepository );
    serviceManager.setResolverRepository( m_resolverRepository );
    serviceManager.setWorklistRepository( m_worklistRepository );
    m_timerNotification = ( ( Integer ) m_server.invoke( m_timerService,
        "addNotification", new Object[]{"WSService", "WSService Notification",
        null,
    // User Object
        new Date( new Date().getTime() + 60000L ),
        new Long( m_notificationInterval * 1000L ),}, new String[]{
        String.class.getName(), String.class.getName(),
        Object.class.getName(), Date.class.getName(), Long.TYPE.getName()} ) )
        .intValue();
    m_notificationListener = new TimedNotificationListener();
    m_server.addNotificationListener( m_timerService, m_notificationListener,
        new Filter( new Integer( m_timerNotification ) ), null );

    if ( m_timerBasedAsyncManager ) {
      m_asyncManager = new AsyncManager( serviceManager, factory );
      serviceManager.setAsyncManager( m_asyncManager );
      m_timerAsync = ( ( Integer ) m_server.invoke( m_timerService,
          "addNotification", new Object[]{"WSService", "WSService Async", null,
      // User Object
          new Date( new Date().getTime() + 60000L ),
          new Long( m_asyncInterval * 1000L ),}, new String[]{
          String.class.getName(), String.class.getName(),
          Object.class.getName(), Date.class.getName(), Long.TYPE.getName()} ) )
          .intValue();
      m_asyncListener = new AsyncListener();
      m_server.addNotificationListener( m_timerService, m_asyncListener,
          new Filter( new Integer( m_timerAsync ) ), null );
    }
    m_started = true;
  }


  /**
   * @exception Exception                Description of the Exception
   * @see                  de.objectcode.flowws.persistent.jmx.WSRepositoriesMBean#stop()
   */
  public void stop()
    throws Exception
  {
    m_server.removeNotificationListener( m_timerService, m_notificationListener );
    m_server.invoke( m_timerService, "removeNotification",
        new Object[]{new Integer( m_timerNotification )},
        new String[]{Integer.class.getName()} );

    if ( m_timerBasedAsyncManager ) {
      m_server.removeNotificationListener( m_timerService, m_asyncListener );

      m_server.invoke( m_timerService, "removeNotification",
          new Object[]{new Integer( m_timerAsync )}, new String[]{Integer.class
          .getName()} );
    }

    m_started = false;
  }


  /**
   * @see   javax.management.MBeanRegistration#postDeregister()
   */
  public void postDeregister() { }


  /**
   * @param registrationDone            Description of the Parameter
   * @see                     javax.management.MBeanRegistration#postRegister(java.lang.Boolean)
   */
  public void postRegister( Boolean registrationDone ) { }


  /**
   * @exception Exception                Description of the Exception
   * @see                  javax.management.MBeanRegistration#preDeregister()
   */
  public void preDeregister()
    throws Exception { }


  /**
   * @param server                   Description of the Parameter
   * @param name                     Description of the Parameter
   * @return               Description of the Return Value
   * @exception Exception                Description of the Exception
   * @see                  javax.management.MBeanRegistration#preRegister(javax.management.MBeanServer, javax.management.ObjectName)
   */
  public ObjectName preRegister( MBeanServer server, ObjectName name )
    throws Exception
  {
    if ( name == null ) {
      name = new ObjectName( server.getDefaultDomain() + ":name="
          + this.getClass().getName() );
    }
    m_server = server;
    return name;
  }


  /**
   * Description of the Class
   *
   *
   * @author    junglas
   * @created   2. Dezember 2003
   */
  public class TimedNotificationListener implements NotificationListener
  {
    /**
     * @param notification            Description of the Parameter
     * @param handback                Description of the Parameter
     * @see                 javax.management.NotificationListener#handleNotification(javax.management.Notification, java.lang.Object)
     */
    public void handleNotification( Notification notification, Object handback )
    {
      if ( log.isDebugEnabled() ) {
        log.debug( "handleNotification: " + notification );
      }
    }
  }


  /**
   * Filter to ensure that each Scheduler only gets notified when it is supposed to.
   *
   *
   * @author    junglas
   * @created   2. Dezember 2003
   */
  public static class Filter implements NotificationFilter
  {
    private  Integer  m_id;


    /**
     * Create a Filter.
     *
     *
     * @param id            Description of the Parameter
     */
    public Filter( Integer id )
    {
      m_id = id;
    }


    /**
     * @param notification            Description of the Parameter
     * @return              The notificationEnabled value
     * @see                 javax.management.NotificationFilter#isNotificationEnabled(javax.management.Notification)
     */
    public boolean isNotificationEnabled( Notification notification )
    {
      if ( notification instanceof TimerNotification ) {
        TimerNotification  lTimerNotification  = ( TimerNotification ) notification;
        return lTimerNotification.getNotificationID().equals( m_id );
      }
      return false;
    }
  }


  /**
   * Description of the Class
   *
   *
   * @author    junglas
   * @created   23. Februar 2004
   */
  public class AsyncListener implements NotificationListener
  {
    /**
     * @param notification            Description of the Parameter
     * @param handback                Description of the Parameter
     * @see                 javax.management.NotificationListener#handleNotification(javax.management.Notification, java.lang.Object)
     */
    public void handleNotification( Notification notification, Object handback )
    {
      if ( log.isDebugEnabled() ) {
        log.debug( "handleNotification: " + notification );
      }
      try {
        m_asyncManager.checkMessages();
      }
      catch ( Exception e ) {
        log.error( "Exception", e );
      }
    }
  }

}
