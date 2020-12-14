package de.objectcode.canyon.persistent.filter;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;

import net.sf.hibernate.Hibernate;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;
import net.sf.hibernate.type.Type;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.objectcode.canyon.spi.filter.IFilterBuilder;

/**
 * @author    junglas
 * @created   15. Oktober 2003
 */
public class QueryBuilder implements IFilterBuilder
{
  private final static  Log         log          = LogFactory.getLog( QueryBuilder.class );

  private final static  String      g_operators[]  = new String[]{
      "=",
      ">",
      ">=",
      "<",
      "<=",
      "!="
      };

  private               LinkedList  m_stack      = new LinkedList();
  private               ArrayList   m_binds      = new ArrayList();


  /*
   *  (non-Javadoc)
   *  @see de.objectcode.flowws.spi.filter.IFilterBuilder#isNullExpr(java.lang.String)
   */
  /**
   * Gets the nullExpr attribute of the QueryBuilder object
   *
   * @param attribute  Description of the Parameter
   */
  public void isNullExpr( String attribute )
  {
    m_stack.addLast( "o." + attribute + " is null" );
  }


  /**
   * Description of the Method
   *
   * @param session                 Description of the Parameter
   * @param prefix                  Description of the Parameter
   * @param orderBy                 Description of the Parameter
   * @return                        Description of the Return Value
   * @exception HibernateException  Description of the Exception
   */
  public Query createQuery( Session session, String prefix, String orderBy )
    throws HibernateException
  {
    if ( m_stack.size() > 1 ) {
      throw new InternalError( "Invalid stack size" );
    }

    if ( orderBy != null ) {
      orderBy = " order by " + orderBy;
    } else {
      orderBy = "";
    }

    if ( m_stack.size() == 0 ) {
      if ( log.isDebugEnabled() ) {
        log.debug( "Query: " + prefix +  orderBy );
      }

      return session.createQuery( prefix + orderBy );
    } else {
      if ( log.isDebugEnabled() ) {
        log.debug( "Query: " + prefix + " where " + m_stack.getFirst() + orderBy + " with " + m_binds );
      }

      Query     query  = session.createQuery( prefix + " where " + m_stack.getFirst() + orderBy );
      Iterator  it     = m_binds.iterator();
      int       i;

      for ( i = 0; it.hasNext(); i++ ) {
        Bind  bind  = ( Bind ) it.next();
        query.setParameter( i, bind.getValue(), bind.getType() );
      }

      return query;
    }
  }


  /*
   *  (non-Javadoc)
   *  @see de.objectcode.flowws.spi.filter.IFilterBuilder#andExpr()
   */
  /**
   * Description of the Method
   */
  public void andExpr()
  {
    if ( m_stack.size() > 1 ) {
      Iterator      it      = m_stack.iterator();
      StringBuffer  buffer  = new StringBuffer( "( " + it.next() );

      while ( it.hasNext() ) {
        buffer.append( " and " );
        buffer.append( it.next() );
      }

      buffer.append( " )" );

      m_stack.clear();
      m_stack.addLast( buffer.toString() );
    }
  }


  /*
   *  (non-Javadoc)
   *  @see de.objectcode.flowws.spi.filter.IFilterBuilder#betweenExpr(java.lang.String, java.util.Date, java.util.Date)
   */
  /**
   * Description of the Method
   *
   * @param attribute  Description of the Parameter
   * @param value1     Description of the Parameter
   * @param value2     Description of the Parameter
   */
  public void betweenExpr( String attribute, Date value1, Date value2 )
  {
    m_stack.addLast( "( o." + attribute + " >= ? and o." + attribute + " <= ? )" );
    m_binds.add( new Bind( Hibernate.DATE, value1 ) );
    m_binds.add( new Bind( Hibernate.DATE, value2 ) );
  }


  /*
   *  (non-Javadoc)
   *  @see de.objectcode.flowws.spi.filter.IFilterBuilder#betweenExpr(java.lang.String, double, double)
   */
  /**
   * Description of the Method
   *
   * @param attribute  Description of the Parameter
   * @param value1     Description of the Parameter
   * @param value2     Description of the Parameter
   */
  public void betweenExpr( String attribute, double value1, double value2 )
  {
    m_stack.addLast( "( o." + attribute + " >= ? and o." + attribute + " <= ? )" );
    m_binds.add( new Bind( Hibernate.DOUBLE, new Double( value1 ) ) );
    m_binds.add( new Bind( Hibernate.DOUBLE, new Double( value2 ) ) );
  }


  /*
   *  (non-Javadoc)
   *  @see de.objectcode.flowws.spi.filter.IFilterBuilder#betweenExpr(java.lang.String, int, int)
   */
  /**
   * Description of the Method
   *
   * @param attribute  Description of the Parameter
   * @param value1     Description of the Parameter
   * @param value2     Description of the Parameter
   */
  public void betweenExpr( String attribute, int value1, int value2 )
  {
    m_stack.addLast( "( o." + attribute + " >= ? and o." + attribute + " <= ? )" );
    m_binds.add( new Bind( Hibernate.INTEGER, new Integer( value1 ) ) );
    m_binds.add( new Bind( Hibernate.INTEGER, new Integer( value2 ) ) );
  }


