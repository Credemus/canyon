package de.objectcode.canyon.bpe.engine.activities;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Element;

import de.objectcode.canyon.bpe.engine.EngineException;
import de.objectcode.canyon.bpe.engine.correlation.Correlation;
import de.objectcode.canyon.bpe.engine.correlation.CorrelationSet;
import de.objectcode.canyon.bpe.engine.handler.Fault;
import de.objectcode.canyon.bpe.engine.handler.FaultHandler;
import de.objectcode.canyon.bpe.engine.handler.OnAlarmHandler;
import de.objectcode.canyon.bpe.engine.variable.IVariable;
import de.objectcode.canyon.bpe.util.HydrationContext;
import de.objectcode.canyon.bpe.util.IStateHolder;

/**
 * @author junglas
 * @created 7. Juni 2004
 */
public class Scope extends Activity implements IActivityContainer
{
  final static long    serialVersionUID = -988697343552498364L;

  protected Activity   m_activity;
  protected Map        m_variables;
  protected BPEProcess m_process;
  protected Map        m_correlations;
  protected Map        m_correlationSets;
  protected Map        m_faultHandlers;
  protected List       m_alarmHandlers;

  /**
   * Constructor for the Scope object
   * 
   * @param name
   *          Description of the Parameter
   * @param process
   *          Description of the Parameter
   */
  public Scope( String name, BPEProcess process )
  {
    super( name, null );

    m_scope = this;
    m_process = process;

    m_correlations = new LinkedHashMap();
    m_correlationSets = new LinkedHashMap();
    m_variables = new LinkedHashMap();
    m_faultHandlers = new LinkedHashMap();
    m_alarmHandlers = new ArrayList();

    if ( process != null ) {
      process.register( this );
    }
  }

  /**
   * Sets the activity attribute of the Scope object
   * 
   * @param activity
   *          The new activity value
   */
  public void setActivity( Activity activity )
  {
    m_activity = activity;
    m_activity.setParentActivity( this );
    m_activity.setScope(this);
  }

  /**
   * Gets the variables attribute of the Scope object
   * 
   * @return The variables value
   */
  public IVariable[] getVariables()
  {
    if ( getParentActivity() == null ) {
      IVariable[] variables = new IVariable[m_variables.size()];
      m_variables.values().toArray( variables );

      return variables;
    } else {
      Map allVariables = new HashMap( m_variables );
      IVariable[] fatherVariables = ((Activity) getParentActivity()).getVariables();
      for ( int i = 0; i < fatherVariables.length; i++ ) {
        if ( allVariables.get( fatherVariables[i].getName() ) == null )
          allVariables.put( fatherVariables[i].getName(), fatherVariables[i] );
      }
      IVariable[] variables = new IVariable[allVariables.size()];
      allVariables.values().toArray( variables );
      return variables;
    }
  }

  /**
   * Gets the variable attribute of the Scope object
   * 
   * @param name
   *          Description of the Parameter
   * @return The variable value
   */
  public IVariable getVariable( String name )
  {
    IVariable result = (IVariable) m_variables.get( name );
    if ( result == null && getParentActivity() != null )
      result = getParentActivity().getVariable( name );
    return result;
  }

  /**
   * Gets the correlation attribute of the Scope object
   * 
   * @param name
   *          Description of the Parameter
   * @return The correlation value
   */
  protected Correlation getCorrelation( String name )
  {
  	return (Correlation) m_correlations.get( name ); 
  }

  /**
   * Gets the elementName attribute of the Scope object
   * 
   * @return The elementName value
   */
  public String getElementName()
  {
    return "scope";
  }

  /**
   * @return Returns the process.
   */
  public BPEProcess getProcess()
  {
    return m_process;
  }

  /**
   * @return The nonBlocked value
   * @see de.objectcode.canyon.bpe.engine.activities.IActivityContainer#isNonBlocked()
   */
  public boolean isNonBlocked()
  {
    return false;
  }

  /**
   * Sets the correlation attribute of the Scope object
   * 
   * @param correlation
   *          The new correlation value
   */
  protected void addCorrelation( Correlation correlation )
  {
    m_correlations.put( correlation.getName(), correlation );
  }

