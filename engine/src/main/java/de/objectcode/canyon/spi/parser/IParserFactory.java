package de.objectcode.canyon.spi.parser;


import de.objectcode.canyon.spi.ObjectAlreadyExistsException;
import de.objectcode.canyon.spi.ObjectNotFoundException;

/**
 * @author    junglas
 * @created   16. Oktober 2003
 */
public interface IParserFactory
{
  /** IBM/Microsoft/BEA's Business Process Execution Language for Web Services. */
  String BPEL4WS = "text/xml/bpel4ws";
  /** BPMI's Business Process Modeling Language. */
  String BPML = "text/xml/bpml";
  /** WfMC's Workflow Process Definition Language. */
  String WPDL = "text/wpdl";
  /** IBM's Web Services Flow Language. */
  String WSFL = "text/xml/wsfl";
  /** BPMI's Web Services Process Language. */
  String WSPL = "text/xml/wspl";
  /** Microsoft's BizTalk Process Definition Language. */
  String XLANG = "text/xml/xlang";
  /** WfMC's XML Process Definition Language. */
  String XPDL = "text/xml/xpdl";

  /**
   * Description of the Method
   *
   * @param contentType  Description of the Parameter
   * @return             Description of the Return Value
   */
  IParser createParser(String contentType) throws ObjectNotFoundException;


  /**
   * Description of the Method
   *
   * @param contentType  Description of the Parameter
   * @return             Description of the Return Value
   */
  ISerializer createSerializer(String contentType) throws ObjectNotFoundException;


  /**
   * Description of the Method
   *
   * @param contentType                       Description of the Parameter
   * @param parserClass                       Description of the Parameter
   * @param serializerClass                   Description of the Parameter
   * @exception ObjectAlreadyExistsException  Description of the Exception
   */
  void registerParser(String contentType, String parserClass,
                      String serializerClass)
    throws ObjectAlreadyExistsException;


  /**
   * Description of the Method
   *
   * @param contentType                  Description of the Parameter
   * @exception ObjectNotFoundException  Description of the Exception
   */
  void unregisterParser(String contentType)
    throws ObjectNotFoundException;
}