  /*
   *  (non-Javadoc)
   *  @see de.objectcode.flowws.spi.filter.IFilterBuilder#betweenExpr(java.lang.String, java.lang.String, java.lang.String)
   */
  /**
   * Description of the Method
   *
   * @param attribute  Description of the Parameter
   * @param value1     Description of the Parameter
   * @param value2     Description of the Parameter
   */
  public void betweenExpr( String attribute, String value1, String value2 )
  {
    m_stack.addLast( "( o." + attribute + " >= ? and o." + attribute + " <= ? )" );
    m_binds.add( new Bind( Hibernate.STRING, value1 ) );
    m_binds.add( new Bind( Hibernate.STRING, value2 ) );
  }


  /**
   * @param attribute  Description of the Parameter
   * @param value1     Description of the Parameter
   * @param value2     Description of the Parameter
   * @see              de.objectcode.flowws.spi.filter.IFilterBuilder#betweenIgnoreCaseExpr(java.lang.String, java.lang.String, java.lang.String)
   */
  public void betweenIgnoreCaseExpr(
      String attribute,
      String value1,
      String value2 )
  {
    // TODO Auto-generated method stub

  }


  /**
   * @param attribute  Description of the Parameter
   * @param operation  Description of the Parameter
   * @param value      Description of the Parameter
   * @see              de.objectcode.flowws.spi.filter.IFilterBuilder#equalsExpr(java.lang.String, boolean)
   */
  public void compareExpr( String attribute, int operation, boolean value )
  {
    m_stack.addLast( "o." + attribute + " " + g_operators[operation] + " ?" );
    m_binds.add( new Bind( Hibernate.BOOLEAN, new Boolean( value ) ) );
  }


  /**
   * @param attribute  Description of the Parameter
   * @param operation  Description of the Parameter
   * @param value      Description of the Parameter
   * @see              de.objectcode.flowws.spi.filter.IFilterBuilder#equalsExpr(java.lang.String, java.util.Date)
   */
  public void compareExpr( String attribute, int operation, Date value )
  {
    m_stack.addLast( "o." + attribute + " " + g_operators[operation] + " ?" );
    m_binds.add( new Bind( Hibernate.TIMESTAMP, value ) );
  }


  /**
   * @param attribute  Description of the Parameter
   * @param operation  Description of the Parameter
   * @param value      Description of the Parameter
   * @see              de.objectcode.flowws.spi.filter.IFilterBuilder#equalsExpr(java.lang.String, double)
   */
  public void compareExpr( String attribute, int operation, double value )
  {
    m_stack.addLast( "o." + attribute + " " + g_operators[operation] + " ?" );
    m_binds.add( new Bind( Hibernate.DOUBLE, new Double( value ) ) );
  }


  /**
   * @param attribute  Description of the Parameter
   * @param operation  Description of the Parameter
   * @param value      Description of the Parameter
   * @see              de.objectcode.flowws.spi.filter.IFilterBuilder#equalsExpr(java.lang.String, int)
   */
  public void compareExpr( String attribute, int operation, int value )
  {
    m_stack.addLast( "o." + attribute + " " + g_operators[operation] + " ?" );
    m_binds.add( new Bind( Hibernate.INTEGER, new Integer( value ) ) );
  }


  /**
   * @param attribute  Description of the Parameter
   * @param operation  Description of the Parameter
   * @param value      Description of the Parameter
   * @see              de.objectcode.flowws.spi.filter.IFilterBuilder#equalsExpr(java.lang.String, long)
   */
  public void compareExpr( String attribute, int operation, long value )
  {
    m_stack.addLast( "o." + attribute + " " + g_operators[operation] + " ?" );
    m_binds.add( new Bind( Hibernate.LONG, new Long( value ) ) );
  }


  /**
   * @param attribute  Description of the Parameter
   * @param operation  Description of the Parameter
   * @param value      Description of the Parameter
   * @see              de.objectcode.flowws.spi.filter.IFilterBuilder#equalsExpr(java.lang.String, java.lang.String)
   */
  public void compareExpr( String attribute, int operation, String value )
  {
    m_stack.addLast( "o." + attribute + " " + g_operators[operation] + " ?" );
    m_binds.add( new Bind( Hibernate.STRING, value ) );
  }


  /**
   * @param attribute  Description of the Parameter
   * @param operation  Description of the Parameter
   * @param value      Description of the Parameter
   * @see              de.objectcode.flowws.spi.filter.IFilterBuilder#equalsIgnoreCaseExpr(java.lang.String, java.lang.String)
   */
  public void compareIgnoreCaseExpr( String attribute, int operation, String value )
  {
    m_stack.addLast( "upper(o." + attribute + ") " + g_operators[operation] + " upper(?)" );
    m_binds.add( new Bind( Hibernate.STRING, value ) );
  }


