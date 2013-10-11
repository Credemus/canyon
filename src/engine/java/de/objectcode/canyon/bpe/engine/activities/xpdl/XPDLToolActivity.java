package de.objectcode.canyon.bpe.engine.activities.xpdl;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Element;

import de.objectcode.canyon.bpe.engine.EngineException;
import de.objectcode.canyon.bpe.engine.activities.Scope;
import de.objectcode.canyon.bpe.engine.handler.Fault;
import de.objectcode.canyon.bpe.engine.variable.IVariable;
import de.objectcode.canyon.model.activity.Tool;
import de.objectcode.canyon.spi.tool.BPEContext;
import de.objectcode.canyon.spi.tool.IToolConnector;
import de.objectcode.canyon.spi.tool.Parameter;
import de.objectcode.canyon.spi.tool.ReturnValue;

/**
 * @author    junglas
 * @created   5. August 2004
 */
public class XPDLToolActivity extends BaseXPDLActivity
{
  static final long serialVersionUID = 3456554877889089781L;
  
  private final static  Log              log                = LogFactory.getLog( XPDLToolActivity.class );

  private               ApplicationInfo  m_applicationData;
  private               ToolInfo         m_toolData;
  private               String           m_scriptLanguage;


  /**
   *Constructor for the XPDLToolActivity object
   *
   * @param name   Description of the Parameter
   * @param scope  Description of the Parameter
   * @param tool   Description of the Parameter
   */
  public XPDLToolActivity( String name, Scope scope, Tool tool, int index )
  {
    super( tool.getActivity().getId() + "_" + tool.getId() + "_" + index, name, scope, tool.getActivity() );

    m_scriptLanguage = tool.getActivity().getWorkflowProcess().getPackage().getScript() != null ? tool.getActivity().getWorkflowProcess().getPackage().getScript().getType() : null;
    m_applicationData = new ApplicationInfo( tool );
    m_toolData = new ToolInfo( tool );
  }


  /**
   * @return   The elementName value
   * @see      de.objectcode.canyon.bpe.util.IDomSerializable#getElementName()
   */
  public String getElementName()
  {
    return "xpdl-tool-activity";
  }


  /**
   * @exception EngineException  Description of the Exception
   * @see                        de.objectcode.canyon.bpe.engine.activities.Activity#start()
   */
  public void start()
    throws EngineException
  {
    super.start();

    m_scope.getEngine().getAsyncManager().asyncCompleteBPEActivity( m_scope.getProcess().getProcessInstanceId(), m_id );
  }


  /**
   * @exception EngineException  Description of the Exception
   * @see                        de.objectcode.canyon.bpe.engine.activities.Activity#complete()
   */
  public void complete()
    throws EngineException
  {
    if ( log.isDebugEnabled() ) {
      log.debug("Complete: " + m_id);
    }
    long start = 0L;
    if ( log.isInfoEnabled() ) {
    	start = System.currentTimeMillis();
    }
    
    try {
      executeTool();

      super.complete();
    }
    catch ( EngineException e ) {
    	Fault fault = new Fault(e,this);
      m_scope.throwFault(fault);
    }
    if ( log.isInfoEnabled() ) {
    	long split =  System.currentTimeMillis() - start;
      log.info( "Split XPDLToolActivity.complete" + getDiagnosticInfo()+":" + split + " ms" );  	
    }
  }

  
  /**
   * Description of the Method
   *
   * @exception EngineException  Description of the Exception
   */
  private void executeTool()
    throws EngineException
  {
    if ( log.isInfoEnabled() ) {
      log.info("executeTool: " + getDiagnosticInfo() + " " + m_toolData);
    }
    try {
      Parameter[]     parameters    = m_applicationData.createParameters( m_toolData.getActualParameters(), m_scriptLanguage, this );
      IToolConnector  connector     = m_applicationData.getToolConnector(getProcess().getBPEEngine());
      String path = getProcess().getParentProcessInstanceIdPath();
      if (path == null)
        path = getProcess().getProcessInstanceId();
      else
        path = path + "_" + getProcess().getProcessInstanceId();
      BPEContext context = new BPEContext(getProcess().getClientId(),m_applicationData.getExtendedAttributes(),getProcess().getProcessInstanceId(),path,getProcess().getId(),getId());;

      ReturnValue[]   returnValues  = connector.invoke( context, parameters );

      // Reaquire lock. A call to an EJB that calls the Workflow-Engine might drop it
      m_scope.getProcess().getBPEEngine().getLockManager().lock(m_scope.getProcess().getProcessInstanceId());
      
      for ( int i = 0; i < returnValues.length; i++ ) {
        ReturnValue  returnValue  = returnValues[i];

        if ( log.isDebugEnabled() ) {
          log.debug( "Putting " + returnValue.actualName + " = " +
              returnValue.value + " into workflow data" );
        }

        IVariable    variable     = getVariable( returnValue.actualName );

        if ( variable != null ) {
          variable.setValue( returnValue.value );
        }
      }
    }
    catch ( Exception e ) {
    	log.error( "Exception in activity '" + getId() + "' of pid='" + getProcess().getProcessInstanceId()+"'", e );
    	throw new EngineException( e );
    }
  }


  /**
   * @param element  Description of the Parameter
   * @see            de.objectcode.canyon.bpe.util.IDomSerializable#toDom(org.dom4j.Element)
   */
  public void toDom( Element element )
  {
    super.toDom( element );
  }
}
