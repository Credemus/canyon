package de.objectcode.canyon.jmx.admin;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;

import javax.naming.InitialContext;
import javax.rmi.PortableRemoteObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wfmc.wapi.WMWorkItemState;

import de.objectcode.canyon.api.worklist.ApplicationData;
import de.objectcode.canyon.api.worklist.WorkItemData;
import de.objectcode.canyon.api.worklist.Worklist;
import de.objectcode.canyon.api.worklist.WorklistHome;
import de.objectcode.canyon.spi.ServiceManager;
import de.objectcode.canyon.spi.instance.IAttributeInstance;
import de.objectcode.canyon.spi.tool.MessageEvent;
import de.objectcode.canyon.spi.tool.Parameter;
import de.objectcode.canyon.worklist.spi.worklist.IApplicationData;
import de.objectcode.canyon.worklist.spi.worklist.IWorkItem;


/**
 * @jmx.mbean name="canyon:service=WorkItemAdmin" description="WorkItem administration bean"
 * @jboss.service servicefile="jboss"
 * @jboss.depends object-name="canyon:service=ServiceManager"
 * @jboss.xmbean
 *
 * @author    junglas
 * @created   4. Februar 2004
 */
public class WorkItemAdmin extends BaseAdmin implements WorkItemAdminMBean
{
  private final static  Log      log        = LogFactory.getLog( WorkItemAdmin.class );

  private               boolean  m_started;


  /**
   * @jmx.managed-attribute access="read-write" description="JNDI name of the ServiceManager"
   *
   * @param string
   */
  public void setJndiName( String string )
  {
    m_jndiName = string;
  }


  /**
   * @jmx.managed-attribute access="read-write" description="JNDI name of the ServiceManager"
   *
   * @return
   */
  public String getJndiName()
  {
    return m_jndiName;
  }


  /**
   * @jmx.managed-operation description="List the work items of an activity instance"
   * @jmx.managed-parameter name="processInstanceId" type="java.lang.String" description="Process instance id"
   * @jmx.managed-parameter name="activityInstanceId" type="java.lang.String" description="Activity instance id"
   *
   * @param activityInstanceId  Description of the Parameter
   * @return                    Description of the Return Value
   * @exception Exception       Description of the Exception
   */
  public String listWorkItems( String processInstanceId, String activityInstanceId )
    throws Exception
  {
    try {
      beginTransaction();

      StringBuffer  list               = new StringBuffer();

      IWorkItem     activityInstances[]  = m_svcMgr.getWorklistRepository().findWorkItems( processInstanceId, activityInstanceId );

      list.append( "<h1>WorkItems of ActivityInstance &quot;" ).append( activityInstanceId ).append( "&quot;</h1>" );
      list.append( "<table>" );
      list.append( "<tr><th>WorkItemId</th><th>Engine Id</th><th>ProcessDefinitionId</th><th>ActivityDefinitionId</th><th>ActivityInstanceId</td><th>Name</th><th>Status</th></tr>" );

      int           i;

      for ( i = 0; i < activityInstances.length; i++ ) {
        list.append( "<tr>" );
        list.append( "<td>" ).append( activityInstances[i].getWorkItemId() ).append( "</td>" );
        list.append( "<td>" ).append( activityInstances[i].getEngineId() ).append( "</td>" );
        list.append( "<td>" ).append( activityInstances[i].getProcessDefinitionId() ).append( "</td>" );
        list.append( "<td>" ).append( activityInstances[i].getActivityDefinitionId() ).append( "</td>" );
        list.append( "<td>" ).append( activityInstances[i].getActivityInstanceId() ).append( "</td>" );
        list.append( "<td>" ).append( activityInstances[i].getName() ).append( "</td>" );
        list.append( "<td>" ).append( WMWorkItemState.fromInt( activityInstances[i].getState() ).stringValue() ).append( "</td>" );
        list.append( "</tr>" );
      }

      return list.toString();
    }
    finally {
      endTransaction();
    }
  }


