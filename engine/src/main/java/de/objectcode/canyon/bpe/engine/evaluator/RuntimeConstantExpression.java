package de.objectcode.canyon.bpe.engine.evaluator;

import java.io.Serializable;

import org.dom4j.Element;

import de.objectcode.canyon.bpe.engine.EngineException;
import de.objectcode.canyon.bpe.engine.activities.Activity;
import de.objectcode.canyon.bpe.engine.correlation.Message;
import de.objectcode.canyon.model.AbstractEnum;

/**
 * @author    junglas
 * @created   14. Juni 2004
 */
public class RuntimeConstantExpression implements Serializable, IExpression
{
  static final long serialVersionUID = 90972519134358971L;
  
  public final static   int                PROCESSINSTANCEID_INT  = 0;
  public final static   RuntimeConstant    PROCESSINSTANCEID      = new RuntimeConstant( PROCESSINSTANCEID_INT, "PROCESSINSTANCE-ID" );
  public final static   int                ACTIVITYID_INT         = 1;
  public final static   RuntimeConstant    ACTIVITYID             = new RuntimeConstant( ACTIVITYID_INT, "ACTIVITY-ID" );
  public final static   int                ACTIVITYNAME_INT       = 2;
  public final static   RuntimeConstant    ACTIVITYNAME           = new RuntimeConstant( ACTIVITYNAME_INT, "ACTIVITY-NAME" );
  public final static   int                PROCESSSTARTER_INT     = 3;
  public final static   RuntimeConstant    PROCESSSTARTER         = new RuntimeConstant( PROCESSSTARTER_INT, "PROCESS-CLIENTID" );
  public final static   int                PROCESSCLIENTID_INT    = 4;
  public final static   RuntimeConstant    PROCESSCLIENTID        = new RuntimeConstant( PROCESSSTARTER_INT, "PROCESS-CLIENTID" );
  public final static   int                PROCESSINSTANCEIDPATH_INT    = 5;
  public final static   RuntimeConstant    PROCESSINSTANCEIDPATH        = new RuntimeConstant( PROCESSINSTANCEIDPATH_INT, "PROCESSINSTANCE-ID-PATH" );

  private final static  RuntimeConstant[]  g_values               = {
      PROCESSINSTANCEID,
      ACTIVITYID,
      ACTIVITYNAME,
      PROCESSSTARTER,
      PROCESSCLIENTID,
      PROCESSINSTANCEIDPATH
      };

  protected             RuntimeConstant    m_runtimeConstant;


  /**
   *Constructor for the RuntimeConstantExpression object
   *
   * @param runtimeConstant  Description of the Parameter
   */
  public RuntimeConstantExpression( RuntimeConstant runtimeConstant )
  {
    m_runtimeConstant = runtimeConstant;
  }


  /**
   * @return   The elementName value
   * @see      de.objectcode.canyon.bpe.util.IDomSerializable#getElementName()
   */
  public String getElementName()
  {
    return "runtime-constant-expression";
  }


  /**
   * @param activity             Description of the Parameter
   * @return                     Description of the Return Value
   * @exception EngineException  Description of the Exception
   * @see                        de.objectcode.canyon.bpe.engine.evaluator.IExpression#eval(de.objectcode.canyon.bpe.engine.activities.Activity)
   */
  public Object eval( Activity activity )
    throws EngineException
  {
    switch ( m_runtimeConstant.getValue() ) {
      case PROCESSINSTANCEID_INT:
        return activity.getScope().getProcess().getProcessInstanceId();
      case ACTIVITYID_INT:
        return activity.getId();
      case ACTIVITYNAME_INT:
        return activity.getName();
      case PROCESSSTARTER_INT:
        return activity.getScope().getProcess().getStartedBy();
      case PROCESSCLIENTID_INT:
        return activity.getScope().getProcess().getClientId();
      case PROCESSINSTANCEIDPATH_INT:
        String path = activity.getScope().getProcess().getParentProcessInstanceIdPath();
        if (path == null)
          return activity.getScope().getProcess().getProcessInstanceId();
        else
          return  path + "_" + activity.getScope().getProcess().getProcessInstanceId();
    }
    return null;
  }


  /**
   * @param message  Description of the Parameter
   * @return         Description of the Return Value
   * @see            de.objectcode.canyon.bpe.engine.evaluator.IExpression#eval(de.objectcode.canyon.bpe.engine.correlation.IMessage)
   */
  public Object eval( Message message )
  {
    return null;
  }


  /**
   * @param element  Description of the Parameter
   * @see            de.objectcode.canyon.bpe.util.IDomSerializable#toDom(org.dom4j.Element)
   */
  public void toDom( Element element )
  {
    element.addAttribute( "constant", m_runtimeConstant.getTag() );
  }


  /**
   * Description of the Class
   *
   * @author    junglas
   * @created   14. Juni 2004
   */
  public static class RuntimeConstant extends AbstractEnum implements Serializable
  {
    static final long serialVersionUID = 188245813245497224L;
    /**
     *Constructor for the RuntimeConstant object
     *
     * @param value  Description of the Parameter
     * @param tag    Description of the Parameter
     */
    private RuntimeConstant( int value, String tag )
    {
      super( value, tag );
    }


    /**
     * @return   Description of the Return Value
     * @see      de.objectcode.canyon.model.AbstractEnum#readResolve()
     */
    public Object readResolve()
    {
      return fromInt( m_value );
    }


    /**
     * Description of the Method
     *
     * @param tag  Description of the Parameter
     * @return     Description of the Return Value
     */
    public static RuntimeConstant fromString( String tag )
    {
      int  i;

      for ( i = 0; i < g_values.length; i++ ) {
        if ( g_values[i].getTag().equals( tag ) ) {
          return g_values[i];
        }
      }
      throw new IllegalArgumentException( tag );
    }


    /**
     * Description of the Method
     *
     * @param value  Description of the Parameter
     * @return       Description of the Return Value
     */
    public static RuntimeConstant fromInt( int value )
    {
      int  i;

      for ( i = 0; i < g_values.length; i++ ) {
        if ( g_values[i].getValue() == value ) {
          return g_values[i];
        }
      }
      throw new IllegalArgumentException( String.valueOf( value ) );
    }
  }
}
