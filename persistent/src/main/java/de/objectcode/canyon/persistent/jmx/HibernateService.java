//$Id: HibernateService.java,v 1.3 2004/02/24 09:59:25 junglas Exp $
package de.objectcode.canyon.persistent.jmx;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Properties;

import javax.naming.InitialContext;
import javax.sql.DataSource;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.SessionFactory;
import net.sf.hibernate.cfg.Configuration;
import net.sf.hibernate.cfg.Environment;
import net.sf.hibernate.dialect.Dialect;
import net.sf.hibernate.dialect.MySQLDialect;
import net.sf.hibernate.dialect.SQLServerDialect;
import net.sf.hibernate.tool.hbm2ddl.DatabaseMetadata;
import net.sf.hibernate.tool.hbm2ddl.SchemaExport;
import net.sf.hibernate.util.NamingHelper;
import net.sf.hibernate.util.PropertiesHelper;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.objectcode.canyon.persistent.util.OracleDialect;
import de.objectcode.canyon.persistent.util.SAPDBDialect;

/**
 * Implementation of <tt>HibernateServiceMBean</tt>. Creates a
 * <tt>SessionFactory</tt> and binds it to the specified JNDI name.<br>
 * <br>
 * All mapping documents are loaded as resources by the MBean.
 *
 * @author    John Urberg
 * @created   5. November 2003
 * @see       HibernateServiceMBean
 * @see       net.sf.hibernate.SessionFactory
 */
public class HibernateService implements HibernateServiceMBean
{

  private final static  Log         log           = LogFactory.getLog( HibernateServiceMBean.class );


	private String mapResources;
	private String boundName;
	private Properties properties = new Properties();
	
	/**
	 * The name of the current bean
	 * @return String
	 */
	public String getName() {
		return getProperty(Environment.SESSION_FACTORY_NAME);
	}
	
	public String getMapResources() {
		return mapResources;
	}
	
	public void setMapResources(String mapResources) {
		if (mapResources==null) {
			this.mapResources=null;
		}
		else {
			this.mapResources = mapResources.trim();
		}
	}
	
	public void addMapResource(String mapResource) {
		if ( mapResources==null || mapResources.length()==0 ) {
			mapResources = mapResource.trim();
		}
		else {
			mapResources += ", " + mapResource.trim();
		}
	}
	
	protected void setProperty(String propName, boolean value) {
		setProperty( propName, value ? "true" : "false" ); //Boolean.toString() only in JDK1.4
	}
	
	protected void setProperty(String propName, Integer value) {
		setProperty( propName, value==null ? null : value.toString() ); //Boolean.toString() only in JDK1.4
	}
	
	public String getDatasource() {
		return getProperty(Environment.DATASOURCE);
	}
	
	public void setDatasource(String datasource) {
		setProperty(Environment.DATASOURCE, datasource);
	}
	
	public String getDialect() {
		return getProperty(Environment.DIALECT);
	}
	
	public void setDialect(String dialect) {
		setProperty(Environment.DIALECT, dialect);
	}
	
	public String getJndiName() {
		return getProperty(Environment.SESSION_FACTORY_NAME);
	}
	
	public void setJndiName(String jndiName) {
		setProperty(Environment.SESSION_FACTORY_NAME, jndiName);
	}
	
	public boolean getShowSql() {
		return getBooleanProperty(Environment.SHOW_SQL, false);
	}
	
	public void setShowSql(boolean showSql) {
		setProperty(Environment.SHOW_SQL, showSql);
	}
	
	public String getUserName() {
		return getProperty(Environment.USER);
	}
	
	public void setUserName(String userName) {
		setProperty(Environment.USER, userName);
	}
	
	public String getPassword() {
		return getProperty(Environment.PASS);
	}
	
	public void setPassword(String password) {
		setProperty(Environment.PASS, password);
	}
	
	private static String[] parseResourceList(String resourceList) {
		return PropertiesHelper.toStringArray(resourceList, " ,\n\t\r\f");
	}
	
	public void start() throws HibernateException {
		boundName = getJndiName();
		try {
			buildSessionFactory();
		}
		catch (HibernateException he) {
			log.info( "Could not build SessionFactory using the MBean classpath - will try again using client classpath: " + he.getMessage() );
			log.debug("Error was", he);
			new SessionFactoryStub(this);
		}
	}
	
	public void stop() {
		log.info("stopping service");
		try {
			InitialContext context = NamingHelper.getInitialContext( getProperties() );
			( (SessionFactory) context.lookup(boundName) ).close();
			//context.unbind(boundName);
		}
		catch (Exception e) {
			log.warn("exception while stopping service", e);
		}
	}
	
	private Configuration getConfiguration() throws HibernateException {
		Configuration cfg = new Configuration().addProperties( getProperties() );
		String[] mappingFiles = parseResourceList( getMapResources() );
		for ( int i=0; i<mappingFiles.length; i++ ) {
			cfg.addResource( mappingFiles[i], Thread.currentThread().getContextClassLoader() );
		}
		return cfg;
	}

