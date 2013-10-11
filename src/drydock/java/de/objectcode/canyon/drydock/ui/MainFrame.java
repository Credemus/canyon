package de.objectcode.canyon.drydock.ui;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.StringWriter;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.io.DocumentSource;

import de.objectcode.canyon.bpe.engine.BPEEngine;
import de.objectcode.canyon.bpe.engine.BPERuntimeContext;
import de.objectcode.canyon.bpe.engine.activities.BPEProcess;
import de.objectcode.canyon.bpe.engine.correlation.Message;
import de.objectcode.canyon.bpe.engine.correlation.MessageType;
import de.objectcode.canyon.bpe.engine.variable.ComplexType;
import de.objectcode.canyon.bpe.factory.xpdl.XPDLProcessFactory;
import de.objectcode.canyon.bpe.util.DomSerializer;
import de.objectcode.canyon.drydock.bpe.repository.MemProcessInstanceRepository;
import de.objectcode.canyon.drydock.bpe.repository.MemProcessRepository;
import de.objectcode.canyon.drydock.bpe.repository.SimpleAsyncManager;
import de.objectcode.canyon.drydock.worklist.SimpleWorklistEngine;
import de.objectcode.canyon.drydock.worklist.Worklist;
import de.objectcode.canyon.model.WorkflowPackage;
import de.objectcode.canyon.model.process.WorkflowProcess;
import de.objectcode.canyon.spiImpl.lock.LockManager;
import de.objectcode.canyon.spiImpl.parser.DefaultXPDLParser;

/**
 * @author    junglas
 * @created   14. Juni 2004
 */
public class MainFrame extends JFrame
{
  private final static  Log                        log                          = LogFactory.getLog( MainFrame.class );

  private               BPEEngine                  m_bpeEngine;
  private               SimpleWorklistEngine       m_worklistEngine;
  private               SimpleAsyncManager         m_asyncManager;
  private               Worklist                   m_worklist;
  private               JTextArea                  m_bpeStatusTextArea          = new JTextArea( 20, 80 );
  private               JEditorPane                m_bpeStatusEditorPane        = new JEditorPane( "text/html", "<html></html>" );
  private               JTable                     m_processInstanceTable;
  private               ProcessInstanceTableModel  m_processInstanceTableModel;
  private               JTable                     m_processTable;
  private               ProcessTableModel          m_processTableModel;


  /**
   *Constructor for the MainFrame object
   */
  public MainFrame()
  {
    super( "Canyon" );

    m_worklist = new Worklist();
    m_worklistEngine = new SimpleWorklistEngine( m_worklist );
    m_asyncManager = new SimpleAsyncManager();
    m_bpeEngine = new BPEEngine();
    m_bpeEngine.setLockManager(new LockManager());
    m_bpeEngine.setProcessRepository( new MemProcessRepository() );
    m_bpeEngine.setProcessInstanceRepository( new MemProcessInstanceRepository() );
    m_bpeEngine.setWorklistEngine( m_worklistEngine );
    m_bpeEngine.setAsyncManager( m_asyncManager );

    m_processTableModel = new ProcessTableModel( m_bpeEngine.getProcessRepository() );
    m_processInstanceTableModel = new ProcessInstanceTableModel( m_bpeEngine.getProcessInstanceRepository() );

    addWindowListener( new MainFrameListener() );

    initComponents();
  }



  /**
   * @param b  The new visible value
   * @see      java.awt.Component#setVisible(boolean)
   */
  public void setVisible( boolean b )
  {
    super.setVisible( b );

    if ( b ) {
      WorklistFrame  worklistFrame  = new WorklistFrame( m_worklist );

      worklistFrame.setVisible( true );
    }
  }


