/*
 *  --
 *  Copyright (C) 2002-2003 Aetrion LLC.
 *  All rights reserved.
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions
 *  are met:
 *  1. Redistributions of source code must retain the above copyright
 *  notice, this list of conditions, and the following disclaimer.
 *  2. Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions, and the disclaimer that follows
 *  these conditions in the documentation and/or other materials
 *  provided with the distribution.
 *  3. The names "OBE" and "Open Business Engine" must not be used to
 *  endorse or promote products derived from this software without prior
 *  written permission.  For written permission, please contact
 *  obe@aetrion.com.
 *  4. Products derived from this software may not be called "OBE" or
 *  "Open Business Engine", nor may "OBE" or "Open Business Engine"
 *  appear in their name, without prior written permission from
 *  Aetrion LLC (obe@aetrion.com).
 *  THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 *  WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 *  OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 *  DISCLAIMED.  IN NO EVENT SHALL THE AUTHOR(S) BE LIABLE FOR ANY DIRECT,
 *  INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 *  (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 *  SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 *  HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 *  STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING
 *  IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 *  POSSIBILITY OF SUCH DAMAGE.
 *  For more information on OBE, please see
 *  <http://www.openbusinessengine.org/>.
 */
package de.objectcode.canyon.engine.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.ejb.EJBHome;
import javax.ejb.EJBObject;
import javax.ejb.Handle;
import javax.ejb.HomeHandle;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXSource;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import de.objectcode.canyon.CanyonRuntimeException;
import de.objectcode.canyon.spiImpl.parser.Converter;

//import weblogic.apache.xalan.serialize.SerializerToText;
//import weblogic.apache.xml.utils.TreeWalker;

/**
 * Converts data between different types.
 *
 * @author    Adrian Price
 * @created   20. Oktober 2003
 */
public final class DataConverter
{
  private final static  Object[]         EMPTY_ARRAY       = new Object[0];
  private final static  int            MAX_VALUE_LENGTH  = 35;

  private static        DataConverter  g_instance;


  /**
   *Constructor for the DataConverter object
   */
  public DataConverter() { }


  /**
   * Gets the instance attribute of the DataConverter class
   *
   * @return   The instance value
   */
  public static DataConverter getInstance()
  {
    if ( g_instance == null ) {
      synchronized ( DataConverter.class ) {
        if ( g_instance == null ) {
          g_instance = new DataConverter();
        }
      }
    }

    return g_instance;
  }


  /**
   * Description of the Method
   *
   * @param src    Description of the Parameter
   * @param clazz  Description of the Parameter
   * @return       Description of the Return Value
   */
  public Object convertValue( Object src, Class clazz )
  {
    Object  dest  = null;
    if ( src == null || clazz.isInstance( src ) ) {
      dest = src;
    } else if ( clazz.isArray() ) {
      dest = toArray( src, clazz );
    } else if ( clazz == Boolean.class || clazz == Boolean.TYPE ) {
      dest = toBoolean( src ) ? Boolean.TRUE : Boolean.FALSE;
    } else if ( clazz == Byte.class || clazz == Byte.TYPE ) {
      dest = new Byte( toByte( src ) );
    } else if ( clazz == Character.class || clazz == Character.TYPE ) {
      dest = new Character( toChar( src ) );
    } else if ( clazz == Double.class || clazz == Double.TYPE ) {
      dest = new Double( toDouble( src ) );
	} else if ( clazz == Float.class || clazz == Float.TYPE ) {
	  dest = new Float( toFloat( src ) );
	} else if ( clazz == Date.class ) {
	  dest = toDate(src);
    } else if ( clazz == Integer.class || clazz == Integer.TYPE ) {
      dest = new Integer( toInt( src ) );
    } else if ( clazz == Long.class || clazz == Long.TYPE ) {
      dest = new Long( toLong( src ) );
    } else if ( clazz == Short.class || clazz == Short.TYPE ) {
      dest = new Short( toShort( src ) );
    } else if ( clazz == String.class ) {
      dest = toString( src );
    } else {
      try {
        if ( EJBHome.class.isAssignableFrom( clazz ) ) {
          dest = toEJBHome( src );
        } else if ( EJBObject.class.isAssignableFrom( clazz ) ) {
          dest = toEJBObject( src );
        } else if ( Handle.class.isAssignableFrom( clazz ) ) {
          dest = toHandle( src );
        } else if ( HomeHandle.class.isAssignableFrom( clazz ) ) {
          dest = toHomeHandle( src );
        } else if ( Source.class.isAssignableFrom( clazz ) ) {
          dest = toSource( src );
        } else if ( InputSource.class.isAssignableFrom( clazz ) ) {
          dest = toInputSource( src );
        } else if ( Document.class.isAssignableFrom( clazz ) ) {
          dest = toDocument( src );
        } else if ( Node.class.isAssignableFrom( clazz ) ) {
          dest = toDocument( src );
        } else if ( Element.class.isAssignableFrom( clazz ) ) {
          dest = toElement( src );
        } else if ( Properties.class.isAssignableFrom( clazz ) ) {
          dest = toProperties( src );
        } else {
          throwDataConversionException( src, clazz );
        }
      }
      catch ( IOException e ) {
        throw new CanyonRuntimeException( e );
      }
      catch ( SAXException e ) {
        throw new CanyonRuntimeException( e );
      }
      if ( !clazz.isAssignableFrom( dest.getClass() ) ) {
        throwDataConversionException( src, clazz );
      }
    }
    return dest;
  }