	public String getTransactionStrategy() {
		return getProperty(Environment.TRANSACTION_STRATEGY);
	}
	
	public String getUserTransactionName() {
		return getProperty(Environment.USER_TRANSACTION);
	}
	public void setTransactionStrategy(String txnStrategy) {
		setProperty(Environment.TRANSACTION_STRATEGY, txnStrategy);
	}
	
	public void setUserTransactionName(String utName) {
		setProperty(Environment.USER_TRANSACTION, utName);
	}
	
	public String getTransactionManagerLookupStrategy() {
		return getProperty(Environment.TRANSACTION_MANAGER_STRATEGY);
	}
	
	public void setTransactionManagerLookupStrategy(String lkpStrategy) {
		setProperty(Environment.TRANSACTION_MANAGER_STRATEGY, lkpStrategy);
	}
	
	protected Properties getProperties() {
		return properties;
	}
	public String getPropertyList() {
		return getProperties().toString();
	}
	
	public String getProperty(String property) {
		return properties.getProperty(property);
	}

	public void setProperty(String property, String value) {
		properties.setProperty(property, value);
	}

	private void setBooleanProperty(String property, boolean value) {
		setProperty(property, value ? "true" : "false");
	}
	
	private void setProperty(String property, Object value) {
		if (value==null) {
			setProperty( property, (String) null );
		}
		else {
			setProperty( property, value.toString() );
		}
	}
	
	private boolean getBooleanProperty(String property, boolean defaultVal) {
		String strVal = getProperty(property);
		return strVal==null ?
			defaultVal :
			Boolean.valueOf(strVal).booleanValue();
	}

	private Boolean getBooleanProperty(String property) {
		String strVal = getProperty(property);
		return strVal==null ?
			null :
			Boolean.valueOf(strVal);
	}

	private Integer getIntegerProperty(String property) {
		String strVal = getProperty(property);
		return strVal==null ?
			null :
			new Integer(strVal);
	}

	public void dropSchema() throws HibernateException {
		new SchemaExport( getConfiguration() ).drop(false, true);
	}

	public void createSchema() throws HibernateException {
		new SchemaExport( getConfiguration() ).create(false, true);
	}

	public boolean getBatchUpdateVersionedEnabled() {
		return getBooleanProperty(Environment.BATCH_VERSIONED_DATA, false);
	}

	public String getCacheProvider() {
		return getProperty(Environment.CACHE_PROVIDER);
	}

	public String getCacheRegionPrefix() {
		return getProperty(Environment.CACHE_REGION_PREFIX);
	}

	public String getDefaultSchema() {
		return getProperty(Environment.DEFAULT_SCHEMA);
	}

	public Boolean getGetGeneratedKeysEnabled() {
		return getBooleanProperty(Environment.USE_GET_GENERATED_KEYS);
	}

	public Integer getJdbcBatchSize() {
		return getIntegerProperty(Environment.STATEMENT_BATCH_SIZE);
	}

	public Integer getJdbcFetchSize() {
		return getIntegerProperty(Environment.STATEMENT_FETCH_SIZE);
	}

	public Integer getMaximumFetchDepth() {
		return getIntegerProperty(Environment.MAX_FETCH_DEPTH);
	}

	public boolean getMinimalPutsEnabled() {
		return getBooleanProperty(Environment.USE_MINIMAL_PUTS, false);
	}

	public boolean getQueryCacheEnabled() {
		return getBooleanProperty(Environment.USE_QUERY_CACHE, false);
	}

	public String getQuerySubstitutions() {
		return getProperty(Environment.QUERY_SUBSTITUTIONS);
	}

	public Boolean getScrollableResultSetsEnabled() {
		return getBooleanProperty(Environment.USE_SCROLLABLE_RESULTSET);
	}

	public void setBatchUpdateVersioned(boolean enabled) {
		setBooleanProperty(Environment.BATCH_VERSIONED_DATA, enabled);
	}

	public void setCacheProvider(String providerClassName) {
		setProperty(Environment.CACHE_PROVIDER, providerClassName);
	}

	public void setCacheRegionPrefix(String prefix) {
		setProperty(Environment.CACHE_REGION_PREFIX, prefix);
	}

	public void setDefaultSchema(String schema) {
		setProperty(Environment.DEFAULT_SCHEMA, schema);
	}

	public void setGetGeneratedKeysEnabled(Boolean enabled) {
		setProperty(Environment.USE_GET_GENERATED_KEYS, enabled);
	}

	public void setJdbcBatchSize(Integer batchSize) {
		setProperty(Environment.STATEMENT_BATCH_SIZE, batchSize);
	}

	public void setJdbcFetchSize(Integer fetchSize) {
		setProperty(Environment.STATEMENT_BATCH_SIZE, fetchSize);
	}

