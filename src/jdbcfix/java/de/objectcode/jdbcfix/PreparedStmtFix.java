package de.objectcode.jdbcfix;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;

/**
 * @author junglas
 */
public class PreparedStmtFix implements PreparedStatement{
  private PreparedStatement m_stmt;
  
  public PreparedStmtFix ( PreparedStatement stmt )
  {
    m_stmt = stmt;
  }
  
  /* (non-Javadoc)
   * @see java.lang.Object#hashCode()
   */
  public int hashCode() {
    return m_stmt.hashCode();
  }

  /**
   * @param parameterIndex
   * @param x
   * @param cal
   * @throws java.sql.SQLException
   */
  public void setTimestamp(int parameterIndex, Timestamp x, Calendar cal)
    throws SQLException {
    m_stmt.setTimestamp(parameterIndex, x, cal);
  }

  /**
   * @param sql
   * @return
   * @throws java.sql.SQLException
   */
  public boolean execute(String sql) throws SQLException {
    return m_stmt.execute(sql);
  }

  /**
   * @param parameterIndex
   * @param x
   * @throws java.sql.SQLException
   */
  public void setShort(int parameterIndex, short x) throws SQLException {
    m_stmt.setShort(parameterIndex, x);
  }

  /**
   * @param parameterIndex
   * @param x
   * @throws java.sql.SQLException
   */
  public void setLong(int parameterIndex, long x) throws SQLException {
    m_stmt.setLong(parameterIndex, x);
  }

  /**
   * @param sql
   * @param columnIndexes
   * @return
   * @throws java.sql.SQLException
   */
  public boolean execute(String sql, int[] columnIndexes) throws SQLException {
    return m_stmt.execute(sql, columnIndexes);
  }

  /**
   * @return
   * @throws java.sql.SQLException
   */
  public int getFetchSize() throws SQLException {
    return m_stmt.getFetchSize();
  }

  /**
   * @return
   * @throws java.sql.SQLException
   */
  public int getUpdateCount() throws SQLException {
    return m_stmt.getUpdateCount();
  }

  /**
   * @return
   * @throws java.sql.SQLException
   */
  public ResultSet getGeneratedKeys() throws SQLException {
    return new ResultSetFix(m_stmt.getGeneratedKeys());
  }

  /**
   * @return
   * @throws java.sql.SQLException
   */
  public ResultSet executeQuery() throws SQLException {
    return new ResultSetFix(m_stmt.executeQuery());
  }

  /**
   * @return
   * @throws java.sql.SQLException
   */
  public Connection getConnection() throws SQLException {
    return m_stmt.getConnection();
  }

  /**
   * @param parameterIndex
   * @param x
   * @throws java.sql.SQLException
   */
  public void setString(int parameterIndex, String x) throws SQLException {
    m_stmt.setString(parameterIndex, x);
  }

  /**
   * @throws java.sql.SQLException
   */
  public void cancel() throws SQLException {
    m_stmt.cancel();
  }

  /**
   * @param max
   * @throws java.sql.SQLException
   */
  public void setMaxRows(int max) throws SQLException {
    m_stmt.setMaxRows(max);
  }

  /**
   * @param parameterIndex
   * @param x
   * @param targetSqlType
   * @param scale
   * @throws java.sql.SQLException
   */
  public void setObject(
    int parameterIndex,
    Object x,
    int targetSqlType,
    int scale)
    throws SQLException {
    m_stmt.setObject(parameterIndex, x, targetSqlType, scale);
  }

  /**
   * @param seconds
   * @throws java.sql.SQLException
   */
  public void setQueryTimeout(int seconds) throws SQLException {
    m_stmt.setQueryTimeout(seconds);
  }

  /**
   * @return
   * @throws java.sql.SQLException
   */
  public SQLWarning getWarnings() throws SQLException {
    return m_stmt.getWarnings();
  }

  /**
   * @param parameterIndex
   * @param x
   * @throws java.sql.SQLException
   */
  public void setDate(int parameterIndex, Date x) throws SQLException {
    m_stmt.setDate(parameterIndex, x);
  }