  /**
   * Description of the Method
   *
   * @param src    Description of the Parameter
   * @param clazz  Description of the Parameter
   * @return       Description of the Return Value
   */
  public Object convertValueEx( Object src, Class clazz )
  {
    Object  dest;
    if ( clazz != Object.class ) {
      dest = convertValue( src, clazz );
    } else if ( src instanceof byte[] ) {
      dest = toDocument( src );
    } else if ( src instanceof Handle ) {
      try {
        dest = toEJBObject( src );
      }
      catch ( RemoteException e ) {
        throw new CanyonRuntimeException( e );
      }
    } else {
      dest = src;
    }
    return dest;
  }


  /**
   * Description of the Method
   *
   * @param src  Description of the Parameter
   * @return     Description of the Return Value
   */
  public Object[] toArray( Object src )
  {
    return toArray( src, null );
  }


  /**
   * Description of the Method
   *
   * @param src    Description of the Parameter
   * @param clazz  Description of the Parameter
   * @return       Description of the Return Value
   */
  public Object[] toArray( Object src, Class clazz )
  {
    if ( !clazz.isArray() ) {
      throw new IllegalArgumentException( String.valueOf( clazz ) );
    }

    Object[]  dest  = null;
    if ( src == null ) {
      dest = EMPTY_ARRAY;
    } else if ( src.getClass().isArray() ) {
      dest = (Object[])src;
    } else if ( src instanceof Collection ) {
      dest = ( ( Collection ) src ).toArray();
    } else if ( src instanceof Enumeration ) {
      ArrayList  list  = new ArrayList();
      for ( Enumeration e = ( Enumeration ) src; e.hasMoreElements();  ) {
        list.add( e.nextElement() );
      }
      dest = list.toArray();
    } else if ( src instanceof Iterator ) {
      ArrayList  list  = new ArrayList();
      for ( Iterator i = ( Iterator ) src; i.hasNext();  ) {
        list.add( i.next() );
      }
      dest = list.toArray();
    } else if ( src instanceof Map ) {
      dest = ( ( Map ) src ).values().toArray();
    } else {
      dest = new Object[]{src};
    }

    // We have an array, but it might not be compatible with the array
    // type the caller requested.  For now, we won't attempt to convert.
    // TODO: implement array-type conversions.
    if ( clazz != null && !clazz.isAssignableFrom( src.getClass() ) ) {
      throwDataConversionException( src, clazz );
    }

    return dest;
  }


  /**
   * Description of the Method
   *
   * @param src  Description of the Parameter
   * @return     Description of the Return Value
   */
  public boolean toBoolean( Object src )
  {
    boolean  dest  = false;
    if ( src instanceof Boolean ) {
      dest = ( ( Boolean ) src ).booleanValue();
    } else if ( src instanceof String ) {
      dest = Boolean.valueOf( ( String ) src ).booleanValue();
    } else {
      throwDataConversionException( src, Boolean.TYPE );
    }
    return dest;
  }


  /**
   * Description of the Method
   *
   * @param src  Description of the Parameter
   * @return     Description of the Return Value
   */
  public byte toByte( Object src )
  {
    byte  dest  = 0;
    if ( src instanceof Number ) {
      dest = ( ( Number ) src ).byteValue();
    } else if ( src instanceof String ) {
      dest = Byte.parseByte( ( String ) src );
    } else {
      throwDataConversionException( src, Byte.TYPE );
    }
    return dest;
  }


