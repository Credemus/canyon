package de.objectcode.jdbcfix;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Map;

/**
 * @author junglas
 */
public class ResultSetFix implements ResultSet 
{
  private ResultSet m_resultSet;
  private ResultSetMetaData m_meta;
  
  public ResultSetFix ( ResultSet resultSet )
    throws SQLException
  {
    m_resultSet = resultSet;

    m_meta = m_resultSet.getMetaData();
  }
  
  /** (non-Javadoc)
   * @see java.lang.Object#hashCode()
   */
  public int hashCode() {
    return m_resultSet.hashCode();
  }

  /**
   * @param columnName
   * @param x
   * @throws java.sql.SQLException
   */
  public void updateArray(String columnName, Array x) throws SQLException {
    m_resultSet.updateArray(columnName, x);
  }

  /**
   * @param columnIndex
   * @param x
   * @throws java.sql.SQLException
   */
  public void updateTime(int columnIndex, Time x) throws SQLException {
    m_resultSet.updateTime(columnIndex, x);
  }

  /**
   * @param row
   * @return
   * @throws java.sql.SQLException
   */
  public boolean absolute(int row) throws SQLException {
    return m_resultSet.absolute(row);
  }

  /**
   * @param columnName
   * @param x
   * @throws java.sql.SQLException
   */
  public void updateBoolean(String columnName, boolean x) throws SQLException {
    m_resultSet.updateBoolean(columnName, x);
  }

  /**
   * @param columnIndex
   * @return
   * @throws java.sql.SQLException
   */
  public Timestamp getTimestamp(int columnIndex) throws SQLException {
    return m_resultSet.getTimestamp(columnIndex);
  }

  /**
   * @return
   * @throws java.sql.SQLException
   */
  public int getConcurrency() throws SQLException {
    return m_resultSet.getConcurrency();
  }

  /**
   * @param columnName
   * @throws java.sql.SQLException
   */
  public void updateNull(String columnName) throws SQLException {
    m_resultSet.updateNull(columnName);
  }

  /**
   * @param columnName
   * @return
   * @throws java.sql.SQLException
   */
  public String getString(String columnName) throws SQLException {
    return m_resultSet.getString(columnName);
  }

  /**
   * @param columnName
   * @return
   * @throws java.sql.SQLException
   */
  public boolean getBoolean(String columnName) throws SQLException {
    return m_resultSet.getBoolean(columnName);
  }

  /**
   * @param columnIndex
   * @param x
   * @throws java.sql.SQLException
   */
  public void updateBlob(int columnIndex, Blob x) throws SQLException {
    m_resultSet.updateBlob(columnIndex, x);
  }

  /**
   * @param columnIndex
   * @return
   * @throws java.sql.SQLException
   */
  public Object getObject(int columnIndex) throws SQLException {
    return m_resultSet.getObject(columnIndex);
  }

  /**
   * @param colName
   * @param map
   * @return
   * @throws java.sql.SQLException
   */
  public Object getObject(String colName, Map map) throws SQLException {
    return m_resultSet.getObject(colName, map);
  }

  /**
   * @return
   * @throws java.sql.SQLException
   */
  public boolean isFirst() throws SQLException {
    return m_resultSet.isFirst();
  }

  /**
   * @param columnIndex
   * @return
   * @throws java.sql.SQLException
   */
  public Time getTime(int columnIndex) throws SQLException {
    return m_resultSet.getTime(columnIndex);
  }

  /**
   * @param columnIndex
   * @return
   * @throws java.sql.SQLException
   */
  public URL getURL(int columnIndex) throws SQLException {
    return m_resultSet.getURL(columnIndex);
  }

  /**
   * @param columnIndex
   * @param cal
   * @return
   * @throws java.sql.SQLException
   */
  public Timestamp getTimestamp(int columnIndex, Calendar cal)
    throws SQLException {
    return m_resultSet.getTimestamp(columnIndex, cal);
  }

  /**
   * @param colName
   * @return
   * @throws java.sql.SQLException
   */
  public Blob getBlob(String colName) throws SQLException {
    return m_resultSet.getBlob(colName);
  }

  /**
   * @throws java.sql.SQLException
   */
  public void updateRow() throws SQLException {
    m_resultSet.updateRow();
  }

