/*
 * Created on 26.03.2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package de.objectcode.canyon.jmx.admin;

import javax.naming.InitialContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.objectcode.canyon.spi.ServiceManager;

/**
 * @jmx.mbean name="canyon:service=ParticipantAdmin" description="Participant administration bean"
 * @jboss.service servicefile="jboss" 
 * @jboss.depends object-name="canyon:service=ServiceManager"
 * @jboss.xmbean
 * 
 * @author    Ruth
 * @created   26.03.2004
 */
public class ParticipantAdmin extends BaseAdmin implements ParticipantAdminMBean {

	private final static  Log             log         = LogFactory.getLog( ParticipantAdmin.class );

	private               boolean         m_started;


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
	 * @jmx.managed-operation
	 * 
	 * @exception Exception  Description of the Exception
	 */
	public void start()
	  throws Exception
	{
	  log.info( "Starting ParticipantAdmin" );

	  InitialContext  ctx  = new InitialContext();

	  m_svcMgr = ( ServiceManager ) ctx.lookup( m_jndiName );

	  m_started = true;
	}


	/**
	 * @jmx.managed-operation
	 * 
	 * @exception Exception  Description of the Exception
	 */
	public void stop()
	  throws Exception
	{
	  log.info( "Stopping ParticipantAdmin" );
	  m_svcMgr = null;
	  m_started = false;
	}

	/**
	 * Description of the Method
	 * 
	 * @jmx.managed-operation description="List all users for workitem and processinstance for reassign"
	 * @jmx.managed-parameter name="workItemId" type="java.lang.String" description="Workitem Id"
	 *	 
	 * @param processDefinitionId  Description of the Parameter
	 * @return                     Description of the Return Value
	 * @exception Exception        Description of the Exception
	 */
	public String getUser( String workItemId )
	  throws Exception
	{
	  try {
		beginTransaction();
	  
	  StringBuffer      list              = new StringBuffer();

	  String[] user	= m_svcMgr.getWorklistEngine().getWorkItemManager().findParticipants(workItemId);

	  list.append( "<h1>User for workitem &quot;" );
	  list.append( workItemId );
	  list.append( "&quot;</h1>" );
	  list.append( "<table>" );
	  list.append( "<tr><th>User</th></tr>" );

	  int               i;

	  for ( i = 0; i < user.length; i++ ) {
		list.append( "<tr>" );
		list.append( "<td>" ).append(user[i]).append( "</td>" );
		list.append( "</tr>" );
	  }
	  list.append("</table>");

	  return list.toString();
	  }
	  finally {
		  endTransaction();
	  }
	  }


}
