package de.objectcode.canyon.ejb.worklist;

import javax.ejb.CreateException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wfmc.wapi.WMWorkflowException;

import de.objectcode.canyon.api.worklist.ApplicationData;
import de.objectcode.canyon.api.worklist.WorkItemData;
import de.objectcode.canyon.api.worklist.WorklistEvent;
import de.objectcode.canyon.ejb.BaseServiceManagerBean;
import de.objectcode.canyon.model.data.ParameterMode;
import de.objectcode.canyon.spi.RepositoryException;
import de.objectcode.canyon.spi.filter.IFilter;
import de.objectcode.canyon.spi.tool.MessageEvent;
import de.objectcode.canyon.spi.tool.Parameter;
import de.objectcode.canyon.worklist.spi.worklist.IApplicationData;
import de.objectcode.canyon.worklist.spi.worklist.IWorkItem;

/**
 * @ejb.bean name="Worklist" type="Stateless"
 *           jndi-name="de/objectcode/canyon/ejb/worklist/Worklist"
 *           local-jndi-name="de/objectcode/canyon/ejb/worklist/WorklistLocal"
 *           transaction-type="Bean"
 *           view-type="both"
 * @ejb.permission unchecked="true"
 *
 * @ejb.resource-ref res-ref-name="ServiceManager" res-type="de.objectcode.canyon.spi.ServiceManager"
 *   res-auth="Application"
 * @jboss.resource-ref res-ref-name="ServiceManager" jndi-name="java:/canyon/ServiceManager"
 *
 * @author    junglas
 * @created   5. Dezember 2003
 */
public class WorklistBean extends BaseServiceManagerBean
{
	static final long serialVersionUID = 652518410424806449L;
	
	private static Log log = LogFactory.getLog(WorklistBean.class);
	
  /**
   * @ejb.interface-method
   *
   * @param workItemId               Description of the Parameter
   * @return                         The workItem value
   * @exception WMWorkflowException  Description of the Exception
   */
  public WorkItemData getWorkItem( String workItemId )
    throws WMWorkflowException
  {
    try {
      beginTransaction();
      IWorkItem  workItem  = m_worklistEngine.getWorkItemManager().getWorkItem( workItemId );

      return WorkItemData.createWorkItemData( workItem );
    }
    catch ( RepositoryException e ) {
      rollbackTransaction();
      m_log.error( "Exception", e );
      if ( e.getCause() != null ) {
        m_log.error( "Cause", e.getCause() );
      }
      throw new WMWorkflowException( e );
    }
    catch ( WMWorkflowException e ) {
      rollbackTransaction();
      m_log.error( "Exception", e );
      if ( e.getCause() != null ) {
        m_log.error( "Cause", e.getCause() );
      }
      throw e;
    }
    finally {
      try {
        commitTransaction();
      }
      catch ( Exception e ) {
        m_log.error( "Exception", e );
      }
    }
  }

  /**
   * @ejb.interface-method
   *
   * @param onlyOpen                 Description of the Parameter
   * @return                         Description of the Return Value
   * @exception WMWorkflowException  Description of the Exception
   */
  public int countWorkItems( String userName, boolean onlyOpen )
    throws WMWorkflowException
  {
    if ( m_log.isDebugEnabled() ) {
      m_log.debug("countWorkItems: " + userName);
    }
    try {
      beginTransaction();
      int  count  = m_worklistEngine.getWorkItemManager().countWorkItems( userName, onlyOpen );

      return count;
    }
    catch ( WMWorkflowException e ) {
      rollbackTransaction();
      m_log.error( "Exception", e );
      if ( e.getCause() != null ) {
        m_log.error( "Cause", e.getCause() );
      }
      throw e;
    }
    catch ( RepositoryException e ) {
      rollbackTransaction();
      m_log.error( "Exception", e );
      if ( e.getCause() != null ) {
        m_log.error( "Cause", e.getCause() );
      }
      throw new WMWorkflowException( e );
    }
    finally {
      try {
        commitTransaction();
      }
      catch ( Exception e ) {
        m_log.error( "Exception", e );
      }
    }
  }
  