  /**
   * Description of the Method
   */
  protected void initComponents()
  {
    JPanel       root                       = new JPanel( new BorderLayout() );

    getContentPane().add( root, BorderLayout.CENTER );

    JSplitPane   splitPane                  = new JSplitPane( JSplitPane.VERTICAL_SPLIT );

    root.add( splitPane, BorderLayout.CENTER );

    JPanel       top                        = new JPanel( new GridBagLayout() );
    splitPane.setTopComponent( top );
    JPanel       bottom                     = new JPanel( new GridBagLayout() );
    splitPane.setBottomComponent( bottom );

    m_processTable = new JTable( m_processTableModel );
    JScrollPane  processScrollPane          = new JScrollPane( m_processTable );

    top.add( processScrollPane,
        new GridBagConstraints( 0, 0, 3, 1, 1.0, 0.5, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets( 2, 2, 2, 2 ), 0, 0 ) );

    m_processInstanceTable = new JTable( m_processInstanceTableModel );
    JScrollPane  processInstanceScrollPane  = new JScrollPane( m_processInstanceTable );

    top.add( processInstanceScrollPane,
        new GridBagConstraints( 3, 0, 3, 1, 1.0, 0.5, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets( 2, 2, 2, 2 ), 0, 0 ) );

    JButton      xpdlLoadButton             = new JButton( "XPDL Load" );

    xpdlLoadButton.addActionListener( new XPDLLoadButtonActionListener() );
    top.add( xpdlLoadButton,
        new GridBagConstraints( 0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets( 2, 2, 2, 2 ), 0, 0 ) );

    JButton      startButton                = new JButton( "Start Process" );

    startButton.addActionListener( new StartButtonActionListener() );
    top.add( startButton,
        new GridBagConstraints( 1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets( 2, 2, 2, 2 ), 0, 0 ) );

    JButton      showButton                 = new JButton ( "Show Process" );
    
    showButton.addActionListener( new ShowButtonActionListener() );
    top.add( showButton,
        new GridBagConstraints( 2, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets( 2, 2, 2, 2 ), 0, 0 ) );
    
    JButton      updateListButton           = new JButton( "Update Lists" );
    updateListButton.addActionListener( new UpdateListButtonActionListener() );
    top.add( updateListButton,
        new GridBagConstraints( 3, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets( 2, 2, 2, 2 ), 0, 0 ) );

    JButton      updateStatusButton         = new JButton( "Update Status" );

    updateStatusButton.addActionListener( new UpdateStatusButtonActionListener() );
    top.add( updateStatusButton,
        new GridBagConstraints( 4, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets( 2, 2, 2, 2 ), 0, 0 ) );

    JButton      executeAsyncButton         = new JButton( "Execute Async" );

    executeAsyncButton.addActionListener( new ExecuteAsyncButtonActionListener() );
    top.add( executeAsyncButton,
        new GridBagConstraints( 5, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets( 2, 2, 2, 2 ), 0, 0 ) );

    JTabbedPane  statusTab                  = new JTabbedPane();

    bottom.add( statusTab,
        new GridBagConstraints( 0, 2, 3, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets( 2, 2, 2, 2 ), 0, 0 ) );

    JScrollPane  bpeStatusScrollPanel       = new JScrollPane( m_bpeStatusTextArea );

    statusTab.add( "XML", bpeStatusScrollPanel );
    statusTab.add( "HTML", new JScrollPane( m_bpeStatusEditorPane ) );

    pack();
  }


  /**
   * Description of the Method
   *
   * @param xpdlFile  Description of the Parameter
   */
  public void loadXPDL( File xpdlFile )
  {
    try {
      DefaultXPDLParser   parser           = new DefaultXPDLParser();

      WorkflowPackage     workflowPackage  = parser.parse( new FileInputStream( xpdlFile ) );

      workflowPackage.validate();
      long packageReleaseOid = m_bpeEngine.createPackageRevision(workflowPackage.getId(),workflowPackage.findPackageVersion());

      StringBuffer        buffer           = new StringBuffer();

      WorkflowProcess     processes[]        = workflowPackage.getWorkflowProcesses();
      int                 i;

      TransformerFactory  factory          = TransformerFactory.newInstance();

      Transformer         transformer      = factory.newTransformer( new StreamSource( getClass().getResourceAsStream( "/de/objectcode/canyon/jmx/bpe/admin/process-status.xsl" ) ) );

      for ( i = 0; i < processes.length; i++ ) {
        XPDLProcessFactory  xpdlFactory  = new XPDLProcessFactory( processes[i] );

        BPEProcess          process      = xpdlFactory.createProcess();

        m_bpeEngine.createProcess( packageReleaseOid, process, processes[i] );

        StringWriter        transformed  = new StringWriter();
        transformer.transform( new DocumentSource( DomSerializer.toDocument( process ) ),
            new StreamResult( transformed ) );

        m_bpeStatusEditorPane.setText( transformed.toString() );

        buffer.append( DomSerializer.toString( process ) );
      }

      m_bpeStatusTextArea.setText( buffer.toString() );
      m_processTableModel.update();
    }
    catch ( Exception ex ) {
      log.error( "Exception", ex );
    }

  }


  /**
   * Description of the Method
   */
  public void updateStatus()
  {
    try {
      int                 idx          = m_processInstanceTable.getSelectedRow();

      TransformerFactory  factory      = TransformerFactory.newInstance();

      Transformer         transformer  = factory.newTransformer( new StreamSource( getClass().getResourceAsStream( "/de/objectcode/canyon/jmx/bpe/admin/process-status.xsl" ) ) );

      if ( idx >= 0 ) {
        BPEProcess    process      = m_bpeEngine.getProcessInstance( m_processInstanceTableModel.getProcessInstanceId( idx ) );

        m_bpeStatusTextArea.setText( DomSerializer.toString( process ) );

        StringWriter  transformed  = new StringWriter();
        transformer.transform( new DocumentSource( DomSerializer.toDocument( process ) ),
            new StreamResult( transformed ) );

        m_bpeStatusEditorPane.setText( transformed.toString() );
      }
    }
    catch ( Exception e ) {
      log.error( "Exception", e );
    }
  }


