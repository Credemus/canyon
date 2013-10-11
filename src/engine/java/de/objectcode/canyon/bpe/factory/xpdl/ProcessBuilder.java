package de.objectcode.canyon.bpe.factory.xpdl;

import java.util.HashMap;
import java.util.Map;

import de.objectcode.canyon.bpe.connector.SubProcessFinishConnector;
import de.objectcode.canyon.bpe.connector.SubProcessInitConnector;
import de.objectcode.canyon.bpe.engine.EngineException;
import de.objectcode.canyon.bpe.engine.activities.BPEProcess;
import de.objectcode.canyon.bpe.engine.activities.Flow;
import de.objectcode.canyon.bpe.engine.activities.Invoke;
import de.objectcode.canyon.bpe.engine.activities.Receive;
import de.objectcode.canyon.bpe.engine.activities.Sequence;
import de.objectcode.canyon.bpe.engine.correlation.CorrelationPattern;
import de.objectcode.canyon.bpe.engine.correlation.CorrelationSet;
import de.objectcode.canyon.bpe.engine.evaluator.ComplexValueExpression;
import de.objectcode.canyon.bpe.engine.evaluator.IAssignableExpression;
import de.objectcode.canyon.bpe.engine.evaluator.IExpression;
import de.objectcode.canyon.bpe.engine.evaluator.RuntimeConstantExpression;
import de.objectcode.canyon.bpe.engine.evaluator.ScriptExpressionFactory;
import de.objectcode.canyon.bpe.engine.evaluator.VariableExpression;
import de.objectcode.canyon.bpe.engine.handler.FaultHandler;
import de.objectcode.canyon.bpe.engine.variable.ComplexType;
import de.objectcode.canyon.bpe.util.ExtendedAttributeHelper;
import de.objectcode.canyon.model.ExtendedAttribute;
import de.objectcode.canyon.model.GraphConformance;
import de.objectcode.canyon.model.activity.Activity;
import de.objectcode.canyon.model.application.Application;
import de.objectcode.canyon.model.data.FormalParameter;
import de.objectcode.canyon.model.data.ParameterMode;
import de.objectcode.canyon.model.process.DataField;
import de.objectcode.canyon.model.process.WorkflowProcess;
import de.objectcode.canyon.model.transition.Transition;

/**
 * @author junglas
 * @created 9. Juni 2004
 */
public class ProcessBuilder
{
  private BPEProcess m_bpeProcess;

  /**
   * Constructor for the ProcessBuilder object
   */
  public ProcessBuilder()
  {
  }

  /**
   * @return Returns the bpeProcess.
   */
  public BPEProcess getBPEProcess()
  {
    return m_bpeProcess;
  }

