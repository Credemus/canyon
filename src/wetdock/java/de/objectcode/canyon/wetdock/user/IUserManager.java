package de.objectcode.canyon.wetdock.user;

import de.objectcode.canyon.spi.RepositoryException;
import de.objectcode.canyon.wetdock.api.user.ClientData;
import de.objectcode.canyon.wetdock.api.user.RoleData;
import de.objectcode.canyon.wetdock.api.user.UserData;

/**
 * @author junglas
 */
public interface IUserManager
{
  public UserData getUser( String userId ) throws RepositoryException;
  
  public void createUser ( UserData userData ) throws RepositoryException;

  public RoleData getRole( String roleId ) throws RepositoryException;
  
  public void createRole ( RoleData roleData ) throws RepositoryException;

  public ClientData getClient ( String clientId ) throws RepositoryException;
  
  public void createClient ( ClientData clientData ) throws RepositoryException;

  public UserData[] findUsersInRole ( String clientId, String roleId ) throws RepositoryException;

  public UserData authenticate ( String userId, String password ) throws RepositoryException;

  public void addUserRole ( String userId, String roleId, String clientId ) throws RepositoryException;
  
  public void removeUserRole ( String userId, String roleId, String clientId ) throws RepositoryException;

  public void clearRepository ( ) throws RepositoryException;
}