  /**
   * @param columnName
   * @param x
   * @throws java.sql.SQLException
   */
  public void updateShort(String columnName, short x) throws SQLException {
    m_resultSet.updateShort(columnName, x);
  }

  /**
   * @param columnIndex
   * @return
   * @throws java.sql.SQLException
   */
  public byte[] getBytes(int columnIndex) throws SQLException {
    return m_resultSet.getBytes(columnIndex);
  }

  /**
   * @param columnIndex
   * @param cal
   * @return
   * @throws java.sql.SQLException
   */
  public Date getDate(int columnIndex, Calendar cal) throws SQLException {
    return m_resultSet.getDate(columnIndex, cal);
  }

  /**
   * @param i
   * @return
   * @throws java.sql.SQLException
   */
  public Clob getClob(int i) throws SQLException {
    return m_resultSet.getClob(i);
  }

  /**
   * @throws java.sql.SQLException
   */
  public void cancelRowUpdates() throws SQLException {
    m_resultSet.cancelRowUpdates();
  }

  /**
   * @return
   * @throws java.sql.SQLException
   */
  public boolean first() throws SQLException {
    return m_resultSet.first();
  }

  /**
   * @param columnIndex
   * @param x
   * @throws java.sql.SQLException
   */
  public void updateInt(int columnIndex, int x) throws SQLException {
    m_resultSet.updateInt(columnIndex, x);
  }

  /**
   * @param columnName
   * @return
   * @throws java.sql.SQLException
   */
  public byte[] getBytes(String columnName) throws SQLException {
    return m_resultSet.getBytes(columnName);
  }

  /**
   * @param columnName
   * @param x
   * @throws java.sql.SQLException
   */
  public void updateRef(String columnName, Ref x) throws SQLException {
    m_resultSet.updateRef(columnName, x);
  }

  /* (non-Javadoc)
   * @see java.lang.Object#equals(java.lang.Object)
   */
  public boolean equals(Object obj) {
    return m_resultSet.equals(obj);
  }

  /**
   * @param rows
   * @return
   * @throws java.sql.SQLException
   */
  public boolean relative(int rows) throws SQLException {
    return m_resultSet.relative(rows);
  }

  /**
   * @return
   * @throws java.sql.SQLException
   */
  public String getCursorName() throws SQLException {
    return m_resultSet.getCursorName();
  }

  /**
   * @return
   * @throws java.sql.SQLException
   */
  public boolean rowDeleted() throws SQLException {
    return m_resultSet.rowDeleted();
  }

  /**
   * @param columnIndex
   * @param x
   * @throws java.sql.SQLException
   */
  public void updateLong(int columnIndex, long x) throws SQLException {
    m_resultSet.updateLong(columnIndex, x);
  }

  /**
   * @param columnIndex
   * @return
   * @throws java.sql.SQLException
   */
  public String getString(int columnIndex) throws SQLException {
    return m_resultSet.getString(columnIndex);
  }

  /**
   * @param i
   * @return
   * @throws java.sql.SQLException
   */
  public Blob getBlob(int i) throws SQLException {
    return m_resultSet.getBlob(i);
  }

  /**
   * @param columnName
   * @param x
   * @throws java.sql.SQLException
   */
  public void updateDate(String columnName, Date x) throws SQLException {
    m_resultSet.updateDate(columnName, x);
  }

  /**
   * @param columnName
   * @param cal
   * @return
   * @throws java.sql.SQLException
   */
  public Timestamp getTimestamp(String columnName, Calendar cal)
    throws SQLException {
    return m_resultSet.getTimestamp(columnName, cal);
  }

  /**
   * @throws java.sql.SQLException
   */
  public void clearWarnings() throws SQLException {
    m_resultSet.clearWarnings();
  }

  /**
   * @param columnIndex
   * @return
   * @throws java.sql.SQLException
   */
  public InputStream getUnicodeStream(int columnIndex) throws SQLException {
    return m_resultSet.getUnicodeStream(columnIndex);
  }

  /**
   * @param columnName
   * @param x
   * @throws java.sql.SQLException
   */
  public void updateClob(String columnName, Clob x) throws SQLException {
    m_resultSet.updateClob(columnName, x);
  }

