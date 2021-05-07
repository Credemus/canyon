package de.objectcode.canyon.spi.parser;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;

import de.objectcode.canyon.model.WorkflowPackage;

/**
 * @author    junglas
 * @created   26. November 2003
 */
public interface ISerializer
{
  /**
   * Description of the Method
   *
   * @param pkg              Description of the Parameter
   * @param out              Description of the Parameter
   * @exception IOException  Description of the Exception
   */
  void serialize(WorkflowPackage pkg, OutputStream out)
    throws IOException;


  /**
   * Description of the Method
   *
   * @param pkg              Description of the Parameter
   * @param out              Description of the Parameter
   * @exception IOException  Description of the Exception
   */
  void serialize(WorkflowPackage pkg, Writer out)
    throws IOException;
}
