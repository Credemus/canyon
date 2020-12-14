package de.objectcode.canyon.jmx;

import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.Name;
import javax.naming.NameAlreadyBoundException;
import javax.naming.NameNotFoundException;
import javax.naming.NamingException;
import javax.naming.RefAddr;
import javax.naming.Reference;
import javax.naming.StringRefAddr;
import javax.naming.spi.ObjectFactory;

/**
 * A utility class that allows one to bind a non-serializable object into a
 * local JNDI context. The binding will only be valid for the lifetime of the
 * VM in which the JNDI InitialContext lives. An example usage code snippet is:
 *
 * <code>
 * 		// The non-Serializable object to bind
 * 		Object nonserializable = ...;
 * 		// An arbitrary key to use in the StringRefAddr. The best key is the jndi
 * 		// name that the object will be bound under.
 * 		String key = ...;
 * 		// This places nonserializable into the NonSerializableFactory hashmap under key
 * 		NonSerializableFactory.rebind(key, nonserializable);
 *
 * 		Context ctx = new InitialContext();
 * 		// Bind a reference to nonserializable using NonSerializableFactory as the ObjectFactory
 * 		String className = nonserializable.getClass().getName();
 * 		String factory = NonSerializableFactory.class.getName();
 * 		StringRefAddr addr = new StringRefAddr("nns", key);
 * 		Reference memoryRef = new Reference(className, addr, factory, null);
 * 		ctx.rebind(key, memoryRef);
 * </code>
 *
 * Or you can use the rebind(Context, String, Object) convience method to simplify
 * the number of steps to:
 * <code>
 * 		Context ctx = new InitialContext();
 * 		// The non-Serializable object to bind
 * 		Object nonserializable = ...;
 * 		// The jndiName that the object will be bound into ctx with
 * 		String jndiName = ...;
 * 		// This places nonserializable into the NonSerializableFactory hashmap under key
 * 		NonSerializableFactory.rebind(ctx, jndiName, nonserializable);
 * </code>
 *
 * To unbind the object, use the following code snippet:
 *
 * <code>
 * 	new InitialContext().unbind(key);
 * 	NonSerializableFactory.unbind(key);
 * </code>
 *
 *
 * @author    <a href="mailto:Scott.Stark@jboss.org">Scott Stark</a>.
 * @created   23. Juni 2003
 * @see       javax.naming.spi.ObjectFactory
 * @see       #rebind(Context, String, Object)
 *
 * @version   $Revision: 1.1 $
 */
public class NonSerializableFactory implements ObjectFactory
{
  private static  Map  wrapperMap  = Collections.synchronizedMap( new HashMap() );


// --- Begin ObjectFactory interface methods
  /**
   * Transform the obj Reference bound into the JNDI namespace into the
   *actual non-Serializable object.
   *
   * @param obj            Description of the Parameter
   * @param name           Description of the Parameter
   * @param nameCtx        Description of the Parameter
   * @param env            Description of the Parameter
   * @return               the non-Serializable object associated with the obj Reference if one
   *exists, null if one does not.
   * @exception Exception  Description of the Exception
   */
  public Object getObjectInstance( Object obj, Name name, Context nameCtx, Hashtable env )
    throws Exception
  {
    // Get the nns value from the Reference obj and use it as the map key
    Reference  ref     = ( Reference ) obj;
    RefAddr    addr    = ref.get( "nns" );
    String     key     = ( String ) addr.getContent();
    Object     target  = wrapperMap.get( key );
    return target;
  }


  /**
   * Place an object into the NonSerializableFactory namespace for subsequent
   *access by getObject. There cannot be an already existing binding for key.
   *
   * @param key                            Description of the Parameter
   * @param target                         Description of the Parameter
   * @exception NameAlreadyBoundException  Description of the Exception
   * @throws NameAlreadyBoundException,    thrown if key already exists in the
   *NonSerializableFactory map
   */
  public static synchronized void bind( String key, Object target )
    throws NameAlreadyBoundException
  {
    if ( wrapperMap.containsKey( key ) == true ) {
      throw new NameAlreadyBoundException( key + " already exists in the NonSerializableFactory map" );
    }
    wrapperMap.put( key, target );
  }


  /**
   * Place or replace an object in the NonSerializableFactory namespce
   *for subsequent access by getObject. Any existing binding for key will be
   *replaced by target.
   *
   * @param key      Description of the Parameter
   * @param target   Description of the Parameter
   */
  public static void rebind( String key, Object target )
  {
    wrapperMap.put( key, target );
  }