  /**
   * Description of the Method
   *
   * @jmx.managed-operation description="Show details of a workitem"
   * @jmx.managed-parameter name="workItemId" type="java.lang.String" description="Workitem instance id"
   *
   * @param workItemId          Description of the Parameter
   * @return                    Description of the Return Value
   * @exception Exception       Description of the Exception
   */
  public String showWorkItem( String workItemId )
    throws Exception
  {
    try {
      beginTransaction();
      StringBuffer  buffer    = new StringBuffer();

      buffer.append( "<h1>Workitem &quot;" ).append( workItemId ).append( "&quot;</h1>" );

      IWorkItem     workItem  = m_svcMgr.getWorklistRepository().findWorkItem( workItemId );

      buffer.append( "<h2>Attributes</h2>" );
      buffer.append( "<table>" );
      Map           attrs     = workItem.getAttributeInstances();
      Iterator      it        = attrs.values().iterator();
      while ( it.hasNext() ) {
        IAttributeInstance  attr  = ( IAttributeInstance ) it.next();

        buffer.append( "<tr><td>" );
        buffer.append( attr.getName() );
        buffer.append( "</td><td>" );
        buffer.append( attr.getValue() );
        buffer.append( "</td></tr>" );
      }
      buffer.append( "</table>" );
      
      IApplicationData[] applicationDataSet = workItem.getApplicationData();
      for (int k = 0; k < applicationDataSet.length; k++) {
	      IApplicationData applicationData = applicationDataSet[k];
	      
	      if ( applicationData != null ) {
	        buffer.append("<h2>Application data</h2>");
	        buffer.append("<table>");
	        buffer.append("<tr><td>Id</td><td>").append(applicationData.getId()).append("</td></tr>");
	        buffer.append("<tr><td>Name</td><td>").append(applicationData.getName()).append("</td></tr>");
	        buffer.append("<tr><td>Description</td><td>").append(applicationData.getDescription()).append("</td></tr>");
	        buffer.append("</table>");
	        buffer.append("<h3>Parameters</h3>");
	        buffer.append("<table>");
	        buffer.append("<tr><th>Formal name</th><th>Actual name</th><th>Data type</th><th>Mode</th><th>Value</th></tr>");
	        int i;
	        Parameter parameters[] = applicationData.getParameters();
	       
	        for ( i = 0; i< parameters.length; i++ ) {
	          buffer.append("<tr>");
	          buffer.append("<td>").append(parameters[i].formalName).append("</td>");
	          buffer.append("<td>").append(parameters[i].actualName).append("</td>");
	          buffer.append("<td>").append(parameters[i].dataType).append("</td>");
	          buffer.append("<td>").append(parameters[i].mode).append("</td>");
	          buffer.append("<td>").append(parameters[i].value).append("</td>");
	        }
	        buffer.append("</table>");
	      }
      }
      return buffer.toString();
    }
    finally {
      endTransaction();
    }
  }

  /**
   * Description of the Method
   *
   * @jmx.managed-operation description="Update a process instance"
   * @jmx.managed-parameter name="workItemId" type="java.lang.String"
   *                        description="WorkItem id"
   * @jmx.managed-parameter name="parameterName" type="java.lang.String"
   *                        description="WorkItem parameter name"
   * @jmx.managed-parameter name="value" type="java.lang.String"
   *                        description="WorkItem variable value"
   *
   * @param workItemId
   *          Description of the Parameter
   * @param parameterName
   * @param value
   * @return Description of the Return Value
   * @exception Exception
   *              Description of the Exception
   */
  public String updateWorkItem(String workItemId, String parameterName, String value) throws Exception {
        try {
            InitialContext ctx = new InitialContext();

            WorklistHome home = (WorklistHome) PortableRemoteObject.narrow(ctx.lookup(WorklistHome.JNDI_NAME), WorklistHome.class);
            Worklist worklist = home.create();

            WorkItemData workItemData = worklist.getWorkItem(workItemId);

            boolean bValueFound = false;
            ApplicationData[] applicationDatas = workItemData.getApplicationData();
            for (int k = 0; k < applicationDatas.length; k++) {
                ApplicationData data = applicationDatas[k];

                de.objectcode.canyon.api.worklist.ApplicationData.Parameter parameter = null;
                parameter = data.getParameter(parameterName);
                if (parameter != null) {
                    parameter.setValue(value);
                    bValueFound = true;
                }
            }

            if (bValueFound == true) {
                worklist.updateWorkItem(workItemData);
                return "OK";
            } 
            
            return "Parameter not found";
        } catch (Exception e) {
            return e.toString();
        }
    }

