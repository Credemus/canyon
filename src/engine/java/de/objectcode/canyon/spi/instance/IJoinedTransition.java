package de.objectcode.canyon.spi.instance;

/**
 * @author    junglas
 * @created   22. Oktober 2003
 */
public interface IJoinedTransition
{
  /**
   * Gets the transitionId attribute of the IJoinedTransition object
   *
   * @return   The transitionId value
   */
  public String getTransitionId();


  /**
   * Description of the Method
   *
   * @return   Description of the Return Value
   */
  public boolean isFired();


  /**
   * Description of the Method
   */
  public void fire();


  /**
   * Description of the Method
   */
  public void reset();
}
