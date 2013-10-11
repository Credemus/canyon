package de.objectcode.canyon.spiImpl.tool;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author    junglas
 * @created   28. Oktober 2003
 */
public class LogTool
{
  /**
   * Description of the Method
   *
   * @param category  Description of the Parameter
   * @param level     Description of the Parameter
   * @param message   Description of the Parameter
   */
  public void log( String category, int level, String message )
  {
    Log  log  = LogFactory.getLog( category );

    switch ( level ) {
      case 1:
        log.trace( message );
        break;
      case 2:
        log.debug( message );
        break;
      case 3:
        log.info( message );
        break;
      case 4:
        log.warn( message );
        break;
      case 5:
        log.error( message );
        break;
      default:
        log.fatal( message );
    }
  }
}
