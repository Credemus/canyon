package de.objectcode.canyon.model.activity;

import de.objectcode.canyon.model.BaseElement;
import de.objectcode.canyon.model.ExtendedAttribute;
import de.objectcode.canyon.model.IValidatable;
import de.objectcode.canyon.model.ValidationErrors;
import de.objectcode.canyon.model.participant.Participant;
import de.objectcode.canyon.model.process.Duration;
import de.objectcode.canyon.model.process.DurationUnit;
import de.objectcode.canyon.model.process.WorkflowProcess;
import de.objectcode.canyon.model.transition.Transition;
import de.objectcode.canyon.model.transition.TransitionRestriction;
import de.objectcode.canyon.spi.ObjectNotFoundException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * @author    junglas
 * @created   21. November 2003
 */
public class Activity extends BaseElement implements IValidatable
{
	static final long serialVersionUID = -4414266827057740202L;
	
  private final static  String                 OBE_COMPLETION_STRATEGY   = "obe:CompletionStrategy";
  private final static  String                 OBE_ASSIGN_STRATEGY   = "obe:AssignStrategy";

  private               IActivityContainer     m_container;
  private               String                 m_performer;
  private               Participant[]          m_performerParticipants;
  private               AutomationMode         m_startMode               = AutomationMode.AUTOMATIC;
  private               AutomationMode         m_finishMode              = AutomationMode.AUTOMATIC;
  private               String                 m_icon;
  private               String                 m_documentation;
  private               int                    m_priority                = -1;
  private               Duration               m_limit;
  private               List                   m_deadlines;
  private               Implementation         m_implementation;
  private               BlockActivity          m_blockActivity;
  private               Route                  m_route;
  private               SimulationInformation  m_simulationInformation;
  private               List                   m_transitionRestrictions;
  private               Map                    m_inboundTransitions;
  private               Map                    m_outboundTransitions;
  private               CompletionStrategy     m_completionStrategy      = CompletionStrategy.ANY;
  private               AssignStrategy         m_assignStrategy          = AssignStrategy.LEAST;


  /**
   *Constructor for the Activity object
   */
  public Activity()
  {
    m_deadlines = new ArrayList();
    m_transitionRestrictions = new ArrayList();
    m_inboundTransitions = new HashMap();
    m_outboundTransitions = new HashMap();
  }


  /**
   * @param assignStrategy  The assignStrategy to set.
   */
  public void setAssignStrategy( AssignStrategy assignStrategy )
  {
    m_assignStrategy = assignStrategy;
  }


  /**
   * @param mode
   */
  public void setFinishMode( AutomationMode mode )
  {
    m_finishMode = mode;
  }


  /**
   * @param string
   */
  public void setPerformer( String string )
  {
    m_performer = string;
  }


  /**
   * @param mode
   */
  public void setStartMode( AutomationMode mode )
  {
    m_startMode = mode;
  }


  /**
   * @param string
   */
  public void setDocumentation( String string )
  {
    m_documentation = string;
  }


  /**
   * @param string
   */
  public void setIcon( String string )
  {
    m_icon = string;
  }


  /**
   * @param i
   */
  public void setPriority( int i )
  {
    m_priority = i;
  }


  /**
   * @param container
   */
  public void setContainer( IActivityContainer container )
  {
    m_container = container;
  }


  /**
   * @param duration
   */
  public void setLimit( Duration duration )
  {
    m_limit = duration;
  }


  /**
   * @param activity
   */
  public void setBlockActivity( BlockActivity activity )
  {
    m_blockActivity = activity;
    m_blockActivity.setActivity( this );
  }


  /**
   * @param implementation
   */
  public void setImplementation( Implementation implementation )
  {
    m_implementation = implementation;
    m_implementation.setActivity( this );
  }


  /**
   * @param route
   */
  public void setRoute( Route route )
  {
    m_route = route;
  }


  /**
   * @param information
   */
  public void setSimulationInformation( SimulationInformation information )
  {
    m_simulationInformation = information;
  }


