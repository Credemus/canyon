package de.objectcode.canyon.bpe.engine.activities.xpdl;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import de.objectcode.canyon.bpe.engine.BPEEngine;
import de.objectcode.canyon.bpe.engine.EngineException;
import de.objectcode.canyon.bpe.engine.activities.Activity;
import de.objectcode.canyon.bpe.engine.evaluator.IExpression;
import de.objectcode.canyon.bpe.engine.evaluator.ScriptExpressionFactory;
import de.objectcode.canyon.bpe.engine.variable.IVariable;
import de.objectcode.canyon.model.ExtendedAttribute;
import de.objectcode.canyon.model.activity.Tool;
import de.objectcode.canyon.model.application.Application;
import de.objectcode.canyon.model.data.ActualParameter;
import de.objectcode.canyon.model.data.ExternalReference;
import de.objectcode.canyon.model.data.FormalParameter;
import de.objectcode.canyon.model.data.ParameterMode;
import de.objectcode.canyon.spi.RepositoryException;
import de.objectcode.canyon.spi.evaluator.IEvaluator;
import de.objectcode.canyon.spi.tool.IToolConnector;
import de.objectcode.canyon.spi.tool.Parameter;
import de.objectcode.canyon.spiImpl.tool.EJBConnector;
import de.objectcode.canyon.spiImpl.tool.JavaClassConnector;
import de.objectcode.canyon.spiImpl.tool.event.BaseEventConnector;
import de.objectcode.canyon.spiImpl.tool.event.ExternalEventConnector;
import de.objectcode.canyon.spiImpl.tool.event.InternalEventConnector;

/**
 * @author    junglas
 * @created   15. Juli 2004
 */
public class ApplicationInfo implements Serializable
{
  final static  long                 serialVersionUID      = -3882893221791467287L;

  private       String               m_id;
  private       String               m_name;
  private       String               m_description;
  private       ExtendedAttribute[]  m_extendedAttributes;
  private       FormalParameter[]    m_formalParameters;
  private				String							 m_processId;
  private				String							 m_processVersion;
  private				String							 m_packageId;
  


  /**
   *Constructor for the ApplicationInfo object
   *
   * @param application  Description of the Parameter
   */
  ApplicationInfo( Tool tool )
  {
    Map extendedAttributes = new HashMap();
    Application application = tool.getApplication();
    int i;
    ExtendedAttribute appExtAttrs[] = application.getExtendedAttributes();
    ExtendedAttribute toolExtAttrs[] = tool.getExtendedAttributes();
    
    for ( i = 0; i < appExtAttrs.length; i++ ) {
      extendedAttributes.put(appExtAttrs[i].getName(), appExtAttrs[i]);
    }
    for ( i = 0; i < toolExtAttrs.length; i++ ) {
      extendedAttributes.put(toolExtAttrs[i].getName(), toolExtAttrs[i]);
    }
    
    m_extendedAttributes = new ExtendedAttribute[extendedAttributes.size()];
    extendedAttributes.values().toArray(m_extendedAttributes);    

    m_id = application.getId();
    m_name = application.getName();
    m_description = application.getDescription();
    m_formalParameters = application.getFormalParameters();
    m_packageId = tool.getActivity().getWorkflowProcess().getPackage().getId();
    m_processId = tool.getActivity().getWorkflowProcess().getId();
    m_processVersion = tool.getActivity().getWorkflowProcess().findWorkflowVersion();
  }


  /**
   * @return   Returns the description.
   */
  public String getDescription()
  {
    return m_description;
  }



  /**
   * @return   Returns the extendedAttributes.
   */
  public ExtendedAttribute[] getExtendedAttributes()
  {
    return m_extendedAttributes;
  }


  /**
   * @return   Returns the formalParameters.
   */
  public FormalParameter[] getFormalParameters()
  {
    return m_formalParameters;
  }


  /**
   * @return   Returns the id.
   */
  public String getId()
  {
    return m_id;
  }


  /**
   * @return   Returns the name.
   */
  public String getName()
  {
    return m_name;
  }