  /**
   * Description of the Method
   *
   * @param src  Description of the Parameter
   * @return     Description of the Return Value
   */
  public char toChar( Object src )
  {
    char  dest  = '\0';
    if ( src instanceof Number ) {
      dest = ( char ) ( ( Number ) src ).shortValue();
    } else {
      throwDataConversionException( src, Character.TYPE );
    }
    return dest;
  }


  /**
   * Description of the Method
   *
   * @param src  Description of the Parameter
   * @return     Description of the Return Value
   */
  public Document toDocument( Object src )
  {
    Document  dest  = null;
    if ( src == null ) {
      dest = null;
    } else if ( src instanceof Node ) {
      dest = ( ( Node ) src ).getOwnerDocument();
    } else if ( src instanceof NodeList ) {
      dest = ( ( NodeList ) src ).item( 0 ).getOwnerDocument();
    } else {
      try {
        InputSource             in          = toInputSource( src );
        DocumentBuilderFactory  factory     = DocumentBuilderFactory.newInstance();
        DocumentBuilder         docBuilder  = factory.newDocumentBuilder();
        dest = docBuilder.parse( in );
      }
      catch ( Exception e ) {
        throw new CanyonRuntimeException( e );
      }
    }
    return dest;
  }


  /**
   * Description of the Method
   *
   * @param src  Description of the Parameter
   * @return     Description of the Return Value
   */
  public double toDouble( Object src )
  {
    double  dest  = 0.0D;
    if ( src instanceof Number ) {
      dest = ( ( Number ) src ).doubleValue();
    } else if ( src instanceof String ) {
      dest = Double.parseDouble( ( String ) src );
    } else {
      throwDataConversionException( src, Double.TYPE );
    }
    return dest;
  }


  /**
   * Description of the Method
   *
   * @param src                  Description of the Parameter
   * @return                     Description of the Return Value
   * @exception RemoteException  Description of the Exception
   */
  public EJBHome toEJBHome( Object src )
    throws RemoteException
  {
    EJBHome  dest  = null;
    if ( src == null ) {
      dest = null;
    } else if ( src instanceof EJBHome ) {
      dest = ( EJBHome ) src;
    } else if ( src instanceof HomeHandle ) {
      dest = ( ( HomeHandle ) src ).getEJBHome();
    } else if ( src instanceof EJBObject ) {
      dest = ( ( EJBObject ) src ).getEJBHome();
    } else {
      throwDataConversionException( src, EJBHome.class );
    }
    return dest;
  }


  /**
   * Description of the Method
   *
   * @param src                  Description of the Parameter
   * @return                     Description of the Return Value
   * @exception RemoteException  Description of the Exception
   */
  public EJBObject toEJBObject( Object src )
    throws RemoteException
  {
    EJBObject  dest  = null;
    if ( src == null ) {
      dest = null;
    } else if ( src instanceof EJBObject ) {
      dest = ( EJBObject ) src;
    } else if ( src instanceof Handle ) {
      dest = ( ( Handle ) src ).getEJBObject();
    } else {
      throwDataConversionException( src, EJBObject.class );
    }
    return dest;
  }


  /**
   * Description of the Method
   *
   * @param src  Description of the Parameter
   * @return     Description of the Return Value
   */
  public Element toElement( Object src )
  {
    Element  dest  = null;
    if ( src == null ) {
      dest = null;
    } else if ( src instanceof Element ) {
      dest = ( Element ) src;
    } else if ( src instanceof Document ) {
      dest = ( ( Document ) src ).getDocumentElement();
    } else if ( src instanceof NodeList ) {
      dest = ( Element ) ( ( NodeList ) src ).item( 0 );
    } else {
      dest = toDocument( src ).getDocumentElement();
    }
    return dest;
  }


  /**
   * Description of the Method
   *
   * @param src  Description of the Parameter
   * @return     Description of the Return Value
   */
  public float toFloat( Object src )
  {
    float  dest  = 0.0F;
    if ( src instanceof Number ) {
      dest = ( ( Number ) src ).floatValue();
    } else if ( src instanceof String ) {
      dest = Float.parseFloat( ( String ) src );
    } else {
      throwDataConversionException( src, Float.TYPE );
    }
    return dest;
  }


  /**
   * Description of the Method
   *
   * @param src                  Description of the Parameter
   * @return                     Description of the Return Value
   * @exception RemoteException  Description of the Exception
   */
  public Handle toHandle( Object src )
    throws RemoteException
  {
    Handle  dest  = null;
    if ( src == null ) {
      dest = null;
    } else if ( src instanceof Handle ) {
      dest = ( Handle ) src;
    } else if ( src instanceof EJBObject ) {
      dest = ( ( EJBObject ) src ).getHandle();
    } else {
      throwDataConversionException( src, Handle.class );
    }
    return dest;
  }


