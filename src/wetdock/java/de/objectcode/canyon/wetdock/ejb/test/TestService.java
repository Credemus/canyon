package de.objectcode.canyon.wetdock.ejb.test;

import javax.ejb.SessionContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author junglas
 */
public class TestService
{
  final private static Log log = LogFactory.getLog(TestService.class);
  
  private static TestService g_instance = new TestService();
  
  private int m_exceptionOn = 0;
  
  private long m_waitDuration = 0L;
  
  public TestService()
  {
    
  }
  
  public String testMethod1Ctx ( SessionContext ctx, String param ) throws Exception {
      try {
      return testMethod1(param);
      } catch (Exception e) {
          ctx.setRollbackOnly();
          throw e;
      }
  }
  
  public String testMethod1 ( String param )
    throws Exception
  {
    if ( log.isDebugEnabled() ) 
      log.debug ( "testMethod1: " + param );
    
    if (m_waitDuration!=0L) {
    	Thread.sleep(m_waitDuration);
    }
    
    if ( m_exceptionOn == 1) {
      throw new Exception("testMethod1 exception as ordered");
    }
    
    return param + "_test";
  }
  
  public String testMethod2 ( String param )
  throws Exception
{
  if ( log.isDebugEnabled() ) 
    log.debug ( "testMethod2: " + param );
  
  if ( m_exceptionOn == 2) {
    throw new Exception("testMethod2 exception as ordered");
  }
  
  return param + "_test";
}

  public void setWaitDuration(long waitDuration) {
	m_waitDuration = waitDuration;  
  }
  
  public long getWaitDuration(){
	  return m_waitDuration;
  }
  
  /**
   * @param exceptionOn The exceptionOn to set.
   */
  public void setExceptionOn( boolean exceptionOn )
  {
    m_exceptionOn = exceptionOn ? 1 : 0;
  }
  
  /**
   * @param exceptionOn The exceptionOn to set.
   */
  public void setExceptionOn( int exceptionOn )
  {
    m_exceptionOn = exceptionOn;
  }
  

  public static TestService getInstance ( )
  {
    return g_instance;
  }
}
