package de.objectcode.canyon.model.process;

import de.objectcode.canyon.model.BaseElement;
import de.objectcode.canyon.model.IValidatable;
import de.objectcode.canyon.model.RedefinableHeader;
import de.objectcode.canyon.model.ValidationErrors;
import de.objectcode.canyon.model.WorkflowPackage;
import de.objectcode.canyon.model.activity.Activity;
import de.objectcode.canyon.model.activity.ActivitySet;
import de.objectcode.canyon.model.activity.IActivityContainer;
import de.objectcode.canyon.model.application.Application;
import de.objectcode.canyon.model.data.BasicType;
import de.objectcode.canyon.model.data.FormalParameter;
import de.objectcode.canyon.model.data.FormalParameterIndexComparator;
import de.objectcode.canyon.model.data.TypeDeclaration;
import de.objectcode.canyon.model.participant.Participant;
import de.objectcode.canyon.model.transition.Transition;
import de.objectcode.canyon.spi.ObjectNotFoundException;
import de.objectcode.canyon.spi.instance.IProcessInstance;

import java.beans.PropertyDescriptor;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * @author    junglas
 * @created   20. November 2003
 */
public class WorkflowProcess extends BaseElement implements IActivityContainer, IValidatable
{
	static final long serialVersionUID = 1417777586879962644L;
	
  private         ProcessHeader      m_processHeader;
  private         RedefinableHeader  m_redefinableHeader;
  private         Map                m_participants;
  private         Map                m_applications;
  private         Map                m_typeDeclarations;
  private         Map                m_activities;
  private         Map                m_transitions;
  private         Map                m_activitySets;
  private         SortedSet          m_formalParameters;
  private         Map                m_dataFields;
  private         WorkflowPackage    m_package;
  private         int                m_state = 1; // (WMProcessDefinitionState.ENABLED_INT);
  private         AccessLevel        m_accessLevel;

  private static  Map                g_systemDataFields;


  /**
   *Constructor for the WorkflowProcess object
   */
  public WorkflowProcess()
  {
    m_participants = new HashMap();
    m_applications = new HashMap();
    m_activities = new LinkedHashMap();
    m_transitions = new LinkedHashMap();
    m_activitySets = new LinkedHashMap();
    m_typeDeclarations = new HashMap();
    m_formalParameters = new TreeSet( new FormalParameterIndexComparator() );
    m_dataFields = new HashMap();
  }


  /**
   * @param header
   */
  public void setProcessHeader( ProcessHeader header )
  {
    m_processHeader = header;
  }


  /**
   * @param header
   */
  public void setRedefinableHeader( RedefinableHeader header )
  {
    m_redefinableHeader = header;
  }


  /**
   * @param package1
   */
  public void setPackage( WorkflowPackage package1 )
  {
    m_package = package1;
  }


  /**
   * @param i
   */
  public void setState( int i )
  {
    m_state = i;
  }


  /**
   * @param level
   */
  public void setAccessLevel( AccessLevel level )
  {
    m_accessLevel = level;
  }


  /**
   * Gets the workflowProcess attribute of the WorkflowProcess object
   *
   * @return   The workflowProcess value
   */
  public WorkflowProcess getWorkflowProcess()
  {
    return this;
  }


  /**
   * Gets the dataField attribute of the WorkflowProcess object
   *
   * @param id  Description of the Parameter
   * @return    The dataField value
   */
  public DataField getDataField( String id )
  {
    return ( DataField ) m_dataFields.get( id );
  }


  /**
   * Gets the dataFields attribute of the WorkflowProcess object
   *
   * @return   The dataFields value
   */
  public DataField[] getDataFields()
  {
    DataField  ret[]  = new DataField[m_dataFields.size()];

    m_dataFields.values().toArray( ret );

    return ret;
  }


  /**
   * Gets the participant attribute of the WorkflowProcess object
   *
   * @param id  Description of the Parameter
   * @return    The participant value
   */
  public Participant getParticipant( String id )
  {
    return ( Participant ) m_participants.get( id );
  }


  /**
   * Gets the participants attribute of the WorkflowPackage object
   *
   * @return   The participants value
   */
  public Participant[] getParticipants()
  {
    Participant[]  ret  = new Participant[m_participants.size()];

    m_participants.values().toArray( ret );

    return ret;
  }


  /**
   * Gets the activitySet attribute of the WorkflowProcess object
   *
   * @param id  Description of the Parameter
   * @return    The activitySet value
   */
  public ActivitySet getActivitySet( String id )
  {
    return ( ActivitySet ) m_activitySets.get( id );
  }


