/*
 * Created on 29.02.2004
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package de.objectcode.canyon.api.worklist;


/**
 * @author xylander
 *
 * Convenience interface to handle transformations of 
 * ApplicationData.Parameters and ProcessData.Parameters to dynabeans
 * on the same code base.
 */
public interface IParameter
{
  // These values are copied from BasicType
  public final static int TYPE_STRING_INT = 0;
  public final static int TYPE_FLOAT_INT = 1;
  public final static int TYPE_INTEGER_INT = 2;
  public final static int TYPE_REFERENCE_INT = 3;
  public final static int TYPE_DATETIME_INT = 4;
  public final static int TYPE_BOOLEAN_INT = 5;
  public final static int TYPE_PERFORMER_INT = 6;


  // These values are copied from ParameterMode
  public final static   int              MODE_IN_INT     = 0;
  public final static   int              MODE_OUT_INT    = 1;
  public final static   int              MODE_INOUT_INT  = 2;
  
  public int getMode();
  
  public String getName();

  public Object getValue();

  public void setValue(Object object) throws IllegalAccessException;

  public int getType();
  
  public String getDescription();
}
