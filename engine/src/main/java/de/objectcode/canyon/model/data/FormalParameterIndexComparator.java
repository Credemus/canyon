/*
 *  Created on 16.02.2004
 *
 *  To change the template for this generated file go to
 *  Window - Preferences - Java - Code Generation - Code and Comments
 */
package de.objectcode.canyon.model.data;

import java.io.Serializable;
import java.util.Comparator;

/**
 * Description of the Class
 *
 * @author    junglas
 * @created   16. Februar 2004
 */
public class FormalParameterIndexComparator implements Comparator, Serializable
{
	static final long serialVersionUID = 120236613714344637L;
	
  /**
   * (non-Javadoc)
   *
   * @param arg0  Description of the Parameter
   * @param arg1  Description of the Parameter
   * @return      Description of the Return Value
   * @see         java.util.Comparator#compare(java.lang.Object, java.lang.Object)
   */
  public int compare( Object arg0, Object arg1 )
  {
    if ( arg0 == arg1 ) {
      return 0;
    }
    if ( arg0 instanceof FormalParameter && arg1 instanceof FormalParameter ) {
      FormalParameter  param1  = ( FormalParameter ) arg0;
      FormalParameter  param2  = ( FormalParameter ) arg1;

      return param1.getIndex() < param2.getIndex() ? -1 : 1;
    }
    return -1;
  }

}