  /**
   * @param columnIndex
   * @param cal
   * @return
   * @throws java.sql.SQLException
   */
  public Time getTime(int columnIndex, Calendar cal) throws SQLException {
    return m_resultSet.getTime(columnIndex, cal);
  }

  /**
   * @throws java.sql.SQLException
   */
  public void refreshRow() throws SQLException {
    m_resultSet.refreshRow();
  }

  /**
   * @param columnName
   * @param x
   * @throws java.sql.SQLException
   */
  public void updateFloat(String columnName, float x) throws SQLException {
    m_resultSet.updateFloat(columnName, x);
  }

  /**
   * @throws java.sql.SQLException
   */
  public void close() throws SQLException {
    m_resultSet.close();
  }

  /**
   * @return
   * @throws java.sql.SQLException
   */
  public boolean rowInserted() throws SQLException {
    return m_resultSet.rowInserted();
  }

  /**
   * @return
   * @throws java.sql.SQLException
   */
  public int getType() throws SQLException {
    return m_resultSet.getType();
  }

  /**
   * @param columnName
   * @return
   * @throws java.sql.SQLException
   */
  public long getLong(String columnName) throws SQLException {
System.out.println(">>>>>>>> " + columnName + " " + findColumn(columnName) + " " + m_meta.getColumnLabel(findColumn(columnName)) + " " + m_meta.getColumnName(findColumn(columnName)));
    return m_resultSet.getLong(columnName);
  }

  /**
   * @return
   * @throws java.sql.SQLException
   */
  public ResultSetMetaData getMetaData() throws SQLException {
    return m_meta;
  }

  /**
   * @param columnIndex
   * @param x
   * @throws java.sql.SQLException
   */
  public void updateTimestamp(int columnIndex, Timestamp x)
    throws SQLException {
    m_resultSet.updateTimestamp(columnIndex, x);
  }

  /**
   * @param columnIndex
   * @param scale
   * @return
   * @throws java.sql.SQLException
   */
  public BigDecimal getBigDecimal(int columnIndex, int scale)
    throws SQLException {
    return m_resultSet.getBigDecimal(columnIndex, scale);
  }

  /**
   * @param columnIndex
   * @param x
   * @throws java.sql.SQLException
   */
  public void updateDate(int columnIndex, Date x) throws SQLException {
    m_resultSet.updateDate(columnIndex, x);
  }

  /**
   * @param columnIndex
   * @return
   * @throws java.sql.SQLException
   */
  public float getFloat(int columnIndex) throws SQLException {
    return m_resultSet.getFloat(columnIndex);
  }

  /**
   * @param columnName
   * @return
   * @throws java.sql.SQLException
   */
  public int findColumn(String columnName) throws SQLException {
    return m_resultSet.findColumn(columnName);
  }

  /**
   * @return
   * @throws java.sql.SQLException
   */
  public boolean isBeforeFirst() throws SQLException {
    return m_resultSet.isBeforeFirst();
  }

  /**
   * @param columnName
   * @param scale
   * @return
   * @throws java.sql.SQLException
   */
  public BigDecimal getBigDecimal(String columnName, int scale)
    throws SQLException {
    return m_resultSet.getBigDecimal(columnName, scale);
  }

  /**
   * @param colName
   * @return
   * @throws java.sql.SQLException
   */
  public Ref getRef(String colName) throws SQLException {
    return m_resultSet.getRef(colName);
  }

  /**
   * @param columnName
   * @return
   * @throws java.sql.SQLException
   */
  public Timestamp getTimestamp(String columnName) throws SQLException {
    return m_resultSet.getTimestamp(columnName);
  }

  /**
   * @param columnName
   * @return
   * @throws java.sql.SQLException
   */
  public double getDouble(String columnName) throws SQLException {
    return m_resultSet.getDouble(columnName);
  }

  /**
   * @param colName
   * @return
   * @throws java.sql.SQLException
   */
  public Array getArray(String colName) throws SQLException {
    return m_resultSet.getArray(colName);
  }

  /**
   * @param columnName
   * @return
   * @throws java.sql.SQLException
   */
  public int getInt(String columnName) throws SQLException {
    return m_resultSet.getInt(columnName);
  }

