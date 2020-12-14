package de.objectcode.canyon.jmx.bpe;

import java.util.Date;
import javax.management.MBeanRegistration;
import javax.management.MBeanServer;
import javax.management.Notification;
import javax.management.NotificationFilter;
import javax.management.NotificationListener;
import javax.management.ObjectName;
import javax.management.timer.TimerNotification;
import javax.naming.InitialContext;
import javax.naming.Name;
import javax.naming.NamingException;
import net.sf.hibernate.Session;
import net.sf.hibernate.Transaction;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import de.objectcode.canyon.bpe.engine.BPEEngine;
import de.objectcode.canyon.jmx.NonSerializableFactory;
import de.objectcode.canyon.spi.ServiceManager;
import de.objectcode.canyon.spi.TransactionLocal;


/**
 * @jmx.mbean name="canyon.bpe:service=BPEEngineService" description="BPE engine service"
 * @jboss.service servicefile="jboss"
 * @jboss.xmbean
 * @jboss.depends object-name="jboss.mq.destination:service=Queue,name=WSAsyncRequest"
 * @jboss.depends object-name="canyon:service=ServiceManager"
 *
 * @author    junglas
 * @created   12. Juli 2004
 */
public class BPEEngineService implements BPEEngineServiceMBean, MBeanRegistration {
  private static final Log log = LogFactory.getLog(BPEEngineService.class);

  private String m_jndiName = "java:/canyon/BPEEngine";
  private String m_serviceManagerJndiName = "java:/canyon/ServiceManager";
  private boolean m_started = false;
  protected ServiceManager m_svcMgr;
  protected BPEEngine m_bpeEngine;
  private int m_timerNotification;
  private ObjectName m_timerService;
  private MBeanServer m_server;
  private int m_notificationInterval = 20;
  private TimedNotificationListener m_notificationListener;


  /**
   * @jmx.managed-attribute access="read-write" description="Name of the JMX timer service" value="canyon:service=Timer"
   *
   * @param timerService  The timerService to set.
   */
  public void setTimerService(ObjectName timerService) {
    m_timerService = timerService;
  }


  /**
   * @jmx.managed-attribute access="read-write" description="Interval to check for alarms"
   *
   * @param notificationInterval  The notificationInterval to set.
   */
  public void setNotificationInterval(int notificationInterval) {
    m_notificationInterval = notificationInterval;
  }


  /**
   * @jmx.managed-attribute access="read-write" description="JNDI name of the async request queue"
   *
   * @param serviceManagerJndiName  The serviceManagerJndiName to set.
   */
  public void setServiceManagerJndiName(String serviceManagerJndiName) {
    m_serviceManagerJndiName = serviceManagerJndiName;
  }


  /**
   * @jmx.managed-attribute access="read-write" description="JNDI name of the ServiceManager"
   *
   * @param jndiName       The new jndiName value
   * @exception Exception  Description of the Exception
   * @see                  de.neutrasoft.saints.core.obe.mbean.OBEServiceManagerMBean#setJndiName(java.lang.String)
   */
  public void setJndiName(String jndiName) throws Exception {
    String oldName = m_jndiName;
    m_jndiName = jndiName;
    if (m_started) {
      unbind();
      rebind();
    }
  }


  /**
   * @jmx.managed-attribute access="read-write" description="Name of the JMX timer service" value="canyon:service=Timer"
   *
   * @return   Returns the timerService.
   */
  public ObjectName getTimerService() {
    return m_timerService;
  }


  /**
   * @jmx.managed-attribute access="read-write" description="Interval to check for alarms"
   *
   * @return   Returns the notificationInterval.
   */
  public int getNotificationInterval() {
    return m_notificationInterval;
  }


  /**
   * @jmx.managed-attribute access="read-write" description="JNDI name of the async request queue"
   *
   * @return   Returns the serviceManagerJndiName.
   */
  public String getServiceManagerJndiName() {
    return m_serviceManagerJndiName;
  }


  /**
   * @jmx.managed-attribute access="read-write" description="JNDI name of the async request queue"
   *
   * @return   The jndiName value
   * @see      de.neutrasoft.saints.core.obe.mbean.OBEServiceManagerMBean#getJndiName()
   */
  public String getJndiName() {
    return m_jndiName;
  }


  /**
   * (non-Javadoc)
   *
   * @see   javax.management.MBeanRegistration#postDeregister()
   */
  public void postDeregister() {
  }


  /**
   * (non-Javadoc)
   *
   * @param registrationDone  Description of the Parameter
   * @see                     javax.management.MBeanRegistration#postRegister(java.lang.Boolean)
   */
  public void postRegister(Boolean registrationDone) {
  }


  /**
   * (non-Javadoc)
   *
   * @exception Exception  Description of the Exception
   * @see                  javax.management.MBeanRegistration#preDeregister()
   */
  public void preDeregister() throws Exception {
  }


  /**
   * @param server         Description of the Parameter
   * @param name           Description of the Parameter
   * @return               Description of the Return Value
   * @exception Exception  Description of the Exception
   * @see                  javax.management.MBeanRegistration#preRegister(javax.management.MBeanServer, javax.management.ObjectName)
   */
  public ObjectName preRegister(MBeanServer server, ObjectName name) throws Exception {
    if (name == null) {
      name = new ObjectName(server.getDefaultDomain() + ":name=" +
        this.getClass().getName());
    }
    m_server = server;

    return name;
  }