  /**
   * @param sql
   * @throws java.sql.SQLException
   */
  public void addBatch(String sql) throws SQLException {
    m_stmt.addBatch(sql);
  }

  /**
   * @param parameterIndex
   * @param x
   * @param length
   * @throws java.sql.SQLException
   */
  public void setAsciiStream(int parameterIndex, InputStream x, int length)
    throws SQLException {
    m_stmt.setAsciiStream(parameterIndex, x, length);
  }

  /**
   * @param paramIndex
   * @param sqlType
   * @param typeName
   * @throws java.sql.SQLException
   */
  public void setNull(int paramIndex, int sqlType, String typeName)
    throws SQLException {
    m_stmt.setNull(paramIndex, sqlType, typeName);
  }

  /* (non-Javadoc)
   * @see java.lang.Object#equals(java.lang.Object)
   */
  public boolean equals(Object obj) {
    return m_stmt.equals(obj);
  }

  /**
   * @param sql
   * @param autoGeneratedKeys
   * @return
   * @throws java.sql.SQLException
   */
  public boolean execute(String sql, int autoGeneratedKeys)
    throws SQLException {
    return m_stmt.execute(sql, autoGeneratedKeys);
  }

  /**
   * @return
   * @throws java.sql.SQLException
   */
  public int getResultSetConcurrency() throws SQLException {
    return m_stmt.getResultSetConcurrency();
  }

  /**
   * @param rows
   * @throws java.sql.SQLException
   */
  public void setFetchSize(int rows) throws SQLException {
    m_stmt.setFetchSize(rows);
  }

  /**
   * @return
   * @throws java.sql.SQLException
   */
  public int getFetchDirection() throws SQLException {
    return m_stmt.getFetchDirection();
  }

  /**
   * @return
   * @throws java.sql.SQLException
   */
  public int executeUpdate() throws SQLException {
    return m_stmt.executeUpdate();
  }

  /**
   * @param parameterIndex
   * @param x
   * @param cal
   * @throws java.sql.SQLException
   */
  public void setDate(int parameterIndex, Date x, Calendar cal)
    throws SQLException {
    m_stmt.setDate(parameterIndex, x, cal);
  }

  /**
   * @throws java.sql.SQLException
   */
  public void clearParameters() throws SQLException {
    m_stmt.clearParameters();
  }

  /**
   * @param parameterIndex
   * @param x
   * @throws java.sql.SQLException
   */
  public void setInt(int parameterIndex, int x) throws SQLException {
    m_stmt.setInt(parameterIndex, x);
  }

  /**
   * @return
   * @throws java.sql.SQLException
   */
  public int getQueryTimeout() throws SQLException {
    return m_stmt.getQueryTimeout();
  }

  /**
   * @param parameterIndex
   * @param x
   * @throws java.sql.SQLException
   */
  public void setBytes(int parameterIndex, byte[] x) throws SQLException {
    m_stmt.setBytes(parameterIndex, x);
  }

  /**
   * @param sql
   * @return
   * @throws java.sql.SQLException
   */
  public int executeUpdate(String sql) throws SQLException {
    return m_stmt.executeUpdate(sql);
  }

  /**
   * @return
   * @throws java.sql.SQLException
   */
  public int getResultSetType() throws SQLException {
    return m_stmt.getResultSetType();
  }

  /**
   * @param parameterIndex
   * @param reader
   * @param length
   * @throws java.sql.SQLException
   */
  public void setCharacterStream(int parameterIndex, Reader reader, int length)
    throws SQLException {
    m_stmt.setCharacterStream(parameterIndex, reader, length);
  }

  /**
   * @throws java.sql.SQLException
   */
  public void clearWarnings() throws SQLException {
    m_stmt.clearWarnings();
  }

  /**
   * @param i
   * @param x
   * @throws java.sql.SQLException
   */
  public void setRef(int i, Ref x) throws SQLException {
    m_stmt.setRef(i, x);
  }

  /**
   * @param max
   * @throws java.sql.SQLException
   */
  public void setMaxFieldSize(int max) throws SQLException {
    m_stmt.setMaxFieldSize(max);
  }

