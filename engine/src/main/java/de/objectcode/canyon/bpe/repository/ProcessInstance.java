package de.objectcode.canyon.bpe.repository;

import de.objectcode.canyon.bpe.engine.activities.ActivityState;

import java.io.Serializable;

/**
 * @author junglas
 * @created 21. Juni 2004
 */
public class ProcessInstance implements Serializable {
  /**
   *
   */
  private static final long serialVersionUID = 2654896981881958642L;

  private long m_processEntityOid;
  private String m_processId;
  private String m_processInstanceId;
  private String m_parentProcessInstanceId;
  private ActivityState m_state;
  private byte[] m_processState;


  /**
   * Constructor for the ProcessInstance object
   *
   * @param processId         Description of the Parameter
   * @param processInstanceId Description of the Parameter
   * @param processState      Description of the Parameter
   * @param state             Description of the Parameter
   */
  public ProcessInstance(long processEntityOid, String processId, String processInstanceId, String parentProcessInstanceId, ActivityState state, byte[] processState) {
    m_processEntityOid = processEntityOid;
    m_processId = processId;
    m_processInstanceId = processInstanceId;
    m_state = state;
    m_processState = processState;
    m_parentProcessInstanceId = parentProcessInstanceId;
  }


  /**
   * @return Returns the processEntityOid.
   */
  public long getProcessEntityOid() {
    return m_processEntityOid;
  }

  /**
   * @param processInstanceId The processInstanceId to set.
   */
  public void setProcessInstanceId(String processInstanceId) {
    m_processInstanceId = processInstanceId;
  }


  /**
   * @return Returns the state.
   */
  public ActivityState getState() {
    return m_state;
  }


  /**
   * @return Returns the processId.
   */
  public String getProcessId() {
    return m_processId;
  }


  /**
   * @return Returns the processInstanceId.
   */
  public String getProcessInstanceId() {
    return m_processInstanceId;
  }


  /**
   * @return Returns the processState.
   */
  public byte[] getProcessState() {
    return m_processState;
  }


  public String getParentProcessInstanceId() {
    return m_parentProcessInstanceId;
  }


  public void setParentProcessInstanceId(String parentProcessInstanceId) {
    m_parentProcessInstanceId = parentProcessInstanceId;
  }


  public void setProcessId(String processId) {
    m_processId = processId;
  }


  public void setProcessState(byte[] processState) {
    m_processState = processState;
  }


  public void setState(ActivityState state) {
    m_state = state;
  }
}