  /**
   * @jmx.managed-operation
   *
   * @exception Exception  Description of the Exception
   * @see                  de.neutrasoft.saints.core.obe.mbean.OBEServiceManagerMBean#start()
   */
  public void start() throws Exception {
    m_started = true;

    InitialContext ctx = new InitialContext();

    m_svcMgr = (ServiceManager) ctx.lookup(m_serviceManagerJndiName);

    m_bpeEngine = new BPEEngine();

    m_svcMgr.setBpeEngine(m_bpeEngine);

    if (System.getProperty("de.objectcode.canyon.jmx.bpe.BPEEngineService.migrationMode") == null) {
      m_timerNotification =
        ((Integer) m_server.invoke(m_timerService, "addNotification",
            new Object[] {
              "BPEEngineService", "BPEEngineService Notification", null,

              // User Object
              new Date(new Date().getTime() + 60000L),
              new Long(m_notificationInterval * 1000L),
          },
            new String[] {
              String.class.getName(), String.class.getName(),
              Object.class.getName(), Date.class.getName(),
              Long.TYPE.getName()
          })).intValue();
      m_notificationListener = new TimedNotificationListener();
      m_server.addNotificationListener(m_timerService, m_notificationListener,
        new Filter(new Integer(m_timerNotification)), null);
    }
    rebind();
  }


  /**
   * @jmx.managed-operation
   *
   * @exception Exception
   *              Description of the Exception
   * @see de.neutrasoft.saints.core.obe.mbean.OBEServiceManagerMBean#start()
   */
  public void stop() throws Exception {
    m_server.removeNotificationListener(m_timerService, m_notificationListener);
    m_server.invoke(m_timerService, "removeNotification", new Object[] { new Integer(m_timerNotification) },
      new String[] { Integer.class.getName() });

    m_started = false;
    unbind();
  }


  /**
   * Description of the Method
   *
   * @param name                 Description of the Parameter
   * @exception NamingException  Description of the Exception
   */
  private void unbind() throws NamingException {
    InitialContext ctx = new InitialContext();

    ctx.unbind(m_jndiName);
    NonSerializableFactory.unbind(m_jndiName);
  }


  /**
   * Description of the Method
   *
   * @exception NamingException  Description of the Exception
   */
  private void rebind() throws NamingException {
    InitialContext ctx = new InitialContext();
    Name fullName = ctx.getNameParser("").parse(m_jndiName);

    NonSerializableFactory.rebind(fullName, m_bpeEngine, true);
  }


  /**
         * @jmx.managed-operation
         *
         */
  public String dumpAllLocks() throws Exception {
    return m_svcMgr.getLockManager().dumpAllLocks();
  }

  /**
   * Filter to ensure that each Scheduler only gets notified when it is supposed to.
   *
   * @author    junglas
   * @created   2. Dezember 2003
   */
  public static class Filter implements NotificationFilter {
    private Integer m_id;


    /**
     * Create a Filter.
     *
     * @param id  Description of the Parameter
     */
    public Filter(Integer id) {
      m_id = id;
    }


    /**
     * @param notification  Description of the Parameter
     * @return              The notificationEnabled value
     * @see                 javax.management.NotificationFilter#isNotificationEnabled(javax.management.Notification)
     */
    public boolean isNotificationEnabled(Notification notification) {
      if (notification instanceof TimerNotification) {
        TimerNotification lTimerNotification = (TimerNotification) notification;
        return lTimerNotification.getNotificationID().equals(m_id);
      }
      return false;
    }
  }


  /**
   * Description of the Class
   *
   * @author    junglas
   * @created   2. Dezember 2003
   */
  public class TimedNotificationListener implements NotificationListener {
    /**
     * @param notification  Description of the Parameter
     * @param handback      Description of the Parameter
     * @see                 javax.management.NotificationListener#handleNotification(javax.management.Notification, java.lang.Object)
     */
    public void handleNotification(Notification notification, Object handback) {
      if (log.isDebugEnabled()) {
        log.debug("handleNotification: " + notification);
      }

      Transaction trx = null;
      boolean hasMore = true;

      while (hasMore) {
        try {
          m_svcMgr.beginTransaction();

          Session session = (Session) TransactionLocal.get();

          trx = session.beginTransaction();

          hasMore = m_bpeEngine.checkAlarms();

          session.flush();

          trx.commit();
          trx = null;
        } catch (Exception e) {
          log.error("Exception", e);
        } finally {
          if (trx != null) {
            try {
              trx.rollback();
            } catch (Exception e) {
            }
          }
          try {
            boolean flush = true;
            m_svcMgr.beforeEndTransaction(flush);
          } catch (Exception e) {
          }
          try {
            m_svcMgr.afterEndTransaction(trx == null);
          } catch (Exception e) {
          }
        }
      }
    }
  }


  /**
   * @jmx.managed-attribute access="read-only" description="CANYON Version"
   */
  public String getVersion() {
    return "CANYON Release 0.19.14";
  }
}
