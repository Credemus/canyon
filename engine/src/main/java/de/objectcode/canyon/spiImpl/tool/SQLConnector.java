package de.objectcode.canyon.spiImpl.tool;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.objectcode.canyon.model.application.Application;
import de.objectcode.canyon.model.data.BasicType;
import de.objectcode.canyon.model.data.ParameterMode;
import de.objectcode.canyon.spi.tool.BPEContext;
import de.objectcode.canyon.spi.tool.Parameter;
import de.objectcode.canyon.spi.tool.ReturnValue;

public class SQLConnector extends AbstractToolConnector {
	private final static Log log = LogFactory.getLog(SQLConnector.class);

	public static int DATASOURCE_CONNECTION_TYPE = 0;

	public static int JDBC_CONNECTION_TYPE = 1;

	protected int m_connectionType;

	protected String m_connectionUrl;

	protected String m_userName;

	protected String m_password;

	protected String m_driver;

	protected String m_query;

	protected Parameter getSingleOutParameter(Parameter[] parameters,
			BasicType basicType) {
		Parameter outParameter = null;
		for (int i = 0; i < parameters.length; i++) {
			Parameter parameter = parameters[i];
			if (parameter.mode == ParameterMode.OUT) {
				if (outParameter != null)
					return null; // has more than one out parameter
				if (parameter.dataType instanceof BasicType) {
					BasicType type = (BasicType) parameter.dataType;
					if (type == basicType) {
						outParameter = parameter;
					}
				} else {
					return null; // has a non matching out parameter
				}
			}
		}
		return outParameter;
	}

	/**
	 * Invoke the application.
	 * 
	 * @param parameters
	 *            The parameters
	 * @return Result values
	 * @throws Exception
	 *             Any Exception
	 */

	protected String getContextDescriptor(BPEContext context,
			Parameter[] parameters) {
		StringBuffer contextDescriptor = new StringBuffer("SQLConnector:");
		int i;

		contextDescriptor.append(" connectionType='").append(m_connectionType)
				.append("'");
		contextDescriptor.append(", connectionUrl='").append(m_connectionUrl)
				.append("'");
		contextDescriptor.append(", userName='").append(m_userName).append("'");
		contextDescriptor.append(", password='").append(m_password).append("'");
		contextDescriptor.append(", driver='").append(m_password).append("'");
		contextDescriptor.append(", query='").append(m_query).append("'");
		contextDescriptor.append(", parameters [");
		for (i = 0; i < parameters.length; i++) {
			if (i > 0)
				contextDescriptor.append(", ");
			contextDescriptor.append(parameters[i]);
		}
		contextDescriptor.append("]");
		return contextDescriptor.toString();
	}

	private void logResult(ReturnValue[] ret, String contextDescriptor) {
		StringBuffer buffy = new StringBuffer(contextDescriptor);
		buffy.append("={");
		if (ret == null)
			buffy.append("null");
		else {
			for (int i = 0; i < ret.length; i++) {
				if (i != 0)
					buffy.append(",");
				buffy.append(ret[i].actualName).append("=")
						.append(ret[i].value);
			}
		}
		buffy.append("}");
		log.debug(buffy.toString());
	}