  /*
   *  (non-Javadoc)
   *  @see de.objectcode.flowws.spi.filter.IFilterBuilder#inExpr(java.lang.String, java.util.Date[])
   */
  /**
   * Description of the Method
   *
   * @param attribute  Description of the Parameter
   * @param values     Description of the Parameter
   */
  public void inExpr( String attribute, Date[] values )
  {
    // TODO Auto-generated method stub

  }


  /*
   *  (non-Javadoc)
   *  @see de.objectcode.flowws.spi.filter.IFilterBuilder#inExpr(java.lang.String, double[])
   */
  /**
   * Description of the Method
   *
   * @param attribute  Description of the Parameter
   * @param values     Description of the Parameter
   */
  public void inExpr( String attribute, double[] values )
  {
    // TODO Auto-generated method stub

  }


  /*
   *  (non-Javadoc)
   *  @see de.objectcode.flowws.spi.filter.IFilterBuilder#inExpr(java.lang.String, int[])
   */
  /**
   * Description of the Method
   *
   * @param attribute  Description of the Parameter
   * @param values     Description of the Parameter
   */
  public void inExpr( String attribute, int[] values )
  {
  	int i;
  	StringBuffer buf = new StringBuffer();
  	buf.append("o." + attribute + " in( ");
  	for(i=0; i<values.length; i++) {
  		if(i>0){
  			buf.append(",");
  		}  		
  		buf.append(values[i]);
  	}
  	buf.append(") ");
  	m_stack.addLast(buf.toString());
  }


  /*
   *  (non-Javadoc)
   *  @see de.objectcode.flowws.spi.filter.IFilterBuilder#inExpr(java.lang.String, java.lang.String[])
   */
  /**
   * Description of the Method
   *
   * @param attribute  Description of the Parameter
   * @param values     Description of the Parameter
   */
  public void inExpr( String attribute, String[] values )
  {
  	int i;
  	StringBuffer buf = new StringBuffer();
  	buf.append("o." + attribute + " in( ");
  	for(i=0; i<values.length; i++) {
  		if(i>0){
  			buf.append(",");
  		}
  		buf.append("'" + values[i] + "'");
  	}
  	buf.append(") ");
  	m_stack.addLast(buf.toString());
  }


  /*
   *  (non-Javadoc)
   *  @see de.objectcode.flowws.spi.filter.IFilterBuilder#likeExpr(java.lang.String, java.lang.String)
   */
  /**
   * Description of the Method
   *
   * @param attribute  Description of the Parameter
   * @param value      Description of the Parameter
   */
  public void likeExpr( String attribute, String value )
  {
    m_stack.addLast( "o." + attribute + " like ?" );
    m_binds.add( new Bind( Hibernate.STRING, value ) );
  }


  /*
   *  (non-Javadoc)
   *  @see de.objectcode.flowws.spi.filter.IFilterBuilder#likeIgnoreCaseExpr(java.lang.String, java.lang.String)
   */
  /**
   * Description of the Method
   *
   * @param attribute  Description of the Parameter
   * @param value      Description of the Parameter
   */
  public void likeIgnoreCaseExpr( String attribute, String value )
  {
    m_stack.addLast( "o." + attribute + " ilike ?" );
    m_binds.add( new Bind( Hibernate.STRING, value ) );
  }


  /*
   *  (non-Javadoc)
   *  @see de.objectcode.flowws.spi.filter.IFilterBuilder#orExpr()
   */
  /**
   * Description of the Method
   */
  public void orExpr()
  {
    if ( m_stack.size() > 1 ) {
      Iterator      it      = m_stack.iterator();
      StringBuffer  buffer  = new StringBuffer( "( " + it.next() );

      while ( it.hasNext() ) {
        buffer.append( " or " );
        buffer.append( it.next() );
      }

      buffer.append( " )" );

      m_stack.clear();
      m_stack.addLast( buffer.toString() );
    }
  }


  /**
   * Description of the Method
   */
  public void notExpr()
  {
    String  last  = ( String ) m_stack.removeLast();

    m_stack.addLast( "not ( " + last + " )" );
  }


  /**
   * Description of the Class
   *
   * @author    junglas
   * @created   2. Dezember 2003
   */
  static class Bind
  {
    Type    m_type;
    Object  m_value;


    /**
     *Constructor for the Bind object
     *
     * @param type   Description of the Parameter
     * @param value  Description of the Parameter
     */
    Bind( Type type, Object value )
    {
      m_type = type;
      m_value = value;
    }


    /**
     * @return
     */
    public Type getType()
    {
      return m_type;
    }


    /**
     * @return
     */
    public Object getValue()
    {
      return m_value;
    }


    /**
     * Description of the Method
     *
     * @return   Description of the Return Value
     */
    public String toString()
    {
      return "Bind[" + m_type + ", " + m_value + "]";
    }
  }
}