  /**
   * @return   Returns the assignStrategy.
   */
  public AssignStrategy getAssignStrategy()
  {
    return m_assignStrategy;
  }


  /**
   * Gets the workflowProcess attribute of the Activity object
   *
   * @return   The workflowProcess value
   */
  public WorkflowProcess getWorkflowProcess()
  {
    return m_container.getWorkflowProcess();
  }


  /**
   * Gets the startActivity attribute of the Activity object
   *
   * @return   The startActivity value
   */
  public boolean isStartActivity()
  {
    return m_inboundTransitions.isEmpty();
  }


  /**
   * Gets the endActivity attribute of the Activity object
   *
   * @return   The endActivity value
   */
  public boolean isExitActivity()
  {
    return m_outboundTransitions.isEmpty();
  }


  /**
   * Gets the transitionRestrictions attribute of the Activity object
   *
   * @return   The transitionRestrictions value
   */
  public TransitionRestriction[] getTransitionRestrictions()
  {
    TransitionRestriction  ret[]  = new TransitionRestriction[m_transitionRestrictions.size()];

    m_transitionRestrictions.toArray( ret );

    return ret;
  }


  /**
   * @return
   */
  public AutomationMode getFinishMode()
  {
    return m_finishMode;
  }


  /**
   * @return
   */
  public String getPerformer()
  {
    return m_performer;
  }


  /**
   * @return
   */
  public AutomationMode getStartMode()
  {
    return m_startMode;
  }


  /**
   * @return
   */
  public String getDocumentation()
  {
    return m_documentation;
  }


  /**
   * @return
   */
  public String getIcon()
  {
    return m_icon;
  }



  /**
   * Gets the deadlines attribute of the Activity object
   *
   * @return   The deadlines value
   */
  public Deadline[] getDeadlines()
  {
    Deadline  ret[]  = new Deadline[m_deadlines.size()];

    m_deadlines.toArray( ret );

    return ret;
  }


  /**
   * @return
   */
  public int getPriority()
  {
    return m_priority;
  }


  /**
   * @return
   */
  public IActivityContainer getContainer()
  {
    return m_container;
  }


  /**
   * @return
   */
  public Duration getLimit()
  {
    return m_limit;
  }


  /**
   * @return
   */
  public BlockActivity getBlockActivity()
  {
    return m_blockActivity;
  }


  /**
   * @return
   */
  public Implementation getImplementation()
  {
    return m_implementation;
  }


  /**
   * @return
   */
  public Route getRoute()
  {
    return m_route;
  }


  /**
   * @return
   */
  public SimulationInformation getSimulationInformation()
  {
    return m_simulationInformation;
  }


  /**
   * @return
   */
  public CompletionStrategy getCompletionStrategy()
  {
    return m_completionStrategy;
  }


  /**
   * @return
   */
  public Map getInboundTransitions()
  {
    return m_inboundTransitions;
  }


  /**
   * @return
   */
  public Map getOutboundTransitions()
  {
    return m_outboundTransitions;
  }


  /**
   * @return
   */
  public Participant[] getPerformerParticipants()
  {
    return m_performerParticipants;
  }


  /**
   * Adds a feature to the InboundTransition attribute of the Activity object
   *
   * @param transition  The feature to be added to the InboundTransition attribute
   */
  public void addInboundTransition( Transition transition )
  {
    m_inboundTransitions.put( transition.getId(), transition );
  }


  /**
   * Adds a feature to the OutboundTransition attribute of the Activity object
   *
   * @param transition  The feature to be added to the OutboundTransition attribute
   */
  public void addOutboundTransition( Transition transition )
  {
    m_outboundTransitions.put( transition.getId(), transition );
  }


  /**
   * Adds a feature to the TransitionRestriction attribute of the Activity object
   *
   * @param transitionRestriction  The feature to be added to the TransitionRestriction attribute
   */
  public void addTransitionRestriction( TransitionRestriction transitionRestriction )
  {
    m_transitionRestrictions.add( transitionRestriction );
  }


