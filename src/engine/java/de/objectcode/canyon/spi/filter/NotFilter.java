package de.objectcode.canyon.spi.filter;

/**
 * @author    junglas
 * @created   29. Oktober 2003
 */
public class NotFilter implements IFilter
{
  private  IFilter  m_filter;


  /**
   *Constructor for the NotFilter object
   *
   * @param filter  Description of the Parameter
   */
  public NotFilter( IFilter filter )
  {
    m_filter = filter;
  }


  /**
   * @param filterBuilder  Description of the Parameter
   * @see                  de.objectcode.canyon.spi.filter.IFilter#toBuilder(de.objectcode.canyon.spi.filter.IFilterBuilder)
   */
  public void toBuilder( IFilterBuilder filterBuilder )
  {
    m_filter.toBuilder( filterBuilder );
    filterBuilder.notExpr();
  }

}
