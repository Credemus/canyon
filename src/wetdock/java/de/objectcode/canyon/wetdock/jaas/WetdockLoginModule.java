package de.objectcode.canyon.wetdock.jaas;

import java.security.Principal;
import java.security.acl.Group;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.naming.InitialContext;
import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.login.FailedLoginException;
import javax.security.auth.login.LoginException;
import javax.security.auth.spi.LoginModule;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.security.NestableGroup;
import org.jboss.security.SimpleGroup;
import org.jboss.security.SimplePrincipal;

import de.objectcode.canyon.wetdock.api.user.UserData;
import de.objectcode.canyon.wetdock.user.IUserManager;

/**
 * @author junglas
 */
public class WetdockLoginModule implements LoginModule
{
  private static final Log log = LogFactory.getLog(WetdockLoginModule.class);
  
  protected  Subject          m_subject;
  protected  CallbackHandler  m_callbackHandler;
  protected  Map              m_sharedState;
  protected  Map              m_options;

  /**
   * Flag indicating if the shared credential should be used
   */
  protected  boolean          m_useFirstPass;
  /**
   * Flag indicating if the login phase succeeded. Subclasses that override
   *the login method must set this to true on successful completion of login
   */
  protected  boolean          m_loginOk;

  /**
   * The login identity
   */
  private    Principal        m_identity;
  /**
   * The proof of login identity
   */
  private    char[]           m_credential;
  /**
   * the principal to use when a null username and password are seen
   */
  private    Principal        m_unauthenticatedIdentity;

  private    String           m_userManagerJndiName;

  private    UserData m_user;

  /**
   * Gets the identity attribute of the SaintsLoginModul object
   *
   * @return   The identity value
   */
  protected Principal getIdentity()
  {
    return m_identity;
  }


  /**
   * Gets the unauthenticatedIdentity attribute of the SaintsLoginModul object
   *
   * @return   The unauthenticatedIdentity value
   */
  protected Principal getUnauthenticatedIdentity()
  {
    return m_unauthenticatedIdentity;
  }


  /**
   * Gets the credentials attribute of the SaintsLoginModul object
   *
   * @return   The credentials value
   */
  protected Object getCredentials()
  {
    return m_credential;
  }


  /**
   * Gets the username attribute of the SaintsLoginModul object
   *
   * @return   The username value
   */
  protected String getUsername()
  {
    String  username  = null;
    if ( getIdentity() != null ) {
      username = getIdentity().getName();
    }
    return username;
  }


  /**
   * Overriden by subclasses to return the Groups that correspond to the
   *to the role sets assigned to the user. Subclasses should create at
   *least a Group named "Roles" that contains the roles assigned to the user.
   *A second common group is "CallerPrincipal" that provides the application
   *identity of the user rather than the security domain identity.
   *
   * @return                    Group[] containing the sets of roles
   * @exception LoginException  Description of the Exception
   */
  protected Group[] getRoleSets()
    throws LoginException
  {
    if ( m_user == null ) {
      return new Group[0];
    }
    String[] roleIds = m_user.getRoleIds();
    Group     group  = new SimpleGroup( "Roles" );
    int i;
    
    for ( i = 0; i < roleIds.length; i++ ) {
      group.addMember(new SimplePrincipal(roleIds[i]));
    }

    return new Group[]{group};
  }


  //--- End LoginModule interface methods

  /**
   * Gets the useFirstPass attribute of the SaintsLoginModul object
   *
   * @return   The useFirstPass value
   */
  protected boolean getUseFirstPass()
  {
    return m_useFirstPass;
  }


