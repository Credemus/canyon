package de.objectcode.canyon.persistent.filter;

import de.objectcode.canyon.spi.filter.IFilterBuilder;
import net.sf.hibernate.Hibernate;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;
import net.sf.hibernate.type.Type;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.*;

/**
 * @author junglas
 * @created 15. Oktober 2003
 */
public class CountQueryBuilder implements IFilterBuilder {
  private final static Log log = LogFactory.getLog(CountQueryBuilder.class);

  private final static String g_operators[] = new String[]{
          "=",
          ">",
          ">=",
          "<",
          "<=",
          "!="
  };

  private LinkedList<String> m_stack = new LinkedList<String>();
  private ArrayList<Bind> m_binds = new ArrayList<Bind>();


  /*
   *  (non-Javadoc)
   *  @see de.objectcode.flowws.spi.filter.IFilterBuilder#isNullExpr(java.lang.String)
   */

  /**
   * Gets the nullExpr attribute of the QueryBuilder object
   *
   * @param attribute Description of the Parameter
   */
  public void isNullExpr(String attribute) {
    m_stack.addLast("o." + attribute + " is null");
  }


  /**
   * Description of the Method
   *
   * @param session       Description of the Parameter
   * @param oid
   * @param sortAttrs
   * @param sortAscending
   * @param columnOidName
   * @param mappings
   * @return Description of the Return Value
   * @throws HibernateException Description of the Exception
   */
  public Query createQuery(Session session, long oid, String[] sortAttrs, boolean[] sortAscending, Class clazz, String columnOidName, Map mappings)
          throws HibernateException {
    if (m_stack.size() > 1) {
      throw new InternalError("Invalid stack size");
    }

    final StringBuilder postQuery = new StringBuilder(" and ( ");

    if (sortAttrs == null || sortAttrs.length == 0) {
      postQuery.append(" o." + columnOidName + " < :oid");
    } else {
      for (int i = 0; i < sortAttrs.length; i++) {
        if (i > 0) {
          postQuery.append(" or ");
        }
        postQuery.append("(");
        for (int j = 0; j < i; j++) {
          postQuery.append("o.").append(sortAttrs[j]).append(" = :").append(sortAttrs[j]).append(i);
          postQuery.append(" and ");
        }
        postQuery.append("o.").append(sortAttrs[i]);
        if (sortAscending[i]) {
          postQuery.append(" < ");
        } else {
          postQuery.append(" > ");
        }
        postQuery.append(":").append(sortAttrs[i]);
        postQuery.append(")");
      }
      postQuery.append(" or (");
      for (int j = 0; j < sortAttrs.length; j++) {
        postQuery.append("o.").append(sortAttrs[j]).append(" = :").append(sortAttrs[j]).append('X');
        postQuery.append(" and ");
      }
      postQuery.append(" o." + columnOidName + " < :oid )");
    }
    postQuery.append(" )");

    Query query = session.createQuery("select count(*) from " + clazz.getName() + " as o where " + m_stack.getFirst() + postQuery);

    if (sortAttrs == null || sortAttrs.length == 0) {
      query.setLong("oid", oid);
    } else {


      for (int i = 0; i < sortAttrs.length; i++) {
        fillQuery(sortAttrs, mappings, query, i);
      }

      query.setLong("oid", oid);
    }

    Iterator<Bind> it = m_binds.iterator();

    for (int i = 0; it.hasNext(); i++) {
      Bind bind = it.next();
      query.setParameter("p_" + bind.getAttributeName(), bind.getValue(), bind.getType());
    }

    return query;
  }