  /**
   * @param columnName
   * @return
   * @throws java.sql.SQLException
   */
  public Reader getCharacterStream(String columnName) throws SQLException {
    return m_resultSet.getCharacterStream(columnName);
  }

  /**
   * @param columnName
   * @return
   * @throws java.sql.SQLException
   */
  public Date getDate(String columnName) throws SQLException {
    return m_resultSet.getDate(columnName);
  }

  /**
   * @param columnName
   * @param x
   * @throws java.sql.SQLException
   */
  public void updateBlob(String columnName, Blob x) throws SQLException {
    m_resultSet.updateBlob(columnName, x);
  }

  /**
   * @param columnName
   * @return
   * @throws java.sql.SQLException
   */
  public Time getTime(String columnName) throws SQLException {
    return m_resultSet.getTime(columnName);
  }

  /**
   * @param columnName
   * @return
   * @throws java.sql.SQLException
   */
  public URL getURL(String columnName) throws SQLException {
    return m_resultSet.getURL(columnName);
  }

  /**
   * @param columnName
   * @return
   * @throws java.sql.SQLException
   */
  public short getShort(String columnName) throws SQLException {
    return m_resultSet.getShort(columnName);
  }

  /**
   * @param columnName
   * @return
   * @throws java.sql.SQLException
   */
  public InputStream getAsciiStream(String columnName) throws SQLException {
    return m_resultSet.getAsciiStream(columnName);
  }

  /**
   * @param columnName
   * @param x
   * @throws java.sql.SQLException
   */
  public void updateLong(String columnName, long x) throws SQLException {
    m_resultSet.updateLong(columnName, x);
  }

  /**
   * @param columnName
   * @param x
   * @throws java.sql.SQLException
   */
  public void updateTime(String columnName, Time x) throws SQLException {
    m_resultSet.updateTime(columnName, x);
  }

  /**
   * @param columnIndex
   * @param x
   * @throws java.sql.SQLException
   */
  public void updateRef(int columnIndex, Ref x) throws SQLException {
    m_resultSet.updateRef(columnIndex, x);
  }

  /**
   * @param columnName
   * @return
   * @throws java.sql.SQLException
   */
  public BigDecimal getBigDecimal(String columnName) throws SQLException {
    return m_resultSet.getBigDecimal(columnName);
  }

  /**
   * @return
   * @throws java.sql.SQLException
   */
  public boolean wasNull() throws SQLException {
    return m_resultSet.wasNull();
  }

  /**
   * @param columnIndex
   * @param x
   * @throws java.sql.SQLException
   */
  public void updateByte(int columnIndex, byte x) throws SQLException {
    m_resultSet.updateByte(columnIndex, x);
  }

  /**
   * @param columnName
   * @param x
   * @throws java.sql.SQLException
   */
  public void updateObject(String columnName, Object x) throws SQLException {
    m_resultSet.updateObject(columnName, x);
  }

  /**
   * @return
   * @throws java.sql.SQLException
   */
  public int getRow() throws SQLException {
    return m_resultSet.getRow();
  }

  /**
   * @param columnIndex
   * @param x
   * @param length
   * @throws java.sql.SQLException
   */
  public void updateCharacterStream(int columnIndex, Reader x, int length)
    throws SQLException {
    m_resultSet.updateCharacterStream(columnIndex, x, length);
  }

  /**
   * @return
   * @throws java.sql.SQLException
   */
  public int getFetchSize() throws SQLException {
    return m_resultSet.getFetchSize();
  }

  /**
   * @param i
   * @param map
   * @return
   * @throws java.sql.SQLException
   */
  public Object getObject(int i, Map map) throws SQLException {
    return m_resultSet.getObject(i, map);
  }

  /**
   * @param columnIndex
   * @param x
   * @param length
   * @throws java.sql.SQLException
   */
  public void updateBinaryStream(int columnIndex, InputStream x, int length)
    throws SQLException {
    m_resultSet.updateBinaryStream(columnIndex, x, length);
  }

  /**
   * @param columnIndex
   * @param x
   * @throws java.sql.SQLException
   */
  public void updateFloat(int columnIndex, float x) throws SQLException {
    m_resultSet.updateFloat(columnIndex, x);
  }

