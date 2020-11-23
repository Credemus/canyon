package de.objectcode.canyon.engine.util;

import java.util.Date;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wfmc.wapi.WMWorkflowException;

import de.objectcode.canyon.InvalidProcessException;
import de.objectcode.canyon.model.Script;
import de.objectcode.canyon.model.WorkflowPackage;
import de.objectcode.canyon.model.activity.Activity;
import de.objectcode.canyon.model.data.ActualParameter;
import de.objectcode.canyon.model.data.FormalParameter;
import de.objectcode.canyon.model.data.ParameterMode;
import de.objectcode.canyon.model.process.Duration;
import de.objectcode.canyon.model.process.DurationUnit;
import de.objectcode.canyon.model.process.ProcessHeader;
import de.objectcode.canyon.model.process.WorkflowProcess;
import de.objectcode.canyon.spi.RepositoryException;
import de.objectcode.canyon.spi.ServiceManager;
import de.objectcode.canyon.spi.evaluator.EvaluatorContext;
import de.objectcode.canyon.spi.evaluator.EvaluatorException;
import de.objectcode.canyon.spi.evaluator.IEvaluator;
import de.objectcode.canyon.spi.instance.AttributeReadOnlyException;
import de.objectcode.canyon.spi.instance.IActivityInstance;
import de.objectcode.canyon.spi.instance.IAttributeInstance;
import de.objectcode.canyon.spi.instance.IProcessInstance;
import de.objectcode.canyon.spi.tool.Parameter;
import de.objectcode.canyon.spi.tool.ReturnValue;

/**
 * @author    junglas
 * @created   21. Oktober 2003
 */
public class WorkflowUtils
{
  private final static  Log  log  = LogFactory.getLog( WorkflowUtils.class );


  /**
   * Gets the evaluator attribute of the WorkflowUtils class
   *
   * @param pkg                      Description of the Parameter
   * @param svcMgr                   Description of the Parameter
   * @return                         The evaluator value
   * @exception RepositoryException  Description of the Exception
   */
  public static IEvaluator getEvaluator( WorkflowPackage pkg, ServiceManager svcMgr )
    throws RepositoryException
  {

    // Determine script type; default is text/xpath.
    Script  script       = pkg.getScript();
    String  contentType  = script == null
        ? System.getProperty( "org.obe.script", "text/xpath" )
        : script.getType();
    return svcMgr.getEvaluatorRepository().findEvaluator( contentType );
  }


  /**
   * Description of the Method
   *
   * @param returnValues                    Description of the Parameter
   * @param processInstance                 Description of the Parameter
   * @exception RepositoryException         Description of the Exception
   * @exception AttributeReadOnlyException  Description of the Exception
   */
  public static void applyResults( ReturnValue[] returnValues,
      IProcessInstance processInstance )
    throws RepositoryException,
      AttributeReadOnlyException
  {

    // Copy the return values into the workflow data.
    for ( int i = 0; i < returnValues.length; i++ ) {
      ReturnValue  returnValue  = returnValues[i];

      if ( log.isDebugEnabled() ) {
        log.debug( "Putting " + returnValue.actualName + " = " +
            returnValue.value + " into workflow data" );
      }

      processInstance.getAttributeInstance( returnValue.actualName )
          .setValue( -1, returnValue.value );
    }
  }


  /**
   * Maps activity state to process state.  When an activity instance with a
   * SubFlow implementation transitions to the <code>newState</code> state,
   * any child process belonging to that activity instance for which it would
   * be a legal transition, should transition to the state returned by this
   * method.
   *
   * @param newState  The new process instance state.
   * @return          The required activity instance state.
   */
  public static int activityStateToProcessState( int newState )
  {
    // The states actually have the same ordinals.
    return newState;
  }


  /**
   * Maps activity state to work item state.  When an activity instance
   * transitions to the <code>newState</code> state, work items belonging
   * to that activity instance and for which it would be a legal transition,
   * should transition to the state returned by this method.
   *
   * @param newState  The new activity state.
   * @return          The required work item state.
   */
  public static int activityStateToWorkItemState( int newState )
  {
    // The states actually have the same ordinals.
    return newState;
  }

  /**
   * Maps process state to activity state.  When a process instance
   * transitions to the <code>newState</code> state, activities belonging
   * to that process instance and for which it would be a legal transition,
   * should transition to the state returned by this method.
   *
   * @param newState The new process instance state.
   * @return The required activity instance state.
   */
  public static int processStateToActivityState(int newState) {
      // The states actually have the same ordinals.
      return newState;
  }