  /**
   * Description of the Method
   *
   * @param src                  Description of the Parameter
   * @return                     Description of the Return Value
   * @exception RemoteException  Description of the Exception
   */
  public HomeHandle toHomeHandle( Object src )
    throws RemoteException
  {
    HomeHandle  dest  = null;
    if ( src == null ) {
      dest = null;
    } else if ( src instanceof HomeHandle ) {
      dest = ( HomeHandle ) src;
    } else if ( src instanceof EJBHome ) {
      dest = ( ( EJBHome ) src ).getHomeHandle();
    } else {
      throwDataConversionException( src, HomeHandle.class );
    }
    return dest;
  }


  /**
   * Description of the Method
   *
   * @param src              Description of the Parameter
   * @return                 Description of the Return Value
   * @exception IOException  Description of the Exception
   */
  public InputSource toInputSource( Object src )
    throws IOException
  {
    InputSource  dest  = null;
    if ( src == null ) {
      dest = null;
    } else if ( src instanceof InputStream ) {
      dest = new InputSource( ( InputStream ) src );
    } else if ( src instanceof URL ) {
      dest = new InputSource( ( ( URL ) src ).openStream() );
    } else if ( src instanceof File ) {
      dest = new InputSource( new FileReader( ( File ) src ) );
    } else if ( src instanceof byte[] ) {
      ByteArrayInputStream  in  = new ByteArrayInputStream( ( byte[] ) src );
      dest = new InputSource( in );
    } else if ( src instanceof String ) {
      // N.B. This is a 'best guess' at interpreting whether src is the
      // name of a file to read, or a string source to be read.
      File  file  = new File( ( String ) src );
      if ( file.exists() ) {
        dest = new InputSource( new FileReader( file ) );
      } else {
        dest = new InputSource( new StringReader( ( String ) src ) );
      }
    } else {
      throwDataConversionException( src, InputSource.class );
    }
    return dest;
  }


  /**
   * Description of the Method
   *
   * @param src  Description of the Parameter
   * @return     Description of the Return Value
   */
  public int toInt( Object src )
  {
    int  dest  = 0;
    if ( src instanceof Number ) {
      dest = ( ( Number ) src ).intValue();
    } else if ( src instanceof String ) {
      dest = Integer.parseInt( ( String ) src );
    } else {
      throwDataConversionException( src, Integer.TYPE );
    }
    return dest;
  }

  public Date toDate( Object src )
  {
	Date dest = null;
	if ( src instanceof String ) {
		dest = (Date) Converter.getConverter(Date.class).fromString((String) src);
	} else {
	  throwDataConversionException( src, Date.class );
	}
	return dest;
  }


  /**
   * Description of the Method
   *
   * @param src  Description of the Parameter
   * @return     Description of the Return Value
   */
  public long toLong( Object src )
  {
    long  dest  = 0L;
    if ( src instanceof Number ) {
      dest = ( ( Number ) src ).longValue();
    } else if ( src instanceof String ) {
      dest = Long.parseLong( ( String ) src );
    } else {
      throwDataConversionException( src, Long.TYPE );
    }
    return dest;
  }


  /**
   * Description of the Method
   *
   * @param src  Description of the Parameter
   * @return     Description of the Return Value
   */
  public Properties toProperties( Object src )
  {
    // Assume it's in Java .properties file format.
    Properties  dest  = null;
    try {
      if ( src == null ) {
        dest = null;
      } else if ( src instanceof Properties ) {
        dest = ( Properties ) src;
      } else if ( src instanceof Map ) {
        dest = new Properties();
        Set  entries  = ( ( Map ) src ).entrySet();
        for ( Iterator i = entries.iterator(); i.hasNext();  ) {
          Map.Entry  entry  = ( Map.Entry ) i.next();
          Object     value  = entry.getValue();
          if ( value != null ) {
            dest.setProperty( entry.getKey().toString(),
                value.toString() );
          }
        }
      } else if ( src instanceof String ) {
        String       text  = ( String ) src;
        InputStream  in    = new ByteArrayInputStream(
            text.getBytes( "UTF-8" ) );
        dest = new Properties();
        dest.load( in );
      } else {
        throwDataConversionException( src, Properties.class );
      }
    }
    catch ( Exception e ) {
      throwDataConversionException( src, Properties.class );
    }
    return dest;
  }


