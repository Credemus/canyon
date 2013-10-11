package de.objectcode.canyon.drydock.ui;

import de.objectcode.canyon.drydock.worklist.IWorklistChangeListener;
import de.objectcode.canyon.drydock.worklist.WorkItem;
import de.objectcode.canyon.drydock.worklist.Worklist;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author    junglas
 * @created   15. Juni 2004
 */
public class WorklistFrame extends JFrame
{
  private final static  Log                   log                     = LogFactory.getLog( WorklistFrame.class );

  private               Worklist              m_worklist;
  private               JTable                m_worklistTable;
  private               WorklistTableModel    m_worklistTableModel;
  private               JTextField            m_idTextField           = new JTextField();
  private               JTextField            m_activityIdTextField   = new JTextField();
  private               JTextField            m_processIdTextField    = new JTextField();
  private               JTextField            m_nameTextField         = new JTextField();
  private               JScrollPane           m_applicationDataPane   = new JScrollPane();
  private               ApplicationDataPanel  m_applicationDataPanel  = null;


  /**
   *Constructor for the WorklistFrame object
   *
   * @param worklist  Description of the Parameter
   */
  public WorklistFrame( Worklist worklist )
  {
    super( "Worklist" );

    m_worklist = worklist;
    m_worklistTableModel = new WorklistTableModel();
    m_worklist.addWorklistChangeListener( m_worklistTableModel );
    m_worklistTable = new JTable( m_worklistTableModel );
    m_worklistTable.getSelectionModel().addListSelectionListener( new WorklistTableSelectionListener() );

    initComponents();
  }


  /**
   * Description of the Method
   *
   * @param workItem  Description of the Parameter
   */
  public void updateDetail( WorkItem workItem )
  {
    m_idTextField.setText( workItem.getWorkItemId() );
    m_processIdTextField.setText( workItem.getProcessId() );
    m_activityIdTextField.setText( workItem.getActivityId() );
    m_nameTextField.setText( workItem.getName() );

    try {
      if ( workItem.getApplicationData() != null && workItem.getApplicationData().length>0 ) {
        m_applicationDataPanel = new ApplicationDataPanel( workItem.getApplicationData()[0] );

        m_applicationDataPane.setViewportView( m_applicationDataPanel );

        pack();
      } else {
        m_applicationDataPanel = null;
        m_applicationDataPane.setViewportView( new JLabel() );

        pack();
      }
    }
    catch ( Exception e ) {
      log.error( "Exception", e );
    }
  }


  /**
   * Description of the Method
   */
  protected void initComponents()
  {
    JPanel       root            = new JPanel( new GridBagLayout() );

    getContentPane().add( root, BorderLayout.CENTER );

    JScrollPane  workItemScroll  = new JScrollPane( m_worklistTable );

    root.add( workItemScroll,
        new GridBagConstraints( 0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets( 2, 2, 2, 2 ), 0, 0 ) );

    JPanel       detailPanel     = new JPanel( new GridBagLayout() );

    root.add( detailPanel,
        new GridBagConstraints( 0, 1, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets( 2, 2, 2, 2 ), 0, 0 ) );

    detailPanel.add( new JLabel( "Id" ),
        new GridBagConstraints( 0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets( 2, 2, 2, 2 ), 0, 0 ) );
    detailPanel.add( m_idTextField,
        new GridBagConstraints( 1, 0, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets( 2, 2, 2, 2 ), 0, 0 ) );

    detailPanel.add( new JLabel( "Process-Id" ),
        new GridBagConstraints( 0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets( 2, 2, 2, 2 ), 0, 0 ) );
    detailPanel.add( m_processIdTextField,
        new GridBagConstraints( 1, 1, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets( 2, 2, 2, 2 ), 0, 0 ) );

    detailPanel.add( new JLabel( "Activity-Id" ),
        new GridBagConstraints( 0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets( 2, 2, 2, 2 ), 0, 0 ) );
    detailPanel.add( m_activityIdTextField,
        new GridBagConstraints( 1, 2, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets( 2, 2, 2, 2 ), 0, 0 ) );

    detailPanel.add( new JLabel( "Name" ),
        new GridBagConstraints( 0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets( 2, 2, 2, 2 ), 0, 0 ) );
    detailPanel.add( m_nameTextField,
        new GridBagConstraints( 1, 3, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets( 2, 2, 2, 2 ), 0, 0 ) );

    m_applicationDataPane.setViewportView( new JLabel() );
    detailPanel.add( m_applicationDataPane,
        new GridBagConstraints( 0, 4, 2, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets( 2, 2, 2, 2 ), 0, 0 ) );

    JButton      completeButton  = new JButton( "Complete" );

    completeButton.addActionListener( new CompleteButtonActionListener() );
    root.add( completeButton,
        new GridBagConstraints( 0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets( 2, 2, 2, 2 ), 0, 0 ) );

    pack();
  }


