package de.objectcode.canyon.ejb.bpe.admin;

import de.objectcode.canyon.CanyonRuntimeException;
import de.objectcode.canyon.bpe.engine.BPEEngine;
import de.objectcode.canyon.bpe.engine.EngineException;
import de.objectcode.canyon.bpe.engine.activities.BPEProcess;
import de.objectcode.canyon.bpe.factory.xpdl.XPDLProcessFactory;
import de.objectcode.canyon.ejb.BaseServiceManagerBean;
import de.objectcode.canyon.model.WorkflowPackage;
import de.objectcode.canyon.model.process.WorkflowProcess;
import de.objectcode.canyon.spi.RepositoryException;
import de.objectcode.canyon.spiImpl.parser.DefaultXPDLParser;

import java.io.ByteArrayInputStream;

import javax.ejb.CreateException;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.wfmc.wapi.WMWorkflowException;

/**
 * @ejb.bean name="BPEProcessRepositoryAdmin" type="Stateless"
 *           jndi-name="de/objectcode/canyon/ejb/bpe/admin/ProcessRepositoryAdmin"
 *           local-jndi-name="de/objectcode/canyon/ejb/bpe/admin/ProcessRepositoryAdminLocal"
 *           transaction-type="Bean"
 *           view-type="both"
 * @ejb.permission unchecked="true"
 *
 * @ejb.resource-ref res-ref-name="ServiceManager" res-type="de.objectcode.canyon.spi.ServiceManager"
 *   res-auth="Application"
 * @jboss.resource-ref res-ref-name="ServiceManager" jndi-name="java:/canyon/ServiceManager"
 * @ejb.resource-ref res-ref-name="BPEEngine" res-type="de.objectcode.canyon.bpe.engine.BPEEngine"
 *   res-auth="Application"
 * @jboss.resource-ref res-ref-name="BPEEngine" jndi-name="java:/canyon/BPEEngine"
 *
 *
 * @author    junglas
 *
 * @created   20. Juni 2003
 */
public class ProcessRepositoryAdminBean extends BaseServiceManagerBean
{
	static final long serialVersionUID = -8345433046933222103L;
	
	private final static  String     BPEENGINE_COMP_NAME  = "java:comp/env/BPEEngine";

  protected transient   BPEEngine  m_bpeEngine;


  /**
   * @ejb.interface-method
   *
   * @param pkg                      Description of the Parameter
   * @exception WMWorkflowException  Description of the Exception
   */
  public void createPackage( WorkflowPackage pkg )
    throws WMWorkflowException
  {
    try {
      beginTransaction();

      WorkflowProcess  processes[]  = pkg.getWorkflowProcesses();
      int              i;

      long packageRevisionOid = m_bpeEngine.createPackageRevision(pkg.getId(),pkg.findPackageVersion());
      for ( i = 0; i < processes.length; i++ ) {
        XPDLProcessFactory  xpdlFactory  = new XPDLProcessFactory( processes[i] );

        BPEProcess          process      = xpdlFactory.createProcess();

        m_bpeEngine.createProcess( packageRevisionOid, process, processes[i] );
      }
      m_serviceManager.getWorkflowEventBroker().firePackageCreated(pkg);      
    }
    catch ( EngineException e ) {
      rollbackTransaction();
      m_log.error( "Exception", e );
      if ( e.getCause() != null ) {
        m_log.error( "Cause", e.getCause() );
      }
      throw new WMWorkflowException( e );
    }
    catch ( RepositoryException e ) {
      rollbackTransaction();
      m_log.error( "Exception", e );
      throw new CanyonRuntimeException( e );
    }
    finally {
      try {
        commitTransaction();
      }
      catch ( Exception e ) {
        m_log.error( "Exception", e );
      }
    }
  }


  /**
   * @ejb.interface-method
   *
   * @param content                  Description of the Parameter
   * @param contentType              Description of the Parameter
   * @exception WMWorkflowException  Description of the Exception
   */
  public void createPackage( byte[] content, String contentType )
    throws WMWorkflowException
  {
    try {
      beginTransaction();

      DefaultXPDLParser  parser           = new DefaultXPDLParser();

      WorkflowPackage    workflowPackage  = parser.parse( new ByteArrayInputStream( content ) );

      workflowPackage.validate();

      long packageRevisionOid = m_bpeEngine.createPackageRevision(workflowPackage.getId(),workflowPackage.findPackageVersion());
      WorkflowProcess    processes[]        = workflowPackage.getWorkflowProcesses();
      int                i;

      for ( i = 0; i < processes.length; i++ ) {
        XPDLProcessFactory  xpdlFactory  = new XPDLProcessFactory( processes[i] );

        BPEProcess          process      = xpdlFactory.createProcess();

        m_bpeEngine.createProcess( packageRevisionOid, process, processes[i] );
      }
      m_serviceManager.getWorkflowEventBroker().firePackageCreated(workflowPackage);      

    }
    catch ( RepositoryException e ) {
      rollbackTransaction();
      m_log.error( "Exception", e );
      throw new CanyonRuntimeException( e );
    }
    catch ( Exception e ) {
      rollbackTransaction();
      m_log.error( "Exception", e );
      if ( e.getCause() != null ) {
        m_log.error( "Cause", e.getCause() );
      }
      throw new WMWorkflowException( e );
    }
    finally {
      try {
        commitTransaction();
      }
      catch ( Exception e ) {
        m_log.error( "Exception", e );
      }
    }
  }


  /**
   * @ejb.interface-method
   *
   * @param content                  Description of the Parameter
   * @param contentType              Description of the Parameter
   * @exception WMWorkflowException  Description of the Exception
   */
  public void createOrReplacePackage( byte[] content, String contentType )
    throws WMWorkflowException
  {
    try {
      beginTransaction();

      DefaultXPDLParser  parser           = new DefaultXPDLParser();

      WorkflowPackage    workflowPackage  = parser.parse( new ByteArrayInputStream( content ) );

      workflowPackage.validate();

      WorkflowProcess    processes[]        = workflowPackage.getWorkflowProcesses();
      int                i;

      for ( i = 0; i < processes.length; i++ ) {
        XPDLProcessFactory  xpdlFactory  = new XPDLProcessFactory( processes[i] );

        BPEProcess          process      = xpdlFactory.createProcess();

        m_bpeEngine.createOrReplaceProcess( -1L, process, processes[i] );
      }

      m_serviceManager.getWorkflowEventBroker().firePackageUpdated(workflowPackage);
    }
    catch ( Exception e ) {
      rollbackTransaction();
      m_log.error( "Exception", e );
      if ( e.getCause() != null ) {
        m_log.error( "Cause", e.getCause() );
      }
      throw new WMWorkflowException( e );
    }
    finally {
      try {
        commitTransaction();
      }
      catch ( Exception e ) {
        m_log.error( "Exception", e );
      }
    }
  }


  /**
   * Create a new ZReihe SessionBean.
   *
   * @exception CreateException  on error
   *
   * @ejb.create-method
   */
  public void ejbCreate()
    throws CreateException
  {
    m_log.debug( "Create" );

    obtainServiceManager();
  }


  /**
   * @see   de.objectcode.canyon.ejb.BaseServiceManagerBean#obtainServiceManager()
   */
  protected void obtainServiceManager()
  {
    super.obtainServiceManager();

    try {
      InitialContext  ctx        = new InitialContext();

      BPEEngine       bpeEngine  = ( BPEEngine ) ctx.lookup( BPEENGINE_COMP_NAME );

      m_bpeEngine = bpeEngine;
    }
    catch ( NamingException e ) {
      m_log.fatal( "Exception", e );
    }
  }
}
