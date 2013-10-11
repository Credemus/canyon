package de.objectcode.canyon.wetdock.user.persistent;

import java.util.Iterator;
import java.util.List;

import net.sf.hibernate.Hibernate;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;
import net.sf.hibernate.SessionFactory;
import net.sf.hibernate.type.Type;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.objectcode.canyon.spi.RepositoryException;
import de.objectcode.canyon.wetdock.api.user.ClientData;
import de.objectcode.canyon.wetdock.api.user.RoleData;
import de.objectcode.canyon.wetdock.api.user.UserData;
import de.objectcode.canyon.wetdock.user.IUserManager;

/**
 * @author junglas
 */
public class UserManager implements IUserManager
{
  private final static Log log = LogFactory.getLog(UserManager.class);
  
  private SessionFactory m_sessionFactory;
  
  public UserManager  ( SessionFactory sessionFactory )
  {
    m_sessionFactory = sessionFactory;
  }

  public UserData getUser( String userId ) throws RepositoryException
  {
    Session session = null;
    try {
      session = m_sessionFactory.openSession();

      PUser user = (PUser)session.load(PUser.class, userId);

      return user.getUserData();
    }
    catch ( HibernateException e ) {
      log.error( "Exception", e );
      throw new RepositoryException( e );
    }
    finally {
      try {
        session.close();
      }
      catch ( Exception e ) {
      }
    }    
  }

  public void createUser ( UserData userData ) throws RepositoryException
  {
    Session session = null;
    try {
      session = m_sessionFactory.openSession();

      PUser user = new PUser(userData);

      session.save(user);
      
      session.flush();
    }
    catch ( HibernateException e ) {
      log.error( "Exception", e );
      throw new RepositoryException( e );
    }
    finally {
      try {
        session.close();
      }
      catch ( Exception e ) {
      }
    }        
  }

  public RoleData getRole( String roleId ) throws RepositoryException
  {
    Session session = null;
    try {
      session = m_sessionFactory.openSession();

      PRole role = (PRole)session.load(PRole.class, roleId);

      return role.getRoleData();
    }
    catch ( HibernateException e ) {
      log.error( "Exception", e );
      throw new RepositoryException( e );
    }
    finally {
      try {
        session.close();
      }
      catch ( Exception e ) {
      }
    }    
  }

  public void createRole ( RoleData roleData ) throws RepositoryException
  {
    Session session = null;
    try {
      session = m_sessionFactory.openSession();

      PRole role = new PRole(roleData);

      session.save(role);
      
      session.flush();
    }
    catch ( HibernateException e ) {
      log.error( "Exception", e );
      throw new RepositoryException( e );
    }
    finally {
      try {
        session.close();
      }
      catch ( Exception e ) {
      }
    }        
  }

  public ClientData getClient( String clientId ) throws RepositoryException
  {
    Session session = null;
    try {
      session = m_sessionFactory.openSession();

      PClient client = (PClient)session.load(PClient.class, clientId);

      return client.getClientData();
    }
    catch ( HibernateException e ) {
      log.error( "Exception", e );
      throw new RepositoryException( e );
    }
    finally {
      try {
        session.close();
      }
      catch ( Exception e ) {
      }
    }    
  }
    
  public void createClient ( ClientData clientData ) throws RepositoryException
  {
    Session session = null;
    try {
      session = m_sessionFactory.openSession();

      PClient client = new PClient(clientData);

      session.save(client);
      
      session.flush();
    }
    catch ( HibernateException e ) {
      log.error( "Exception", e );
      throw new RepositoryException( e );
    }
    finally {
      try {
        session.close();
      }
      catch ( Exception e ) {
      }
    }        
  }
  
  public UserData[] findUsersInRole ( String clientId, String roleId ) throws RepositoryException
  {
    Session session = null;
    try {
      session = m_sessionFactory.openSession();

      Query query = session.createQuery("from o in class " + PUserRoles.class + " where o.client = :client and o.role = :role");

      query.setString("client", clientId);
      query.setString("role", roleId);
      
      List result = query.list();
      UserData[] ret = new UserData[result.size()];
      int i;
      Iterator it = result.iterator();
      
      for ( i = 0; it.hasNext(); i++ ) {
        PUserRoles userRoles = (PUserRoles)it.next();
        
        ret[i] = userRoles.getUser().getUserData(); 
      }
      
      return ret;
    }
    catch ( HibernateException e ) {
      log.error( "Exception", e );
      throw new RepositoryException( e );
    }
    finally {
      try {
        session.close();
      }
      catch ( Exception e ) {
      }
    }        
  }

  public UserData authenticate ( String userId, String password ) throws RepositoryException
  {
    Session session = null;
    try {
      session = m_sessionFactory.openSession();

      PUser user = (PUser)session.load(PUser.class, userId);

      if ( password.equals(user.getPassword()))
        return user.getUserData();
    }
    catch ( HibernateException e ) {
      log.error( "Exception", e );
      throw new RepositoryException( e );
    }
    finally {
      try {
        session.close();
      }
      catch ( Exception e ) {
      }
    }   
    
    return null;
  }
  
  public void addUserRole ( String userId, String roleId, String clientId ) throws RepositoryException
  {
    Session session = null;
    try {
      session = m_sessionFactory.openSession();

      PUser user = (PUser)session.load(PUser.class, userId);
      PRole role = (PRole)session.load(PRole.class, roleId);
      PClient client = (PClient)session.load(PClient.class, clientId);
      
      PUserRoles userRoles = new PUserRoles(user, role, client);

      session.save(userRoles);
      
      session.flush();
    }
    catch ( HibernateException e ) {
      log.error( "Exception", e );
      throw new RepositoryException( e );
    }
    finally {
      try {
        session.close();
      }
      catch ( Exception e ) {
      }
    }            
  }

  public void removeUserRole ( String userId, String roleId, String clientId ) throws RepositoryException
  {
    Session session = null;
    try {
      session = m_sessionFactory.openSession();

      session.delete("from o in class " + PUserRoles.class + " where o.user = ? and o.role = ? and o.client = ?",
          new Object[] { userId, roleId, clientId }, new Type[] { Hibernate.STRING, Hibernate.STRING, Hibernate.STRING });
      
      session.flush();
    }
    catch ( HibernateException e ) {
      log.error( "Exception", e );
      throw new RepositoryException( e );
    }
    finally {
      try {
        session.close();
      }
      catch ( Exception e ) {
      }
    }            
  }
  
  public void clearRepository () throws RepositoryException
  {
    Session session = null;
    try {
      session = m_sessionFactory.openSession();

      session.delete("from o in class " + PUserRoles.class );
      session.delete("from o in class " + PClient.class);
      session.delete("from o in class " + PRole.class);
      session.delete("from o in class " + PUser.class);
      
      session.flush();
    }
    catch ( HibernateException e ) {
      log.error( "Exception", e );
      throw new RepositoryException( e );
    }
    finally {
      try {
        session.close();
      }
      catch ( Exception e ) {
      }
    }                
  }
}