  /**
   * Remove a binding from the NonSerializableFactory map.
   *
   * @param key                        Description of the Parameter
   * @exception NameNotFoundException  Description of the Exception
   * @throws NameNotFoundException,    thrown if key does not exist in the
   *NonSerializableFactory map
   */
  public static void unbind( String key )
    throws NameNotFoundException
  {
    if ( wrapperMap.remove( key ) == null ) {
      throw new NameNotFoundException( key + " was not found in the NonSerializableFactory map" );
    }
  }


  /**
   * Remove a binding from the NonSerializableFactory map.
   *
   * @param name                       Description of the Parameter
   * @exception NameNotFoundException  Description of the Exception
   * @throws NameNotFoundException,    thrown if key does not exist in the
   *NonSerializableFactory map
   */
  public static void unbind( Name name )
    throws NameNotFoundException
  {
    String  key  = name.toString();
    if ( wrapperMap.remove( key ) == null ) {
      throw new NameNotFoundException( key + " was not found in the NonSerializableFactory map" );
    }
  }


  /**
   * Lookup a value from the NonSerializableFactory map.
   *
   * @param key  Description of the Parameter
   * @return     the object bound to key is one exists, null otherwise.
   */
  public static Object lookup( String key )
  {
    Object  value  = wrapperMap.get( key );
    return value;
  }


  /**
   * Lookup a value from the NonSerializableFactory map.
   *
   * @param name  Description of the Parameter
   * @return      the object bound to key is one exists, null otherwise.
   */
  public static Object lookup( Name name )
  {
    String  key    = name.toString();
    Object  value  = wrapperMap.get( key );
    return value;
  }


  /**
   * A convience method that simplifies the process of rebinding a
   *non-zerializable object into a JNDI context.
   *
   * @param ctx                  Description of the Parameter
   * @param key                  Description of the Parameter
   * @param target               Description of the Parameter
   * @exception NamingException  Description of the Exception
   * @throws NamingException,    thrown on failure to rebind key into ctx.
   */
  public static synchronized void rebind( Context ctx, String key, Object target )
    throws NamingException
  {
    NonSerializableFactory.rebind( key, target );
    // Bind a reference to target using NonSerializableFactory as the ObjectFactory
    String         className  = target.getClass().getName();
    String         factory    = NonSerializableFactory.class.getName();
    StringRefAddr  addr       = new StringRefAddr( "nns", key );
    Reference      memoryRef  = new Reference( className, addr, factory, null );
    ctx.rebind( key, memoryRef );
  }


  /**
   * A convience method that simplifies the process of rebinding a
   *non-zerializable object into a JNDI context. This version binds the
   *target object into the default IntitialContext using name path.
   *
   * @param name                 Description of the Parameter
   * @param target               Description of the Parameter
   * @exception NamingException  Description of the Exception
   * @throws NamingException,    thrown on failure to rebind key into ctx.
   */
  public static synchronized void rebind( Name name, Object target )
    throws NamingException
  {
    rebind( name, target, false );
  }


  /**
   * A convience method that simplifies the process of rebinding a
   *non-zerializable object into a JNDI context. This version binds the
   *target object into the default IntitialContext using name path.
   *
   * @param name                 Description of the Parameter
   * @param target               Description of the Parameter
   * @param createSubcontexts    Description of the Parameter
   * @exception NamingException  Description of the Exception
   * @throws NamingException,    thrown on failure to rebind key into ctx.
   */
  public static synchronized void rebind( Name name, Object target,
      boolean createSubcontexts )
    throws NamingException
  {
    String          key  = name.toString();
    InitialContext  ctx  = new InitialContext();
    if ( createSubcontexts == true && name.size() > 1 ) {
      int  size  = name.size() - 1;
      createSubcontext( ctx, name.getPrefix( size ) );
    }
    rebind( ctx, key, target );
  }

// --- End ObjectFactory interface methods

  /**
   * Create a subcontext including any intermediate contexts.
   *
   * @param ctx                  Description of the Parameter
   * @param name                 Description of the Parameter
   * @return                     The new or existing JNDI subcontext
   * @exception NamingException  Description of the Exception
   * @throws NamingException,    on any JNDI failure
   */
  public static Context createSubcontext( Context ctx, Name name )
    throws NamingException
  {
    Context  subctx  = ctx;
    for ( int pos = 0; pos < name.size(); pos++ ) {
      String  ctxName  = name.get( pos );
      try {
        subctx = ( Context ) ctx.lookup( ctxName );
      }
      catch ( NameNotFoundException e ) {
        subctx = ctx.createSubcontext( ctxName );
      }
      // The current subctx will be the ctx for the next name component
      ctx = subctx;
    }
    return subctx;
  }
}