	public void setMaximumFetchDepth(Integer fetchDepth) {
		setProperty(Environment.MAX_FETCH_DEPTH, fetchDepth);
	}

	public void setMinimalPutsEnabled(boolean enabled) {
		setBooleanProperty(Environment.USE_MINIMAL_PUTS, enabled);
	}

	public void setQueryCacheEnabled(boolean enabled) {
		setBooleanProperty(Environment.USE_QUERY_CACHE, enabled);
	}

	public void setQuerySubstitutions(String querySubstitutions) {
		setProperty(Environment.QUERY_SUBSTITUTIONS, querySubstitutions);
	}

	public void setScrollableResultSetsEnabled(Boolean enabled) {
		setProperty(Environment.USE_SCROLLABLE_RESULTSET, enabled);
	}

  
	// -- removed methods
//	SessionFactory buildSessionFactory() throws HibernateException {
//		log.info( "starting service at JNDI name: " + boundName );
//		log.info( "service properties: " + properties );
//		return getConfiguration().buildSessionFactory();
//	}
	
  // ---- canyon specific stuff --
  
  /**
   * Sets the useOuterJoin attribute of the HibernateService object
   *
   * @param uoj  The new useOuterJoin value
   */
  public void setUseOuterJoin( boolean uoj )
  {
    setProperty( Environment.USE_OUTER_JOIN, uoj ? "true" : "false" );
    //Boolean.toString() only in JDK1.4
  }


  

  /**
   * Gets the useOuterJoin attribute of the HibernateService object
   *
   * @return   The useOuterJoin value
   */
  public boolean getUseOuterJoin()
  {
    String  prop  = getProperty( Environment.USE_OUTER_JOIN );
    return Boolean.valueOf( prop ).booleanValue();
  }


  /**
   * Description of the Method
   *
   * @param connection  Description of the Parameter
   * @return            Description of the Return Value
   */
  Dialect autoDetectDialect( Connection connection )
  {
    if ( log.isDebugEnabled() ) {
      log.debug( "Autodetecting dialect" );
    }
    ArrayList  ret  = new ArrayList();

    try {
      DatabaseMetaData  meta     = connection.getMetaData();

      String            name     = meta.getDatabaseProductName();

      if ( log.isDebugEnabled() ) {
        log.debug( "Found: " + name );
      }

      if ( "oracle".equalsIgnoreCase( name ) ) {
        log.debug("Using Oracle Dialect");
        return new OracleDialect();
      } else if ( "sap db".equalsIgnoreCase( name ) ) {
        log.debug("Using SAPDB Dialect");
        return new SAPDBDialect();
      } else if ( "Microsoft SQL Server".equalsIgnoreCase(name)) {
        log.debug("Using MSSQL Dialect");
        return new SQLServerDialect();
      } else if ( "mysql".equalsIgnoreCase(name)) {
        log.debug("Using MySQL Dialect");
        return new MySQLDialect();
      }
    }
    catch ( Exception e ) {
      log.error( "Exception", e );
    }

    return null;
  }


  /**
   * Description of the Method
   *
   * @param cfg  Description of the Parameter
   */
  void updateSchema( Configuration cfg )
  {
    log.debug( "Update Schema" );
    ArrayList   ret         = new ArrayList();
    Connection  connection  = null;

    try {
      InitialContext    ctx        = new InitialContext();

      DataSource        ds         = ( DataSource ) ctx.lookup( getDatasource() );

      connection = ds.getConnection();

      Dialect           dialect    = autoDetectDialect( connection );

      if ( dialect != null ) {
        cfg.setProperty( Environment.DIALECT, dialect.getClass().getName() );
      } else {
        dialect = Dialect.getDialect( cfg.getProperties() );
      }

      DatabaseMetadata  meta       = new DatabaseMetadata( connection, dialect );

      String[]          createSQL  = cfg.generateSchemaUpdateScript( dialect, meta );

      Statement         stmt       = connection.createStatement();

      for ( int j = 0; j < createSQL.length; j++ ) {
        final  String  sql  = createSQL[j];

        if ( log.isDebugEnabled() ) {
          log.debug( "Excecute: " + sql );
        }
        try {
        	stmt.executeUpdate( sql );
        } catch ( Exception ee ) {
          log.error( "Exception", ee );
        }
      }

      stmt.close();
    }
    catch ( Exception e ) {
      log.error( "Exception", e );
    }
    finally {
      if ( connection != null ) {
        try {
          connection.close();
        }
        catch ( Exception e ) {
        }
      }
    }
  }


  /**
   * Description of the Method
   *
   * @return                        Description of the Return Value
   * @exception HibernateException  Description of the Exception
   */
  SessionFactory buildSessionFactory()
    throws HibernateException
  {
    log.info( "starting service at JNDI name: " + boundName );
    log.info( "service properties: " + properties );

    Configuration  cfg           = getConfiguration();

    updateSchema( cfg );

    return cfg.buildSessionFactory();
  }
}