  /**
   * Description of the Method
   *
   * @param formalParms              Description of the Parameter
   * @param actualParms              Description of the Parameter
   * @param svcMgr                   Description of the Parameter
   * @param workflow                 Description of the Parameter
   * @param processInstance          Description of the Parameter
   * @param activity                 Description of the Parameter
   * @param activityInstance         Description of the Parameter
   * @return                         Description of the Return Value
   * @exception WMWorkflowException  Description of the Exception
   */
  public static Parameter[] createParameters( FormalParameter formalParms[],
      ActualParameter actualParms[], ServiceManager svcMgr,
      WorkflowProcess workflow, IProcessInstance processInstance,
      Activity activity, IActivityInstance activityInstance )
    throws WMWorkflowException
  {

    Parameter[]  parms  = new Parameter[formalParms.length];
    try {
      Map         attrs      = processInstance.getAttributeInstances();
      IEvaluator  evaluator  = null;
      for ( int i = 0, n = formalParms.length; i < n; i++ ) {
        FormalParameter  fp          =  formalParms[i];
        ActualParameter  ap          =  actualParms[i];
        String           text        = ap.getText();
        String           actualName  = text;
        Object           value;
        ParameterMode    mode        = fp.getMode();
        if ( mode == ParameterMode.IN ) {
          // IN parameter: text is an expression to evaluate.
          if ( evaluator == null ) {
            evaluator = getEvaluator( workflow.getPackage(), svcMgr );
          }
          actualName = null;
          value = evaluator.evaluateExpression( text, new EvaluatorContext( workflow, processInstance, activity, activityInstance ) );
        } else if ( mode == ParameterMode.INOUT ) {
          // INOUT parameter: text is the name of the attr to pass.
          value = ( ( IAttributeInstance ) attrs.get( text ) ).getValue();
        } else {
          value = null;
        }
        parms[i] = new Parameter( fp.getId(), actualName,
            fp.getDataType(), mode, fp.getDescription(), value );
      }
    }
    catch ( EvaluatorException e ) {
      throw new WMWorkflowException( e );
    }
    catch ( RepositoryException e ) {
      throw new WMWorkflowException( e );
    }
    return parms;
  }


  /**
   * Validate that the WorkflowProcess is within valid limits for execution.
   *
   * @param wp                        The WorkflowProcess
   * @throws InvalidProcessException
   */
  public static void validate( WorkflowProcess wp )
    throws InvalidProcessException
  {

    ProcessHeader  processHeader  = wp.getProcessHeader();
    DurationUnit   defaultUnit    = processHeader.getDurationUnit();

    // Current behaviour is to inherit the package's creation date
    // if there is no creation date specified in the process
    // header.  Is this the correct behavior? -AE
    Date           created        = processHeader.getCreated();
    if ( created == null ) {
      WorkflowPackage  pkg  = wp.getPackage();
      created = pkg.getPackageHeader().getCreated();
    }
    if ( log.isDebugEnabled() ) {
      log.debug( "Created date: " + created );
    }

    Duration       validFrom      = processHeader.getValidFrom();
    if ( validFrom != null ) {
      long  validFromMs  = validFrom.getMillis( defaultUnit );
      if ( validFromMs != 0 ) {
        Date  validFromDate  = new Date( System.currentTimeMillis() +
            validFromMs );
        if ( log.isDebugEnabled() ) {
          log.debug( "Valid from " + validFromDate + " [" +
              validFromMs + ']' );
        }
      }
      if ( validFromMs != 0 && created != null &&
          created.getTime() + validFromMs > System.currentTimeMillis() ) {
        throw new InvalidProcessException(
            "Workflow is not yet valid." );
      }
    }

    Duration       validTo        = processHeader.getValidTo();
    if ( validTo != null ) {
      long  validToMs  = validTo.getMillis( defaultUnit );
      if ( validToMs != 0 && created != null ) {
        Date  validToDate  = new Date( created.getTime() + validToMs );
        if ( log.isDebugEnabled() ) {
          log.debug( "Valid to " + validToDate + " [" +
              validToMs + "]" );
        }
      }
      if ( validToMs != 0 && created != null &&
          System.currentTimeMillis() > created.getTime() + validToMs ) {
        throw new InvalidProcessException(
            "Workflow is no longer valid." );
      }
    }
  }
}
