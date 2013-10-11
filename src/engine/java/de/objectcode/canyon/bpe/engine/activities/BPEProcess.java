package de.objectcode.canyon.bpe.engine.activities;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MultiHashMap;
import org.apache.commons.collections.MultiMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Element;

import de.objectcode.canyon.bpe.engine.BPEEngine;
import de.objectcode.canyon.bpe.engine.EngineException;
import de.objectcode.canyon.bpe.engine.correlation.Correlation;
import de.objectcode.canyon.bpe.engine.correlation.CorrelationSet;
import de.objectcode.canyon.bpe.engine.correlation.IMessageReceiver;
import de.objectcode.canyon.bpe.engine.correlation.Message;
import de.objectcode.canyon.bpe.engine.correlation.MessageType;
import de.objectcode.canyon.bpe.engine.handler.Fault;
import de.objectcode.canyon.bpe.engine.handler.FaultHandler;
import de.objectcode.canyon.bpe.engine.handler.IAlarmReceiver;
import de.objectcode.canyon.bpe.engine.variable.ComplexType;
import de.objectcode.canyon.bpe.util.HydrationContext;
import de.objectcode.canyon.model.process.DurationUnit;

/**
 * @author    junglas
 * @created   15. Juni 2004
 */
public class BPEProcess extends Scope
{
  final static         long               serialVersionUID            = 6866122520435746113L;
  
  private final static  Log                         log                            = LogFactory.getLog( BPEProcess.class );

  protected            String			  m_version;
  protected            int                m_activityCount;
  protected            Map                m_activities;
  protected transient  String             m_startedBy;
  protected transient  Date               m_startedDate;
  protected transient  String             m_clientId;
  protected transient  String			  m_parentProcessInstanceIdPath;
  protected            MultiMap           m_messageReceivers;
  protected            List               m_alarmReceivers;
  protected            Map                m_complexTypes;
  protected            List               m_createInstanceOperations;
  protected						 DurationUnit			  m_defaultDurationUnit;
  
  protected transient  String             m_processInstanceId;
  protected transient  long               m_processEntityOid;
  protected transient  BPEEngine          m_bpeEngine;



  /**
   *Constructor for the BPEProcess object
   *
   * @param name  Description of the Parameter
   * @param id    Description of the Parameter
   */
  public BPEProcess( String id, String version, String name, DurationUnit  durationUnit)
  {
    super( name, null );

    m_id = id;
    m_version = version;
    m_process = this;
    m_activityCount = 0;
    m_activities = new LinkedHashMap();
    m_messageReceivers = new MultiHashMap();
    m_alarmReceivers = new ArrayList();
    m_complexTypes = new LinkedHashMap();
    m_createInstanceOperations = new ArrayList();
    m_defaultDurationUnit = durationUnit;
  }


  /**
   * @param processEntityOid  The processEntityOid to set.
   */
  public void setProcessEntityOid( long processEntityOid )
  {
    m_processEntityOid = processEntityOid;
  }


  /**
   * @param processInstanceId  The processInstanceId to set.
   */
  public void setProcessInstanceId( String processInstanceId )
  {
    m_processInstanceId = processInstanceId;
  }


  /**
   * @param startedBy  The startedBy to set.
   */
  public void setStartedBy( String startedBy )
  {
    m_startedBy = startedBy;
  }


  /**
   * @param clientId  The clientId to set.
   */
  public void setClientId( String clientId )
  {
    m_clientId = clientId;
//    IVariable clientVar = new BasicVariable("_canyon_clientId",BasicType.STRING);
//    clientVar.setValue(m_clientId);
//    addVariable(clientVar);
  }


  /**
   * Sets the id attribute of the BPEProcess object
   *
   * @param id  The new id value
   */
  public void setId( String id )
  {
    m_id = id;
  }


  /**
   * @param bpeEngine  The bpeEngine to set.
   */
  public void setBPEEngine( BPEEngine bpeEngine )
  {
    m_bpeEngine = bpeEngine;
  }


  /**
   * @return   Returns the bpeEngine.
   */
  public BPEEngine getBPEEngine()
  {
    return m_bpeEngine;
  }


