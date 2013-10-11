package de.objectcode.canyon.model;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * @author    junglas
 * @created   21. November 2003
 */
public class ValidationErrors
{
  private  static ResourceBundle g_bundle = ResourceBundle.getBundle(ValidationErrors.class.getName(), Locale.getDefault());
  private  List  m_messages;

  /**
   *Constructor for the ValidationErrors object
   */
  public ValidationErrors()
  {
    m_messages = new ArrayList();
    
  }


  /**
   * Gets the empty attribute of the ValidationErrors object
   *
   * @return   The empty value
   */
  public boolean isEmpty()
  {
    return m_messages.isEmpty();
  }


  /**
   * Adds a feature to the Error attribute of the ValidationErrors object
   *
   * @param message  The feature to be added to the Error attribute
   */
  public void addMessage( String messageKey, Object[] args )
  {
    m_messages.add( MessageFormat.format(g_bundle.getString(messageKey), args) );
  }


  /**
   * Adds a feature to the Errors attribute of the ValidationErrors object
   *
   * @param errors  The feature to be added to the Errors attribute
   */
  public void addErrors( ValidationErrors errors )
  {
    m_messages.addAll( errors.m_messages );
  }


  /**
   * Description of the Method
   *
   * @param validatable  Description of the Parameter
   */
  public void check( IValidatable validatable )
  {
    if ( validatable != null )
      addErrors( validatable.validate() );
  }


  /**
   * Description of the Method
   *
   * @param validatables  Description of the Parameter
   */
  public void check( Collection validatables )
  {
    Iterator  it  = validatables.iterator();

    while ( it.hasNext() ) {
      IValidatable  validatable  = ( IValidatable ) it.next();

      addErrors( validatable.validate() );
    }
  }
  
  public String[] getMessages ( )
  {
    String ret[] = new String[m_messages.size()];
    
    m_messages.toArray(ret);
    
    return ret; 
  }
}
