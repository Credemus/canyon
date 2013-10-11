package de.objectcode.canyon.bpe.factory.xpdl;

import de.objectcode.canyon.bpe.engine.activities.BPEProcess;
import de.objectcode.canyon.bpe.engine.variable.BasicVariable;
import de.objectcode.canyon.bpe.engine.variable.IVariable;
import de.objectcode.canyon.model.data.BasicType;
import de.objectcode.canyon.model.data.DataType;
import de.objectcode.canyon.model.data.FormalParameter;
import de.objectcode.canyon.model.process.DataField;

/**
 * @author junglas
 */
public class DataFieldBuilder
{
  private BPEProcess m_bpeProcess;
  
  DataFieldBuilder ( BPEProcess bpeProcess )
  {
    m_bpeProcess = bpeProcess;
  }
  
  public void build ( DataField dataField )
  {
    DataType type = dataField.getDataType();
    
    if ( type instanceof BasicType ) {
      IVariable variable = new BasicVariable(dataField.getId(), (BasicType)type, dataField.getInitialValue());
      
      if ( dataField.getInitialValue() != null ) {
        variable.setValue(dataField.getInitialValue());
      }
    	m_bpeProcess.addVariable(variable);
    }
  }
  
  public void build ( FormalParameter formalParameter ) 
  {
    DataType type = formalParameter.getDataType();

    if ( type instanceof BasicType ) {
      if ( m_bpeProcess.getVariable(formalParameter.getId()) == null ) {
        IVariable variable = new BasicVariable(formalParameter.getId(), (BasicType)type );
      
        m_bpeProcess.addVariable(variable);
      }
    }
  }
}
