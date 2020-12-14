package de.objectcode.canyon.persistent.util;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Arrays;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.UserType;

import org.apache.commons.lang.SerializationUtils;

/**
 * @author junglas
 * @created 28.07.2003
 * @version $Id: SerializableType.java,v 1.1 2003/10/17 09:07:32 junglas Exp $
 */
public class ImmutableSerializableType implements UserType
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
		return obj;
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
		InputStream is = rs.getBinaryStream(names[0]);
  	
		if ( is == null )
			return null;

		return SerializationUtils.deserialize(is);
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
			ps.setNull(index, Types.BLOB);
		else {
			byte[] blob = SerializationUtils.serialize((Serializable)value);
  		
			ps.setBinaryStream(index, new ByteArrayInputStream(blob), blob.length);
		}
	}


	/**
	 * @return   Description of the Return Value
	 * @see      net.sf.hibernate.UserType#returnedClass()
	 */
	public Class returnedClass()
	{
		return Serializable.class;
	}


	/**
	 * @return   Description of the Return Value
	 * @see      net.sf.hibernate.UserType#sqlTypes()
	 */
	public int[] sqlTypes()
	{
		return new int[]{Types.BLOB};
	}

}