  private void fillQuery(String[] sortAttrs, Map mappings, Query query, int i) {
    final String attribName = sortAttrs[i];
    final Object mapped = mappings.get(attribName);

    if (mapped instanceof Integer) {
      final Integer _int = (Integer) mapped;
      query.setInteger(attribName, _int);
      query.setInteger(attribName + "X", _int);
      for (int j = i + 1; j < sortAttrs.length; j++) {
        query.setInteger(attribName + j, _int);
      }
    } else if (mapped instanceof Long) {
      final Long _long = (Long) mapped;
      query.setLong(attribName, _long);
      query.setLong(attribName + "X", _long);
      for (int j = i + 1; j < sortAttrs.length; j++) {
        query.setLong(attribName + j, _long);
      }
    } else if (mapped instanceof Double) {
      final Double _double = (Double) mapped;
      query.setDouble(attribName, _double);
      query.setDouble(attribName + "X", _double);
      for (int j = i + 1; j < sortAttrs.length; j++) {
        query.setDouble(attribName + j, _double);
      }
    } else if (mapped instanceof String) {
      final String str = (String) mapped;
      query.setString(attribName, str);
      query.setString(attribName + "X", str);
      for (int j = i + 1; j < sortAttrs.length; j++) {
        query.setString(attribName + j, str);
      }
    } else if (mapped instanceof Date) {
      final Date date = (Date) mapped;
      query.setDate(attribName, date);
      query.setDate(attribName + "X", date);
      for (int j = i + 1; j < sortAttrs.length; j++) {
        query.setDate(attribName + j, date);
      }
    } else if (mapped instanceof Timestamp) {
      final Timestamp tsp = (Timestamp) mapped;
      query.setTimestamp(attribName, tsp);
      query.setTimestamp(attribName + "X", tsp);
      for (int j = i + 1; j < sortAttrs.length; j++) {
        query.setTimestamp(attribName + j, tsp);
      }
    } else if (mapped instanceof Time) {
      final Time time = (Time) mapped;
      query.setTime(attribName, time);
      query.setTime(attribName + "X", time);
      for (int j = i + 1; j < sortAttrs.length; j++) {
        query.setTime(attribName + j, time);
      }
    }
  }


  /*
   *  (non-Javadoc)
   *  @see de.objectcode.flowws.spi.filter.IFilterBuilder#andExpr()
   */

  /**
   * Description of the Method
   */
  public void andExpr() {
    if (m_stack.size() > 1) {
      Iterator it = m_stack.iterator();
      StringBuffer buffer = new StringBuffer("( " + it.next());

      while (it.hasNext()) {
        buffer.append(" and ");
        buffer.append(it.next());
      }

      buffer.append(" )");

      m_stack.clear();
      m_stack.addLast(buffer.toString());
    }
  }


  /*
   *  (non-Javadoc)
   *  @see de.objectcode.flowws.spi.filter.IFilterBuilder#betweenExpr(java.lang.String, java.util.Date, java.util.Date)
   */

  /**
   * Description of the Method
   *
   * @param attribute Description of the Parameter
   * @param value1    Description of the Parameter
   * @param value2    Description of the Parameter
   */
  public void betweenExpr(String attribute, Date value1, Date value2) {
    m_stack.addLast("( o." + attribute + " >= :p_" + attribute + " and o." + attribute + " <= :p_" + attribute + " )");
    m_binds.add(new Bind(attribute, Hibernate.DATE, value1));
    m_binds.add(new Bind(attribute, Hibernate.DATE, value2));
  }


  /*
   *  (non-Javadoc)
   *  @see de.objectcode.flowws.spi.filter.IFilterBuilder#betweenExpr(java.lang.String, double, double)
   */

  /**
   * Description of the Method
   *
   * @param attribute Description of the Parameter
   * @param value1    Description of the Parameter
   * @param value2    Description of the Parameter
   */
  public void betweenExpr(String attribute, double value1, double value2) {
    m_stack.addLast("( o." + attribute + " >= :p_" + attribute + " and o." + attribute + " <= :p_" + attribute + " )");
    m_binds.add(new Bind(attribute, Hibernate.DOUBLE, value1));
    m_binds.add(new Bind(attribute, Hibernate.DOUBLE, value2));
  }


  /*
   *  (non-Javadoc)
   *  @see de.objectcode.flowws.spi.filter.IFilterBuilder#betweenExpr(java.lang.String, int, int)
   */

