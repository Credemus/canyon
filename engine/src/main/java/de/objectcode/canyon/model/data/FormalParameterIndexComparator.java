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
public class FormalParameterIndexComparator implements Comparator<FormalParameter>, Serializable
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
  public int compare( FormalParameter arg0, FormalParameter arg1 )
  {
    if ( arg0 == arg1 ) {
      return 0;
    }
    if ( arg0 != null && arg1 != null) {
      return arg0.getIndex() < arg1.getIndex() ? -1 : 1;
    }
    return -1;
  }

}
