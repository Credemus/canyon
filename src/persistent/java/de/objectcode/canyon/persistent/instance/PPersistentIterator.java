package de.objectcode.canyon.persistent.instance;

import de.objectcode.canyon.spi.instance.IPersistentIterator;

/**
 * @author    junglas
 * @created   22. Oktober 2003
 */
public class PPersistentIterator implements IPersistentIterator
{
  private  int  m_current;
  private  int  m_length;


  /**
   *Constructor for the PPersistentIterator object
   */
  public PPersistentIterator() { }


  /**
   *Constructor for the PPersistentIterator object
   *
   * @param length  Description of the Parameter
   */
  public PPersistentIterator( int length )
  {
    m_current = -1;
    m_length = length;
  }


  /**
   * @param i
   */
  public void setCurrent( int i )
  {
    m_current = i;
  }


  /**
   * @param i
   */
  public void setLength( int i )
  {
    m_length = i;
  }


  /**
   * @hibernate.property column="ITER_CURRENT" type="integer" not-null="true"
   * 
   * @return
   */
  public int getCurrent()
  {
    return m_current;
  }


  /**
   * @hibernate.property column="ITER_LENGTH" type="integer" not-null="true"
   * 
   * @return
   */
  public int getLength()
  {
    return m_length;
  }


  /**
   * @return   Description of the Return Value
   * @see      de.objectcode.canyon.spi.instance.IPersistentIterator#next()
   */
  public int next()
  {
    return ++m_current;
  }


  /**
   * @see   de.objectcode.canyon.spi.instance.IPersistentIterator#reset()
   */
  public void reset()
  {
    m_current = -1;
  }


  /**
   * Description of the Method
   *
   * @return   Description of the Return Value
   */
  public boolean hasNext()
  {
    return m_current < m_length - 1;
  }
}