  /**
   * Adds a feature to the Variable attribute of the Scope object
   * 
   * @param variable
   *          The feature to be added to the Variable attribute
   */
  public void addVariable( IVariable variable )
  {
    m_variables.put( variable.getName(), variable );
  }

  /**
   * Adds a feature to the FaultHandler attribute of the Scope object
   * 
   * @param faultHandler
   *          The feature to be added to the FaultHandler attribute
   */
  public void addFaultHandler( FaultHandler faultHandler )
  {
    faultHandler.setScope( this );
    m_faultHandlers.put( faultHandler.getFaultName(), faultHandler );
  }

  /**
   * Adds a feature to the EventHandler attribute of the Scope object
   * 
   * @param alarmHandler
   *          The feature to be added to the EventHandler attribute
   */
  public void addEventHandler( OnAlarmHandler alarmHandler )
  {
    alarmHandler.setScope( this );
    m_alarmHandlers.add( alarmHandler );
    m_process.addAlarmReceiver( alarmHandler );
  }

  /**
   * Adds a feature to the CorrelationSet attribute of the Scope object
   * 
   * @param correlationSet
   *          The feature to be added to the CorrelationSet attribute
   */
  void addCorrelationSet( CorrelationSet correlationSet )
  {
    m_correlationSets.put( correlationSet.getName(), correlationSet );
  }

  /**
   * @exception EngineException
   *              Description of the Exception
   * @see de.objectcode.canyon.bpe.engine.activities.Activity#start()
   */
  public void start()
      throws EngineException
  {
    super.start();

    m_activity.activate();

    Iterator it = m_alarmHandlers.iterator();

    while (it.hasNext()) {
      OnAlarmHandler handler = (OnAlarmHandler) it.next();

      handler.activate();
    }
  }

  public void complete ( ) throws EngineException
  {
    // Composite activities might be completed if still open
    // This should be refactored
    if ( m_state == ActivityState.OPEN )
      m_state = ActivityState.RUNNING;
    
    super.complete();
  }
  
  public void terminate ( ) throws EngineException {
  	deactivateHandlers();
  	super.terminate();
	}

  public void skip()
      throws EngineException
  {    
    super.skip();
    deactivateHandlers();
  }

  public void abort() throws EngineException {
	  // TODO refactor
	  // Needed for basic.test.LoopTest
	  if (m_state==ActivityState.RUNNING)
		  super.abort();
	  if (m_activity.getState()==ActivityState.RUNNING)
		  m_activity.abort();
  }

public void reopen()
      throws EngineException
  {
    super.reopen();

    m_activity.reopen();
//    Iterator it = m_alarmHandlers.iterator();
//
//    while (it.hasNext()) {
//      OnAlarmHandler handler = (OnAlarmHandler) it.next();
//
//      handler.activate();
//    }
    Iterator it = m_faultHandlers.values().iterator();

    while (it.hasNext()) {
      FaultHandler handler = (FaultHandler) it.next();

      handler.activate();
    }
  }

  public void deactivate()
      throws EngineException
  {
    m_activity.deactivate();

  }

  /**
   * Description of the Method
   * 
   * @param activity
   *          Description of the Parameter
   */
  protected void register( Activity activity )
  {
    m_process.register( activity );
  }

  /**
   * Description of the Method
   * 
   * @param faultName
   *          Description of the Parameter
   */
  public void throwFault( Fault fault )
      throws EngineException
  {
  	String faultName = fault.getName();
    FaultHandler faultHandler = (FaultHandler) m_faultHandlers.get( faultName );

    if ( faultHandler == null )
      faultHandler = (FaultHandler) m_faultHandlers.get( null );

    if ( faultHandler != null )
      faultHandler.fire(fault);
    else
      m_scope.throwFault( fault );
  }

