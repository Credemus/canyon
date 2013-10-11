package de.objectcode.canyon.spiImpl.parser;

import org.apache.commons.digester.Rule;
import org.xml.sax.Attributes;

import de.objectcode.canyon.model.activity.AutomationMode;

/**
 * @author    junglas
 * @created   25. November 2003
 */
public class AutomationModeRule extends Rule
{

  /**
   * @param namespace      Description of the Parameter
   * @param name           Description of the Parameter
   * @param attrs          Description of the Parameter
   * @exception Exception  Description of the Exception
   * @see                  org.apache.commons.digester.Rule#begin(java.lang.String, java.lang.String, org.xml.sax.Attributes)
   */
  public void begin( String namespace, String name, Attributes attrs )
    throws Exception
  {
    digester.push(AutomationMode.fromString(name.toUpperCase())); 
  }
  

  /**
   * @see org.apache.commons.digester.Rule#end(java.lang.String, java.lang.String)
   */
  public void end(String namespace, String name) throws Exception {
    digester.pop();
  }

}
