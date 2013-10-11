package de.objectcode.canyon.spi.filter;

import java.util.Date;

/**
 * Build a filter expression from reverse polish notation.
 *
 *
 * @author    junglas
 * @created   25.03.2003
 * @version   $Id: IFilterBuilder.java,v 1.3 2003/12/02 16:38:10 junglas Exp $
 */
public interface IFilterBuilder
{
  /**
   * Description of the Method
   *
   * @param attribute  Description of the Parameter
   * @param value      Description of the Parameter
   */
  public void compareExpr( String attribute, int operation, String value );


  /**
   * Description of the Method
   *
   * @param attribute  Description of the Parameter
   * @param value      Description of the Parameter
   */
  public void compareExpr( String attribute, int operation, int value );


  /**
   * Description of the Method
   *
   * @param attribute  Description of the Parameter
   * @param value      Description of the Parameter
   */
  public void compareExpr( String attribute, int operation, long value );


  /**
   * Description of the Method
   *
   * @param attribute  Description of the Parameter
   * @param value      Description of the Parameter
   */
  public void compareExpr( String attribute, int operation, double value );

  /**
   * Description of the Method
   *
   * @param attribute  Description of the Parameter
   * @param value      Description of the Parameter
   */
  public void compareExpr( String attribute, int operation, Date value );


  /**
   * Description of the Method
   *
   * @param attribute  Description of the Parameter
   * @param value      Description of the Parameter
   */
  public void compareExpr( String attribute, int operation, boolean value );


  /**
   * Description of the Method
   *
   * @param attribute  Description of the Parameter
   * @param value      Description of the Parameter
   */
  public void compareIgnoreCaseExpr( String attribute, int operation, String value );


  /**
   * Description of the Method
   *
   * @param attribute  Description of the Parameter
   * @param value      Description of the Parameter
   */
  public void likeExpr( String attribute, String value );


  /**
   * Description of the Method
   *
   * @param attribute  Description of the Parameter
   * @param value      Description of the Parameter
   */
  public void likeIgnoreCaseExpr( String attribute, String value );


  /**
   * Description of the Method
   *
   * @param attribute  Description of the Parameter
   * @param value1     Description of the Parameter
   * @param value2     Description of the Parameter
   */
  public void betweenExpr( String attribute, String value1, String value2 );


  /**
   * Description of the Method
   *
   * @param attribute  Description of the Parameter
   * @param value1     Description of the Parameter
   * @param value2     Description of the Parameter
   */
  public void betweenExpr( String attribute, int value1, int value2 );


  /**
   * Description of the Method
   *
   * @param attribute  Description of the Parameter
   * @param value1     Description of the Parameter
   * @param value2     Description of the Parameter
   */
  public void betweenExpr( String attribute, double value1, double value2 );


  /**
   * Description of the Method
   *
   * @param attribute  Description of the Parameter
   * @param value1     Description of the Parameter
   * @param value2     Description of the Parameter
   */
  public void betweenExpr( String attribute, Date value1, Date value2 );


  /**
   * Description of the Method
   *
   * @param attribute  Description of the Parameter
   * @param value1     Description of the Parameter
   * @param value2     Description of the Parameter
   */
  public void betweenIgnoreCaseExpr( String attribute, String value1, String value2 );


  /**
   * Description of the Method
   *
   * @param attribute  Description of the Parameter
   * @param values     Description of the Parameter
   */
  public void inExpr( String attribute, String[] values );


  /**
   * Description of the Method
   *
   * @param attribute  Description of the Parameter
   * @param values     Description of the Parameter
   */
  public void inExpr( String attribute, int[] values );


  /**
   * Description of the Method
   *
   * @param attribute  Description of the Parameter
   * @param values     Description of the Parameter
   */
  public void inExpr( String attribute, double[] values );


  /**
   * Description of the Method
   *
   * @param attribute  Description of the Parameter
   * @param values     Description of the Parameter
   */
  public void inExpr( String attribute, Date[] values );


  /**
   * Gets the nullExpr attribute of the IFilterBuilder object
   *
   * @param attribute  Description of the Parameter
   */
  public void isNullExpr( String attribute );


  /**
   * Description of the Method
   */
  public void andExpr();


  /**
   * Description of the Method
   */
  public void orExpr();
  
  public void notExpr();
}
