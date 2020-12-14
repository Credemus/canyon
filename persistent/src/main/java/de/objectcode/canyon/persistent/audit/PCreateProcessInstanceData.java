package de.objectcode.canyon.persistent.audit;

/**
 * Conforms to WfMC CreateProcessInstanceData.
 * 
 * @hibernate.subclass
 * 
 * @author junglas
 */
public class PCreateProcessInstanceData extends PAuditObject
{
  private String m_processDefinitionId;
  private String m_processDefinitionBusinessName;

  /**
   * @hibernate.property column="PROCESSDEFINITIONNAME" length="64"
   * 
   * @return
   */
  public String getProcessDefinitionBusinessName() {
    return m_processDefinitionBusinessName;
  }

  /**
   * @hibernate.property column="PROCESSDEFINITIONID" length="64"
   * 
   * @return
   */
  public String getProcessDefinitionId() {
    return m_processDefinitionId;
  }

  /**
   * @param string
   */
  public void setProcessDefinitionBusinessName(String string) {
    m_processDefinitionBusinessName = string;
  }

  /**
   * @param string
   */
  public void setProcessDefinitionId(String string) {
    m_processDefinitionId = string;
  }

}