  /**
   * Description of the Method
   *
   * @param attribute Description of the Parameter
   * @param value1    Description of the Parameter
   * @param value2    Description of the Parameter
   */
  public void betweenExpr(String attribute, int value1, int value2) {
    m_stack.addLast("( o." + attribute + " >= :p_" + attribute + " and o." + attribute + " <= :p_" + attribute + " )");
    m_binds.add(new Bind(attribute, Hibernate.INTEGER, value1));
    m_binds.add(new Bind(attribute, Hibernate.INTEGER, value2));
  }


  /*
   *  (non-Javadoc)
   *  @see de.objectcode.flowws.spi.filter.IFilterBuilder#betweenExpr(java.lang.String, java.lang.String, java.lang.String)
   */

  /**
   * Description of the Method
   *
   * @param attribute Description of the Parameter
   * @param value1    Description of the Parameter
   * @param value2    Description of the Parameter
   */
  public void betweenExpr(String attribute, String value1, String value2) {
    m_stack.addLast("( o." + attribute + " >= :p_" + attribute + " and o." + attribute + " <= :p_" + attribute + " )");
    m_binds.add(new Bind(attribute, Hibernate.STRING, value1));
    m_binds.add(new Bind(attribute, Hibernate.STRING, value2));
  }


  /**
   * @param attribute Description of the Parameter
   * @param value1    Description of the Parameter
   * @param value2    Description of the Parameter
   */
  public void betweenIgnoreCaseExpr(
          String attribute,
          String value1,
          String value2) {
    // TODO Auto-generated method stub

  }


  /**
   * @param attribute Description of the Parameter
   * @param operation Description of the Parameter
   * @param value     Description of the Parameter
   */
  public void compareExpr(String attribute, int operation, boolean value) {
    m_stack.addLast("o." + attribute + " " + g_operators[operation] + " :p_" + attribute + "");
    m_binds.add(new Bind(attribute, Hibernate.BOOLEAN, value));
  }


  /**
   * @param attribute Description of the Parameter
   * @param operation Description of the Parameter
   * @param value     Description of the Parameter
   */
  public void compareExpr(String attribute, int operation, Date value) {
    m_stack.addLast("o." + attribute + " " + g_operators[operation] + " :p_" + attribute + "");
    m_binds.add(new Bind(attribute, Hibernate.TIMESTAMP, value));
  }


  /**
   * @param attribute Description of the Parameter
   * @param operation Description of the Parameter
   * @param value     Description of the Parameter
   */
  public void compareExpr(String attribute, int operation, double value) {
    m_stack.addLast("o." + attribute + " " + g_operators[operation] + " :p_" + attribute + "");
    m_binds.add(new Bind(attribute, Hibernate.DOUBLE, value));
  }


  /**
   * @param attribute Description of the Parameter
   * @param operation Description of the Parameter
   * @param value     Description of the Parameter
   */
  public void compareExpr(String attribute, int operation, int value) {
    m_stack.addLast("o." + attribute + " " + g_operators[operation] + " :p_" + attribute + "");
    m_binds.add(new Bind(attribute, Hibernate.INTEGER, value));
  }


  /**
   * @param attribute Description of the Parameter
   * @param operation Description of the Parameter
   * @param value     Description of the Parameter
   */
  public void compareExpr(String attribute, int operation, long value) {
    m_stack.addLast("o." + attribute + " " + g_operators[operation] + " :p_" + attribute + "");
    m_binds.add(new Bind(attribute, Hibernate.LONG, value));
  }


  /**
   * @param attribute Description of the Parameter
   * @param operation Description of the Parameter
   * @param value     Description of the Parameter
   */
  public void compareExpr(String attribute, int operation, String value) {
    m_stack.addLast("o." + attribute + " " + g_operators[operation] + " :p_" + attribute + "");
    m_binds.add(new Bind(attribute, Hibernate.STRING, value));
  }


  /**
   * @param attribute Description of the Parameter
   * @param operation Description of the Parameter
   * @param value     Description of the Parameter
   */
  public void compareIgnoreCaseExpr(String attribute, int operation, String value) {
    m_stack.addLast("upper(o." + attribute + ") " + g_operators[operation] + " upper(:p_" + attribute + ")");
    m_binds.add(new Bind(attribute, Hibernate.STRING, value));
  }