	public ReturnValue[] invoke(BPEContext context, Parameter[] parameters)
			throws Exception {
		String contextDescriptor = null;
		if (log.isDebugEnabled()) {
			contextDescriptor = getContextDescriptor(context, parameters);
		}

		ReturnValue[] ret = null;
		Connection connection = null;
		PreparedStatement stmnt = null;
		ResultSet rs = null;
		int nr = -1;
		boolean isUpdate = m_query.trim().toUpperCase().startsWith("UPDATE");
		try {
			connection = getConnection(context);
			stmnt = prepareStatement(parameters, connection);

			if (isUpdate)
				nr = stmnt.executeUpdate();
			else
				rs = stmnt.executeQuery();

			if (isUpdate) {
				Parameter singleIntOutParameter = getSingleOutParameter(
						parameters, BasicType.INTEGER);
				if (singleIntOutParameter!=null) {
					ret = new ReturnValue[] { new ReturnValue(
							singleIntOutParameter.actualName,
							new Integer(nr)) };					
				} else {
					return new ReturnValue[0];
				}

			} else {
				// Statements with single out parameters have special semantics
				Parameter singleBooleanOutParameter = getSingleOutParameter(
						parameters, BasicType.BOOLEAN);
				if (singleBooleanOutParameter != null) {
					if (rs.next())
						ret = new ReturnValue[] { new ReturnValue(
								singleBooleanOutParameter.actualName,
								Boolean.TRUE) };
					else
						ret = new ReturnValue[] { new ReturnValue(
								singleBooleanOutParameter.actualName,
								Boolean.FALSE) };
				} else {
					if (rs.next()) {
						ret = handleResultSet(parameters, rs);
					} else {
						ret = handleNullSet(parameters);
					}
				}
			}
			if (log.isDebugEnabled()) {
				logResult(ret, contextDescriptor);
			}
			return ret;
		} catch (Exception e) {
			log.error("Error in SQLConnector [" + contextDescriptor + "]", e);
			throw e;
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception e) {
				}
			}
			if (stmnt != null) {
				try {
					stmnt.close();
				} catch (Exception e) {
				}
			}
			if (connection != null) {
				try {
					connection.close();
				} catch (Exception e) {
				}
			}
		}
	}

	/**
	 * @param parameters
	 * @param contextDescriptor
	 * @param connection
	 * @throws SQLException
	 * @throws IllegalArgumentException
	 * @throws Exception
	 */
	protected PreparedStatement prepareStatement(Parameter[] parameters,
			Connection connection) throws Exception {
		PreparedStatement stmnt = connection.prepareStatement(m_query);

		int i;

		int inCounter = 0;
		for (i = 0; i < parameters.length; i++) {
			if (parameters[i].mode == ParameterMode.IN) {
				inCounter++;
				if (parameters[i].value == null)
					throw new IllegalArgumentException("Parameter '"
							+ parameters[i].formalName
							+ "' not set in SQLConnector'");

				try {
					if (parameters[i].dataType instanceof BasicType) {
						BasicType type = (BasicType) parameters[i].dataType;

						switch (type.getValue()) {
						case BasicType.BOOLEAN_INT:
							stmnt.setBoolean(inCounter, new Boolean(
									parameters[i].value.toString())
									.booleanValue());
							break;
						case BasicType.FLOAT_INT:
							stmnt
									.setFloat(inCounter, Float
											.parseFloat(parameters[i].value
													.toString()));
							break;
						case BasicType.DATETIME_INT:
							stmnt.setDate(inCounter, new Date(((java.util.Date) parameters[i].value).getTime()));
							break;
						case BasicType.INTEGER_INT:
							stmnt.setInt(inCounter, Integer
									.parseInt(parameters[i].value.toString()));
							break;
						case BasicType.STRING_INT:
							stmnt.setString(inCounter, parameters[i].value
									.toString());
							break;
						}
					}
				} catch (Exception e) {
					log.error("Cannot handle input parameter "
							+ parameters[i].getName(), e);
					throw e;
				}
			}
		}
		return stmnt;
	}

	protected ReturnValue[] handleResultSet(Parameter[] parameters, ResultSet rs)
			throws Exception {
		List ret = new ArrayList();
		for (int i = 0; i < parameters.length; i++) {
			Parameter parameter = parameters[i];
			try {
				if (parameter.mode != ParameterMode.IN) {

					int type = ((BasicType) parameter.dataType).getValue();
					switch (type) {
					case BasicType.STRING_INT:
						ret.add(new ReturnValue(parameter.actualName, rs
								.getString(parameter.getName())));
						break;
					case BasicType.FLOAT_INT:
						ret.add(new ReturnValue(parameter.actualName,
								new Float(rs.getFloat(parameter.getName()))));
						break;
					case BasicType.INTEGER_INT:
						ret.add(new ReturnValue(parameter.actualName,
								new Integer(rs.getInt(parameter.getName()))));
						break;
					case BasicType.DATETIME_INT:
						ret.add(new ReturnValue(parameter.actualName, rs
								.getDate(parameter.getName())));
						break;
					case BasicType.BOOLEAN_INT:
						ret
								.add(new ReturnValue(parameter.actualName,
										new Boolean(rs.getBoolean(parameter
												.getName()))));
						break;
					case BasicType.REFERENCE_INT:
						log.warn("Unsupported parameter type 'reference'");
						break;
					case BasicType.PERFORMER_INT:
						log.warn("Unsupported parameter type 'performer'");
						break;
					}
				}
			} catch (Exception e) {
				log.error("Cannot handle out parameter " + parameter.getName(),
						e);
				throw e;
			}
		}
		ReturnValue[] result = new ReturnValue[ret.size()];
		ret.toArray(result);
		return result;
	}

	protected ReturnValue[] handleNullSet(Parameter[] parameters)
			throws Exception {
		List ret = new ArrayList();
		for (int i = 0; i < parameters.length; i++) {
			Parameter parameter = parameters[i];
			try {
				if (parameter.mode != ParameterMode.IN) {
					ret.add(new ReturnValue(parameter.actualName, null));
				}
			} catch (Exception e) {
				log.error("Cannot handle out parameter " + parameter.getName(),
						e);
				throw e;
			}
		}
		ReturnValue[] result = new ReturnValue[ret.size()];
		ret.toArray(result);
		return result;
	}

	/**
	 * @return
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	protected Connection getConnection(BPEContext context) throws Exception,
			SQLException {
		if (m_connectionType == JDBC_CONNECTION_TYPE) {
			if (m_driver != null)
				Class.forName(m_driver);
			return DriverManager.getConnection(m_connectionUrl, m_userName,
					m_password);
		} else if (m_connectionType == DATASOURCE_CONNECTION_TYPE) {
			InitialContext ctx = new InitialContext();
			DataSource ds = (DataSource) ctx.lookup(m_connectionUrl);
			return ds.getConnection();
		} else {
			throw new RuntimeException(
					"Unknown connection type in SQLConnector "
							+ m_connectionType);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.objectcode.canyon.spi.tool.IToolConnector#init(de.objectcode.canyon.model.application.Application)
	 */
	public void init(Application application) throws Exception {
		String connectionType = getExtendedStringAttribute(application,
				"canyon:sqlConnector:connectionType");
		m_connectionUrl = getExtendedStringAttribute(application,
				"canyon:sqlConnector:connectionUrl");
		m_query = getExtendedStringAttribute(application,
				"canyon:sqlConnector:statement");
		if (connectionType.equals("JDBC")) {
			m_connectionType = JDBC_CONNECTION_TYPE;
			m_userName = getExtendedStringAttribute(application,
					"canyon:sqlConnector:userName");
			m_password = getExtendedStringAttribute(application,
					"canyon:sqlConnector:password");
			m_driver = getExtendedStringAttribute(application,
					"canyon:sqlConnector:driver");
		} else if (connectionType.equals("DATASOURCE")) {
			m_connectionType = DATASOURCE_CONNECTION_TYPE;
		} else {
			throw new RuntimeException("Illegal connection type '"
					+ connectionType + "' for SQLConnector");
		}
	}
}