  /**
   * Adds a feature to the Tool attribute of the Activity object
   *
   * @param tool  The feature to be added to the Tool attribute
   */
  public void addTool( Tool tool )
  {
    if ( m_implementation == null ) {
      m_implementation = new ToolSet();
      m_implementation.setActivity( this );
    }
    ( ( ToolSet ) m_implementation ).addTool( tool );
    tool.setActivity( this );
  }


  /**
   * Adds a feature to the Deadline attribute of the Activity object
   *
   * @param deadline  The feature to be added to the Deadline attribute
   */
  public void addDeadline( Deadline deadline )
  {
    m_deadlines.add( deadline );
    deadline.setActivity( this );
  }


  /**
   * Description of the Method
   *
   * @return   Description of the Return Value
   */
  public ValidationErrors validate()
  {
    ValidationErrors  errors  = new ValidationErrors();

    if ( m_extendedAttributes.containsKey( OBE_COMPLETION_STRATEGY ) ) {
      ExtendedAttribute  attr  = ( ExtendedAttribute ) m_extendedAttributes.get( OBE_COMPLETION_STRATEGY );

      m_completionStrategy = CompletionStrategy.fromString( attr.getValue().toUpperCase() );

      m_extendedAttributes.remove( OBE_COMPLETION_STRATEGY );
    }

    if ( m_extendedAttributes.containsKey( OBE_ASSIGN_STRATEGY ) ) {
      ExtendedAttribute  attr  = ( ExtendedAttribute ) m_extendedAttributes.get( OBE_ASSIGN_STRATEGY );

      m_assignStrategy = AssignStrategy.fromString( attr.getValue().toUpperCase() );

      m_extendedAttributes.remove( OBE_ASSIGN_STRATEGY );
    }

    if ( m_priority < 0 ) {
      m_priority = m_container.getWorkflowProcess().getProcessHeader().getPriority();
    }

    if ( m_route != null && m_performer != null ) {
      errors.addMessage( "route.noPerformer", new Object[]{m_id} );
    }

    errors.check( m_blockActivity );
    errors.check( m_implementation );
    errors.check( m_deadlines );
    Iterator  it       = m_deadlines.iterator();

    while ( it.hasNext() ) {
      Deadline  deadline  = ( Deadline ) it.next();
      Duration duration = deadline.getDeadlineCondition();
      handleDuration(duration);
    }
    
    handleDuration(m_limit);
    if ( m_deadlines.size() >= 2 ) {
      boolean   hasSync  = false;

      it       = m_deadlines.iterator();

      while ( it.hasNext() ) {
        Deadline  deadline  = ( Deadline ) it.next();

        if ( deadline.getExecutionType() == ExecutionType.SYNCHRONOUS ) {
          if ( hasSync ) {
            errors.addMessage( "activity.deadline.onlyOneSync", new Object[]{m_id} );
          } else {
            hasSync = true;
          }
        }
      }
    }

    if ( m_performer != null ) {
      StringTokenizer  strTok        = new StringTokenizer( m_performer, ", " );
      List             participants  = new ArrayList();

      while ( strTok.hasMoreTokens() ) {
        String  id  = strTok.nextToken();

        try {
          participants.add( m_container.getWorkflowProcess().findParticipant( id ) );
        }
        catch ( ObjectNotFoundException e ) {
          errors.addMessage( "activity.performer.noParticipant", new Object[]{m_id, id} );
        }
      }

      m_performerParticipants = ( Participant[] ) participants.toArray(
          new Participant[participants.size()] );
    }

    return errors;
  }


	private void handleDuration(Duration duration) {
		if (duration != null) {
			if (duration.getUnit() == null) {
				duration.setUnit(m_container.getWorkflowProcess().getProcessHeader()
						.getDurationUnit());
			}
			if (duration.getUnit() == null) {
				duration.setUnit(DurationUnit.DAY);
			}
		}
	}

}