  /**
   * Called by login() to acquire the username and password strings for
   *authentication. This method does no validation of either.
   *
   * @return                    String[], [0] = username, [1] = password
   * @exception LoginException  thrown if CallbackHandler is not set or fails.
   */
  protected String[] getUsernameAndPassword()
    throws LoginException
  {
    String[]          info       = {null, null};
    // prompt for a username and password
    if ( m_callbackHandler == null ) {
      throw new LoginException( "Error: no CallbackHandler available " +
          "to collect authentication information" );
    }
    NameCallback      nc         = new NameCallback( "User name: ", "guest" );
    PasswordCallback  pc         = new PasswordCallback( "Password: ", false );
    Callback[]        callbacks  = {nc, pc};
    String            username   = null;
    String            password   = null;
    try {
      m_callbackHandler.handle( callbacks );
      username = nc.getName();
      char[]  tmpPassword  = pc.getPassword();
      if ( tmpPassword != null ) {
        m_credential = new char[tmpPassword.length];
        System.arraycopy( tmpPassword, 0, m_credential, 0, tmpPassword.length );
        pc.clearPassword();
        password = new String( m_credential );
      }
    }
    catch ( java.io.IOException ioe ) {
      throw new LoginException( ioe.toString() );
    }
    catch ( UnsupportedCallbackException uce ) {
      throw new LoginException( "CallbackHandler does not support: " + uce.getCallback() );
    }
    info[0] = username;
    info[1] = password;
    
    return info;
  }


  /**
   * Initialize this LoginModule.
   *
   * @param subject          Description of the Parameter
   * @param callbackHandler  Description of the Parameter
   * @param sharedState      Description of the Parameter
   * @param options          Description of the Parameter
   */
  public void initialize( Subject subject, CallbackHandler callbackHandler, Map sharedState, Map options )
  {
    this.m_subject = subject;
    this.m_callbackHandler = callbackHandler;
    this.m_sharedState = sharedState;
    this.m_options = options;

    log.debug( "initialize" );
    /*
     *  Check for password sharing options. Any non-null value for
     *  password_stacking sets useFirstPass as this module has no way to
     *  validate any shared password.
     */
    String  passwordStacking  = ( String ) options.get( "password-stacking" );
    if ( passwordStacking != null && passwordStacking.equalsIgnoreCase( "useFirstPass" ) ) {
      m_useFirstPass = true;
    }

    // Check for unauthenticatedIdentity option.
    String  name              = ( String ) options.get( "unauthenticatedIdentity" );
    if ( name != null ) {
      m_unauthenticatedIdentity = new SimplePrincipal( name );
      if ( log.isDebugEnabled() ) {
        log.debug( "Saw unauthenticatedIdentity=" + name );
      }
    }

    m_userManagerJndiName = ( String ) options.get ( "userManagerJndiName" );
    if ( m_userManagerJndiName == null ) {
      m_userManagerJndiName = "java:/canyon/wetdock/UserManager";
    }
    if ( log.isDebugEnabled() ) {
      log.debug( "SaintsLoginModule, userManagerJndiName=" + m_userManagerJndiName );
    }
  }


  /**
   * A hook to allow subclasses to convert a password from the database
   *into a plain text string or whatever form is used for matching against
   *the user input. It is called from within the getUsersPassword() method.
   *
   * @param rawPassword  Description of the Parameter
   * @return             the argument rawPassword
   */
  protected String convertRawPassword( String rawPassword )
  {
    return rawPassword;
  }

  /**
   * Perform the authentication of the username and password.
   *
   * @return                    Description of the Return Value
   * @exception LoginException  Description of the Exception
   */
  public boolean login()
    throws LoginException
  {
    log.debug( "login" );

    m_loginOk = false;
    // If useFirstPass is true, look for the shared password
    if ( m_useFirstPass ) {
      // See if shared credentials exist
      Object  username  = m_sharedState.get( "javax.security.auth.login.name" );
      Object  password  = m_sharedState.get( "javax.security.auth.login.password" );
      
      if ( username != null && password != null  ) {
        // Setup our view of the user
        if ( username instanceof Principal ) {
          m_identity = ( Principal ) username;
        } else {
          String  name  = username.toString();
          m_identity = new SimplePrincipal( name );
        }
        if ( password instanceof char[] ) {
          m_credential = ( char[] ) password;
        } else if ( password != null ) {
          String  tmp  = password.toString();
          m_credential = tmp.toCharArray();
        }
        m_loginOk = true;
        return true;
      }
    }

    m_loginOk = false;
    
    String[]  info      = getUsernameAndPassword();
    String    username  = info[0];
    String    password  = info[1];
    if ( username == null && password == null ) {
      m_identity = m_unauthenticatedIdentity;
      if ( log.isDebugEnabled() ) {
        log.debug( "Authenticating as unauthenticatedIdentity=" + m_identity );
      }
    }

    if ( m_identity == null ) {
      try {
        InitialContext ctx = new InitialContext();
      
        IUserManager userManager = (IUserManager)ctx.lookup(m_userManagerJndiName);
      
        m_user = userManager.authenticate ( username, password );
      }
      catch ( Exception e ) {
        log.error("Exception", e);
        throw new FailedLoginException ( "Lookup error" );
      }
      
      if ( m_user == null )
        throw new FailedLoginException ( "User/Password/Client combination does not exists");
      
      m_identity = new SimplePrincipal( username );
    }

    if ( getUseFirstPass() == true ) {
      m_credential = password.toCharArray();
      
      // Add the username and password to the shared state map
      m_sharedState.put( "javax.security.auth.login.name", username );
      m_sharedState.put( "javax.security.auth.login.password", m_credential );
    }
    m_loginOk = true;
    if ( log.isDebugEnabled() ) {
      log.debug( "User '" + m_identity + "' authenticated, loginOk=" + m_loginOk );
    }
    return true;
  }