  private void deactivateHandlers()  throws EngineException {
    Iterator it = m_alarmHandlers.iterator();

    while (it.hasNext()) {
      OnAlarmHandler handler = (OnAlarmHandler) it.next();

      handler.deactivate();
    }

    it = m_faultHandlers.values().iterator();

    while (it.hasNext()) {
      FaultHandler handler = (FaultHandler) it.next();

      handler.deactivate();
    }  	
  }
  
  private boolean checkForActiveHandlers()  throws EngineException {
  	deactivateHandlers();
    // Deactivating all handlers does not mean that all activities in handlers are completed    
    Iterator it = m_alarmHandlers.iterator();
    
    while (it.hasNext()) {
      OnAlarmHandler handler = (OnAlarmHandler) it.next();

      if ( handler.getActivity().getState() != ActivityState.COMPLETED &&
          handler.getActivity().getState() != ActivityState.DEACTIVATED &&
          handler.getActivity().getState() != ActivityState.ABORT &&
          handler.getActivity().getState() != ActivityState.SKIPED ) {
        // Don't abort in this case
        return true;
      }
    }

    it = m_faultHandlers.values().iterator();

    while (it.hasNext()) {
      FaultHandler handler = (FaultHandler) it.next();

      if ( handler.getActivity().getState() != ActivityState.COMPLETED &&
          handler.getActivity().getState() != ActivityState.ABORT &&
          handler.getActivity().getState() != ActivityState.DEACTIVATED &&
          handler.getActivity().getState() != ActivityState.SKIPED ) {
        // Don't abort in this case
        return true;
      }
    }
    return false;
  }
  
  /**
   * @param childActivity
   *          Description of the Parameter
   * @exception EngineException
   *              Description of the Exception
   * @see de.objectcode.canyon.bpe.engine.activities.IActivityContainer#childAborted(de.objectcode.canyon.bpe.engine.activities.Activity)
   */
  public void childAborted( Activity childActivity )
      throws EngineException
  {
    if (!checkForActiveHandlers()) 
     abort();
  }

  /**
   * @param childActivity
   *          Description of the Parameter
   * @exception EngineException
   *              Description of the Exception
   * @see de.objectcode.canyon.bpe.engine.activities.IActivityContainer#childCompleted(de.objectcode.canyon.bpe.engine.activities.Activity)
   */
  public void childCompleted( Activity childActivity )
      throws EngineException
  {
    if (!checkForActiveHandlers()) 
      complete();
  }

  /**
   * Description of the Method
   * 
   * @param childActivity
   *          Description of the Parameter
   * @exception EngineException
   *              Description of the Exception
   */
  public void childSkiped( Activity childActivity )
      throws EngineException
  {
      // TODO I hate this stuff
    	if ( getState() == ActivityState.OPEN )
    	  super.deactivate();
      skip();    
  }

  /**
   * Description of the Method
   * 
   * @param element
   *          Description of the Parameter
   */
  public void toDom( Element element )
  {
    super.toDom( element );

    if ( !m_variables.isEmpty() ) {
      Element variables = element.addElement( "variables" );
      Iterator it = m_variables.values().iterator();

      while (it.hasNext()) {
        IVariable variable = (IVariable) it.next();

        variable.toDom( variables.addElement( variable.getElementName() ) );
      }
    }

    if ( !m_correlations.isEmpty() ) {
      Element correlations = element.addElement( "correlations" );
      Iterator it = m_correlations.values().iterator();

      while (it.hasNext()) {
        Correlation correlation = (Correlation) it.next();

        correlation.toDom( correlations.addElement( correlation.getElementName() ) );
      }
    }

    if ( !m_faultHandlers.isEmpty() ) {
      Element faultHandlers = element.addElement( "faultHandlers" );
      Iterator it = m_faultHandlers.values().iterator();

      while (it.hasNext()) {
        FaultHandler faultHandler = (FaultHandler) it.next();

        faultHandler.toDom( faultHandlers.addElement( faultHandler.getElementName() ) );
      }
    }

    if ( !m_alarmHandlers.isEmpty() ) {
      Element eventHandlers = element.addElement( "eventHandlers" );
      Iterator it = m_alarmHandlers.iterator();

      while (it.hasNext()) {
        OnAlarmHandler alarmHandler = (OnAlarmHandler) it.next();

        alarmHandler.toDom( eventHandlers.addElement( alarmHandler.getElementName() ) );
      }
    }

    m_activity.toDom( element.addElement( m_activity.getElementName() ) );
  }

