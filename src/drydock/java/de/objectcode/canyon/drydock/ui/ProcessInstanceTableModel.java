package de.objectcode.canyon.drydock.ui;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.objectcode.canyon.bpe.engine.activities.ActivityState;
import de.objectcode.canyon.bpe.repository.IProcessInstanceRepository;
import de.objectcode.canyon.bpe.repository.IProcessInstanceVisitor;
import de.objectcode.canyon.bpe.repository.ProcessInstance;

/**
 * @author    junglas
 * @created   22. Juni 2004
 */
public class ProcessInstanceTableModel extends AbstractTableModel implements IProcessInstanceVisitor
{
  private final static  Log   log                     = LogFactory.getLog( ProcessInstanceTableModel.class );

  private IProcessInstanceRepository m_processInstanceRepository;
  private               List  m_processInstanceInfos;

  public ProcessInstanceTableModel ( IProcessInstanceRepository processInstanceRepository )
  {
    m_processInstanceRepository = processInstanceRepository;
    
    update();
  }
  
  public void update ()
  {
    try {
      m_processInstanceInfos = new ArrayList();
      
      m_processInstanceRepository.iterateProcessInstances ( false, this );
      
      fireTableDataChanged();
    }
    catch ( Exception e ) {
      log.error("Exception", e);
    }
  }

  /**
   * @see de.objectcode.canyon.bpe.repository.IProcessInstanceVisitor#visit(de.objectcode.canyon.bpe.repository.ProcessInstance)
   */
  public void visit (ProcessInstance processInstance)
  {
    m_processInstanceInfos.add(new ProcessInstanceInfo(processInstance.getProcessInstanceId(), 
        	processInstance.getProcessId(), processInstance.getState()));
  }

  public String getProcessInstanceId ( int row )
  {
    ProcessInstanceInfo info = (ProcessInstanceInfo)m_processInstanceInfos.get(row);
    
    return info.getProcessInstanceId();
  }
  
  /**
   * @param column  Description of the Parameter
   * @return        The columnName value
   * @see           javax.swing.table.TableModel#getColumnName(int)
   */
  public String getColumnName( int column )
  {
    switch ( column ) {
      case 0:
        return "Instance-Id";
      case 1:
        return "Process-Id";
      case 2:
        return "State";
    }
    return super.getColumnName( column );
  }


  /**
   * @return   The columnCount value
   * @see      javax.swing.table.TableModel#getColumnCount()
   */
  public int getColumnCount()
  {
    return 3;
  }


  /**
   * @return   The rowCount value
   * @see      javax.swing.table.TableModel#getRowCount()
   */
  public int getRowCount()
  {
    return m_processInstanceInfos.size();
  }


  /**
   * @param rowIndex     Description of the Parameter
   * @param columnIndex  Description of the Parameter
   * @return             The valueAt value
   * @see                javax.swing.table.TableModel#getValueAt(int, int)
   */
  public Object getValueAt( int rowIndex, int columnIndex )
  {
    ProcessInstanceInfo  info  = ( ProcessInstanceInfo ) m_processInstanceInfos.get( rowIndex );

    switch ( columnIndex ) {
      case 0:
        return info.getProcessInstanceId();
      case 1:
        return info.getProcessId();
      case 2:
        return info.getState().getTag();
    }
    return null;
  }


  /**
   * Description of the Class
   *
   * @author    junglas
   * @created   22. Juni 2004
   */
  private class ProcessInstanceInfo
  {
    private  String  m_processInstanceId;
    private  String  m_processId;
    private  ActivityState m_state;


    /**
     *Constructor for the ProcessInstanceInfo object
     *
     * @param processInstanceId  Description of the Parameter
     * @param processId          Description of the Parameter
     */
    ProcessInstanceInfo( String processInstanceId, String processId, ActivityState state )
    {
      m_processInstanceId = processInstanceId;
      m_processId = processId;
      m_state = state;
    }


    /**
     * @return Returns the state.
     */
    public ActivityState getState ( )
    {
      return m_state;
    }
    /**
     * @return   Returns the processId.
     */
    public String getProcessId()
    {
      return m_processId;
    }


    /**
     * @return   Returns the processInstanceId.
     */
    public String getProcessInstanceId()
    {
      return m_processInstanceId;
    }
  }
}