  /**
   * @return   Returns the clientId.
   */
  public String getClientId()
  {
    return m_clientId;
  }


  /**
   * @return   Returns the processEntityOid.
   */
  public long getProcessEntityOid()
  {
    return m_processEntityOid;
  }

  public String getVersion() {
    return m_version;
  }

  /**
   * @return   Returns the processInstanceId.
   */
  public String getProcessInstanceId()
  {
    return m_processInstanceId;
  }


  /**
   * Gets the type attribute of the BPEProcess object
   *
   * @param name  Description of the Parameter
   * @return      The type value
   */
  public ComplexType getType( String name )
  {
    return ( ComplexType ) m_complexTypes.get( name );
  }


  /**
   * @return   Returns the startedBy.
   */
  public String getStartedBy()
  {
    return m_startedBy;
  }


  /**
   * Gets the elementName attribute of the BPEProcess object
   *
   * @return   The elementName value
   */
  public String getElementName()
  {
    return "process";
  }


  /**
   * @return   Returns the bpeEngine.
   */
  public BPEEngine getEngine()
  {
    return m_bpeEngine;
  }


  /**
   * @return   Returns the createInstanceOperations.
   */
  public List getCreateInstanceOperations()
  {
    return m_createInstanceOperations;
  }


  /**
   * Gets the alarmReceivers attribute of the BPEProcess object
   *
   * @return   The alarmReceivers value
   */
  public List getAlarmReceivers()
  {
    return m_alarmReceivers;
  }


  /**
   * Gets the activity attribute of the BPEProcess object
   *
   * @param activityId  Description of the Parameter
   * @return            The activity value
   */
  public Activity getActivity( String activityId )
  {
    return ( Activity ) m_activities.get( activityId );
  }

  public Collection getActitivities() {
  	return m_activities.values();
  }

  /**
   * @param out              Description of the Parameter
   * @exception IOException  Description of the Exception
   * @see                    de.objectcode.canyon.bpe.util.IStateHolder#dehydrate(java.io.ObjectOutput)
   */
  public void dehydrate( HydrationContext context, ObjectOutput out )
    throws IOException
  {
    if ( m_parentProcessInstanceIdPath != null ) {
      out.writeBoolean( true );
      out.writeUTF( m_parentProcessInstanceIdPath );
    } else {
      out.writeBoolean( false );
    }
    if ( m_clientId != null ) {
      out.writeBoolean( true );
      out.writeUTF( m_clientId );
    } else {
      out.writeBoolean( false );
    }
    if ( m_startedBy != null ) {
      out.writeBoolean( true );
      out.writeUTF( m_startedBy );
    } else {
      out.writeBoolean( false );
    }
    if (m_startedDate != null) {
      out.writeBoolean( true );
      out.writeLong(m_startedDate.getTime());
    } else {
      out.writeBoolean( false );      
    }
    super.dehydrate( context, out );
  }


  /**
   * @param in               Description of the Parameter
   * @exception IOException  Description of the Exception
   * @see                    de.objectcode.canyon.bpe.util.IStateHolder#hydrate(java.io.ObjectInput)
   */
  public void hydrate( HydrationContext context, ObjectInput in )
    throws IOException
  {
    if ( in.readBoolean() ) {
      m_parentProcessInstanceIdPath = in.readUTF();
    } else {
      m_parentProcessInstanceIdPath = null;
    }
    if ( in.readBoolean() ) {
      m_clientId = in.readUTF();
    } else {
      m_clientId = null;
    }
    if ( in.readBoolean() ) {
      m_startedBy = in.readUTF();
    } else {
      m_startedBy = null;
    }
    if (in.readBoolean()) {
      m_startedDate = new Date(in.readLong());
    } else {
      m_startedDate = null;
    }
    super.hydrate( context, in );
  }


  /**
   * Adds a feature to the MessageReceiver attribute of the BPEProcess object
   *
   * @param messageReceiver  The feature to be added to the MessageReceiver attribute
   */
  void addMessageReceiver( IMessageReceiver messageReceiver )
  {
    m_messageReceivers.put( messageReceiver.getMessageOperation(), messageReceiver );
    if ( messageReceiver.isCreateInstance() ) {
      m_createInstanceOperations.add( new MessageType( messageReceiver.getMessageOperation(), messageReceiver.getMessageContentType() ) );
    }
  }