  /**
   * @ejb.interface-method
   *
   * @param onlyOpen                 Description of the Parameter
   * @return                         Description of the Return Value
   * @exception WMWorkflowException  Description of the Exception
   */
  public int countWorkItems( boolean onlyOpen )
    throws WMWorkflowException
  {
    if ( m_log.isDebugEnabled() ) {
      m_log.debug("countWorkItems: " + m_ctx.getCallerPrincipal());
    }
    try {
      beginTransaction();
      int  count  = m_worklistEngine.getWorkItemManager().countWorkItems( m_ctx.getCallerPrincipal().getName(), onlyOpen );

      return count;
    }
    catch ( WMWorkflowException e ) {
      rollbackTransaction();
      m_log.error( "Exception", e );
      if ( e.getCause() != null ) {
        m_log.error( "Cause", e.getCause() );
      }
      throw e;
    }
    catch ( RepositoryException e ) {
      rollbackTransaction();
      m_log.error( "Exception", e );
      if ( e.getCause() != null ) {
        m_log.error( "Cause", e.getCause() );
      }
      throw new WMWorkflowException( e );
    }
    finally {
      try {
        commitTransaction();
      }
      catch ( Exception e ) {
        m_log.error( "Exception", e );
      }
    }
  }

  /**
   * @ejb.interface-method
   *
   * @param offset                   Description of the Parameter
   * @param length                   Description of the Parameter
   * @param onlyOpen                 Description of the Parameter
   * @return                         Description of the Return Value
   * @exception WMWorkflowException  Description of the Exception
   */
  public WorkItemData[] listWorkItemsForProcessInstance( String processInstanceId, int offset, int length, boolean onlyOpen)
	throws WMWorkflowException
  {
	if ( m_log.isDebugEnabled() ) {
	  m_log.debug("listWorkItemsForProcessInstance: " + processInstanceId);
	}
	try {
	  beginTransaction();
	  IWorkItem     workItems[]  = m_worklistEngine.getWorkItemManager().listWorkItemsForProcessInstance( processInstanceId, onlyOpen );
	  int           len        = length;

	  if ( offset >= workItems.length ) {
		return new WorkItemData[0];
	  } else if ( length + offset > workItems.length ) {
		len = workItems.length - offset;
	  }

	  WorkItemData  ret[]        = new WorkItemData[len];
	  int           i;

	  for ( i = 0; i < len; i++ ) {
		ret[i] = WorkItemData.createWorkItemData( workItems[i + offset] );
	  }

	  return ret;
	}
	catch ( RepositoryException e ) {
	  rollbackTransaction();
	  m_log.error( "Exception", e );
	  if ( e.getCause() != null ) {
		m_log.error( "Cause", e.getCause() );
	  }
	  throw new WMWorkflowException( e );
	}
	catch ( WMWorkflowException e ) {
	  rollbackTransaction();
	  m_log.error( "Exception", e );
	  if ( e.getCause() != null ) {
		m_log.error( "Cause", e.getCause() );
	  }
	  throw e;
	}
	finally {
	  try {
		commitTransaction();
	  }
	  catch ( Exception e ) {
		m_log.error( "Exception", e );
	  }
	}
  }

  /**
   * @ejb.interface-method
   *
   * @param offset                   Description of the Parameter
   * @param length                   Description of the Parameter
   * @param onlyOpen                 Description of the Parameter
   * @return                         Description of the Return Value
   * @exception WMWorkflowException  Description of the Exception
   */
  public WorkItemData[] listWorkItems( String userName, int offset, int length, boolean onlyOpen )
    throws WMWorkflowException
  {
    if ( m_log.isDebugEnabled() ) {
      m_log.debug("listWorkItems: " + userName);
    }
    try {
      beginTransaction();
      IWorkItem     workItems[]  = m_worklistEngine.getWorkItemManager().listWorkItems( userName, onlyOpen );
      int           len        = length;

      if ( offset >= workItems.length ) {
        return new WorkItemData[0];
      } else if ( length + offset > workItems.length ) {
        len = workItems.length - offset;
      }

      WorkItemData  ret[]        = new WorkItemData[len];
      int           i;

      for ( i = 0; i < len; i++ ) {
        ret[i] = WorkItemData.createWorkItemData( workItems[i + offset] );
      }

      return ret;
    }
    catch ( RepositoryException e ) {
      rollbackTransaction();
      m_log.error( "Exception", e );
      if ( e.getCause() != null ) {
        m_log.error( "Cause", e.getCause() );
      }
      throw new WMWorkflowException( e );
    }
    catch ( WMWorkflowException e ) {
      rollbackTransaction();
      m_log.error( "Exception", e );
      if ( e.getCause() != null ) {
        m_log.error( "Cause", e.getCause() );
      }
      throw e;
    }
    finally {
      try {
        commitTransaction();
      }
      catch ( Exception e ) {
        m_log.error( "Exception", e );
      }
    }
  }