  /*
   *  (non-Javadoc)
   *  @see de.objectcode.flowws.spi.filter.IFilterBuilder#inExpr(java.lang.String, java.util.Date[])
   */

  /**
   * Description of the Method
   *
   * @param attribute Description of the Parameter
   * @param values    Description of the Parameter
   */
  public void inExpr(String attribute, Date[] values) {
    // TODO Auto-generated method stub

  }


  /*
   *  (non-Javadoc)
   *  @see de.objectcode.flowws.spi.filter.IFilterBuilder#inExpr(java.lang.String, double[])
   */

  /**
   * Description of the Method
   *
   * @param attribute Description of the Parameter
   * @param values    Description of the Parameter
   */
  public void inExpr(String attribute, double[] values) {
    // TODO Auto-generated method stub

  }


  /*
   *  (non-Javadoc)
   *  @see de.objectcode.flowws.spi.filter.IFilterBuilder#inExpr(java.lang.String, int[])
   */

  /**
   * Description of the Method
   *
   * @param attribute Description of the Parameter
   * @param values    Description of the Parameter
   */
  public void inExpr(String attribute, int[] values) {
    int i;
    StringBuilder buf = new StringBuilder();
    buf.append("o." + attribute + " in( ");
    for (i = 0; i < values.length; i++) {
      if (i > 0) {
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
   * @param attribute Description of the Parameter
   * @param values    Description of the Parameter
   */
  public void inExpr(String attribute, String[] values) {
    int i;
    StringBuilder buf = new StringBuilder();
    buf.append("o." + attribute + " in( ");
    for (i = 0; i < values.length; i++) {
      if (i > 0) {
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
   * @param attribute Description of the Parameter
   * @param value     Description of the Parameter
   */
  public void likeExpr(String attribute, String value) {
    m_stack.addLast("o." + attribute + " like :p_" + attribute + "");
    m_binds.add(new Bind(attribute, Hibernate.STRING, value));
  }


  /*
   *  (non-Javadoc)
   *  @see de.objectcode.flowws.spi.filter.IFilterBuilder#likeIgnoreCaseExpr(java.lang.String, java.lang.String)
   */

  /**
   * Description of the Method
   *
   * @param attribute Description of the Parameter
   * @param value     Description of the Parameter
   */
  public void likeIgnoreCaseExpr(String attribute, String value) {
    m_stack.addLast("o." + attribute + " ilike :p_" + attribute + "");
    m_binds.add(new Bind(attribute, Hibernate.STRING, value));
  }


  /*
   *  (non-Javadoc)
   *  @see de.objectcode.flowws.spi.filter.IFilterBuilder#orExpr()
   */

  /**
   * Description of the Method
   */
  public void orExpr() {
    if (m_stack.size() > 1) {
      Iterator<String> it = m_stack.iterator();
      StringBuilder buffer = new StringBuilder("( " + it.next());

      while (it.hasNext()) {
        buffer.append(" or ");
        buffer.append(it.next());
      }

      buffer.append(" )");

      m_stack.clear();
      m_stack.addLast(buffer.toString());
    }
  }


  /**
   * Description of the Method
   */
  public void notExpr() {
    String last = (String) m_stack.removeLast();

    m_stack.addLast("not ( " + last + " )");
  }


  /**
   * Description of the Class
   *
   * @author junglas
   * @created 2. Dezember 2003
   */
  static class Bind {
    Type m_type;
    Object m_value;
    String m_attributeName;


    /**
     * Constructor for the Bind object
     *
     * @param type  Description of the Parameter
     * @param value Description of the Parameter
     */
    Bind(String attributeName, Type type, Object value) {
      m_type = type;
      m_value = value;
      m_attributeName = attributeName;
    }


    /**
     * @return
     */
    public Type getType() {
      return m_type;
    }


    /**
     * @return
     */
    public Object getValue() {
      return m_value;
    }


    /**
     * Description of the Method
     *
     * @return Description of the Return Value
     */
    public String toString() {
      return "Bind[" + m_type + ", " + m_value + "]";
    }


    public String getAttributeName() {
      return m_attributeName;
    }


    public void setAttributeName(String attributeName) {
      m_attributeName = attributeName;
    }
  }
}
