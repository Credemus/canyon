package de.objectcode.canyon.persistent.bpe.repository;

import java.util.Iterator;

import de.objectcode.canyon.persistent.instance.PImmutableObjectValue;
import de.objectcode.canyon.persistent.instance.PObjectValue;
import net.sf.hibernate.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.objectcode.canyon.bpe.repository.IProcessInstanceRepository;
import de.objectcode.canyon.bpe.repository.IProcessInstanceVisitor;
import de.objectcode.canyon.bpe.repository.ProcessInstance;
import de.objectcode.canyon.spi.ObjectNotFoundException;
import de.objectcode.canyon.spi.RepositoryException;
import de.objectcode.canyon.spi.instance.IProcessInstance;

/**
 * @author    junglas
 * @created   21. Juni 2004
 */
public class ProcessInstanceRepository implements IProcessInstanceRepository
{
  private final static  Log  log  = LogFactory.getLog( ProcessInstanceRepository.class );

  private final SessionFactory m_sessionFactory;
  
  public ProcessInstanceRepository  ( SessionFactory sessionFactory )
  {
    m_sessionFactory = sessionFactory;
  }

  /**
   * @param processInstanceId        Description of the Parameter
   * @return                         The processInstance value
   * @exception RepositoryException  Description of the Exception
   * @see                            de.objectcode.canyon.bpe.repository.IProcessInstanceRepository#getProcessInstance(java.lang.String)
   */
  public ProcessInstance getProcessInstance( String processInstanceId )
    throws RepositoryException
  {
    if ( log.isDebugEnabled() ) {
      log.debug( "getProcessInstance: " + processInstanceId );
    }
    Session session = null;
    try {
			long start = 0L;
			if (log.isInfoEnabled()) {
				start = System.currentTimeMillis();
			}
      session = m_sessionFactory.openSession();

      PBPEProcessInstance  processInstance  = ( PBPEProcessInstance ) session.load( PBPEProcessInstance.class, new Long( processInstanceId ) );

      ProcessInstance result = processInstance.getProcessInstance(); 
  			if (log.isInfoEnabled()) {
  				long stop = System.currentTimeMillis();
  				log.info("Split ProcessInstanceRepository.getProcessInstance [" +processInstanceId+"]:" + (stop-start) + " ms");
  			}

      return result ;
    }
    catch ( NumberFormatException e ) {
      throw new ObjectNotFoundException( processInstanceId );
    }
    catch ( net.sf.hibernate.ObjectNotFoundException e ) {
      throw new ObjectNotFoundException( processInstanceId );
    }
    catch ( HibernateException e ) {
      log.error( "Exception", e );
      throw new RepositoryException( e );
    }
    finally {
      try {
        session.close();
      }
      catch ( Exception e ) {
      }
    }
  }

  /**
   * @param processInstance          Description of the Parameter
   * @return                         Description of the Return Value
   * @exception RepositoryException  Description of the Exception
   * @see                            de.objectcode.canyon.bpe.repository.IProcessInstanceRepository#saveProcessInstance(de.objectcode.canyon.bpe.repository.ProcessInstance)
   */
  public String saveProcessInstance( ProcessInstance processInstance )
    throws RepositoryException
  {
    if ( log.isDebugEnabled() ) {
      log.debug( "saveProcessInstance: " + processInstance );
    }
    Session session = null;
    try {
      session = m_sessionFactory.openSession();

      PBPEProcessInstance  pprocessInstance  = new PBPEProcessInstance( processInstance );

      session.save( pprocessInstance );
      session.flush();

      return String.valueOf( pprocessInstance.getEntityOid() );
    }
    catch ( HibernateException e ) {
      log.error( "Exception", e );
      throw new RepositoryException( e );
    }
    finally {
      try {
        session.close();
      }
      catch ( Exception e ) {
      }
    }
  }


  /**
   * @param processInstance          Description of the Parameter
   * @exception RepositoryException  Description of the Exception
   * @see                            de.objectcode.canyon.bpe.repository.IProcessInstanceRepository#updateProcessInstance(de.objectcode.canyon.bpe.repository.ProcessInstance)
   */
  public void updateProcessInstance( ProcessInstance processInstance )
    throws RepositoryException
  {
    if ( log.isDebugEnabled() ) {
      log.debug( "updateProcessInstance: " + processInstance.getProcessInstanceId() + " " + processInstance.getProcessState().length );
    }
    Session session = null;
    try {
      session = m_sessionFactory.openSession();

      PBPEProcessInstance  pprocessInstance  = ( PBPEProcessInstance ) session.load( PBPEProcessInstance.class, new Long( processInstance.getProcessInstanceId() ) );

			pprocessInstance.setProcessInstance( processInstance );

      session.update( pprocessInstance );
      session.flush();
    }
    catch ( HibernateException e ) {
      log.error( "Exception", e );
      throw new RepositoryException( e );
    }
    finally {
      try {
        session.close();
      }
      catch ( Exception e ) {
      }
    }
  }


