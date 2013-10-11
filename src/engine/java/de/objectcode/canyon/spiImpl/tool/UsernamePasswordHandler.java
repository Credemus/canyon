package de.objectcode.canyon.spiImpl.tool;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;

/**
 * @author    junglas
 * @created   16.06.2003
 * @version   $Id: UsernamePasswordHandler.java,v 1.1 2004/03/04 10:40:00 xylander Exp $
 */
public class UsernamePasswordHandler implements CallbackHandler
{
  private transient  String  username;
  private transient  char[]  password;


  /**
   * Initialize the UsernamePasswordHandler with the usernmae
   *and password to use.
   *
   * @param username  Description of the Parameter
   * @param password  Description of the Parameter
   */
  public UsernamePasswordHandler( String username, char[] password )
  {
    this.username = username;
    this.password = password;
  }


  /**
   * Initialize the UsernamePasswordHandler with the usernmae
   *and password to use.
   *
   * @param username  Description of the Parameter
   * @param password  Description of the Parameter
   */
  public UsernamePasswordHandler( String username, String password )
  {
    this.username = username;
    this.password = password.toCharArray();
  }


  /**
   * Sets any NameCallback name property to the instance username,
   *sets any PasswordCallback password property to the instance, and any
   *password.
   *
   * @param callbacks                          Description of the Parameter
   * @exception UnsupportedCallbackException   Description of the Exception
   */
  public void handle( Callback[] callbacks )
    throws
      UnsupportedCallbackException
  {
    for ( int i = 0; i < callbacks.length; i++ ) {
      Callback  c  = callbacks[i];
      if ( c instanceof NameCallback ) {
        NameCallback  nc  = ( NameCallback ) c;
        nc.setName( username );
      } else if ( c instanceof PasswordCallback ) {
        PasswordCallback  pc  = ( PasswordCallback ) c;
        pc.setPassword( password );
      } else {
        throw new UnsupportedCallbackException( callbacks[i], "Unrecognized Callback" );
      }
    }
  }
}
