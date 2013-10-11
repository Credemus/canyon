package de.objectcode.jdbcfix;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.util.Properties;

/**
 * @author    junglas
 * @created   17. Dezember 2003
 */
public class DriverFix implements Driver
{
  private  Driver  m_realDriver;


  /**
   *Constructor for the Driver object
   */
  public DriverFix()
  {
    try {
      Class.forName( "com.microsoft.jdbc.sqlserver.SQLServerDriver" );
    }
    catch ( Exception e ) {
      e.printStackTrace();
    }
  }


  /**
   * Gets the realDriver attribute of the DriverFix object
   *
   * @param url  Description of the Parameter
   * @return     The realDriver value
   */
  public Driver getRealDriver( String url )
  {
    if ( m_realDriver == null ) {
      try {
        m_realDriver = DriverManager.getDriver( getRealUrl( url ) );
      }
      catch ( Exception e ) {
        e.printStackTrace();
      }
    }

    return m_realDriver;
  }


  /**
   * Gets the realUrl attribute of the DriverFix object
   *
   * @param url  Description of the Parameter
   * @return     The realUrl value
   */
  public String getRealUrl( String url )
  {
    return "jdbc" + url.substring( 8 );
  }


  /**
   * @return   The majorVersion value
   * @see      java.sql.Driver#getMajorVersion()
   */
  public int getMajorVersion()
  {
    return 0;
  }


  /**
   * @return   The minorVersion value
   * @see      java.sql.Driver#getMinorVersion()
   */
  public int getMinorVersion()
  {
    return 0;
  }


  /**
   * @param url               Description of the Parameter
   * @param info              Description of the Parameter
   * @return                  The propertyInfo value
   * @exception SQLException  Description of the Exception
   * @see                     java.sql.Driver#getPropertyInfo(java.lang.String, java.util.Properties)
   */
  public DriverPropertyInfo[] getPropertyInfo( String url, Properties info )
    throws SQLException
  {
    return getRealDriver( url ).getPropertyInfo( getRealUrl(url), info );
  }


  /**
   * @param url               Description of the Parameter
   * @return                  Description of the Return Value
   * @exception SQLException  Description of the Exception
   * @see                     java.sql.Driver#acceptsURL(java.lang.String)
   */
  public boolean acceptsURL( String url )
    throws SQLException
  {
    if ( !url.startsWith("jdbc:fix") )
      return false;
      
    return getRealDriver( url ).acceptsURL( getRealUrl(url) );
  }


  /**
   * @param url               Description of the Parameter
   * @param info              Description of the Parameter
   * @return                  Description of the Return Value
   * @exception SQLException  Description of the Exception
   * @see                     java.sql.Driver#connect(java.lang.String, java.util.Properties)
   */
  public Connection connect( String url, Properties info )
    throws SQLException
  {
    return new ConnectionFix( getRealDriver( url ).connect( getRealUrl(url), info ) );
  }


  /**
   * @return   Description of the Return Value
   * @see      java.sql.Driver#jdbcCompliant()
   */
  public boolean jdbcCompliant()
  {
    return true;
  }

  static {
    try {
      DriverManager.registerDriver( new DriverFix() );
    }
    catch ( Exception e ) {
      e.printStackTrace();
    }
  }

}
