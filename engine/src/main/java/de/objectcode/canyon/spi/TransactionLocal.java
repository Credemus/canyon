package de.objectcode.canyon.spi;

/**
 * @author    junglas
 * @created   4. Februar 2004
 */
public class TransactionLocal
{
  private static  ThreadLocal  g_threadlocal  = new ThreadLocal();


  /**
   * Description of the Method
   *
   * @param transactionObject  Description of the Parameter
   */
  public static void set( Object transactionObject )
  {
    g_threadlocal.set( transactionObject );
  }


  /**
   * Description of the Method
   *
   * @return   Description of the Return Value
   */
  public static Object get()
  {
    return g_threadlocal.get();
  }
}