  /**
   * @return
   * @throws java.sql.SQLException
   */
  public Statement getStatement() throws SQLException {
    return m_resultSet.getStatement();
  }

  /**
   * @param columnName
   * @param x
   * @throws java.sql.SQLException
   */
  public void updateBigDecimal(String columnName, BigDecimal x)
    throws SQLException {
    m_resultSet.updateBigDecimal(columnName, x);
  }

  /**
   * @param columnIndex
   * @return
   * @throws java.sql.SQLException
   */
  public InputStream getBinaryStream(int columnIndex) throws SQLException {
    return m_resultSet.getBinaryStream(columnIndex);
  }

  /**
   * @return
   * @throws java.sql.SQLException
   */
  public boolean previous() throws SQLException {
    return m_resultSet.previous();
  }

  /**
   * @param columnName
   * @param x
   * @param length
   * @throws java.sql.SQLException
   */
  public void updateAsciiStream(String columnName, InputStream x, int length)
    throws SQLException {
    m_resultSet.updateAsciiStream(columnName, x, length);
  }

  /**
   * @param columnIndex
   * @return
   * @throws java.sql.SQLException
   */
  public Date getDate(int columnIndex) throws SQLException {
    return m_resultSet.getDate(columnIndex);
  }

  /**
   * @throws java.sql.SQLException
   */
  public void moveToInsertRow() throws SQLException {
    m_resultSet.moveToInsertRow();
  }

  /**
   * @param columnName
   * @param x
   * @throws java.sql.SQLException
   */
  public void updateTimestamp(String columnName, Timestamp x)
    throws SQLException {
    m_resultSet.updateTimestamp(columnName, x);
  }

  /**
   * @param columnName
   * @param x
   * @throws java.sql.SQLException
   */
  public void updateString(String columnName, String x) throws SQLException {
    m_resultSet.updateString(columnName, x);
  }

  /**
   * @return
   * @throws java.sql.SQLException
   */
  public SQLWarning getWarnings() throws SQLException {
    return m_resultSet.getWarnings();
  }

  /**
   * @param columnIndex
   * @param x
   * @throws java.sql.SQLException
   */
  public void updateArray(int columnIndex, Array x) throws SQLException {
    m_resultSet.updateArray(columnIndex, x);
  }

  /**
   * @param columnIndex
   * @param x
   * @throws java.sql.SQLException
   */
  public void updateDouble(int columnIndex, double x) throws SQLException {
    m_resultSet.updateDouble(columnIndex, x);
  }

  /**
   * @param i
   * @return
   * @throws java.sql.SQLException
   */
  public Array getArray(int i) throws SQLException {
    return m_resultSet.getArray(i);
  }

  /**
   * @return
   * @throws java.sql.SQLException
   */
  public boolean last() throws SQLException {
    return m_resultSet.last();
  }

  /**
   * @param rows
   * @throws java.sql.SQLException
   */
  public void setFetchSize(int rows) throws SQLException {
    m_resultSet.setFetchSize(rows);
  }

  /**
   * @param i
   * @return
   * @throws java.sql.SQLException
   */
  public Ref getRef(int i) throws SQLException {
    return m_resultSet.getRef(i);
  }

  /**
   * @param columnIndex
   * @return
   * @throws java.sql.SQLException
   */
  public Reader getCharacterStream(int columnIndex) throws SQLException {
    return m_resultSet.getCharacterStream(columnIndex);
  }

  /**
   * @throws java.sql.SQLException
   */
  public void beforeFirst() throws SQLException {
    m_resultSet.beforeFirst();
  }

  /**
   * @param columnName
   * @param x
   * @param scale
   * @throws java.sql.SQLException
   */
  public void updateObject(String columnName, Object x, int scale)
    throws SQLException {
    m_resultSet.updateObject(columnName, x, scale);
  }

  /**
   * @return
   * @throws java.sql.SQLException
   */
  public int getFetchDirection() throws SQLException {
    return m_resultSet.getFetchDirection();
  }

  /**
   * @param columnIndex
   * @return
   * @throws java.sql.SQLException
   */
  public InputStream getAsciiStream(int columnIndex) throws SQLException {
    return m_resultSet.getAsciiStream(columnIndex);
  }

