package de.objectcode.canyon.spiImpl.lock;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import EDU.oswego.cs.dl.util.concurrent.Mutex;
import EDU.oswego.cs.dl.util.concurrent.ReentrantLock;
import de.objectcode.canyon.spi.RepositoryException;
import de.objectcode.canyon.spi.lock.ILockManager;

/**
 * @author    junglas
 * @created   19. Februar 2004
 */
public class LockManager implements ILockManager
{
  private final static  Log          log            = LogFactory.getLog( LockManager.class );

  private               ThreadLocal  m_threadLocal;
  private               Mutex        m_mapLock;
  private               Map          m_locks;


  /**
   *Constructor for the LockManager object
   */
  public LockManager()
  {
    m_threadLocal = new ThreadLocal();
    m_locks = new HashMap();
    m_mapLock = new Mutex();
  }


  /**
   * @param id                       Description of the Parameter
   * @exception RepositoryException  Description of the Exception
   * @see                            de.objectcode.canyon.spi.lock.ILockManager#lock(java.lang.String)
   */
  public void lock( String id )
    throws RepositoryException
  {
    if ( log.isDebugEnabled() ) {
      log.debug( "lock: " + id );
    }

    try {
      LockEntry  entry;

      m_mapLock.acquire();
      // jv 03.10.2006 16:36:55
	// disable diagnostic stacktraces
//      log.debug("Lock-Map: " + m_locks);      
      try {
        entry = ( LockEntry ) m_locks.get( id );
        if ( entry == null ) {
          entry = new LockEntry( id );
          m_locks.put( id, entry );
        }
        entry.incrCount();
      }
      finally {
        m_mapLock.release();
      }

      List       threadLocks  = ( List ) m_threadLocal.get();

      if ( threadLocks == null ) {
        threadLocks = new ArrayList();
        m_threadLocal.set( threadLocks );
      }


      if ( !entry.getLock().attempt( 1800 * 1000L ) ) {
	Exception e = new Exception("Unable to obtain lock within 30 minutes: " + entry.getId() + " Holder: " + entry.getHolder() );
        log.fatal( "PANIC", e);
      } else {
        // Lock optained
        threadLocks.add( entry );
//        if (log.isDebugEnabled()) {
          StringWriter trace = new StringWriter();
          new Exception().printStackTrace( new PrintWriter(trace) );
          entry.setHolder( new Date() + ":" + trace.toString() );
          log.debug("Thread-Locks: " + threadLocks);
//        }
      }
//  	entry.getLock().acquire();
    }
    catch ( Exception e ) {
      log.error( "Exception", e );
      throw new RepositoryException( e );
    }
  }


  /**
   * @param id                       Description of the Parameter
   * @exception RepositoryException  Description of the Exception
   * @see                            de.objectcode.canyon.spi.lock.ILockManager#unlock(java.lang.String)
   */
  public void unlock( String id )
    throws RepositoryException
  {
    if ( log.isDebugEnabled() ) {
      log.debug( "unlock: " + id );
    }

    try {
      m_mapLock.acquire();
      try {
        LockEntry  entry  = ( LockEntry ) m_locks.get( id );
        if ( entry != null ) {
          entry.getLock().release();

          if ( entry.decrCount() ) {
            m_locks.remove( id );
          }

          List  threadLocks  = ( List ) m_threadLocal.get();

          if ( threadLocks == null ) {
            threadLocks = new ArrayList();
            m_threadLocal.set( threadLocks );
          }
          threadLocks.remove( entry );
        } else {
          log.fatal( "Trying to unlock non-existing lock: " + id );
        }
      }
      finally {
        m_mapLock.release();
      }

    }
    catch ( Exception e ) {
      log.error( "Exception", e );
      throw new RepositoryException( e );
    }
  }


  /**
   * Description of the Method
   *
   * @exception RepositoryException  Description of the Exception
   */
  public void releaseAllLocks()
    throws RepositoryException
  {
    if ( log.isDebugEnabled() ) {
      log.debug( "releaseAllLocks" );
    }

    List  threadLocks  = ( List ) m_threadLocal.get();

    if ( threadLocks != null ) {
      Iterator  it  = new ArrayList( threadLocks ).iterator();

      while ( it.hasNext() ) {
        LockEntry  entry  = ( LockEntry ) it.next();

        unlock( entry.getId() );
      }
    }
  }

  public String dumpAllLocks() throws RepositoryException {
    StringBuffer buffy = new StringBuffer("LOCKS:");
    try {
      m_mapLock.acquire();
      try {
	Iterator i = m_locks.values().iterator();
	while (i.hasNext()) {
	  LockEntry entry = (LockEntry ) i.next();
	  buffy.append(entry).append(",\n");
	}
      }
      finally {
        m_mapLock.release();
      }
    }
    catch ( Exception e ) {
      log.error( "Exception", e );
      throw new RepositoryException( e );
    }
    return buffy.toString();
  }

  /**
   * Description of the Class
   *
   * @author    junglas
   * @created   19. Februar 2004
   */
  private static class LockEntry
  {
    private  String         m_id;
    private  ReentrantLock  m_lock;
    private  int            m_count;
    private  String         m_holder;

    /**
     *Constructor for the LockEntry object
     *
     * @param id  Description of the Parameter
     */
    LockEntry( String id )
    {
      m_id = id;
      m_lock = new ReentrantLock();
      m_count = 0;
    }


    /**
     * Gets the lock attribute of the LockEntry object
     *
     * @return   The lock value
     */
    public ReentrantLock getLock()
    {
      return m_lock;
    }


    /**
     * Gets the id attribute of the LockEntry object
     *
     * @return   The id value
     */
    public String getId()
    {
      return m_id;
    }


    /**
     * Description of the Method
     */
    public void incrCount()
    {
      m_count++;
    }


    /**
     * Description of the Method
     *
     * @return   Description of the Return Value
     */
    public boolean decrCount()
    {
      m_count--;

      return m_count == 0;
    }
    
    public String getHolder()
    {
      return m_holder;
    }


    public void setHolder( String holder )
    {
      m_holder = holder;
    }


    public String toString()
    {
      return "LockEntry(" + m_id + ", " + m_count + ", " + m_holder + ")";
    }
  }
}
