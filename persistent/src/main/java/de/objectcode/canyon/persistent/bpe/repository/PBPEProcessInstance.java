package de.objectcode.canyon.persistent.bpe.repository;

import de.objectcode.canyon.bpe.repository.ProcessInstance;
import de.objectcode.canyon.persistent.instance.PObjectValue;

/**
 * @hibernate.class table="PBPEPROCESSINSTS"
 *
 * @author    junglas
 * @created   21. Juni 2004
 */
public class PBPEProcessInstance
{
  protected  long          m_entityOid;
  protected  long          m_processEntityOid;
  protected  String        m_processId;
  protected  int           m_state;
  protected  PObjectValue  m_processInstanceData;
  protected  Long          m_parentEntityOid;


  /**
   *Constructor for the PBPEProcessInstance object
   */
  public PBPEProcessInstance() { }


  /**
   *Constructor for the PBPEProcessInstance object
   *
   * @param processInstance  Description of the Parameter
   */
  public PBPEProcessInstance( ProcessInstance processInstance )
  {
    m_processEntityOid = processInstance.getProcessEntityOid();
    m_processId = processInstance.getProcessId();
    m_state = processInstance.getState().getValue();
    m_processInstanceData = new PObjectValue( processInstance );
    String ppiid = processInstance.getParentProcessInstanceId();
    m_parentEntityOid = ppiid == null ? null : new Long(ppiid);
  }


  /**
   * Sets the processInstance attribute of the PBPEProcessInstance object
   *
   * @param processInstance  The new processInstance value
   */
  public void setProcessInstance( ProcessInstance processInstance )
  {
    m_state = processInstance.getState().getValue();
    m_processInstanceData.setValue( processInstance );
  }


  /**
   * @param entityOid  The entityOid to set.
   */
  public void setEntityOid( long entityOid )
  {
    m_entityOid = entityOid;
  }


  /**
   * @param processId  The processId to set.
   */
  public void setProcessId( String processId )
  {
    m_processId = processId;
  }


  /**
   * @param processInstanceData  The processInstanceData to set.
   */
  public void setProcessInstanceData( PObjectValue processInstanceData )
  {
    m_processInstanceData = processInstanceData;
  }


  /**
   * @param state  The state to set.
   */
  public void setState( int state )
  {
    m_state = state;
  }


  /**
   * @param processEntityOid  The processEntityOid to set.
   */
  public void setProcessEntityOid( long processEntityOid )
  {
    m_processEntityOid = processEntityOid;
  }


  /**
   * Gets the processInstance attribute of the PBPEProcessInstance object
   *
   * @return   The processInstance value
   */
  public ProcessInstance getProcessInstance()
  {
    return ( ProcessInstance ) m_processInstanceData.getValue();
  }


  /**
   * @hibernate.id generator-class="native" column="ENTITYID" type="long" unsaved-value="0"
   *
   * @return
   */
  public long getEntityOid()
  {
    return m_entityOid;
  }

  /**
   * @hibernate.property column="PARENTENTITYID" type="java.lang.Long"
   *
   * @return
   */
  public Long getParentEntityOid()
  {
    return m_parentEntityOid;
  }

  /**
   * @hibernate.many-to-one not-null="false" class="de.objectcode.canyon.persistent.instance.PObjectValue" column="BLOBID" cascade="all"
   *
   * @return   Returns the applicationDataBlob.
   */
  public PObjectValue getProcessInstanceData()
  {
    return m_processInstanceData;
  }



  /**
   * @hibernate.property type="integer" column="STATE" not-null="true"
   *
   * @return   Returns the state.
   */
  public int getState()
  {
    return m_state;
  }


  /**
   * @hibernate.property type="string" length="64" column="PROCESSID" not-null="true"
   *
   * @return   Returns the state.
   */
  public String getProcessId()
  {
    return m_processId;
  }


  public String getParentProcessInstanceId()
  {
  	if (m_parentEntityOid==null)
  		return null;
  	else
  		return String.valueOf(m_parentEntityOid);
  }

  /**
   ***
   * @hibernate.property type="long" column="PROCESS_OID" not-null="true"
   *
   * @return   Returns the state.
   */
  public long getProcessEntityOid()
  {
    return m_processEntityOid;
  }


	public void setParentProcessInstanceId(String parentProcessInstanceId) {
    m_parentEntityOid = parentProcessInstanceId == null ? null : new Long(parentProcessInstanceId);
	}

  public void setParentEntityOid(Long parentEntityOid)
  {
    m_parentEntityOid=parentEntityOid;
  }

	
	public void migrate(ProcessInstance processInstance) {
		setProcessInstance(processInstance);
    setParentProcessInstanceId(processInstance.getParentProcessInstanceId());		
	}
}