  /**
   * @ejb.interface-method
   *
   * @param offset                   Description of the Parameter
   * @param length                   Description of the Parameter
   * @param onlyOpen                 Description of the Parameter
   * @return                         Description of the Return Value
   * @exception WMWorkflowException  Description of the Exception
   */
  public WorkItemData[] listWorkItems( String userName, String clientId, int offset, int length, boolean onlyOpen )
    throws WMWorkflowException
  {
    if ( m_log.isDebugEnabled() ) {
      m_log.debug("listWorkItems: " + userName);
    }
    try {
      beginTransaction();
      IWorkItem     workItems[]  = m_worklistEngine.getWorkItemManager().listWorkItems( userName, clientId, onlyOpen );
      int           len        = length;

      if ( offset >= workItems.length ) {
        return new WorkItemData[0];
      } else if ( length + offset > workItems.length ) {
        len = workItems.length - offset;
      }

      WorkItemData  ret[]        = new WorkItemData[len];
      int           i;

      for ( i = 0; i < len; i++ ) {
        ret[i] = WorkItemData.createWorkItemData( workItems[i + offset] );
      }

      return ret;
    }
    catch ( RepositoryException e ) {
      rollbackTransaction();
      m_log.error( "Exception", e );
      if ( e.getCause() != null ) {
        m_log.error( "Cause", e.getCause() );
      }
      throw new WMWorkflowException( e );
    }
    catch ( WMWorkflowException e ) {
      rollbackTransaction();
      m_log.error( "Exception", e );
      if ( e.getCause() != null ) {
        m_log.error( "Cause", e.getCause() );
      }
      throw e;
    }
    finally {
      try {
        commitTransaction();
      }
      catch ( Exception e ) {
        m_log.error( "Exception", e );
      }
    }
  }
  
  /**
   * @ejb.interface-method
   *
   * @param offset                   Description of the Parameter
   * @param length                   Description of the Parameter
   * @param onlyOpen                 Description of the Parameter
   * @return                         Description of the Return Value
   * @exception WMWorkflowException  Description of the Exception
   */
  public WorkItemData[] listWorkItems( String userName, String clientId, int offset, int length, boolean onlyOpen, IFilter filter, String[] sortAttrs, boolean[] sortAscending )
    throws WMWorkflowException
  {
    if ( m_log.isDebugEnabled() ) {
      m_log.debug("listWorkItems: " + userName);
    }
    try {
      beginTransaction();
      IWorkItem     workItems[]  = m_worklistEngine.getWorkItemManager().listWorkItems( userName, clientId, onlyOpen, offset, length, filter, sortAttrs, sortAscending  );
      int           len        = workItems.length;

      WorkItemData  ret[]        = new WorkItemData[len];
      int           i;

      for ( i = 0; i < len; i++ ) {
        ret[i] = WorkItemData.createWorkItemData( workItems[i] );
      }

      return ret;
    }
    catch ( RepositoryException e ) {
      rollbackTransaction();
      m_log.error( "Exception", e );
      if ( e.getCause() != null ) {
        m_log.error( "Cause", e.getCause() );
      }
      throw new WMWorkflowException( e );
    }
    catch ( WMWorkflowException e ) {
      rollbackTransaction();
      m_log.error( "Exception", e );
      if ( e.getCause() != null ) {
        m_log.error( "Cause", e.getCause() );
      }
      throw e;
    }
    finally {
      try {
        commitTransaction();
      }
      catch ( Exception e ) {
        m_log.error( "Exception", e );
      }
    }
  }

