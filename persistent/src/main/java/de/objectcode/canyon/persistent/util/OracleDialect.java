package de.objectcode.canyon.persistent.util;

import java.sql.Types;

import net.sf.hibernate.cfg.Environment;

/**
 * @author junglas
 * @created 28.07.2003
 * @version $Id: OracleDialect.java,v 1.2 2003/12/16 13:32:29 junglas Exp $
 */
public class OracleDialect extends net.sf.hibernate.dialect.OracleDialect 
{
	public OracleDialect ( )
	{
		super();

		// registerColumnType(Types.BLOB, "LONG RAW");  // changes by SGR, 07/2011
		registerColumnType(Types.BLOB, "BLOB");
 		registerColumnType(Types.CLOB, "LONG");
		
		getDefaultProperties().setProperty(Environment.STATEMENT_BATCH_SIZE, "0");
	}
}
