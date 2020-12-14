//$Id: HibernateServiceMBean.java,v 1.1 2003/11/12 14:14:38 junglas Exp $
package de.objectcode.canyon.persistent.jmx;

import net.sf.hibernate.HibernateException;

/**
 * Hibernate JMX Management API
 *
 * @author    John Urberg
 * @created   5. November 2003
 * @see       HibernateService
 */
public interface HibernateServiceMBean extends net.sf.hibernate.jmx.HibernateServiceMBean
{



  /**
   * Is outerjoin fetching enabled?
   *
   * @return   boolean
   */
  public boolean getUseOuterJoin();


  /**
   * Enable outerjoin fetching
   *
   * @param uoj      The new useOuterJoin value
   */
  public void setUseOuterJoin( boolean uoj );


}



