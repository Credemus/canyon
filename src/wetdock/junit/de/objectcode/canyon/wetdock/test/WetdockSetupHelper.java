package de.objectcode.canyon.wetdock.test;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;

import de.objectcode.canyon.wetdock.api.setup.WetdockSetup;
import de.objectcode.canyon.wetdock.api.setup.WetdockSetupHome;

/**
 * @author junglas
 */
public class WetdockSetupHelper
{
  private WetdockSetup m_wetdockSetup;

  public WetdockSetupHelper()
      throws NamingException, RemoteException, CreateException
  {
    InitialContext ctx = new InitialContext();

    WetdockSetupHome wetdockSetupHome = (WetdockSetupHome) PortableRemoteObject.narrow( ctx
        .lookup( WetdockSetupHome.JNDI_NAME ), WetdockSetupHome.class );

    m_wetdockSetup = wetdockSetupHome.create();
  }
  
  
  /**
   * @return Returns the wetdockSetup.
   */
  public WetdockSetup getWetdockSetup()
  {
    return m_wetdockSetup;
  }
}
