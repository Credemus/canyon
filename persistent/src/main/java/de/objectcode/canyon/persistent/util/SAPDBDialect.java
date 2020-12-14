package de.objectcode.canyon.persistent.util;

import java.sql.Types;

/**
 * @author junglas
 * @created 28.07.2003
 * @version $Id: SAPDBDialect.java,v 1.2 2003/12/16 13:32:29 junglas Exp $
 */
public class SAPDBDialect extends net.sf.hibernate.dialect.SAPDBDialect 
{
	public SAPDBDialect ( ) 
	{
		super();
		
		registerColumnType( Types.VARBINARY, 2000, "VARCHAR($l) BYTE" );
	}
}