  /**
   * Adds a feature to the AlarmReceiver attribute of the BPEProcess object
   *
   * @param alarmReceiver  The feature to be added to the AlarmReceiver attribute
   */
  void addAlarmReceiver( IAlarmReceiver alarmReceiver )
  {
    m_alarmReceivers.add( alarmReceiver );
  }


  /**
   * Description of the Method
   *
   * @param messageReceiver  Description of the Parameter
   */
  void removeMessageReceiver( IMessageReceiver messageReceiver )
  {
    m_messageReceivers.remove( messageReceiver.getMessageOperation(), messageReceiver );
  }


  /**
   * Description of the Method
   *
   * @param activity  Description of the Parameter
   */
  protected void register( Activity activity )
  {
    String  id  = activity.getId();

    if ( id == null ) {
      m_activityCount++;

      id = String.valueOf( m_activityCount );

      activity.setId( id );
    }

    if ( m_activities.containsKey( id ) ) {
      throw new InternalError( "Process '" + m_id + "' already contains an activity '" + id + "'" );
    }

    m_activities.put( id, activity );
  }


  /**
   * Description of the Method
   *
   * @exception EngineException  Description of the Exception
   */
  public void terminateProcess()
    throws EngineException
  {
  	
    Iterator       it     = m_activities.values().iterator();
    ActivityState  state  = ActivityState.COMPLETED;

    while ( it.hasNext() ) {
      Activity  activity  = ( Activity ) it.next();

      if ( activity.getState() == ActivityState.RUNNING ) {
        state = ActivityState.ABORT;
        activity.terminate();
      }
    }

    m_state = state;
    if ( log.isInfoEnabled() ) {
      log.info( "Terminated process id='" + getId() + "', piidPath='"+ getProcessInstanceIdPath()+"'");
    }
		getEngine().getEventHub().fireActivityStateEvent(this, m_state);
    
  }


  /**
   * Description of the Method
   *
   * @param message              Description of the Parameter
   * @exception EngineException  Description of the Exception
   */
  public void handleMessage( Message message )
    throws EngineException
  {
    m_bpeEngine.getEventHub().fireProcessMessageEvent( this, message );

    Collection  receivers  = ( Collection ) m_messageReceivers.get( message.getOperation() );

    if ( receivers != null ) {
      Iterator  it  = receivers.iterator();

      while ( it.hasNext() ) {
        IMessageReceiver  receiver         = ( IMessageReceiver ) it.next();

        if ( !receiver.isActive() ) {
          continue;
        }

        CorrelationSet[]  correlationSets  = receiver.getCorrelationSets();

        if ( correlationSets != null && correlationSets.length > 0 ) {
          int  i;

          for ( i = 0; i < correlationSets.length; i++ ) {
            Correlation  correlation  = getCorrelation( correlationSets[i].getName(), receiver );

            if ( correlation == null || !correlation.match( message ) ) {
              break;
            }
          }

          if ( i == correlationSets.length ) {
            if ( receiver.onMessage( message ) ) {
              break;
            }
          }
        } else {
          if ( receiver.onMessage( message ) ) {
            break;
          }
        }
      }
    }

    startActivatedActivities();
  }

  private Correlation getCorrelation(String name, IMessageReceiver receiver) {
  	Correlation c =  ( Correlation ) m_correlations.get( name );
  	if (c==null && receiver instanceof Activity) {
  		Scope scope = ((Activity) receiver).getScope();
  		c = scope.getCorrelation(name);
  	}
  	return c;
  }

  /**
   * Description of the Method
   *
   * @exception EngineException  Description of the Exception
   */
  public void handleAlarm()
    throws EngineException
  {
    long      now  = System.currentTimeMillis();
    Iterator  it   = m_alarmReceivers.iterator();

    while ( it.hasNext() ) {
      IAlarmReceiver  alarmReceiver  = ( IAlarmReceiver ) it.next();

      if ( alarmReceiver.isActive() && alarmReceiver.getAlarmTime() < now ) {
        alarmReceiver.onAlarm();
      }
    }

    startActivatedActivities();
  }

