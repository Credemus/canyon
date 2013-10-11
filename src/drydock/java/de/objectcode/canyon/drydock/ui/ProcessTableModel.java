package de.objectcode.canyon.drydock.ui;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.objectcode.canyon.bpe.engine.activities.BPEProcess;
import de.objectcode.canyon.bpe.repository.IProcessRepository;
import de.objectcode.canyon.bpe.repository.IProcessVisitor;

/**
 * @author    junglas
 * @created   22. Juni 2004
 */
public class ProcessTableModel extends AbstractTableModel implements IProcessVisitor
{
  private final static  Log                 log                  = LogFactory.getLog( ProcessTableModel.class );

  private               IProcessRepository  m_processRepository;
  private               List                m_processInfos;


  /**
   *Constructor for the ProcessTableModel object
   *
   * @param processRepository  Description of the Parameter
   */
  public ProcessTableModel( IProcessRepository processRepository )
  {
    m_processRepository = processRepository;

    update();
  }


  /**
   * @return   The columnCount value
   * @see      javax.swing.table.TableModel#getColumnCount()
   */
  public int getColumnCount()
  {
    return 2;
  }


  /**
   * @return   The rowCount value
   * @see      javax.swing.table.TableModel#getRowCount()
   */
  public int getRowCount()
  {
    return m_processInfos.size();
  }


  /**
   * @param rowIndex     Description of the Parameter
   * @param columnIndex  Description of the Parameter
   * @return             The valueAt value
   * @see                javax.swing.table.TableModel#getValueAt(int, int)
   */
  public Object getValueAt( int rowIndex, int columnIndex )
  {
    ProcessInfo  info  = ( ProcessInfo ) m_processInfos.get( rowIndex );
    switch ( columnIndex ) {
      case 0:
        return info.getProcessId();
      case 1:
        return info.getProcessName();
    }
    return null;
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
        return "Id";
      case 1:
        return "Name";
    }
    return super.getColumnName( column );
  }


  /**
   * Description of the Method
   */
  public void update()
  {
    try {
      m_processInfos = new ArrayList();
      m_processRepository.iterateProcesses( this , false);
      
      fireTableDataChanged();
    }
    catch ( Exception e ) {
      log.error( "Exception", e );
    }
  }

  public String getProcessId ( int row )
  {
    ProcessInfo info = (ProcessInfo)m_processInfos.get(row);
    
    return info.getProcessId();
  }

  /**
   * @param process  Description of the Parameter
   * @see            de.objectcode.canyon.bpe.repository.IProcessVisitor#visit(de.objectcode.canyon.bpe.engine.activities.BPEProcess)
   */
  public void visit( BPEProcess process, Serializable processSource )
  {
    m_processInfos.add( new ProcessInfo( process.getId(), process.getName() ) );
  }


  /**
   * Description of the Class
   *
   * @author    junglas
   * @created   22. Juni 2004
   */
  private static class ProcessInfo
  {
    private  String  m_processId;
    private  String  m_processName;


    /**
     *Constructor for the ProcessInfo object
     *
     * @param processId    Description of the Parameter
     * @param processName  Description of the Parameter
     */
    ProcessInfo( String processId, String processName )
    {
      m_processId = processId;
      m_processName = processName;
    }


    /**
     * @return   Returns the processId.
     */
    public String getProcessId()
    {
      return m_processId;
    }


    /**
     * @return   Returns the processName.
     */
    public String getProcessName()
    {
      return m_processName;
    }
  }
}
