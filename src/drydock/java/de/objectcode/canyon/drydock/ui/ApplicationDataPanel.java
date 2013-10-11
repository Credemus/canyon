package de.objectcode.canyon.drydock.ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import de.objectcode.canyon.model.data.ParameterMode;
import de.objectcode.canyon.spi.tool.Parameter;
import de.objectcode.canyon.worklist.spi.worklist.IApplicationData;

/**
 * @author junglas
 */
public class ApplicationDataPanel extends JPanel
{
  private IApplicationData m_applicationData;
  private Map m_fields;
  
  public ApplicationDataPanel ( IApplicationData applicationData )
  {
    m_applicationData = applicationData;
    m_fields = new HashMap();
    
    initComponents();
  }
    
  public void updateData ( )
  {
    Parameter parameters[] = m_applicationData.getParameters();
    int i;
    
    for ( i = 0; i < parameters.length; i++ ) {
      if ( parameters[i].mode != ParameterMode.IN ) {
        JTextField textField = (JTextField)m_fields.get(parameters[i].formalName);
        m_applicationData.setParameterValue(parameters[i].formalName, textField.getText());
      }
    }
  }
  
  protected void initComponents()
  {
    setLayout(new GridBagLayout());
    
    Parameter parameters[] = m_applicationData.getParameters();
    int i;
    
    for ( i = 0; i < parameters.length; i++ ) {
      add( new JLabel(parameters[i].formalName),
          new GridBagConstraints(0,i,1,1,0.0,0.0,GridBagConstraints.CENTER,GridBagConstraints.NONE,new Insets(2,2,2,2),0,0));

      JTextField textField = new JTextField();
      
      textField.setText(String.valueOf(parameters[i].value));
      textField.setEditable(parameters[i].mode != ParameterMode.IN);
        
      m_fields.put(parameters[i].formalName, textField);
      
      add( textField,
          new GridBagConstraints(1,i,1,1,1.0,0.0,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2,2,2,2),0,0));
    }
  }
}
