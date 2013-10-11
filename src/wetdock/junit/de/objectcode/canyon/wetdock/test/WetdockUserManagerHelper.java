package de.objectcode.canyon.wetdock.test;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;

import org.wfmc.wapi.WMWorkflowException;

import de.objectcode.canyon.wetdock.api.user.ClientData;
import de.objectcode.canyon.wetdock.api.user.RoleData;
import de.objectcode.canyon.wetdock.api.user.UserData;
import de.objectcode.canyon.wetdock.api.user.WetdockUserManager;
import de.objectcode.canyon.wetdock.api.user.WetdockUserManagerHome;

/**
 * @author junglas
 */
public class WetdockUserManagerHelper
{
  private WetdockUserManager m_wetdockUserManager;

  public WetdockUserManagerHelper()
      throws NamingException, RemoteException, CreateException
  {
    InitialContext ctx = new InitialContext();

    WetdockUserManagerHome wetdockUserManagerHome = (WetdockUserManagerHome) PortableRemoteObject
        .narrow( ctx.lookup( WetdockUserManagerHome.JNDI_NAME ), WetdockUserManagerHome.class );

    m_wetdockUserManager = wetdockUserManagerHome.create();
  }

  /**
   * @return Returns the wetdockUserManager.
   */
  public WetdockUserManager getWetdockUserManager()
  {
    return m_wetdockUserManager;
  }

  public void createUser( String userId, String password )
      throws RemoteException, WMWorkflowException
  {
    m_wetdockUserManager.createUser( new UserData( userId, password ) );
  }

  public void createRole( String roleId )
      throws RemoteException, WMWorkflowException
  {
    m_wetdockUserManager.createRole( new RoleData( roleId ) );
  }

  public void createClient( String clientId )
      throws RemoteException, WMWorkflowException
  {
    m_wetdockUserManager.createClient( new ClientData( clientId ) );
  }
  
  public void addUserRole( String userId, String roleId, String clientId )
      throws RemoteException, WMWorkflowException
  {
    m_wetdockUserManager.addUserRole(userId, roleId, clientId);
  }

  public void removeUserRole( String userId, String roleId, String clientId )
  throws RemoteException, WMWorkflowException
  {
  	m_wetdockUserManager.removeUserRole(userId, roleId, clientId);
  }
  
}
