package de.objectcode.canyon.wetdock.test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wfmc.wapi.WMWorkflowException;

import de.objectcode.canyon.api.bpe.admin.BPEProcessRepositoryAdmin;
import de.objectcode.canyon.api.bpe.admin.BPEProcessRepositoryAdminHome;
import de.objectcode.canyon.model.WorkflowPackage;
import de.objectcode.canyon.spi.parser.IParserFactory;
import de.objectcode.canyon.spi.parser.XPDLParserException;
import de.objectcode.canyon.spiImpl.parser.DefaultXPDLParser;

/**
 * @author junglas
 */
public class ProcessAdminHelper
{
  private final static Log  log = LogFactory.getLog( ProcessAdminHelper.class );

  BPEProcessRepositoryAdmin m_processRepositoryAdmin;

  public ProcessAdminHelper()
      throws NamingException, RemoteException, CreateException
  {
    InitialContext ctx = new InitialContext();

    BPEProcessRepositoryAdminHome processRepositoryAdminHome = (BPEProcessRepositoryAdminHome) PortableRemoteObject
        .narrow( ctx.lookup( BPEProcessRepositoryAdminHome.JNDI_NAME ),
            BPEProcessRepositoryAdminHome.class );

    m_processRepositoryAdmin = processRepositoryAdminHome.create();
  }

  /**
   * @return Returns the processRepositoryAdmin.
   */
  public BPEProcessRepositoryAdmin getProcessRepositoryAdmin()
  {
    return m_processRepositoryAdmin;
  }

  public void createPackage( byte[] content )
      throws RemoteException, WMWorkflowException, IOException, XPDLParserException
  {

    if ( log.isDebugEnabled() ) {
      log.debug( "create package" );
    }

    WorkflowPackage pkg = parseContent( content );

    if ( log.isDebugEnabled() ) {
      log.debug( "create package on WfMS" );
    }

    getProcessRepositoryAdmin().createPackage( content, IParserFactory.XPDL );
  }


  public void updatePackage( byte[] content )
      throws RemoteException, WMWorkflowException, IOException, XPDLParserException
  {

    if ( log.isDebugEnabled() ) {
      log.debug( "create package" );
    }

    WorkflowPackage pkg = parseContent( content );

    if ( log.isDebugEnabled() ) {
      log.debug( "create package on WfMS" );
    }

    getProcessRepositoryAdmin().createOrReplacePackage( content, IParserFactory.XPDL );
  }
  
  private WorkflowPackage parseContent( byte[] fileData )
      throws IOException, XPDLParserException
  {

    if ( log.isDebugEnabled() ) {
      log.debug( "parse parseByteArray" );
    }

    DefaultXPDLParser parser = new DefaultXPDLParser();
    return parser.parse( new ByteArrayInputStream( fileData ) );
  }
}