  /**
   * @ejb.interface-method
   *
   * @param offset                   Description of the Parameter
   * @param length                   Description of the Parameter
   * @param onlyOpen                 Description of the Parameter
   * @return                         Description of the Return Value
   * @exception WMWorkflowException  Description of the Exception
   */
  public int countWorkItems( String userName, String clientId, boolean onlyOpen, IFilter filter )
    throws WMWorkflowException
  {
    if ( m_log.isDebugEnabled() ) {
      m_log.debug("listWorkItems: " + userName);
    }
    try {
      beginTransaction();
      int result = m_worklistEngine.getWorkItemManager().countWorkItems( userName, clientId, onlyOpen, filter  );

      return result;
    }
    catch ( RepositoryException e ) {
      rollbackTransaction();
      m_log.error( "Exception", e );
      if ( e.getCause() != null ) {
        m_log.error( "Cause", e.getCause() );
      }
      throw new WMWorkflowException( e );
    }
    catch ( WMWorkflowException e ) {
      rollbackTransaction();
      m_log.error( "Exception", e );
      if ( e.getCause() != null ) {
        m_log.error( "Cause", e.getCause() );
      }
      throw e;
    }
    finally {
      try {
        commitTransaction();
      }
      catch ( Exception e ) {
        m_log.error( "Exception", e );
      }
    }
  }

  /**
   * @ejb.interface-method
   *
   * @param offset                   Description of the Parameter
   * @param length                   Description of the Parameter
   * @param onlyOpen                 Description of the Parameter
   * @return                         Description of the Return Value
   * @exception WMWorkflowException  Description of the Exception
   */
  public WorkItemData[] listWorkItems( int offset, int length, boolean onlyOpen )
    throws WMWorkflowException
  {
    if ( m_log.isDebugEnabled() ) {
      m_log.debug("listWorkItems: " + m_ctx.getCallerPrincipal());
    }
    try {
      beginTransaction();
      IWorkItem     workItems[]  = m_worklistEngine.getWorkItemManager().listWorkItems( m_ctx.getCallerPrincipal().getName(), onlyOpen );
      int           len        = length;

      if ( offset >= workItems.length ) {
        return new WorkItemData[0];
      } else if ( length + offset > workItems.length ) {
        len = workItems.length - offset;
      }

      WorkItemData  ret[]        = new WorkItemData[len];
      int           i;

      for ( i = 0; i < len; i++ ) {
        ret[i] = WorkItemData.createWorkItemData( workItems[i + offset] );
      }

      return ret;
    }
    catch ( RepositoryException e ) {
      rollbackTransaction();
      m_log.error( "Exception", e );
      if ( e.getCause() != null ) {
        m_log.error( "Cause", e.getCause() );
      }
      throw new WMWorkflowException( e );
    }
    catch ( WMWorkflowException e ) {
      rollbackTransaction();
      m_log.error( "Exception", e );
      if ( e.getCause() != null ) {
        m_log.error( "Cause", e.getCause() );
      }
      throw e;
    }
    finally {
      try {
        commitTransaction();
      }
      catch ( Exception e ) {
        m_log.error( "Exception", e );
      }
    }
  }

  /**
   * @ejb.interface-method
   *
   * @param processDefinitionId
   * @param activityDefinitionId
   * @param overdue
   * @param performer
   * @param participants
   * @param states
   * @param activityName
   * 
   * @return
   * @throws WMWorkflowException
   */
  public WorkItemData[] listWorkItems(String processDefinitionId,
      String activityDefinitionId, Boolean overdue, String performer,
      String[] participants, int[] states, String activityName,
      String processName, String processInstanceIdPath)
      throws WMWorkflowException {
    return listWorkItems(processDefinitionId, activityDefinitionId, overdue,
	performer, participants, states, activityName, processName,
	processInstanceIdPath, Integer.MAX_VALUE);
  }
  
