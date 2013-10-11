package de.objectcode.canyon.persistent.process;

import java.util.Iterator;
import java.util.List;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;
import net.sf.hibernate.SessionFactory;

import org.apache.commons.lang.SerializationUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.objectcode.canyon.model.WorkflowPackage;
import de.objectcode.canyon.model.process.WorkflowProcess;
import de.objectcode.canyon.persistent.filter.QueryBuilder;
import de.objectcode.canyon.spi.ObjectNotFoundException;
import de.objectcode.canyon.spi.RepositoryException;
import de.objectcode.canyon.spi.TransactionLocal;
import de.objectcode.canyon.spi.filter.IFilter;
import de.objectcode.canyon.spi.process.IProcessDefinitionID;
import de.objectcode.canyon.spi.process.IProcessRepository;

/**
 * @author    junglas
 * @created   18.06.2003
 * @version   $Id: ProcessRepository.java,v 1.16 2004/04/23 15:07:22 ruth Exp $
 */
public class ProcessRepository
     implements IProcessRepository
{
  private final static  Log             log        = LogFactory.getLog( ProcessRepository.class );

  private               SessionFactory  m_factory;


  /**
   *Constructor for the HibProcessRepository object
   *
   * @param sessionFactory  Description of the Parameter
   */
  public ProcessRepository( SessionFactory sessionFactory )
  {
    m_factory = sessionFactory;
  }


  /**
   * @param pkg                      Description of the Parameter
   * @param state                    Description of the Parameter
   * @exception RepositoryException  Description of the Exception
   * @see                            org.obe.spi.service.ProcessRepository#createPackage(org.obe.Package, int)
   */
  public void createPackage( WorkflowPackage pkg, int state )
    throws RepositoryException
  {
    try {
    	Session session = (Session)TransactionLocal.get();
    	
			WorkflowProcess[] procs = pkg.getWorkflowProcesses();
			for (int i = 0; i < procs.length; i++) {
				procs[i].setState(state);
			}

      PPackage  hibPackage  = new PPackage( pkg );

      Iterator  it          = hibPackage.getProcessDefinitionsSet().iterator();

      while ( it.hasNext() ) {
        PProcessDefinition  procDef  = ( PProcessDefinition ) it.next();				
        procDef.setState( state );
      }

      session.save( hibPackage );

      session.flush();
    }
    catch ( HibernateException e ) {
      log.error( "Exception", e );
      throw new RepositoryException( e );
    }
  }


  /**
   * @param packageId                Description of the Parameter
   * @exception RepositoryException  Description of the Exception
   * @see                            org.obe.spi.service.ProcessRepository#deletePackage(java.lang.String)
   */
  public void deletePackage( String packageId )
    throws RepositoryException
  {
    try {
    	Session session = (Session)TransactionLocal.get();

      Query     query  = session.createQuery( "from o in class " + PPackage.class.getName() + " where o.id.id = :packageId order by o.id.version desc" );

      query.setString( "packageId", packageId );

      Iterator  it     = query.iterate();

      while ( it.hasNext() ) {
        PPackage  hibPackage  = ( PPackage ) it.next();

        session.delete( hibPackage );
      }

      session.flush();
    }
    catch ( net.sf.hibernate.ObjectNotFoundException e ) {
      throw new ObjectNotFoundException( packageId );
    }
    catch ( HibernateException e ) {
      log.error( "Exception", e );
      throw new RepositoryException( e );
    }
  }


  /**
   * @param packageId                Description of the Parameter
   * @return                         Description of the Return Value
   * @exception RepositoryException  Description of the Exception
   * @see                            org.obe.spi.service.ProcessRepository#findPackage(java.lang.String)
   */
  public WorkflowPackage findPackage( String packageId )
    throws RepositoryException
  {
    try {
    	Session session = (Session)TransactionLocal.get();
    	
      Query     query  = session.createQuery( "from o in class " + PPackage.class.getName() + " where o.id.id = :packageId order by o.id.version desc" );

      query.setString( "packageId", packageId );

      Iterator  it     = query.iterate();

      if ( it.hasNext() ) {
        PPackage  hibPackage  = ( PPackage ) it.next();

        return (WorkflowPackage)SerializationUtils.deserialize(hibPackage.getPackageData());
      }
      throw new ObjectNotFoundException( packageId );
    }
    catch ( HibernateException e ) {
      log.error( "Exception", e );
      throw new RepositoryException( e );
    }
  }


  /**
   * Description of the Method
   *
   * @param packageId                Description of the Parameter
   * @param version                  Description of the Parameter
   * @return                         Description of the Return Value
   * @exception RepositoryException  Description of the Exception
   */
  public WorkflowPackage findPackage( String packageId, String version )
    throws RepositoryException
  {
    try {
    	Session session = (Session)TransactionLocal.get();

      PPackage  hibPackage  = ( PPackage ) session.load( PPackage.class, new PPackageID( packageId, version ) );

      return (WorkflowPackage)SerializationUtils.deserialize(hibPackage.getPackageData());
    }
    catch ( net.sf.hibernate.ObjectNotFoundException e ) {
      throw new ObjectNotFoundException( packageId + " " + version );
    }
    catch ( HibernateException e ) {
      log.error( "Exception", e );
      throw new RepositoryException( e );
    }
  }


  /**
   * Description of the Method
   *
   * @param filter                   Description of the Parameter
   * @return                         Description of the Return Value
   * @exception RepositoryException  Description of the Exception
   */
  public int countPackages( IFilter filter )
    throws RepositoryException
  {
    try {
    	Session session = (Session)TransactionLocal.get();
    	
      QueryBuilder  builder  = new QueryBuilder();

      if ( filter != null ) {
        filter.toBuilder( builder );
      }

      Query         query    = builder.createQuery( session, "select count(*) from o in class " + PPackage.class.getName(), null );

      return ( ( Integer ) query.iterate().next() ).intValue();
    }
    catch ( HibernateException e ) {
      log.error( "Exception", e );
      throw new RepositoryException( e );
    }
  }


  /**
   * @param filter                   Description of the Parameter
   * @return                         Description of the Return Value
   * @exception RepositoryException  Description of the Exception
   * @see                            org.obe.spi.service.ProcessRepository#findPackages(org.wfmc.wapi.WMFilter, boolean)
   */
  public WorkflowPackage[] findPackages( IFilter filter )
    throws RepositoryException
  {
    try {
    	Session session = (Session)TransactionLocal.get();
    	
      QueryBuilder     builder  = new QueryBuilder();

      if ( filter != null ) {
        filter.toBuilder( builder );
      }

      Query            query    = builder.createQuery( session, "from o in class " + PPackage.class.getName(), "o.id.id asc" );

      List             result   = query.list();
      WorkflowPackage  ret[]      = new WorkflowPackage[result.size()];
      Iterator         it       = result.iterator();
      int              i;

      for ( i = 0; it.hasNext(); i++ ) {
        PPackage  hibPackage  = ( PPackage ) it.next();
        ret[i] = (WorkflowPackage)SerializationUtils.deserialize(hibPackage.getPackageData());
      }

      return ret;
    }
    catch ( HibernateException e ) {
      log.error( "Exception", e );
      throw new RepositoryException( e );
    }
  }


  /**
   * @param processDefinitionId      Description of the Parameter
   * @return                         Description of the Return Value
   * @exception RepositoryException  Description of the Exception
   * @see                            org.obe.spi.service.ProcessRepository#findProcessDefinitionState(java.lang.String)
   */
  public int findProcessDefinitionState( String processDefinitionId )
    throws RepositoryException
  {
    try {
    	
    	Session session = (Session)TransactionLocal.get();

      Query     query  = session.createQuery( "from o in class " + PProcessDefinition.class.getName() + " where o.id.id = :processDefinitionId order by o.id.version desc" );

      query.setString( "processDefinitionId", processDefinitionId );

      Iterator  it     = query.iterate();

      if ( it.hasNext() ) {
        PProcessDefinition  procDef  = ( PProcessDefinition ) it.next();

        return procDef.getState();
      } else {
        throw new ObjectNotFoundException( processDefinitionId );
      }
    }
    catch ( net.sf.hibernate.ObjectNotFoundException e ) {
      throw new ObjectNotFoundException( processDefinitionId );
    }
    catch ( HibernateException e ) {
      log.error( "Exception", e );
      throw new RepositoryException( e );
    }
  }


  /**
   * @param processDefinitionId      Description of the Parameter
   * @return                         Description of the Return Value
   * @exception RepositoryException  Description of the Exception
   * @see                            org.obe.spi.service.ProcessRepository#findWorkflowProcess(java.lang.String)
   */
  public WorkflowProcess findWorkflowProcess( IProcessDefinitionID processDefinitionId )
    throws RepositoryException
  {
    try {
    	Session session = (Session)TransactionLocal.get();
    	
      PProcessDefinition  procDef     = ( PProcessDefinition ) session.load( PProcessDefinition.class, new PProcessDefinitionID( processDefinitionId.getId(), processDefinitionId.getVersion() ) );
      PPackage            hibPackage  = procDef.getPackage();

      return hibPackage.getProcessDefinition( processDefinitionId.getId() );
    }
    catch ( net.sf.hibernate.ObjectNotFoundException e ) {
      throw new ObjectNotFoundException( processDefinitionId.toString() );
    }
    catch ( HibernateException e ) {
      log.error( "Exception", e );
      throw new RepositoryException( e );
    }
  }


  /**
   * Retrieve the latest version of a process definition.
   *
   * @param processDefinitionId      Description of the Parameter
   * @return                         Description of the Return Value
   * @exception RepositoryException  Description of the Exception
   */
  public WorkflowProcess findWorkflowProcess( String processDefinitionId )
    throws RepositoryException
  {
    if ( log.isDebugEnabled() ) {
      log.debug("findWorkflowProcess: " + processDefinitionId);
    }
    try {
    	Session session = (Session)TransactionLocal.get();

      Query     query  = session.createQuery( "from o in class " + PProcessDefinition.class.getName() + " where o.id.id = :processDefinitionId order by o.id.version desc" );

      query.setString( "processDefinitionId", processDefinitionId );

      Iterator  it     = query.iterate();

      if ( it.hasNext() ) {
        PProcessDefinition  procDef     = ( PProcessDefinition ) it.next();
        PPackage            hibPackage  = procDef.getPackage();

        return hibPackage.getProcessDefinition( processDefinitionId );
      } else {
        throw new ObjectNotFoundException( processDefinitionId.toString() );
      }
    }
    catch ( net.sf.hibernate.ObjectNotFoundException e ) {
      throw new ObjectNotFoundException( processDefinitionId.toString() );
    }
    catch ( HibernateException e ) {
      log.error( "Exception", e );
      throw new RepositoryException( e );
    }
  }


  /**
   * Description of the Method
   *
   * @param filter                   Description of the Parameter
   * @return                         Description of the Return Value
   * @exception RepositoryException  Description of the Exception
   */
  public int countWorkflowProcesses( IFilter filter )
    throws RepositoryException
  {
    try {
    	Session session = (Session)TransactionLocal.get();
    	
      QueryBuilder  builder  = new QueryBuilder();

      if ( filter != null ) {
        filter.toBuilder( builder );
      }

      Query         query    = builder.createQuery( session, "select count(*) from o in class " + PProcessDefinition.class.getName(), null );

      return ( ( Integer ) query.iterate().next() ).intValue();
    }
    catch ( HibernateException e ) {
      log.error( "Exception", e );
      throw new RepositoryException( e );
    }
  }


  /**
   * @param filter                   Description of the Parameter
   * @return                         Description of the Return Value
   * @exception RepositoryException  Description of the Exception
   * @see                            org.obe.spi.service.ProcessRepository#findWorkflowProcesses(org.wfmc.wapi.WMFilter, boolean)
   */
  public WorkflowProcess[] findWorkflowProcesses( IFilter filter )
    throws RepositoryException
  {
    try {
    	Session session = (Session)TransactionLocal.get();

      QueryBuilder     builder  = new QueryBuilder();

      if ( filter != null ) {
        filter.toBuilder( builder );
      }

      Query            query    = builder.createQuery( session, "from o in class " + PProcessDefinition.class.getName(), "o.id.id asc" );

      List             result   = query.list();
      WorkflowProcess  ret[]      = new WorkflowProcess[result.size()];
      Iterator         it       = result.iterator();
      int              i;

      for ( i = 0; it.hasNext(); i++ ) {
        PProcessDefinition  procDef     = ( PProcessDefinition ) it.next();
        PPackage            hibPackage  = procDef.getPackage();        
        ret[i] = hibPackage.getProcessDefinition( procDef.getProcessDefinitionId() );        
      }

      return ret;
    }
    catch ( HibernateException e ) {
      log.error( "Exception", e );
      throw new RepositoryException( e );
    }
  }


  /**
   * @param pkg                      Description of the Parameter
   * @exception RepositoryException  Description of the Exception
   * @see                            org.obe.spi.service.ProcessRepository#updatePackage(org.obe.Package)
   */
  public void updatePackage( WorkflowPackage pkg )
    throws RepositoryException
  {
    try {
    	Session session = (Session)TransactionLocal.get();

      PPackage  hibPackage  = ( PPackage ) session.load( PPackage.class, pkg.getId() );

      hibPackage.update( pkg );

      session.flush();
    }
    catch ( net.sf.hibernate.ObjectNotFoundException e ) {
      throw new ObjectNotFoundException( pkg.getId() );
    }
    catch ( HibernateException e ) {
      log.error( "Exception", e );
      throw new RepositoryException( e );
    }
  }


  /**
   * @param processDefinitionId      Description of the Parameter
   * @param newState                 Description of the Parameter
   * @exception RepositoryException  Description of the Exception
   * @see                            org.obe.spi.service.ProcessRepository#updateProcessDefinitionState(java.lang.String, int)
   */
  public void updateProcessDefinitionState( String processDefinitionId, int newState )
    throws RepositoryException
  {
    try {
    	Session session = (Session)TransactionLocal.get();

      Query     query  = session.createQuery( "from o in class " + PProcessDefinition.class.getName() + " where o.id.id = :processDefinitionId order by o.id.version desc" );

      query.setString( "processDefinitionId", processDefinitionId );

      Iterator  it     = query.iterate();

      if ( it.hasNext() ) {
        PProcessDefinition  procDef     = ( PProcessDefinition ) it.next();
        procDef.setState( newState );
        
        
        PPackage            hibPackage  = procDef.getPackage();
        hibPackage.setProcessDefinitionState( processDefinitionId, newState );
        
        session.update(procDef);
        session.update(hibPackage);
        
      } else {
        throw new ObjectNotFoundException( processDefinitionId.toString() );
      }
            
      session.flush();
      
    }
    catch ( net.sf.hibernate.ObjectNotFoundException e ) {
      throw new ObjectNotFoundException( processDefinitionId );
    }
    catch ( HibernateException e ) {
      log.error( "Exception", e );
      throw new RepositoryException( e );
    }
  }

  /**
   *  (non-Javadoc)
   *
   * @exception RepositoryException  Description of the Exception
   * @see                            de.objectcode.canyon.spi.instance.IInstanceRepository#beginTransaction()
   */
  public void beginTransaction()
    throws RepositoryException
  {
    try {
      if ( TransactionLocal.get() == null ) {
        TransactionLocal.set( m_factory.openSession() );
      }
    }
    catch ( HibernateException e ) {
      log.error( "Exception", e );
      throw new RepositoryException( e );
    }
  }


  /**
   *  (non-Javadoc)
   *
   * @exception RepositoryException  Description of the Exception
   * @see                            de.objectcode.canyon.spi.instance.IInstanceRepository#endTransaction()
   */
  public void endTransaction(boolean flush)
    throws RepositoryException
  {
    try {
      Session  session  = ( Session ) TransactionLocal.get();

      if ( session != null ) {
				if(flush){
					session.flush();    			
				}
        session.close();
        TransactionLocal.set( null );
      }
    }
    catch ( HibernateException e ) {
      log.error( "Exception", e );
      throw new RepositoryException( e );
    }
  }
}
