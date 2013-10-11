package de.objectcode.canyon.spi.instance;

/**
 * @author    junglas
 * @created   22. Oktober 2003
 */
public interface IPersistentIterator
{
  /**
   * Description of the Method
   */
  public void reset();


  /**
   * Description of the Method
   *
   * @return   Description of the Return Value
   */
  public int next();


  /**
   * Gets the current attribute of the IPersistentIterator object
   *
   * @return   The current value
   */
  public int getCurrent();


  /**
   * Sets the length attribute of the IPersistentIterator object
   *
   * @param length  The new length value
   */
  public void setLength( int length );


  /**
   * Gets the length attribute of the IPersistentIterator object
   *
   * @return   The length value
   */
  public int getLength();


  /**
   * Description of the Method
   *
   * @return   Description of the Return Value
   */
  public boolean hasNext();
}
