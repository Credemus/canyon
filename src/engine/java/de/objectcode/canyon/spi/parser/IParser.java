package de.objectcode.canyon.spi.parser;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

import de.objectcode.canyon.model.WorkflowPackage;

/**
 * @author    junglas
 * @created   24. November 2003
 */
public interface IParser
{
  /**
   * Description of the Method
   *
   * @param in                       Description of the Parameter
   * @return                         Description of the Return Value
   * @exception IOException          Description of the Exception
   * @exception XPDLParserException  Description of the Exception
   */
  public WorkflowPackage parse( InputStream in )
    throws IOException, XPDLParserException;


  /**
   * Description of the Method
   *
   * @param in                       Description of the Parameter
   * @return                         Description of the Return Value
   * @exception IOException          Description of the Exception
   * @exception XPDLParserException  Description of the Exception
   */
  public WorkflowPackage parse( Reader in )
    throws IOException, XPDLParserException;
}