    /**
         * @ejb.interface-method
         * 
         * @param processDefinitionId
         * @param activityDefinitionId
         * @param overdue
         * @param performer
         * @param participants
         * @param states
         * @param activityName
         * 
         * @return
         * @throws WMWorkflowException
         */
  public WorkItemData[] listWorkItems(
  		String processDefinitionId, 
			String activityDefinitionId, 
			Boolean overdue, 
			String performer, 
			String[] participants, 
			int[] states,
  		String activityName,
  		String processName,
  		String processInstanceIdPath,
  		int maxResultSize
			)
    throws WMWorkflowException
  {
    if ( m_log.isDebugEnabled() ) {
      m_log.debug("listWorkItems: ");
    }
    try {
        beginTransaction();
        IWorkItem     workItems[]  = 
        	m_worklistEngine.getWorkItemManager().listWorkItems(
        		processDefinitionId, 
						activityDefinitionId, 
						overdue, 
						performer, 
						participants, 
						states,
						activityName,
						processName,
						processInstanceIdPath,
						maxResultSize);
        WorkItemData  ret[]        = new WorkItemData[workItems.length];
        int           i;

        for ( i = 0; i < ret.length; i++ ) {
          ret[i] = WorkItemData.createWorkItemData( workItems[i] );
        }
        
        return ret;
    }
    catch ( RepositoryException e ) {
      rollbackTransaction();
      m_log.error( "Exception", e );
      if ( e.getCause() != null ) {
        m_log.error( "Cause", e.getCause() );
      }
      throw new WMWorkflowException( e );
    }
    catch ( WMWorkflowException e ) {
      rollbackTransaction();
      m_log.error( "Exception", e );
      if ( e.getCause() != null ) {
        m_log.error( "Cause", e.getCause() );
      }
      throw e;
    }
    finally {
      try {
        commitTransaction();
      }
      catch ( Exception e ) {
        m_log.error( "Exception", e );
      }
    }
  }
  
  /**
   * @ejb.interface-method
   *
   * @param offset                   Description of the Parameter
   * @param length                   Description of the Parameter
   * @param onlyOpen                 Description of the Parameter
   * @return                         Description of the Return Value
   * @exception WMWorkflowException  Description of the Exception
   */
  public boolean handleEvent( WorklistEvent event )
    throws WMWorkflowException
  {
    if ( m_log.isDebugEnabled() ) {
      m_log.debug("listWorkItems: " + m_ctx.getCallerPrincipal());
    }
    try {
      beginTransaction();
      
      MessageEvent mEvent = new MessageEvent();
      mEvent.setClientId(event.getClientId());
      mEvent.setEventType(event.getEventType());
      mEvent.setEventParams(event.getEventParameters());
      String[] workItemIds = m_worklistEngine.findWorkItemsForEvent(mEvent);
      
      if ( workItemIds.length > 0 ) {
        rollbackTransaction();
        beginTransaction();
        return m_worklistEngine.handleEvent(workItemIds[0], mEvent );
      }
      return false;
    }
    catch ( RepositoryException e ) {
      rollbackTransaction();
      m_log.error( "Exception", e );
      if ( e.getCause() != null ) {
        m_log.error( "Cause", e.getCause() );
      }
      throw new WMWorkflowException( e );
    }
    catch ( WMWorkflowException e ) {
      rollbackTransaction();
      m_log.error( "Exception", e );
      if ( e.getCause() != null ) {
        m_log.error( "Cause", e.getCause() );
      }
      throw e;
    }
    finally {
      try {
        commitTransaction();
      }
      catch ( Exception e ) {
        m_log.error( "Exception", e );
      }
    }
  }
  