  /**
   * Description of the Method
   */
  public void updateLists()
  {
    m_processTableModel.update();
    m_processInstanceTableModel.update();
  }


  /**
   * Description of the Class
   *
   * @author    junglas
   * @created   14. Juni 2004
   */
  private class XPDLLoadButtonActionListener implements ActionListener
  {
    /**
     * @param e  Description of the Parameter
     * @see      java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed( ActionEvent e )
    {
      JFileChooser  fileChooser  = new JFileChooser();

      if ( fileChooser.showOpenDialog( MainFrame.this ) == JFileChooser.APPROVE_OPTION ) {
        loadXPDL( fileChooser.getSelectedFile() );
      }
    }
  }


  /**
   * Description of the Class
   *
   * @author    junglas
   * @created   15. Juni 2004
   */
  private class StartButtonActionListener implements ActionListener
  {

    /**
     * @param e  Description of the Parameter
     * @see      java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed( ActionEvent e )
    {
      try {
        int  idx  = m_processTable.getSelectedRow();

        if ( idx >= 0 ) {
          String      processId     = m_processTableModel.getProcessId( idx );

          BPEProcess  process       = m_bpeEngine.getProcessRepository().getProcess( processId );
          List        messageTypes  = process.getCreateInstanceOperations();

          if ( !messageTypes.isEmpty() ) {
            String messageOperation = ((MessageType)messageTypes.get(0)).getMessageOperation();
            ComplexType type = ((MessageType)messageTypes.get(0)).getContentType();
                        
            StartProcessParameterDialog parameterDialog = new StartProcessParameterDialog(MainFrame.this, type );
            
            parameterDialog.setVisible(true);
            
            if ( parameterDialog.getValue() != null ) {
              Message message = new Message( messageOperation, parameterDialog.getValue() );
              
              m_bpeEngine.handleMessage(parameterDialog.getBPERuntimeContext(), message);
            }
          } else {
            ComplexType  type     = new ComplexType( "flow-request" );
            Message      message  = new Message( m_processTableModel.getProcessId( idx ) + "-init", type );

            m_bpeEngine.handleMessage( new BPERuntimeContext(null,null), message );
          }
          m_processInstanceTableModel.update();
        }
      }
      catch ( Exception ex ) {
        log.error( "Exception", ex );
      }
    }
  }

  /**
   * Description of the Class
   *
   * @author    junglas
   * @created   15. Juni 2004
   */
  private class ShowButtonActionListener implements ActionListener
  {

    /**
     * @param e  Description of the Parameter
     * @see      java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed( ActionEvent e )
    {
      try {
        int  idx  = m_processTable.getSelectedRow();

        if ( idx >= 0 ) {
          TransformerFactory  factory      = TransformerFactory.newInstance();

          Transformer         transformer  = factory.newTransformer( new StreamSource( getClass().getResourceAsStream( "/de/objectcode/canyon/jmx/bpe/admin/process-status.xsl" ) ) );

          BPEProcess    process      = m_bpeEngine.getProcessRepository().getProcess( m_processTableModel.getProcessId(idx) );

          m_bpeStatusTextArea.setText( DomSerializer.toString( process ) );

          StringWriter  transformed  = new StringWriter();
          transformer.transform( new DocumentSource( DomSerializer.toDocument( process ) ),
              new StreamResult( transformed ) );

          m_bpeStatusEditorPane.setText( transformed.toString() );
        }
      }
      catch ( Exception ex ) {
        log.error( "Exception", ex );
      }
    }
  }


  /**
   * Description of the Class
   *
   * @author    junglas
   * @created   15. Juni 2004
   */
  private class UpdateStatusButtonActionListener implements ActionListener
  {

    /**
     * @param e  Description of the Parameter
     * @see      java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed( ActionEvent e )
    {
      updateStatus();
    }
  }


  /**
   * Description of the Class
   *
   * @author    junglas
   * @created   16. Juli 2004
   */
  private class ExecuteAsyncButtonActionListener implements ActionListener
  {
    /**
     * @param e  Description of the Parameter
     * @see      java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed( ActionEvent e )
    {
      try {
        m_asyncManager.executeRequests( m_bpeEngine );
      }
      catch ( Exception ex ) {
        log.error( "Exception", ex );
      }
    }
  }


  /**
   * Description of the Class
   *
   * @author    junglas
   * @created   23. Juni 2004
   */
  private class UpdateListButtonActionListener implements ActionListener
  {

    /**
     * @param e  Description of the Parameter
     * @see      java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed( ActionEvent e )
    {
      updateLists();

    }
  }


  /**
   * Description of the Class
   *
   * @author    junglas
   * @created   14. Juni 2004
   */
  private class MainFrameListener extends WindowAdapter
  {

    /**
     * @param e  Description of the Parameter
     * @see      java.awt.event.WindowListener#windowClosing(java.awt.event.WindowEvent)
     */
    public void windowClosing( WindowEvent e )
    {
      System.exit( 0 );
    }
  }
}