  /**
   * @see de.objectcode.canyon.bpe.engine.activities.Scope#throwFault(java.lang.String)
   */
  public void throwFault( Fault fault)
      throws EngineException
  {
  	String faultName = fault.getName(); 
    FaultHandler faultHandler = (FaultHandler) m_faultHandlers.get( faultName );

    if ( faultHandler == null )
      faultHandler = (FaultHandler) m_faultHandlers.get( null );

    if ( faultHandler != null )
      faultHandler.fire(fault);
    else
      handlerFired();
  }

  /**
   * Description of the Method
   *
   * @exception EngineException  Description of the Exception
   */
  public void startActivatedActivities()
    throws EngineException
  {
    if ( m_state == ActivityState.ACTIVATED ) {
      start();
    }

    while ( true ) {
      Iterator  it     = m_activities.values().iterator();
      boolean   found  = false;

      while ( it.hasNext() ) {
        Activity  activity  = ( Activity ) it.next();

        if ( activity.getState() == ActivityState.ACTIVATED ) {
          found = true;
          activity.start();
        } else if ( activity.getState() == ActivityState.DEACTIVATED ) {
          found = true;
          activity.skip();
        }
      }

      if ( !found ) {
        return;
      }
    }
  }


  /**
   * Description of the Method
   *
   * @param type  Description of the Parameter
   */
  public void registerType( ComplexType type )
  {
    m_complexTypes.put( type.getName(), type );
  }


  /**
   * Description of the Method
   *
   * @param element  Description of the Parameter
   */
  public void toDom( Element element )
  {
    if ( m_processInstanceId != null ) {
      element.addAttribute( "processInstanceId", m_processInstanceId );
    }
    if ( m_startedBy != null ) {
      element.addAttribute( "started-by", m_startedBy );
    }
    if ( m_clientId != null ) {
      element.addAttribute( "client-id", m_clientId );
    }
    if ( m_id != null ) {
      element.addAttribute( "process-id", m_id );
    }
    if ( m_version != null ) {
      element.addAttribute( "process-version", m_version );
    }

    if ( !m_complexTypes.isEmpty() ) {
      Element   definitions  = element.addElement( "definitions" );
      Iterator  it           = m_complexTypes.values().iterator();

      while ( it.hasNext() ) {
        ComplexType  type  = ( ComplexType ) it.next();

        type.toDom( definitions.addElement( type.getElementName() ) );
      }
    }
    if ( m_parentProcessInstanceIdPath != null ) {
      element.addAttribute( "processInstanceIdPath", m_parentProcessInstanceIdPath );
    }
  
    super.toDom( element );
  }


  public Date getStartedDate() {
    return m_startedDate;
  }
  
  /**
   * @param date
   */
  public void setStartedDate(Date date) {
    m_startedDate = date;
  }
  
  
  public String getParentProcessInstanceIdPath() {
    return m_parentProcessInstanceIdPath;
  }
  
  public String getProcessInstanceIdPath() {
  	if (getParentProcessInstanceIdPath()!=null)
  		return getParentProcessInstanceIdPath() + "_" + getProcessInstanceId();
  	else
  		return getProcessInstanceId();
  }
  
  public void setParentProcessInstanceIdPath(String processInstanceIdPath) {
    m_parentProcessInstanceIdPath = processInstanceIdPath;
  }
  
  public void addFaultHandler( FaultHandler faultHandler )
  {
  	super.addFaultHandler(faultHandler);
  	faultHandler.registerVariables(this);
  }


	public DurationUnit getDefaultDurationUnit() {
		return m_defaultDurationUnit;
	}


	public String getParentProcessInstanceId() {
		if (m_parentProcessInstanceIdPath==null)
			return null;
		else {
			int index = m_parentProcessInstanceIdPath.lastIndexOf("_");
			if (index==-1)
				return m_parentProcessInstanceIdPath;
			else
				return m_parentProcessInstanceIdPath.substring(index+1);
		}
	}
  
}