  /**
   * Description of the Class
   *
   * @author    junglas
   * @created   21. Juni 2004
   */
  private class CompleteButtonActionListener implements ActionListener
  {

    /**
     * @param e  Description of the Parameter
     * @see      java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed( ActionEvent e )
    {
      int  index  = m_worklistTable.getSelectedRow();

      if ( index >= 0 ) {
        if ( m_applicationDataPanel != null ) {
          m_applicationDataPanel.updateData();
        }
        m_worklist.completeWorkItem( index );
      }
    }
  }


  /**
   * Description of the Class
   *
   * @author    junglas
   * @created   21. Juni 2004
   */
  private class WorklistTableSelectionListener implements ListSelectionListener
  {
    /**
     * @param e  Description of the Parameter
     * @see      javax.swing.event.ListSelectionListener#valueChanged(javax.swing.event.ListSelectionEvent)
     */
    public void valueChanged( ListSelectionEvent e )
    {
      int  idx  = m_worklistTable.getSelectedRow();

      if ( idx >= 0 ) {
        WorkItem  workItem  = m_worklist.getWorkItem( idx );

        updateDetail( workItem );
      }
    }
  }


  /**
   * Description of the Class
   *
   * @author    junglas
   * @created   15. Juni 2004
   */
  private class WorklistTableModel extends AbstractTableModel implements IWorklistChangeListener
  {

    /**
     * @param column  Description of the Parameter
     * @return        The columnName value
     * @see           javax.swing.table.TableModel#getColumnName(int)
     */
    public String getColumnName( int column )
    {
      switch ( column ) {
        case 0:
          return "Status";
        case 1:
          return "Name";
        case 2:
          return "Performer";
        case 3:
          return "Process-ID";
        case 4:
          return "Activity-ID";
      }
      return super.getColumnName( column );
    }


    /**
     * @return   The columnCount value
     * @see      javax.swing.table.TableModel#getColumnCount()
     */
    public int getColumnCount()
    {
      return 4;
    }


    /**
     * @return   The rowCount value
     * @see      javax.swing.table.TableModel#getRowCount()
     */
    public int getRowCount()
    {
      return m_worklist.getWorkItemCount();
    }


    /**
     * @param rowIndex     Description of the Parameter
     * @param columnIndex  Description of the Parameter
     * @return             The valueAt value
     * @see                javax.swing.table.TableModel#getValueAt(int, int)
     */
    public Object getValueAt( int rowIndex, int columnIndex )
    {
      WorkItem  workItem  = m_worklist.getWorkItem( rowIndex );
      switch ( columnIndex ) {
        case 0:
          return new Integer( workItem.getStatus() );
        case 1:
          return workItem.getName();
        case 2:
          return workItem.getPerformer();
        case 3:
          return workItem.getProcessId();
        case 4:
          return workItem.getActivityId();
      }
      return "";
    }


    /**
     * @param workItem  Description of the Parameter
     * @see             de.objectcode.canyon.drydock.worklist.IWorklistChangeListener#workItemCompleted(de.objectcode.canyon.drydock.worklist.WorkItem)
     */
    public void workItemCompleted( WorkItem workItem )
    {
      fireTableDataChanged();
    }


    /**
     * @see   de.objectcode.canyon.drydock.worklist.IWorklistChangeListener#worklistChanged()
     */
    public void worklistChanged()
    {
      fireTableDataChanged();
    }
  }
}