  /**
   * Description of the Method
   *
   * @param src  Description of the Parameter
   * @return     Description of the Return Value
   */
  public short toShort( Object src )
  {
    short  dest  = 0;
    if ( src instanceof Number ) {
      dest = ( ( Number ) src ).shortValue();
    } else if ( src instanceof String ) {
      dest = Short.parseShort( ( String ) src );
    } else {
      throwDataConversionException( src, Short.TYPE );
    }
    return dest;
  }


  /**
   * Description of the Method
   *
   * @param src               Description of the Parameter
   * @return                  Description of the Return Value
   * @exception IOException   Description of the Exception
   * @exception SAXException  Description of the Exception
   */
  public Source toSource( Object src )
    throws IOException,
      SAXException
  {

    Object       dest  = null;
    InputSource  in    = null;
    if ( src == null ) {
      dest = null;
    } else if ( src instanceof Node ) {
      dest = new DOMSource( ( Node ) src );
    } else if ( src instanceof InputStream ) {
      in = new InputSource( ( InputStream ) src );
    } else if ( src instanceof URL ) {
      in = new InputSource( ( ( URL ) src ).openStream() );
    } else if ( src instanceof File ) {
      in = new InputSource( new FileInputStream( ( File ) src ) );
    } else if ( src instanceof byte[] ) {
      in = new InputSource( new StringReader( new String( ( byte[] ) src,
          "UTF-8" ) ) );
    } else if ( src instanceof String ) {
      in = new InputSource( new StringReader( ( String ) src ) );
    } else {
      throwDataConversionException( src, Source.class );
    }
    if ( dest == null ) {
      dest = new SAXSource( in );
    }
    if ( dest instanceof SAXSource ) {
      XMLReader  xmlreader  = XMLReaderFactory.createXMLReader();
      ( ( SAXSource ) dest ).setXMLReader( xmlreader );
    }
    return ( ( Source ) ( dest ) );
  }


  /**
   * Description of the Method
   *
   * @param src  Description of the Parameter
   * @return     Description of the Return Value
   */
  public String toString( Object src )
  {
    String  s  = null;
    try {
      if ( src == null ) {
        s = null;
      } else if ( src instanceof String ) {
        s = ( String ) src;
      } else if ( src instanceof byte[] ) {
        try {
          s = new String( ( byte[] ) src, "UTF-8" );
        }
        catch ( UnsupportedEncodingException e ) {
          throw new CanyonRuntimeException( e );
        }
      } else if ( src instanceof Node ) {
//                SerializerToText serializer = new SerializerToText();
//                StringWriter out = new StringWriter();
//                serializer.setWriter(out);
//                Properties props = new Properties();
//                props.setProperty("encoding", "UTF-8");
//                serializer.setOutputFormat(props);
//                serializer.serialize((Node)src);
//                serializer.flushWriter();
//                out.close();
//                s = out.toString();
      } else if ( src instanceof NodeList ) {
//                SerializerToText serializer = new SerializerToText();
//                StringWriter out = new StringWriter();
//                serializer.setWriter(out);
//                Properties props = new Properties();
//                props.setProperty("encoding", "UTF-8");
//                serializer.setOutputFormat(props);
//                TreeWalker walker = new TreeWalker(serializer);
//                int i = 0;
//                for (int j = ((NodeList)src).getLength(); i < j; i++) {
//                    walker.traverse(((NodeList)src).item(i));
//                    serializer.flushWriter();
//                }
//                out.close();
//                s = out.toString();
      } else {
        s = src.toString();
      }
    }
    catch ( Exception e ) {
      throw new CanyonRuntimeException( e );
    }
    return s;
  }


  /**
   * Description of the Method
   *
   * @param src    Description of the Parameter
   * @param clazz  Description of the Parameter
   */
  private static void throwDataConversionException( Object src, Class clazz )
  {
    throw new CanyonRuntimeException( "Cannot convert " + truncate( src ) +
        " to " + clazz );
  }


  /**
   * Description of the Method
   *
   * @param obj  Description of the Parameter
   * @return     Description of the Return Value
   */
  private static String truncate( Object obj )
  {
    String  s  = null;
    if ( obj != null ) {
      s = obj.toString();
      if ( s.length() > MAX_VALUE_LENGTH ) {
        s = s.substring( 0, MAX_VALUE_LENGTH - 3 ) + "...";
      }
    }
    return s;
  }
}