  /**
   * Gets the activitySets attribute of the WorkflowProcess object
   *
   * @return   The activitySets value
   */
  public ActivitySet[] getActivitySets()
  {
    ActivitySet  ret[]  = new ActivitySet[m_activitySets.size()];

    m_activitySets.values().toArray( ret );

    return ret;
  }


  /**
   * @return
   */
  public ProcessHeader getProcessHeader()
  {
    return m_processHeader;
  }


  /**
   * @return
   */
  public RedefinableHeader getRedefinableHeader()
  {
    return m_redefinableHeader;
  }


  /**
   * Gets the application attribute of the WorkflowProcess object
   *
   * @param id  Description of the Parameter
   * @return    The application value
   */
  public Application getApplication( String id )
  {
    return ( Application ) m_applications.get( id );
  }


  /**
   * Gets the typeDeclaration attribute of the WorkflowProcess object
   *
   * @param id  Description of the Parameter
   * @return    The typeDecaltraion value
   */
  public TypeDeclaration getTypeDeclaration( String id )
  {
    return ( TypeDeclaration ) m_typeDeclarations.get( id );
  }


  /**
   * Gets the applications attribute of the WorkflowProcess object
   *
   * @return   The applications value
   */
  public Application[] getApplications()
  {
    Application[]  ret  = new Application[m_applications.size()];

    m_applications.values().toArray( ret );

    return ret;
  }


  /**
   * Gets the typeDeclarations attribute of the WorkflowProcess object
   *
   * @return   The typeDeclrations value
   */
  public TypeDeclaration[] getTypeDeclarations()
  {
    TypeDeclaration[]  ret  = new TypeDeclaration[m_typeDeclarations.size()];

    m_typeDeclarations.values().toArray( ret );

    return ret;
  }


  /**
   * Gets the activity attribute of the WorkflowProcess object
   *
   * @param id  Description of the Parameter
   * @return    The activity value
   */
  public Activity getActivity( String id )
  {
    if ( m_activities.containsKey( id ) ) {
      return ( Activity ) m_activities.get( id );
    }

    Iterator  it  = m_activitySets.values().iterator();

    while ( it.hasNext() ) {
      ActivitySet  activitySet  = ( ActivitySet ) it.next();

      Activity     activity     = activitySet.getActivity( id );

      if ( activity != null ) {
        return activity;
      }
    }

    return null;
  }


  /**
   * Gets the activities attribute of the WorkflowProcess object
   *
   * @return   The activities value
   */
  public Activity[] getActivities()
  {
    Activity  ret[]  = new Activity[m_activities.size()];

    m_activities.values().toArray( ret );

    return ret;
  }


  /**
   * @return
   */
  public WorkflowPackage getPackage()
  {
    return m_package;
  }



  /**
   * Gets the formalParameters attribute of the Application object
   *
   * @return   The formalParameters value
   */
  public FormalParameter[] getFormalParameters()
  {
    FormalParameter  ret[]  = new FormalParameter[m_formalParameters.size()];

    m_formalParameters.toArray( ret );

    return ret;
  }


  /**
   * Gets the transition attribute of the WorkflowProcess object
   *
   * @param id  Description of the Parameter
   * @return    The transition value
   */
  public Transition getTransition( String id )
  {
    return ( Transition ) m_transitions.get( id );
  }


  /**
   * Gets the transitions attribute of the WorkflowProcess object
   *
   * @return   The transitions value
   */
  public Transition[] getTransitions()
  {
    Transition  ret[]  = new Transition[m_transitions.size()];

    m_transitions.values().toArray( ret );

    return ret;
  }


  /**
   * @return
   */
  public int getState()
  {
    return m_state;
  }


  /**
   * @return
   */
  public AccessLevel getAccessLevel()
  {
    return m_accessLevel;
  }


  /**
   * Description of the Method
   *
   * @return   Description of the Return Value
   */
  public String findWorkflowVersion()
  {
    if ( m_redefinableHeader != null && m_redefinableHeader.getVersion() != null ) {
      return m_redefinableHeader.getVersion();
    }
    return m_package.findPackageVersion();
  }


  /**
   * Description of the Method
   *
   * @param participantId                Description of the Parameter
   * @return                             Description of the Return Value
   * @exception ObjectNotFoundException  Description of the Exception
   */
  public Participant findParticipant( String participantId )
    throws ObjectNotFoundException
  {
    Participant  participant  = ( Participant ) m_participants.get( participantId );

    if ( participant != null ) {
      return participant;
    }

    participant = m_package.getParticipant( participantId );
    if ( participant != null ) {
      return participant;
    }

    throw new ObjectNotFoundException( participantId );
  }