  /**
   * @jmx.managed-operation description="Manually completes a workitem"
   * @jmx.managed-parameter name="workItemId" type="java.lang.String" description="Work item instance id"
   *
   * @param workItemId     Description of the Parameter
   * @exception Exception  Description of the Exception
   */
  public void completeWorkItem( String workItemId )
    throws Exception
  {
    try {
      beginTransaction();
      m_svcMgr.getWorklistEngine().getWorkItemManager().completeWorkItem( workItemId );
    }
    catch ( Exception e ) {
      log.error("Exception", e);
      throw e;
    }
    finally {
      endTransaction();
    }
  }


  /**
   * @jmx.managed-operation description="Handle a worklist event"
   * @jmx.managed-parameter name="clientId" type="java.lang.String" description="Client Id"
   * @jmx.managed-parameter name="eventType" type="java.lang.String" description="Event type"
   * @jmx.managed-parameter name="eventParameters" type="java.lang.String" description="Event parameters"
   *
   * @param clientId         Description of the Parameter
   * @param eventType        Description of the Parameter
   * @param eventParameters  Description of the Parameter
   * @return                 Description of the Return Value
   * @exception Exception    Description of the Exception
   */
  public boolean handleEvent( String clientId, String eventType, String eventParameters )
    throws Exception
  {
    StringTokenizer  t         = new StringTokenizer( eventParameters, ",;" );
    Map              paramMap  = new HashMap();

    while ( t.hasMoreTokens() ) {
      String  token  = t.nextToken();
      int     idx    = token.indexOf( '=' );

      if ( idx >= 0 ) {
        paramMap.put( token.substring( 0, idx ), token.substring( idx + 1 ) );
      }
    }

    try {
      beginTransaction();
      
      MessageEvent event = new MessageEvent();
      event.setClientId(clientId);
      event.setEventType(eventType);
      event.setEventParams(paramMap);
      String[] workItemIds = m_svcMgr.getWorklistEngine().findWorkItemsForEvent(event);
      
      if ( workItemIds.length>0  ) {
        endTransaction();
        beginTransaction();
      
        return m_svcMgr.getWorklistEngine().handleEvent( workItemIds[0], event );
      }
      return false;
    }
    catch ( Throwable e ) {
      log.error( "Exception", e );
      throw new Exception( "Exception", e );
    }
    finally {
      endTransaction();
    }
  }


  /**
   * Description of the Method
   *
   * @jmx.managed-operation
   *
   * @exception Exception  Description of the Exception
   */
  public void start()
    throws Exception
  {
    log.info( "Starting ProcessAdmin" );

    InitialContext  ctx  = new InitialContext();

    m_svcMgr = ( ServiceManager ) ctx.lookup( m_jndiName );

    m_started = true;
  }


  /**
   * @jmx.managed-operation
   *
   * @exception Exception  Description of the Exception
   * @see                  de.neutrasoft.saints.core.obe.mbean.OBEServiceManagerMBean#stop()
   */
  public void stop()
    throws Exception
  {
    log.info( "Stopping ProcessAdmin" );
    m_svcMgr = null;
    m_started = false;
  }
}