  /**
   * @param sql
   * @param columnNames
   * @return
   * @throws java.sql.SQLException
   */
  public int executeUpdate(String sql, String[] columnNames)
    throws SQLException {
    return m_stmt.executeUpdate(sql, columnNames);
  }

  /**
   * @throws java.sql.SQLException
   */
  public void addBatch() throws SQLException {
    m_stmt.addBatch();
  }

  /**
   * @param parameterIndex
   * @param x
   * @throws java.sql.SQLException
   */
  public void setDouble(int parameterIndex, double x) throws SQLException {
    m_stmt.setDouble(parameterIndex, x);
  }

  /**
   * @throws java.sql.SQLException
   */
  public void close() throws SQLException {
    m_stmt.close();
  }

  /**
   * @param current
   * @return
   * @throws java.sql.SQLException
   */
  public boolean getMoreResults(int current) throws SQLException {
    return m_stmt.getMoreResults(current);
  }

  /**
   * @param parameterIndex
   * @param sqlType
   * @throws java.sql.SQLException
   */
  public void setNull(int parameterIndex, int sqlType) throws SQLException {
    m_stmt.setNull(parameterIndex, sqlType);
  }

  /**
   * @param parameterIndex
   * @param x
   * @throws java.sql.SQLException
   */
  public void setURL(int parameterIndex, URL x) throws SQLException {
    m_stmt.setURL(parameterIndex, x);
  }

  /* (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  public String toString() {
    return m_stmt.toString();
  }

  /**
   * @param parameterIndex
   * @param x
   * @param targetSqlType
   * @throws java.sql.SQLException
   */
  public void setObject(int parameterIndex, Object x, int targetSqlType)
    throws SQLException {
    m_stmt.setObject(parameterIndex, x, targetSqlType);
  }

  /**
   * @return
   * @throws java.sql.SQLException
   */
  public ParameterMetaData getParameterMetaData() throws SQLException {
    return m_stmt.getParameterMetaData();
  }

  /**
   * @return
   * @throws java.sql.SQLException
   */
  public int getMaxFieldSize() throws SQLException {
    return m_stmt.getMaxFieldSize();
  }

  /**
   * @param parameterIndex
   * @param x
   * @param length
   * @throws java.sql.SQLException
   */
  public void setBinaryStream(int parameterIndex, InputStream x, int length)
    throws SQLException {
    m_stmt.setBinaryStream(parameterIndex, x, length);
  }

  /**
   * @param enable
   * @throws java.sql.SQLException
   */
  public void setEscapeProcessing(boolean enable) throws SQLException {
    m_stmt.setEscapeProcessing(enable);
  }

  /**
   * @return
   * @throws java.sql.SQLException
   */
  public ResultSetMetaData getMetaData() throws SQLException {
    return m_stmt.getMetaData();
  }

  /**
   * @throws java.sql.SQLException
   */
  public void clearBatch() throws SQLException {
    m_stmt.clearBatch();
  }

  /**
   * @return
   * @throws java.sql.SQLException
   */
  public boolean getMoreResults() throws SQLException {
    return m_stmt.getMoreResults();
  }

  /**
   * @param parameterIndex
   * @param x
   * @throws java.sql.SQLException
   */
  public void setBoolean(int parameterIndex, boolean x) throws SQLException {
    m_stmt.setBoolean(parameterIndex, x);
  }

  /**
   * @return
   * @throws java.sql.SQLException
   */
  public int getMaxRows() throws SQLException {
    return m_stmt.getMaxRows();
  }

  /**
   * @param direction
   * @throws java.sql.SQLException
   */
  public void setFetchDirection(int direction) throws SQLException {
    m_stmt.setFetchDirection(direction);
  }

  /**
   * @return
   * @throws java.sql.SQLException
   */
  public boolean execute() throws SQLException {
    return m_stmt.execute();
  }

  /**
   * @param i
   * @param x
   * @throws java.sql.SQLException
   */
  public void setBlob(int i, Blob x) throws SQLException {
    m_stmt.setBlob(i, x);
  }