  /**
   * Description of the Method
   *
   * @param activityId                   Description of the Parameter
   * @return                             Description of the Return Value
   * @exception ObjectNotFoundException  Description of the Exception
   */
  public Activity findActivity( String activityId )
    throws ObjectNotFoundException
  {
    Activity  activity  = ( Activity ) m_activities.get( activityId );

    if ( activity != null ) {
      return activity;
    }

    Iterator  it        = m_activitySets.values().iterator();

    while ( it.hasNext() ) {
      ActivitySet  set  = ( ActivitySet ) it.next();

      activity = ( Activity ) set.getActivity( activityId );

      if ( activity != null ) {
        return activity;
      }
    }
    throw new ObjectNotFoundException( activityId );
  }


  /**
   * Adds a feature to the DataField attribute of the WorkflowProcess object
   *
   * @param dataField  The feature to be added to the DataField attribute
   */
  public void addDataField( DataField dataField )
  {
    m_dataFields.put( dataField.getId(), dataField );
  }


  /**
   * Adds a feature to the Participant attribute of the WorkflowPackage object
   *
   * @param participant  The feature to be added to the Participant attribute
   */
  public void addParticipant( Participant participant )
  {
    m_participants.put( participant.getId(), participant );
  }


  /**
   * Adds a feature to the ActivitySet attribute of the WorkflowProcess object
   *
   * @param activitySet  The feature to be added to the ActivitySet attribute
   */
  public void addActivitySet( ActivitySet activitySet )
  {
    m_activitySets.put( activitySet.getId(), activitySet );
    activitySet.setWorkflowProcess( this );
  }


  /**
   * Description of the Method
   *
   * @param id                           Description of the Parameter
   * @return                             Description of the Return Value
   * @exception ObjectNotFoundException  Description of the Exception
   */
  public DataField findDataField( String id )
    throws ObjectNotFoundException
  {
    if ( g_systemDataFields == null ) {
      Map                   fields     = new HashMap();
      PropertyDescriptor[]  propDescs  = IProcessInstance.PROPERTYDESCRIPTORS;
      int                   i;

      for ( i = 0; i < propDescs.length; i++ ) {
        PropertyDescriptor  propDesc  = propDescs[i];
        fields.put( propDesc.getName(), new DataField( propDesc.getName(), null,
            BasicType.fromClass( propDesc.getPropertyType() ) ) );
      }
      g_systemDataFields = fields;
    }

    DataField  dataField  = ( DataField ) g_systemDataFields.get( id );

    if ( dataField != null ) {
      return dataField;
    }

    dataField = ( DataField ) m_dataFields.get( id );

    if ( dataField != null ) {
      return dataField;
    }

    dataField = m_package.getDataField( id );

    if ( dataField != null ) {
      return dataField;
    }
    throw new ObjectNotFoundException( id );
  }


  /**
   * Adds a feature to the Application attribute of the WorkflowProcess object
   *
   * @param application  The feature to be added to the Application attribute
   */
  public void addApplication( Application application )
  {
    m_applications.put( application.getId(), application );
  }


  /**
   * Adds a feature to the TypeDeclarations attribute of the WorkflowProcess object
   *
   * @param typeDeclaration  The feature to be added to the TypeDeclaration attribute
   */
  public void addTypeDeclaration( TypeDeclaration typeDeclaration )
  {
    m_typeDeclarations.put( typeDeclaration.getId(), typeDeclaration );
  }


  /**
   * Adds a feature to the Activity attribute of the WorkflowProcess object
   *
   * @param activity  The feature to be added to the Activity attribute
   */
  public void addActivity( Activity activity )
  {
    m_activities.put( activity.getId(), activity );
    activity.setContainer( this );
  }


  /**
   * Adds a feature to the Transition attribute of the WorkflowProcess object
   *
   * @param transition  The feature to be added to the Transition attribute
   */
  public void addTransition( Transition transition )
  {
    m_transitions.put( transition.getId(), transition );
    transition.setContainer( this );
  }


  /**
   * Adds a feature to the FormalParameter attribute of the Application object
   *
   * @param formalParameter  The feature to be added to the FormalParameter attribute
   */
  public void addFormalParameter( FormalParameter formalParameter )
  {
    m_formalParameters.add( formalParameter );
  }


  /**
   * Description of the Method
   *
   * @return   Description of the Return Value
   */
  public ValidationErrors validate()
  {
    ValidationErrors  errors  = new ValidationErrors();

    errors.check( m_participants.values() );
    errors.check( m_activitySets.values() );
    errors.check( m_transitions.values() );
    errors.check( m_activities.values() );

    return errors;
  }

}
