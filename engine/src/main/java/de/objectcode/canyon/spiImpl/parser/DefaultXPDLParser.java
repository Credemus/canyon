package de.objectcode.canyon.spiImpl.parser;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

import org.apache.commons.digester.Digester;
import org.apache.commons.digester.ExtendedBaseRules;
import org.xml.sax.SAXException;

import de.objectcode.canyon.model.WorkflowPackage;
import de.objectcode.canyon.spi.parser.IParser;
import de.objectcode.canyon.spi.parser.XPDLParserException;

/**
 * @author    junglas
 * @created   24. November 2003
 */
public class DefaultXPDLParser implements IParser
{
  /**
   * @param in                       Description of the Parameter
   * @return                         Description of the Return Value
   * @exception IOException          Description of the Exception
   * @exception XPDLParserException  Description of the Exception
   * @see                            de.objectcode.canyon.spi.parser.IParser#parse(java.io.Reader)
   */
  public WorkflowPackage parse( InputStream in )
    throws IOException, XPDLParserException
  {
    WorkflowPackage  pkg       = new WorkflowPackage();

    Digester         digester  = new Digester();

    digester.setRules( new ExtendedBaseRules() );
    digester.register( "http://www.wfmc.org/2002/XPDL1.0", getClass().getResource( "TC-1025_schema_10_xpdl.xsd" ).toString() );
    digester.setNamespaceAware( true );
    digester.setValidating( false );
    digester.addRuleSet( new XPDLRuleSet() );
    digester.push( pkg );

    try {
      digester.parse( in );
    }
    catch ( SAXException e ) {
      throw new XPDLParserException( e );
    }

    return pkg;
  }


  /**
   * Description of the Method
   *
   * @param in                       Description of the Parameter
   * @return                         Description of the Return Value
   * @exception IOException          Description of the Exception
   * @exception XPDLParserException  Description of the Exception
   */
  public WorkflowPackage parse( Reader in )
    throws IOException, XPDLParserException
  {
    WorkflowPackage  pkg       = new WorkflowPackage();

    Digester         digester  = new Digester();

    digester.setRules( new ExtendedBaseRules() );
    digester.register( "http://www.wfmc.org/2002/XPDL1.0", getClass().getResource( "TC-1025_schema_10_xpdl.xsd" ).toString() );
    digester.setNamespaceAware( true );
    digester.setValidating( false );
    digester.addRuleSet( new XPDLRuleSet() );
    digester.push( pkg );

    try {
      digester.parse( in );
    }
    catch ( SAXException e ) {
      throw new XPDLParserException( e );
    }

    return pkg;
  }
}