  /**
   * @ejb.interface-method
   *
   * @param workItemData             Description of the Parameter
   * @exception WMWorkflowException  Description of the Exception
   */
  public void updateWorkItem( WorkItemData workItemData )
    throws WMWorkflowException
  {
  	if (log.isDebugEnabled()) {
  		log.debug("updateWorkItem:"+workItemData);
  	}
    try {
      beginTransaction();
      
      IWorkItem workItem = m_worklistEngine.getWorkItemManager().getWorkItem(workItemData.getId());
      
      workItem.setName(workItemData.getName());
      workItem.setPriority(workItemData.getPriority());

      if ( workItemData.getApplicationData() != null && workItemData.getApplicationData().length > 0 &&
           workItem.getApplicationData() != null &&  workItem.getApplicationData().length > 0) {
        IApplicationData[] applicationDataSet = workItem.getApplicationData();
        for (int k = 0; k < applicationDataSet.length; k++) {
            IApplicationData applicationData = applicationDataSet[k];
	        Parameter                  params[]     = applicationData.getParameters();
	        int i;
	        
	        for ( i = 0; i < params.length; i++ ) {
	          if ( params[i].mode != ParameterMode.IN ) {
	            ApplicationData.Parameter  paramData  = workItemData.getApplicationData()[k].getParameter(params[i].formalName);
	            if ( paramData != null ) {
					if (log.isDebugEnabled()) {
						log.debug("Parameter " + params[i].formalName + "[" +  params[i].actualName + "]=" + paramData.getValue());        
					}
	              params[i] = new Parameter(params[i].formalName, params[i].actualName, params[i].dataType, params[i].mode, params[i].description,
	                             paramData.getValue());
	            } else {
					if (log.isDebugEnabled()) {
						log.debug("Parameter " + params[i].formalName + "[" +  params[i].actualName + "]= null");        
					}           	
	            }
	          } else {
				if (log.isDebugEnabled()) {
					log.debug("Parameter " + params[i].formalName + "[" +  params[i].actualName + "] has mode IN");        
				}           	          	
	          }
	        }
        }
        workItem.setApplicationData(applicationDataSet);
      }

      m_worklistEngine.getWorkItemManager().changeWorkItemState( workItemData.getId(), workItemData.getState() );
    }
    catch ( WMWorkflowException e ) {
      rollbackTransaction();
      m_log.error( "Exception", e );
      if ( e.getCause() != null ) {
        m_log.error( "Cause", e.getCause() );
      }
      throw e;
    }
    catch ( RepositoryException e ) {
      rollbackTransaction();
      m_log.error( "Exception", e );
      if ( e.getCause() != null ) {
        m_log.error( "Cause", e.getCause() );
      }
      throw new WMWorkflowException( e );
    }
    finally {
      try {
        commitTransaction();
      }
      catch ( Exception e ) {
        m_log.error( "Exception", e );
      }
    }
  }

	  /**
	   * @ejb.interface-method 
	   * 
	   * @param workItemData
	   * @param newParticipant
	   * @throws WMWorkflowException
	   */
	  public void reassignWorkItem( WorkItemData workItemData, String targeUser )
		throws WMWorkflowException
	  {
		if (log.isDebugEnabled()) {
			log.debug("reassignWorkItem:"+workItemData + " to" + targeUser);
		}
		try {
		  beginTransaction();
	 	   m_worklistEngine.getWorkItemManager().reassignWorkItem(
		   workItemData.getParticipant(), 
		   targeUser,
		  workItemData.getId() );
		}
		catch ( WMWorkflowException e ) {
		  rollbackTransaction();
		  m_log.error( "Exception", e );
		  if ( e.getCause() != null ) {
			m_log.error( "Cause", e.getCause() );
		  }
		  throw e;
		}
		catch ( RepositoryException e ) {
		  rollbackTransaction();
		  m_log.error( "Exception", e );
		  if ( e.getCause() != null ) {
			m_log.error( "Cause", e.getCause() );
		  }
		  throw new WMWorkflowException( e );
		}
		finally {
		  try {
			commitTransaction();
		  }
		  catch ( Exception e ) {
			m_log.error( "Exception", e );
		  }
		}
  }

  /**
   * Create a new ZReihe SessionBean.
   *
   * @exception CreateException  on error
   *
   * @ejb.create-method
   */
  public void ejbCreate()
    throws CreateException
  {
    m_log.debug( "Create" );

    obtainServiceManager();
  }
  
	/**
	 * @ejb.interface-method
	 */
	public int indexOf(WorkItemData workItemData, String userName, String clientId,
			boolean onlyOpen, IFilter filter, String[] sortAttrs,
			boolean[] sortAscending) throws WMWorkflowException {
		if (m_log.isDebugEnabled()) {
			m_log.debug("listWorkItems: " + userName);
		}
		try {
			beginTransaction();
			final int indexOf = m_worklistEngine.getWorkItemManager().indexOf(workItemData,
					userName, clientId, onlyOpen, filter, sortAttrs, sortAscending);

			return indexOf;
		} catch (RepositoryException e) {
			rollbackTransaction();
			m_log.error("Exception", e);
			if (e.getCause() != null) {
				m_log.error("Cause", e.getCause());
			}
			throw new WMWorkflowException(e);
		} catch (WMWorkflowException e) {
			rollbackTransaction();
			m_log.error("Exception", e);
			if (e.getCause() != null) {
				m_log.error("Cause", e.getCause());
			}
			throw e;
		} finally {
			try {
				commitTransaction();
			} catch (Exception e) {
				m_log.error("Exception", e);
			}
		}
	}
}