  /**
   * Method to commit the authentication process (phase 2). If the login
   *method completed successfully as indicated by loginOk == true, this
   *method adds the getIdentity() value to the subject getPrincipals() Set.
   *It also adds the members of each Group returned by getRoleSets()
   *to the subject getPrincipals() Set.
   *
   * @return                    true always.
   * @exception LoginException  Description of the Exception
   * @see                       javax.security.auth.Subject;
   * @see                       java.security.acl.Group;
   */
  public boolean commit()
    throws LoginException
  {
    if ( log.isDebugEnabled() ) {
      log.debug( "commit, loginOk=" + m_loginOk );
    }
    if ( m_loginOk == false ) {
      return false;
    }

    Set        principals  = m_subject.getPrincipals();
    Principal  identity    = getIdentity();
    principals.add( identity );
    Group[]    roleSets    = getRoleSets();
    for ( int g = 0; g < roleSets.length; g++ ) {
      Group        group         = roleSets[g];
      String       name          = group.getName();
      Group        subjectGroup  = createGroup( name, principals );
      if ( subjectGroup instanceof NestableGroup ) {
        /*
         *  A NestableGroup only allows Groups to be added to it so we
         *  need to add a SimpleGroup to subjectRoles to contain the roles
         */
        SimpleGroup  tmp  = new SimpleGroup( "Roles" );
        subjectGroup.addMember( tmp );
        subjectGroup = tmp;
      }
      // Copy the group members to the Subject group
      Enumeration  members       = group.members();
      while ( members.hasMoreElements() ) {
        Principal  role  = ( Principal ) members.nextElement();
        subjectGroup.addMember( role );
      }
    }
    return true;
  }


  /**
   * Method to abort the authentication process (phase 2).
   *
   * @return                    true alaways
   * @exception LoginException  Description of the Exception
   */
  public boolean abort()
    throws LoginException
  {
    log.debug( "abort" );
    return true;
  }


  /**
   * Remove the user identity and roles added to the Subject during commit.
   *
   * @return                    true always.
   * @exception LoginException  Description of the Exception
   */
  public boolean logout()
    throws LoginException
  {
    log.debug( "logout" );
    // Remove the user identity
    Principal  identity    = getIdentity();
    Set        principals  = m_subject.getPrincipals();
    principals.remove( identity );
    // Remove any added Groups...
    return true;
  }


  /**
   * Find or create a Group with the given name. Subclasses should use this
   *method to locate the 'Roles' group or create additional types of groups.
   *
   * @param name        Description of the Parameter
   * @param principals  Description of the Parameter
   * @return            A named Group from the principals set.
   */
  protected Group createGroup( String name, Set principals )
  {
    Group     roles  = null;
    Iterator  iter   = principals.iterator();
    while ( iter.hasNext() ) {
      Object  next  = iter.next();
      if ( ( next instanceof Group ) == false ) {
        continue;
      }
      Group   grp   = ( Group ) next;
      if ( grp.getName().equals( name ) ) {
        roles = grp;
        break;
      }
    }
    // If we did not find a group create one
    if ( roles == null ) {
      roles = new NestableGroup( name );
      principals.add( roles );
    }
    return roles;
  }
}