  /**
   * @throws java.sql.SQLException
   */
  public void moveToCurrentRow() throws SQLException {
    m_resultSet.moveToCurrentRow();
  }

  /**
   * @throws java.sql.SQLException
   */
  public void insertRow() throws SQLException {
    m_resultSet.insertRow();
  }

  /**
   * @param columnIndex
   * @param x
   * @throws java.sql.SQLException
   */
  public void updateObject(int columnIndex, Object x) throws SQLException {
    m_resultSet.updateObject(columnIndex, x);
  }

  /**
   * @param columnName
   * @param x
   * @throws java.sql.SQLException
   */
  public void updateBytes(String columnName, byte[] x) throws SQLException {
    m_resultSet.updateBytes(columnName, x);
  }

  /**
   * @return
   * @throws java.sql.SQLException
   */
  public boolean isAfterLast() throws SQLException {
    return m_resultSet.isAfterLast();
  }

  /**
   * @param columnIndex
   * @param x
   * @param length
   * @throws java.sql.SQLException
   */
  public void updateAsciiStream(int columnIndex, InputStream x, int length)
    throws SQLException {
    m_resultSet.updateAsciiStream(columnIndex, x, length);
  }

  /**
   * @param columnIndex
   * @param x
   * @throws java.sql.SQLException
   */
  public void updateShort(int columnIndex, short x) throws SQLException {
    m_resultSet.updateShort(columnIndex, x);
  }

  /**
   * @param columnIndex
   * @param x
   * @param scale
   * @throws java.sql.SQLException
   */
  public void updateObject(int columnIndex, Object x, int scale)
    throws SQLException {
    m_resultSet.updateObject(columnIndex, x, scale);
  }

  /**
   * @param columnIndex
   * @return
   * @throws java.sql.SQLException
   */
  public long getLong(int columnIndex) throws SQLException {
    return m_resultSet.getLong(columnIndex);
  }

  /**
   * @param columnIndex
   * @param x
   * @throws java.sql.SQLException
   */
  public void updateBoolean(int columnIndex, boolean x) throws SQLException {
    m_resultSet.updateBoolean(columnIndex, x);
  }

  /**
   * @param columnIndex
   * @param x
   * @throws java.sql.SQLException
   */
  public void updateBytes(int columnIndex, byte[] x) throws SQLException {
    m_resultSet.updateBytes(columnIndex, x);
  }

  /**
   * @param columnName
   * @return
   * @throws java.sql.SQLException
   */
  public InputStream getUnicodeStream(String columnName) throws SQLException {
    return m_resultSet.getUnicodeStream(columnName);
  }

  /**
   * @param columnIndex
   * @return
   * @throws java.sql.SQLException
   */
  public byte getByte(int columnIndex) throws SQLException {
    return m_resultSet.getByte(columnIndex);
  }

  /* (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  public String toString() {
    return m_resultSet.toString();
  }

  /**
   * @return
   * @throws java.sql.SQLException
   */
  public boolean next() throws SQLException {
    return m_resultSet.next();
  }

  /**
   * @param columnName
   * @return
   * @throws java.sql.SQLException
   */
  public Object getObject(String columnName) throws SQLException {
    return m_resultSet.getObject(columnName);
  }

  /**
   * @param columnIndex
   * @param x
   * @throws java.sql.SQLException
   */
  public void updateBigDecimal(int columnIndex, BigDecimal x)
    throws SQLException {
    m_resultSet.updateBigDecimal(columnIndex, x);
  }

  /**
   * @param columnName
   * @param x
   * @param length
   * @throws java.sql.SQLException
   */
  public void updateBinaryStream(String columnName, InputStream x, int length)
    throws SQLException {
    m_resultSet.updateBinaryStream(columnName, x, length);
  }

  /**
   * @param columnIndex
   * @return
   * @throws java.sql.SQLException
   */
  public int getInt(int columnIndex) throws SQLException {
    return m_resultSet.getInt(columnIndex);
  }

  /**
   * @return
   * @throws java.sql.SQLException
   */
  public boolean rowUpdated() throws SQLException {
    return m_resultSet.rowUpdated();
  }

