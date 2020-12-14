package de.objectcode.canyon.persistent.util;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.UserType;

/**
 * @author junglas
 * @created 28.07.2003
 * @version $Id: ClobType.java,v 1.1 2003/10/17 09:07:32 junglas Exp $
 */
public class ClobType implements UserType
{
	/**
	 * @return   The mutable value
	 * @see      net.sf.hibernate.UserType#isMutable()
	 */
	public boolean isMutable()
	{
		return false;
	}


	/**
	 * @param arg0  Description of the Parameter
	 * @return      Description of the Return Value
	 * @see         net.sf.hibernate.UserType#deepCopy(java.lang.Object)
	 */
	public Object deepCopy( Object obj )
	{
		if ( obj == null )
			return null;
		return new String(obj.toString());
	}


	/**
	 * @param arg0  Description of the Parameter
	 * @param arg1  Description of the Parameter
	 * @return      Description of the Return Value
	 * @see         net.sf.hibernate.UserType#equals(java.lang.Object, java.lang.Object)
	 */
	public boolean equals( Object obj1, Object obj2 )
	{
		if ( obj1 == null && obj2 == null )
			return true;
		if ( obj1 == null || obj2 == null )
			return false;
	  
		return obj1.equals(obj2);
	}


	/**
	 * @param arg0                    Description of the Parameter
	 * @param arg1                    Description of the Parameter
	 * @param arg2                    Description of the Parameter
	 * @return                        Description of the Return Value
	 * @exception HibernateException  Description of the Exception
	 * @exception SQLException        Description of the Exception
	 * @see                           net.sf.hibernate.UserType#nullSafeGet(java.sql.ResultSet, java.lang.String[], java.lang.Object)
	 */
	public Object nullSafeGet( ResultSet rs, String[] names, Object owner )
		throws HibernateException, SQLException
	{
		Reader reader = rs.getCharacterStream(names[0]);
  	
		if ( reader == null )
			return null;

		try {  		
			StringWriter writer = new StringWriter();
			char[] buffer = new char[8192];
			int readed;
			
			while ( ( readed = reader.read(buffer)) > 0 ) {
				writer.write(buffer,0, readed);
			}
			reader.close();
			writer.close();
			
			return writer.toString();
		}
		catch ( IOException e ) {
			throw new HibernateException( "IOException occurred reading a clobtype value", e );
		}
	}


	/**
	 * @param arg0                    Description of the Parameter
	 * @param arg1                    Description of the Parameter
	 * @param arg2                    Description of the Parameter
	 * @exception HibernateException  Description of the Exception
	 * @exception SQLException        Description of the Exception
	 * @see                           net.sf.hibernate.UserType#nullSafeSet(java.sql.PreparedStatement, java.lang.Object, int)
	 */
	public void nullSafeSet( PreparedStatement ps, Object value, int index )
		throws HibernateException, SQLException
	{
		if ( value == null )
			ps.setNull(index, Types.CLOB);
		else {
			String str = value.toString();
  		
			ps.setCharacterStream(index, new StringReader(str), str.length());
		}
	}


	/**
	 * @return   Description of the Return Value
	 * @see      net.sf.hibernate.UserType#returnedClass()
	 */
	public Class returnedClass()
	{
		return String.class;
	}


	/**
	 * @return   Description of the Return Value
	 * @see      net.sf.hibernate.UserType#sqlTypes()
	 */
	public int[] sqlTypes()
	{
		return new int[]{Types.CLOB};
	}

}