  /**
   * @param sql
   * @param columnIndexes
   * @return
   * @throws java.sql.SQLException
   */
  public int executeUpdate(String sql, int[] columnIndexes)
    throws SQLException {
    return m_stmt.executeUpdate(sql, columnIndexes);
  }

  /**
   * @param i
   * @param x
   * @throws java.sql.SQLException
   */
  public void setClob(int i, Clob x) throws SQLException {
    m_stmt.setClob(i, x);
  }

  /**
   * @param sql
   * @return
   * @throws java.sql.SQLException
   */
  public ResultSet executeQuery(String sql) throws SQLException {
    return new ResultSetFix(m_stmt.executeQuery(sql));
  }

  /**
   * @param sql
   * @param autoGeneratedKeys
   * @return
   * @throws java.sql.SQLException
   */
  public int executeUpdate(String sql, int autoGeneratedKeys)
    throws SQLException {
    return m_stmt.executeUpdate(sql, autoGeneratedKeys);
  }

  /**
   * @param parameterIndex
   * @param x
   * @throws java.sql.SQLException
   */
  public void setBigDecimal(int parameterIndex, BigDecimal x)
    throws SQLException {
    m_stmt.setBigDecimal(parameterIndex, x);
  }

  /**
   * @param parameterIndex
   * @param x
   * @throws java.sql.SQLException
   */
  public void setFloat(int parameterIndex, float x) throws SQLException {
    m_stmt.setFloat(parameterIndex, x);
  }

  /**
   * @param parameterIndex
   * @param x
   * @throws java.sql.SQLException
   */
  public void setTime(int parameterIndex, Time x) throws SQLException {
    m_stmt.setTime(parameterIndex, x);
  }

  /**
   * @return
   * @throws java.sql.SQLException
   */
  public ResultSet getResultSet() throws SQLException {
    return new ResultSetFix(m_stmt.getResultSet());
  }

  /**
   * @param parameterIndex
   * @param x
   * @throws java.sql.SQLException
   */
  public void setByte(int parameterIndex, byte x) throws SQLException {
    m_stmt.setByte(parameterIndex, x);
  }

  /**
   * @param parameterIndex
   * @param x
   * @param cal
   * @throws java.sql.SQLException
   */
  public void setTime(int parameterIndex, Time x, Calendar cal)
    throws SQLException {
    m_stmt.setTime(parameterIndex, x, cal);
  }

  /**
   * @return
   * @throws java.sql.SQLException
   */
  public int[] executeBatch() throws SQLException {
    return m_stmt.executeBatch();
  }

  /**
   * @param parameterIndex
   * @param x
   * @param length
   * @throws java.sql.SQLException
   */
  public void setUnicodeStream(int parameterIndex, InputStream x, int length)
    throws SQLException {
    m_stmt.setUnicodeStream(parameterIndex, x, length);
  }

  /**
   * @param name
   * @throws java.sql.SQLException
   */
  public void setCursorName(String name) throws SQLException {
    m_stmt.setCursorName(name);
  }

  /**
   * @param i
   * @param x
   * @throws java.sql.SQLException
   */
  public void setArray(int i, Array x) throws SQLException {
    m_stmt.setArray(i, x);
  }

  /**
   * @return
   * @throws java.sql.SQLException
   */
  public int getResultSetHoldability() throws SQLException {
    return m_stmt.getResultSetHoldability();
  }

  /**
   * @param parameterIndex
   * @param x
   * @throws java.sql.SQLException
   */
  public void setObject(int parameterIndex, Object x) throws SQLException {
    m_stmt.setObject(parameterIndex, x);
  }

  /**
   * @param parameterIndex
   * @param x
   * @throws java.sql.SQLException
   */
  public void setTimestamp(int parameterIndex, Timestamp x)
    throws SQLException {
    m_stmt.setTimestamp(parameterIndex, x);
  }

  /**
   * @param sql
   * @param columnNames
   * @return
   * @throws java.sql.SQLException
   */
  public boolean execute(String sql, String[] columnNames)
    throws SQLException {
    return m_stmt.execute(sql, columnNames);
  }

}
