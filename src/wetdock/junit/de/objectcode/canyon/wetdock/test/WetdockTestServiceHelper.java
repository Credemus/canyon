package de.objectcode.canyon.wetdock.test;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;

import de.objectcode.canyon.wetdock.api.test.WetdockTestService;
import de.objectcode.canyon.wetdock.api.test.WetdockTestServiceHome;

/**
 * @author junglas
 */
public class WetdockTestServiceHelper
{
  private WetdockTestService m_testService;
  
  public WetdockTestServiceHelper ()
      throws NamingException, RemoteException, CreateException
  {
    InitialContext ctx = new InitialContext();

    WetdockTestServiceHome testServiceHome = (WetdockTestServiceHome) PortableRemoteObject.narrow( ctx
        .lookup( WetdockTestServiceHome.JNDI_NAME ), WetdockTestServiceHome.class );

    m_testService = testServiceHome.create();
  }
  
  public void setExceptionOn(boolean exceptionOn)
    throws RemoteException
  {
    m_testService.setExceptionOn(exceptionOn);
  }

  public void setExceptionOn(int exceptionOn)
  throws RemoteException
{
  m_testService.setExceptionOn(exceptionOn);
}

  public void setWaitDuration(long waitDuration) {
	  try {
			m_testService.setWaitDuration(waitDuration);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
  }
}