  /**
   * @param columnName
   * @param reader
   * @param length
   * @throws java.sql.SQLException
   */
  public void updateCharacterStream(
    String columnName,
    Reader reader,
    int length)
    throws SQLException {
    m_resultSet.updateCharacterStream(columnName, reader, length);
  }

  /**
   * @param columnName
   * @param x
   * @throws java.sql.SQLException
   */
  public void updateDouble(String columnName, double x) throws SQLException {
    m_resultSet.updateDouble(columnName, x);
  }

  /**
   * @param columnName
   * @param cal
   * @return
   * @throws java.sql.SQLException
   */
  public Time getTime(String columnName, Calendar cal) throws SQLException {
    return m_resultSet.getTime(columnName, cal);
  }

  /**
   * @param columnIndex
   * @return
   * @throws java.sql.SQLException
   */
  public short getShort(int columnIndex) throws SQLException {
    return m_resultSet.getShort(columnIndex);
  }

  /**
   * @param columnIndex
   * @throws java.sql.SQLException
   */
  public void updateNull(int columnIndex) throws SQLException {
    m_resultSet.updateNull(columnIndex);
  }

  /**
   * @param columnIndex
   * @param x
   * @throws java.sql.SQLException
   */
  public void updateClob(int columnIndex, Clob x) throws SQLException {
    m_resultSet.updateClob(columnIndex, x);
  }

  /**
   * @param columnIndex
   * @return
   * @throws java.sql.SQLException
   */
  public BigDecimal getBigDecimal(int columnIndex) throws SQLException {
    return m_resultSet.getBigDecimal(columnIndex);
  }

  /**
   * @param columnIndex
   * @return
   * @throws java.sql.SQLException
   */
  public boolean getBoolean(int columnIndex) throws SQLException {
    return m_resultSet.getBoolean(columnIndex);
  }

  /**
   * @param direction
   * @throws java.sql.SQLException
   */
  public void setFetchDirection(int direction) throws SQLException {
    m_resultSet.setFetchDirection(direction);
  }

  /**
   * @param columnName
   * @param cal
   * @return
   * @throws java.sql.SQLException
   */
  public Date getDate(String columnName, Calendar cal) throws SQLException {
    return m_resultSet.getDate(columnName, cal);
  }

  /**
   * @param columnName
   * @return
   * @throws java.sql.SQLException
   */
  public float getFloat(String columnName) throws SQLException {
    return m_resultSet.getFloat(columnName);
  }

  /**
   * @throws java.sql.SQLException
   */
  public void deleteRow() throws SQLException {
    m_resultSet.deleteRow();
  }

  /**
   * @throws java.sql.SQLException
   */
  public void afterLast() throws SQLException {
    m_resultSet.afterLast();
  }

  /**
   * @param columnName
   * @return
   * @throws java.sql.SQLException
   */
  public InputStream getBinaryStream(String columnName) throws SQLException {
    return m_resultSet.getBinaryStream(columnName);
  }

  /**
   * @param colName
   * @return
   * @throws java.sql.SQLException
   */
  public Clob getClob(String colName) throws SQLException {
    return m_resultSet.getClob(colName);
  }

  /**
   * @param columnIndex
   * @param x
   * @throws java.sql.SQLException
   */
  public void updateString(int columnIndex, String x) throws SQLException {
    m_resultSet.updateString(columnIndex, x);
  }

  /**
   * @param columnIndex
   * @return
   * @throws java.sql.SQLException
   */
  public double getDouble(int columnIndex) throws SQLException {
    return m_resultSet.getDouble(columnIndex);
  }

  /**
   * @param columnName
   * @return
   * @throws java.sql.SQLException
   */
  public byte getByte(String columnName) throws SQLException {
    return m_resultSet.getByte(columnName);
  }

  /**
   * @return
   * @throws java.sql.SQLException
   */
  public boolean isLast() throws SQLException {
    return m_resultSet.isLast();
  }

  /**
   * @param columnName
   * @param x
   * @throws java.sql.SQLException
   */
  public void updateInt(String columnName, int x) throws SQLException {
    m_resultSet.updateInt(columnName, x);
  }

  /**
   * @param columnName
   * @param x
   * @throws java.sql.SQLException
   */
  public void updateByte(String columnName, byte x) throws SQLException {
    m_resultSet.updateByte(columnName, x);
  }

}