  /**
   * Description of the Method
   * 
   * @param workflowProcess
   *          Description of the Parameter
   */
  public void build( WorkflowProcess workflowProcess )
      throws EngineException
  {
    m_bpeProcess = new BPEProcess( workflowProcess.getId(), workflowProcess.findWorkflowVersion(),
        workflowProcess.getName(), workflowProcess.getProcessHeader().getDurationUnit() );

    buildMessageTypes( workflowProcess );

    DataField packageFields[] = workflowProcess.getPackage().getDataFields();
    DataField processFields[] = workflowProcess.getDataFields();
    DataFieldBuilder dataFieldBuilder = new DataFieldBuilder( m_bpeProcess );
    int i;

    for ( i = 0; i < packageFields.length; i++ ) {
      dataFieldBuilder.build( packageFields[i] );
    }

    for ( i = 0; i < processFields.length; i++ ) {
      dataFieldBuilder.build( processFields[i] );
    }

    FormalParameter formalParameters[] = workflowProcess.getFormalParameters();

    for ( i = 0; i < formalParameters.length; i++ ) {
      dataFieldBuilder.build( formalParameters[i] );
    }

    Sequence processSequence = new Sequence( workflowProcess.getName(), m_bpeProcess );

    m_bpeProcess.setActivity( processSequence );

    Receive initialReceive = new Receive( workflowProcess.getName(), m_bpeProcess, workflowProcess
        .getId()
        + "-init", m_bpeProcess.getType( workflowProcess.getId() + "-subflow-request" ), true );
    CorrelationSet parentCorrelationSet = new CorrelationSet( "parent-flow",
        new IAssignableExpression[] { new VariableExpression( "parentProcessId" ),
            new VariableExpression( "parentActivityId" ) } );

    initialReceive.addCorrelation( parentCorrelationSet, true );

    if ( workflowProcess.getFormalParameters() != null ) {
      FormalParameter[] parameters = workflowProcess.getFormalParameters();

      ComplexValueExpression inputExpression = new ComplexValueExpression( m_bpeProcess
          .getType( workflowProcess.getId() + "-subflow-request" ) );

      initialReceive.setInputExpression( inputExpression );

      for ( i = 0; i < parameters.length; i++ ) {
        if ( parameters[i].getMode() != ParameterMode.OUT ) {
          VariableExpression expression = new VariableExpression( parameters[i].getId() );

          inputExpression.addExpression( parameters[i].getId(), expression );
        }
      }
    }

    processSequence.addActivity( initialReceive );

    Flow processFlow = new Flow( workflowProcess.getName(), m_bpeProcess );

    processFlow
        .setNonBlocked( workflowProcess.getPackage().getGraphConformance() != GraphConformance.FULL_BLOCKED );
    processSequence.addActivity( processFlow );

    Activity activities[] = workflowProcess.getActivities();
    ActivityBuilder activityBuilder = new ActivityBuilder( workflowProcess, m_bpeProcess,
        processFlow );

    for ( i = 0; i < activities.length; i++ ) {
      activityBuilder.build( activities[i] );
    }

    Transition transitions[] = workflowProcess.getTransitions();

    for ( i = 0; i < transitions.length; i++ ) {
      activityBuilder.build( transitions[i] );
    }

    Invoke invoke = new Invoke( workflowProcess.getName(), m_bpeProcess, workflowProcess.getId()
        + "-finish" );

    invoke.setConnector( new SubProcessFinishConnector() );
    invoke
        .setOutMessageType( m_bpeProcess.getType( workflowProcess.getId() + "-subflow-response" ) );
    invoke.addCorrelation( parentCorrelationSet, false, CorrelationPattern.OUT );

    if ( workflowProcess.getFormalParameters() != null ) {
      FormalParameter[] parameters = workflowProcess.getFormalParameters();

      ComplexValueExpression outputExpression = new ComplexValueExpression( m_bpeProcess
          .getType( workflowProcess.getId() + "-subflow-response" ) );

      invoke.setOutputExpression( outputExpression );

      for ( i = 0; i < parameters.length; i++ ) {
        if ( parameters[i].getMode() != ParameterMode.IN ) {
          VariableExpression expression = new VariableExpression( parameters[i].getId() );

          outputExpression.addExpression( parameters[i].getId(), expression );
        }
      }
    }

    processSequence.addActivity( invoke );

    if ( workflowProcess.getExtendedAttribute( "canyon:OnException" ) != null ) {
			ExtendedAttributeHelper eah = new ExtendedAttributeHelper(workflowProcess.getExtendedAttributes());
			
    	
      String exceptionProcessId = eah.getMandatoryValue( "canyon:OnException" );
			String faultContextId = eah.getOptionalValue("canyon:OnException:faultContextId");
      FaultHandler faultHandler = new FaultHandler();
      Invoke exceptionInvoke = activityBuilder.buildFaultFlowInvoker(exceptionProcessId, faultContextId, faultHandler, m_bpeProcess);
      faultHandler.setActivity( exceptionInvoke );

      m_bpeProcess.addFaultHandler( faultHandler );
    }
  }