  /**
   * @param visitor                  Description of the Parameter
   * @exception RepositoryException  Description of the Exception
   * @see                            de.objectcode.canyon.bpe.repository.IProcessInstanceRepository#iterateProcessInstances(de.objectcode.canyon.bpe.repository.IProcessInstanceVisitor)
   */
  public void iterateProcessInstances( boolean onlyOpenRunning, IProcessInstanceVisitor visitor )
    throws RepositoryException
  {
    log.info( "iterateProcessInstances: start " + onlyOpenRunning );
    Session session = null;
    try {
      session = m_sessionFactory.openSession();

      Query     query;
      
      if ( onlyOpenRunning ) {
        query = session.createQuery( "select o.processInstanceData from o in class " + PBPEProcessInstance.class + " where o.state <= 3");
      } else {
        query = session.createQuery( "select o.processInstanceData from o in class " + PBPEProcessInstance.class );
      }

      ScrollableResults results = query.scroll(ScrollMode.FORWARD_ONLY);

      while ( results.next() ) {
        PObjectValue processInstanceData  = ( PObjectValue ) results.get(0);
        ProcessInstance processInstance = (ProcessInstance)processInstanceData.getValue();

        visitor.visit( processInstance );

        session.evict( processInstanceData );
      }
    }
    catch ( HibernateException e ) {
      log.error( "Exception", e );
      throw new RepositoryException( e );
    }
    finally {
      try {
        session.close();
      }
      catch ( Exception e ) {
      }
      log.info( "iterateProcessInstances: done" );
    }
  }
  
  /**
   * @param visitor                  Description of the Parameter
   * @exception RepositoryException  Description of the Exception
   * @see                            de.objectcode.canyon.bpe.repository.IProcessInstanceRepository#iterateProcessInstances(de.objectcode.canyon.bpe.repository.IProcessInstanceVisitor)
   */
  public void  iterateSubProcessInstances( String parentProcessInstanceId, IProcessInstanceVisitor visitor )
    throws RepositoryException
  {
    if ( log.isDebugEnabled() ) {
      log.debug( "iterateSubProcessInstances: " + parentProcessInstanceId );
    }
    Session session = null;
    try {
      session = m_sessionFactory.openSession();

      Query     query;
      
      query = session.createQuery( "from o in class " + PBPEProcessInstance.class + " where o.parentEntityOid = :parentEntityOid");
      query.setLong("parentEntityOid",Long.parseLong(parentProcessInstanceId));
      Iterator  it       = query.iterate();

      while ( it.hasNext() ) {
        PBPEProcessInstance  processInstance  = ( PBPEProcessInstance ) it.next();

        visitor.visit( processInstance.getProcessInstance() );

        session.evict( processInstance );
      }
    }
    catch ( HibernateException e ) {
      log.error( "Exception", e );
      throw new RepositoryException( e );
    }
    finally {
      try {
        session.close();
      }
      catch ( Exception e ) {
      }
    }
  }

  public void updateProcessInstances( IProcessInstanceVisitor visitor )
  throws RepositoryException
{
  Session session = null;
  try {
    session = m_sessionFactory.openSession();

    Query     query = session.createQuery( "from o in class " + PBPEProcessInstance.class );
    
    Iterator  it       = query.iterate();

    while ( it.hasNext() ) {
      PBPEProcessInstance  processInstance  = ( PBPEProcessInstance ) it.next();

      ProcessInstance pi = processInstance.getProcessInstance(); 
      visitor.visit( pi );
      processInstance.setProcessInstance(pi);

      session.update( processInstance );
      session.flush();
    }
  }
  catch ( HibernateException e ) {
    log.error( "Exception", e );
    throw new RepositoryException( e );
  }
  finally {
    try {
      session.close();
    }
    catch ( Exception e ) {
    }
  }
}
  
  public void migrateProcessInstances( IProcessInstanceVisitor visitor )
  throws RepositoryException
{
  Session session = null;
  try {
    session = m_sessionFactory.openSession();

    Query     query = session.createQuery( "from o in class " + PBPEProcessInstance.class );
    
    Iterator  it       = query.iterate();

    while ( it.hasNext() ) {
      PBPEProcessInstance  processInstance  = ( PBPEProcessInstance ) it.next();

      ProcessInstance pi = processInstance.getProcessInstance();
      visitor.visit( pi );
      processInstance.migrate(pi);
      
      session.update( processInstance );
      session.flush();
    }
  }
  catch ( HibernateException e ) {
    log.error( "Exception", e );
    throw new RepositoryException( e );
  }
  finally {
    try {
      session.close();
    }
    catch ( Exception e ) {
    }
  }
}

  /**
   * @see de.objectcode.canyon.bpe.repository.IProcessInstanceRepository#iterateProcessInstances(java.lang.String, boolean, de.objectcode.canyon.bpe.repository.IProcessInstanceVisitor)
   */
  public void iterateProcessInstances (String processId,
                                       boolean onlyOpenRunning,
                                       IProcessInstanceVisitor visitor)
      throws RepositoryException
  {
    if ( log.isDebugEnabled() ) {
      log.debug( "iterateProcessInstances: " + processId + " " + onlyOpenRunning );
    }
    Session session = null;
    try {
      session = m_sessionFactory.openSession();

      Query     query;
      
      if ( onlyOpenRunning ) {
        query = session.createQuery( "from o in class " + PBPEProcessInstance.class + " where o.state <= 3 and o.processId = :processId");
      } else {
        query = session.createQuery( "from o in class " + PBPEProcessInstance.class + " where o.processId = :processId");
      }
      query.setString("processId", processId);
      
      Iterator  it       = query.iterate();

      while ( it.hasNext() ) {
        PBPEProcessInstance  processInstance  = ( PBPEProcessInstance ) it.next();

        visitor.visit( processInstance.getProcessInstance() );

        session.evict( processInstance );
      }
    }
    catch ( HibernateException e ) {
      log.error( "Exception", e );
      throw new RepositoryException( e );
    }
    finally {
      try {
        session.close();
      }
      catch ( Exception e ) {
      }
    }
  }
}
