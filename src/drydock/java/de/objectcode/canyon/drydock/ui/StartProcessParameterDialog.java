package de.objectcode.canyon.drydock.ui;

import de.objectcode.canyon.bpe.engine.BPERuntimeContext;
import de.objectcode.canyon.bpe.engine.variable.ComplexType;
import de.objectcode.canyon.bpe.engine.variable.ComplexType.PropertyDefinition;
import de.objectcode.canyon.bpe.engine.variable.ComplexValue;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * @author    junglas
 * @created   16. Juli 2004
 */
public class StartProcessParameterDialog extends JDialog
{
  private  JTextField    m_userIdField;
  private  JTextField    m_clientIdField;
  private  ComplexType   m_contentType;
  private  Map           m_fields;
  private  ComplexValue  m_value;


  /**
   *Constructor for the StartProcessParameterDialog object
   *
   * @param parent       Description of the Parameter
   * @param contentType  Description of the Parameter
   */
  public StartProcessParameterDialog( JFrame parent, ComplexType contentType )
  {
    super( parent, "Start Process", true );

    m_contentType = contentType;
    m_fields = new HashMap();

    initComponents();
  }


  /**
   * @return   Returns the value.
   */
  public ComplexValue getValue()
  {
    return m_value;
  }


  /**
   * Gets the bPERuntimeContext attribute of the StartProcessParameterDialog object
   *
   * @return   The bPERuntimeContext value
   */
  public BPERuntimeContext getBPERuntimeContext()
  {
    return new BPERuntimeContext( m_userIdField.getText(), m_clientIdField.getText() );
  }


  /**
   * Description of the Method
   */
  protected void initComponents()
  {
    JPanel   root          = new JPanel( new GridBagLayout() );

    getContentPane().add( root, BorderLayout.CENTER );

    root.add( new JLabel( "UserId" ),
        new GridBagConstraints( 0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets( 2, 2, 2, 2 ), 0, 0 ) );
    m_userIdField = new JTextField( 20 );
    root.add( m_userIdField,
        new GridBagConstraints( 1, 0, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets( 2, 2, 2, 2 ), 0, 0 ) );
    root.add( new JLabel( "ClientId" ),
        new GridBagConstraints( 0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets( 2, 2, 2, 2 ), 0, 0 ) );
    m_clientIdField = new JTextField( 20 );
    root.add( m_clientIdField,
        new GridBagConstraints( 1, 1, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets( 2, 2, 2, 2 ), 0, 0 ) );

    String   names[]         = m_contentType.getPropertyNames();
    int      i;

    for ( i = 0; i < names.length; i++ ) {
      PropertyDefinition  def    = m_contentType.getPropertyDefinition( names[i] );

      root.add( new JLabel( def.getName() ),
          new GridBagConstraints( 0, i + 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets( 2, 2, 2, 2 ), 0, 0 ) );

      JTextField          field  = new JTextField( 20 );

      m_fields.put( def.getName(), field );
      root.add( field,
          new GridBagConstraints( 1, i + 2, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets( 2, 2, 2, 2 ), 0, 0 ) );
    }

    JPanel   buttonPanel   = new JPanel( new GridBagLayout() );

    root.add( buttonPanel,
        new GridBagConstraints( 0, names.length + 2, 2, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets( 2, 2, 2, 2 ), 0, 0 ) );

    JButton  okButton      = new JButton( "Start" );

    okButton.addActionListener( new OkButtonActionListener() );
    buttonPanel.add( okButton,
        new GridBagConstraints( 0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets( 2, 2, 2, 2 ), 0, 0 ) );

    JButton  cancelButton  = new JButton( "Cancel" );

    cancelButton.addActionListener( new CancelButtonActionListener() );
    buttonPanel.add( cancelButton,
        new GridBagConstraints( 1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets( 2, 2, 2, 2 ), 0, 0 ) );

    pack();
  }


  /**
   * Description of the Class
   *
   * @author    junglas
   * @created   16. Juli 2004
   */
  private class OkButtonActionListener implements ActionListener
  {
    /**
     * @param e  Description of the Parameter
     * @see      java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed( ActionEvent e )
    {
      m_value = new ComplexValue( m_contentType );

      Iterator  it  = m_fields.keySet().iterator();

      while ( it.hasNext() ) {
        String      name   = ( String ) it.next();
        JTextField  field  = ( JTextField ) m_fields.get( name );
        m_value.set( name, field.getText() );
      }
      setVisible( false );
    }
  }


  /**
   * Description of the Class
   *
   * @author    junglas
   * @created   16. Juli 2004
   */
  private class CancelButtonActionListener implements ActionListener
  {
    /**
     * @param e  Description of the Parameter
     * @see      java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed( ActionEvent e )
    {
      m_value = null;
      setVisible( false );
    }

  }
}