  /**
   * Gets the toolConnector attribute of the ApplicationInfo object
   *
   * @param bpeEngine                Description of the Parameter
   * @return                         The toolConnector value
   * @exception RepositoryException  Description of the Exception
   */
  public IToolConnector getToolConnector( BPEEngine bpeEngine )
    throws RepositoryException
  {
    int  i;

    Application application = createLookupApplication();
    
    // EventConnector is core functionality
    BaseEventConnector eventConnector = BaseEventConnector.getEventConnector(BPEEngine.ENGINE_ID, application);
    if (eventConnector != null)
      return eventConnector;
    
    // Now let ToolRepository handle it
    if ( bpeEngine.getToolRepository() != null ) {
      return bpeEngine.getToolRepository().findTool( application, m_id );
    }

    // Default handling
    Map  attrs  = new HashMap();

    for ( i = 0; i < m_extendedAttributes.length; i++ ) {
      attrs.put( m_extendedAttributes[i].getName(), m_extendedAttributes[i].getValue() );
    }

    if ( attrs.get( "canyon:javaClass" ) != null ) {
      String  className   = ( String ) attrs.get( "canyon:javaClass" );
      String  methodName  = ( String ) attrs.get( "canyon:javaMethod" );

      if ( methodName == null ) {
        methodName = m_id;
      }

      return new JavaClassConnector( className, methodName );
    } else if ( attrs.get( "canyon:ejbJndi" ) != null ) {
      String   jndiName       = ( String ) attrs.get( "canyon:ejbJndi" );
      String   homeClassName  = ( String ) attrs.get( "canyon:ejbHome" );
      String   className      = ( String ) attrs.get( "canyon:ejbClass" );
      boolean  local          = "true".equalsIgnoreCase( ( String ) attrs.get( "canyon:ejbLocal" ) );
      String   methodName     = ( String ) attrs.get( "canyon:ejbMethod" );
      String   userName       = ( String ) attrs.get( "canyon:userName" );
      String   password       = ( String ) attrs.get( "canyon:password" );
      String   policyName     = ( String ) attrs.get( "canyon:policyName" );

      if ( methodName == null ) {
        methodName = m_id;
      }

      if ( userName != null && password != null ) {
        return new EJBConnector( userName, password, policyName, jndiName, homeClassName, className, local, methodName );
      } else {
        return new EJBConnector( jndiName, homeClassName, className, local, methodName );
      }
    } 

    return null;
  }


  /**
   * @return
   */
  private Application createLookupApplication() {
    int i;
    Application  application  = new Application();

    application.setId( m_id );
    application.setName( m_name );
    application.setDescription( m_description );
    // TODO handle case, where an ER is set in the model
    ExternalReference er = new ExternalReference();
    er.setNamespace(m_packageId+"/"+m_processId+"/"+m_processVersion);    
    application.setExternalReference(er);

    for ( i = 0; i < m_extendedAttributes.length; i++ ) {
      application.addExtendedAttribute( m_extendedAttributes[i] );
    }
    for ( i = 0; i < m_formalParameters.length; i++ ) {
      application.addFormalParameter( m_formalParameters[i] );
    }
    return application;
  }


  /**
   * Description of the Method
   *
   * @param actualParms          Description of the Parameter
   * @param scriptLanguage       Description of the Parameter
   * @param activity             Description of the Parameter
   * @return                     Description of the Return Value
   * @exception EngineException  Description of the Exception
   */
  public Parameter[] createParameters( ActualParameter actualParms[], String scriptLanguage, Activity activity )
    throws EngineException
  {
    Parameter[]  parms      = new Parameter[m_formalParameters.length];

    IEvaluator   evaluator  = null;
    if (actualParms.length != m_formalParameters.length)
      throw new EngineException("Actual params do not match formal params for activity '" + activity.getId() + "' in process '" + activity.getProcess().getId()+"'!");
    
    for ( int i = 0, n = m_formalParameters.length; i < n; i++ ) {
      FormalParameter  fp          = m_formalParameters[i];
      ActualParameter  ap          = actualParms[i];
      String           text        = ap.getText();
      String           actualName  = text;
      Object           value;
      ParameterMode    mode        = fp.getMode();
      if ( mode == ParameterMode.IN ) {
        // IN parameter: text is an expression to evaluate.
        actualName = null;

        IExpression  expression  = ScriptExpressionFactory.createExpression( scriptLanguage, text );
        value = expression.eval( activity );
      } else if ( mode == ParameterMode.INOUT ) {
        // INOUT parameter: text is the name of the attr to pass.
        IVariable variable = activity.getVariable( text );
        if (variable == null)
          throw new EngineException("Actual parameter '" + text +"' for formal parameter '" + fp.getName() + "' in activity '"+ activity.getId() + "' of process '" + activity.getProcess().getId() +"' does not exists in workflow relavant data");
        value = variable.getValue();
      } else {
        value = null;
      }
      parms[i] = new Parameter( fp.getId(), actualName,
          fp.getDataType(), mode, fp.getDescription(), value );
    }
    return parms;
  }
}