  /**
   * @param in
   *          Description of the Parameter
   * @exception IOException
   *              Description of the Exception
   * @see de.objectcode.canyon.bpe.util.IStateHolder#hydrate(java.io.ObjectInput)
   */
  public void hydrate( HydrationContext context, ObjectInput in )
      throws IOException
  {
    super.hydrate( context, in );

    Iterator it = m_variables.values().iterator();

    while (it.hasNext()) {
      IVariable variable = (IVariable) it.next();

      variable.hydrate( context, in );
    }

    it = m_alarmHandlers.iterator();

    while (it.hasNext()) {
      IStateHolder handler = (IStateHolder) it.next();

      handler.hydrate( context, in );
    }

    it = m_faultHandlers.values().iterator();

    while (it.hasNext()) {
      IStateHolder handler = (IStateHolder) it.next();

      handler.hydrate( context, in );
    }

    
    if (context.getSchema() == HydrationContext.CLASSIC_SCHEMA) {
			int i;
			int correlationsSize = in.readInt();

			m_correlations = new HashMap();

			for (i = 0; i < correlationsSize; i++) {
				String name = in.readUTF();
				CorrelationSet correlationSet = (CorrelationSet) m_correlationSets
						.get(name);

				if (correlationSet == null) {
					throw new IOException("CorrelationSet not found: " + name);
				}

				Correlation correlation = new Correlation(correlationSet);

				correlation.hydrate( context, in);
				m_correlations.put(name, correlation);
			}
		} else {
			// TODO Order???
			m_correlations = new HashMap();
			it = m_correlationSets.keySet().iterator();
			while (it.hasNext()) {
				String name = (String) it.next();
				CorrelationSet correlationSet = (CorrelationSet) m_correlationSets
				.get(name);
				Correlation correlation = new Correlation(correlationSet);

				correlation.hydrate(context, in);
				if (!correlation.isEmpty())
					m_correlations.put(name, correlation);
			}
		}

    m_activity.hydrate( context, in );
  }

  /**
   * @param out
   *          Description of the Parameter
   * @exception IOException
   *              Description of the Exception
   * @see de.objectcode.canyon.bpe.util.IStateHolder#dehydrate(java.io.ObjectOutput)
   */
  public void dehydrate( HydrationContext context, ObjectOutput out )
      throws IOException
  {
    super.dehydrate( context, out );

    Iterator it = m_variables.values().iterator();

    while (it.hasNext()) {
      IVariable variable = (IVariable) it.next();

      variable.dehydrate( context, out );
    }

    it = m_alarmHandlers.iterator();

    while (it.hasNext()) {
      IStateHolder handler = (IStateHolder) it.next();

      handler.dehydrate( context, out );
    }

    it = m_faultHandlers.values().iterator();

    while (it.hasNext()) {
      IStateHolder handler = (IStateHolder) it.next();

      handler.dehydrate( context, out );
    }

    if (context.getSchema() == HydrationContext.CLASSIC_SCHEMA) {
			out.writeInt(m_correlations.size());
			it = m_correlations.values().iterator();

			while (it.hasNext()) {
				Correlation correlation = (Correlation) it.next();

				out.writeUTF(correlation.getName());
				correlation.dehydrate(context, out);
			}
		} else {
			it = m_correlationSets.keySet().iterator();

			while (it.hasNext()) {
				String name = (String) it.next();
				Correlation correlation = (Correlation) m_correlations.get(name);
				if (correlation==null)
					correlation = new Correlation((CorrelationSet) m_correlationSets.get(name));
				correlation.dehydrate(context, out);
			}
		}
    m_activity.dehydrate( context, out );
  }

  public void handlerFired()
      throws EngineException
  {
    m_activity.abort();
  }

	public List getAlarmHandlers() {
		return m_alarmHandlers;
	}

  
}
