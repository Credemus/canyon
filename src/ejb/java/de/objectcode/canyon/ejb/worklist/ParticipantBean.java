/*
 * Created on 24.03.2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package de.objectcode.canyon.ejb.worklist;

import javax.ejb.CreateException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wfmc.wapi.WMWorkflowException;

import de.objectcode.canyon.api.worklist.WorkItemData;
import de.objectcode.canyon.ejb.BaseServiceManagerBean;
import de.objectcode.canyon.model.participant.Participant;
import de.objectcode.canyon.model.process.WorkflowProcess;
import de.objectcode.canyon.spi.RepositoryException;
import de.objectcode.canyon.spi.instance.IProcessInstance;
import de.objectcode.canyon.spi.process.IProcessRepository;

/**
 * @ejb.bean name="Participant" type="Stateless"
 *           jndi-name="de/objectcode/canyon/ejb/worklist/Participant"
 *           local-jndi-name="de/objectcode/canyon/ejb/worklist/ParticipantLocal"
 *           transaction-type="Bean"
 *           view-type="both"
 * @ejb.permission unchecked="true"
 *
 * @ejb.resource-ref res-ref-name="ServiceManager" res-type="de.objectcode.canyon.spi.ServiceManager"
 *   res-auth="Application"
 * @jboss.resource-ref res-ref-name="ServiceManager" jndi-name="java:/canyon/ServiceManager"
 *
 * @author    ruth
 * @created   11. Dezember 2003
 */

public class ParticipantBean extends BaseServiceManagerBean {

	static final long serialVersionUID = -6867406868746595846L;
	
	private static Log log = LogFactory.getLog(ParticipantBean.class);


	/**
	 * @ejb.interface-method
	 *
	 * @param workItemData             Description of the Parameter
	 * @exception WMWorkflowException  Description of the Exception
	 */
	public String[] findParticipants( WorkItemData workItemData )
	  throws WMWorkflowException
	{

		if (log.isDebugEnabled()) {
			log.debug("findParticipants " + workItemData);
		}	  
				
	  try {
	  		  		    
			String workItemId = workItemData.getId();
	
			beginTransaction();
		
			String[] user	= m_serviceManager.getWorklistEngine().getWorkItemManager().findParticipants(workItemId);;
	
			if (log.isDebugEnabled()) {
				log.debug("found "+user.length+" user");
			}	  
			
			return user;
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
	 * @param workItemData             Description of the Parameter
	 * @exception WMWorkflowException  Description of the Exception
	 */
	public String[] findMembers( String processInstanceId, String participantId, String clientId )
	  throws WMWorkflowException
	{

		if (log.isDebugEnabled()) {
			log.debug("findMembers " + processInstanceId + "," + participantId + "," + clientId);
		}	  
				
	  try {
	  		  		    
			beginTransaction();
		
			Participant participant = new Participant();
			participant.setId(participantId);
			String[] user	= m_serviceManager.getParticipantRepository().findMembers(participant, clientId );
	
			if (log.isDebugEnabled()) {
				log.debug("found "+user.length+" user");
			}	  
			
			return user;
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
	
}