  private void buildMessageTypes( WorkflowProcess workflowProcess )
  {
    /*
     * ComplexType applicationData = new ComplexType("application-data");
     * 
     * applicationData.addProperty("id", null, null, String.class);
     * applicationData.addProperty("name", null, null, String.class);
     * applicationData.addProperty("description", null, null, String.class);
     * applicationData.addProperty("extendedAttributes", null, null, Map.class);
     * 
     * ComplexType worklistRequest = new ComplexType("worklist-request");
     * 
     * worklistRequest.addProperty("activityId", null, null, String.class);
     * worklistRequest.addProperty("processId", null, null, String.class);
     * worklistRequest.addProperty("name", null, null, String.class);
     * worklistRequest.addProperty("priority", null, null, Integer.class);
     * worklistRequest.addProperty("dueDate", null, null, Date.class);
     * worklistRequest.addProperty("processStarter", null, null, String.class);
     * worklistRequest.addProperty("performers", null,
     * null,Participant[].class); worklistRequest.addProperty("applicationData",
     * null, null,applicationData);
     * 
     * ComplexType worklistResponse = new ComplexType("worklist-reponse");
     * 
     * worklistResponse.addProperty("activityId", null, null,String.class);
     * worklistResponse.addProperty("processId", null, null,String.class);
     * worklistRequest.addProperty("applicationData", null,
     * null,applicationData);
     * 
     * m_bpeProcess.registerType(applicationData);
     * m_bpeProcess.registerType(worklistRequest);
     * m_bpeProcess.registerType(worklistResponse);
     */

    ComplexType subflowRequest = new ComplexType( "subflow-request" );

    subflowRequest.addProperty( "parentActivityId", null, null, String.class );
    subflowRequest.addProperty( "parentProcessId", null, null, String.class );
    subflowRequest.addProperty( "parentProcessIdPath", null, null, String.class );

    ComplexType subflowResponse = new ComplexType( "subflow-response" );

    subflowResponse.addProperty( "parentActivityId", null, null, String.class );
    subflowResponse.addProperty( "parentProcessId", null, null, String.class );
    subflowResponse.addProperty( "parentProcessIdPath", null, null, String.class );

    m_bpeProcess.registerType( subflowRequest );
    m_bpeProcess.registerType( subflowResponse );

    ComplexType processRequest = new ComplexType( workflowProcess.getId() + "-subflow-request",
        subflowRequest );
    ComplexType processResponse = new ComplexType( workflowProcess.getId() + "-subflow-response",
        subflowResponse );

    if ( workflowProcess.getFormalParameters() != null ) {
      FormalParameter[] parameters = workflowProcess.getFormalParameters();
      int i;

      for ( i = 0; i < parameters.length; i++ ) {
        if ( parameters[i].getMode() != ParameterMode.OUT ) {
          processRequest.addProperty( parameters[i].getId(), parameters[i].getName(), parameters[i]
              .getDescription(), parameters[i].getDataType().getValueClass() );
        }
        if ( parameters[i].getMode() != ParameterMode.IN ) {
          processResponse.addProperty( parameters[i].getId(), parameters[i].getName(),
              parameters[i].getDescription(), parameters[i].getDataType().getValueClass() );
        }
      }
    }

    m_bpeProcess.registerType( processRequest );
    m_bpeProcess.registerType( processResponse );

    /*
     * Application[] applications =
     * workflowProcess.getPackage().getApplications(); int i;
     * 
     * for ( i = 0; i < applications.length; i++ ) { buildMessageType (
     * workflowProcess, applications[i]); }
     * 
     * applications = workflowProcess.getApplications();
     * 
     * for ( i = 0; i < applications.length; i++ ) { buildMessageType (
     * workflowProcess, applications[i]); }
     */
  }

  private void buildMessageType( WorkflowProcess workflowProcess, Application application )
  {
    ComplexType inData = new ComplexType( application.getId() + "-data-in" );

    FormalParameter parameters[] = application.getFormalParameters();
    int i;

    for ( i = 0; i < parameters.length; i++ ) {
      if ( parameters[i].getMode() != ParameterMode.OUT ) {
        inData.addProperty( parameters[i].getId(), parameters[i].getName(), parameters[i]
            .getDescription(), parameters[i].getDataType().getValueClass() );
      }
    }

    ComplexType outData = new ComplexType( application.getId() + "-data-out" );

    for ( i = 0; i < parameters.length; i++ ) {
      if ( parameters[i].getMode() != ParameterMode.IN ) {
        outData.addProperty( parameters[i].getId(), parameters[i].getName(), parameters[i]
            .getDescription(), parameters[i].getDataType().getValueClass() );
      }
    }

    ComplexType applicationData = new ComplexType( application.getId() + "-data", m_bpeProcess
        .getType( "application-data" ) );

    applicationData.addProperty( "id", null, null, String.class, application.getId() );
    applicationData.addProperty( "name", null, null, String.class, application.getName() );
    applicationData.addProperty( "description", null, null, String.class, application
        .getDescription() );
    applicationData.addProperty( "in", null, null, inData );
    applicationData.addProperty( "out", null, null, outData );

    ExtendedAttribute extendedAttributes[] = application.getExtendedAttributes();

    if ( extendedAttributes.length > 0 ) {
      Map extendedAttributesMap = new HashMap();
      for ( i = 0; i < extendedAttributes.length; i++ ) {
        extendedAttributesMap.put( extendedAttributes[i].getName(), extendedAttributes[i]
            .getValue() );
      }
      applicationData.addProperty( "extendedAttributes", null, null, Map.class,
          extendedAttributesMap );
    }

    m_bpeProcess.registerType( inData );
    m_bpeProcess.registerType( outData );
    m_bpeProcess.registerType( applicationData );
  }
}
