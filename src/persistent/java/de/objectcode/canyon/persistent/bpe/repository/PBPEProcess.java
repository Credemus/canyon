package de.objectcode.canyon.persistent.bpe.repository;

import de.objectcode.canyon.bpe.engine.activities.BPEProcess;
import de.objectcode.canyon.persistent.instance.PImmutableObjectValue;

import java.io.Serializable;

/**
 * @hibernate.class table="PBPEPROCESSES"
 *
 * @author    junglas
 * @created   9. Juni 2004
 */
public class PBPEProcess
{
  protected  long          m_entityOid;
  protected  Long					 m_packageRevisionOid; 			
  protected  String        m_processId;
  protected  String		   m_processVersion;
  protected  PImmutableObjectValue  m_processBlob;
  protected  PImmutableObjectValue  m_processSourceBlob;
  protected  int           m_state=1;


  /**
   *Constructor for the PBPEProcess object
   */
  public PBPEProcess() { }


  /**
   *Constructor for the PBPEProcess object
   *
   * @param process        Description of the Parameter
   * @param processSource  Description of the Parameter
   */
  public PBPEProcess( long packageRevisionOid, BPEProcess process, Serializable processSource )
  {
  	m_packageRevisionOid = new Long(packageRevisionOid); 
    m_processBlob = new PImmutableObjectValue( process );
    m_processSourceBlob = new PImmutableObjectValue( processSource );
    m_processId = process.getId();
    m_processVersion = process.getVersion();
    m_state = process.getState().getValue();
  }


  /**
   * @param entityOid  The entityOid to set.
   */
  public void setEntityOid( long entityOid )
  {
    m_entityOid = entityOid;
  }

  public void setPackageRevisionOid( Long packageRevisionOid )
  {
    m_packageRevisionOid = packageRevisionOid;
  }

  /**
   * @param processSourceBlob  The processSourceBlob to set.
   */
  public void setProcessSourceBlob( PImmutableObjectValue processSourceBlob )
  {
    m_processSourceBlob = processSourceBlob;
  }


  /**
   * @param processId  The new processId value
   */
  public void setProcessId( String processId )
  {
    m_processId = processId;
  }

  /**
   * @param processVersion  The new processVersion value
   */
  public void setProcessVersion( String processVersion )
  {
    m_processVersion = processVersion;
  }

  
  /**
   * @param processBlob  The processBlob to set.
   */
  public void setProcessBlob( PImmutableObjectValue processBlob )
  {
    m_processBlob = processBlob;
  }


  /**
   * Sets the process attribute of the PBPEProcess object
   *
   * @param process        The new process value
   * @param processSource  The new process value
   */
//  public void setProcess( BPEProcess process, Serializable processSource )
//  {
//    m_processBlob.setValue( process );
//    m_processSourceBlob.setValue( processSource );
//    m_state = process.getState().getValue();
//  }


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
   * @hibernate.property column="PACKAGE_REV_OID" type="java.lang.Long"
   *
   * @return
   */
  public Long getPackageRevisionOid()
  {
    return m_packageRevisionOid;
  }

  /**
   * @hibernate.many-to-one not-null="false" class="de.objectcode.canyon.persistent.instance.PImmutableObjectValue" column="SOURCEBLOBID" cascade="all"
   *
   * @return   Returns the processSourceBlob.
   */
  public PImmutableObjectValue getProcessSourceBlob()
  {
    return m_processSourceBlob;
  }


  /**
   * Gets the process attribute of the PBPEProcess object
   *
   * @return   The process value
   */
  public BPEProcess getProcess()
  {
    BPEProcess  process  = ( BPEProcess ) m_processBlob.getValue();

    process.setProcessEntityOid(m_entityOid);
    
    return process;
  }


  /**
   * @hibernate.property type="string" length="64" column="PROCESSID" not-null="true"
   *
   * @return
   */
  public String getProcessId()
  {
    return m_processId;
  }

  /**
   * @hibernate.property type="string" length="64" column="PROCESSVERSION" not-null="true"
   *
   * @return
   */
  public String getProcessVersion()
  {
    return m_processVersion;
  }

  
  /**
   * @hibernate.many-to-one not-null="false" class="de.objectcode.canyon.persistent.instance.PImmutableObjectValue" column="BLOBID" cascade="all"
   *
   * @return   Returns the applicationDataBlob.
   */
  public PImmutableObjectValue getProcessBlob()
  {
    return m_processBlob;
  }

  /**
   * @hibernate.property type="integer" column="STATE" not-null="true"
   *
   * @return
   */
	public int getState() {
		return m_state;
	}


	public void setState(int state) {
		m_state = state;
	}

}
