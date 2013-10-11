package de.objectcode.canyon.spiImpl.parser;

import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.beanutils.PropertyUtils;

import de.objectcode.canyon.model.WorkflowPackage;
import de.objectcode.canyon.spi.parser.ISerializer;

/**
 * @author    junglas
 * @created   24. November 2003
 */
public class DefaultTxtSerializer implements ISerializer
{
  /**
   * @param pkg              Description of the Parameter
   * @param out              Description of the Parameter
   * @exception IOException  Description of the Exception
   * @see                    de.objectcode.canyon.spi.parser.ISerializer#serialize(de.objectcode.canyon.model.WorkflowPackage, java.io.Writer)
   */
  public void serialize( WorkflowPackage pkg, OutputStream out )
    throws IOException
  {
    PrintWriter  pout  = new PrintWriter( out );

    dumpBean( pkg, "", pout, new HashSet() );

    pout.flush();
  }


  /**
   * Description of the Method
   *
   * @param pkg              Description of the Parameter
   * @param out              Description of the Parameter
   * @exception IOException  Description of the Exception
   */
  public void serialize( WorkflowPackage pkg, Writer out )
    throws IOException
  {
    PrintWriter  pout  = new PrintWriter( out );

    dumpBean( pkg, "", pout, new HashSet() );

    pout.flush();
  }


  /**
   * Description of the Method
   *
   * @param bean   Description of the Parameter
   * @param ident  Description of the Parameter
   * @param out    Description of the Parameter
   * @param done   Description of the Parameter
   */
  private void dumpBean( Object bean, String ident, PrintWriter out, Set done )
  {
    out.println( bean );

    if ( bean == null || done.contains( bean ) ) {
      return;
    }

    done.add( bean );

    PropertyDescriptor  descriptors[]  = PropertyUtils.getPropertyDescriptors( bean );
    int                 i;

    for ( i = 0; i < descriptors.length; i++ ) {
      if ( descriptors[i].getPropertyType().isPrimitive() ||
          descriptors[i].getPropertyType().getName().startsWith( "java.lang" ) ) {
        try {
          out.println( ident + " " + descriptors[i].getName() + " = '" + PropertyUtils.getProperty( bean, descriptors[i].getName() ) + "'" );
        }
        catch ( Exception e ) {
          out.println( e.toString() );
        }
      }
    }

    for ( i = 0; i < descriptors.length; i++ ) {
      if ( descriptors[i].getPropertyType().getName().startsWith( "de.objectcode.canyon.model" ) ) {
        try {
          out.print( ident + " " + descriptors[i].getName() + " = " );
          dumpBean( PropertyUtils.getProperty( bean, descriptors[i].getName() ), ident + "  ", out, done );
        }
        catch ( Exception e ) {
          out.println( e.toString() );
        }
      } else if ( descriptors[i].getPropertyType().isArray() &&
          descriptors[i].getPropertyType().getComponentType().getName().startsWith( "de.objectcode.canyon.model" ) ) {
        try {
          Object[]  values  = ( Object[] ) PropertyUtils.getProperty( bean, descriptors[i].getName() );

          if ( values == null ) {
            out.println( ident + " " + descriptors[i].getName() + " = null" );
          } else {
            int  j;

            out.println( ident + " " + descriptors[i].getName() + " = " + values.length + "[" );
            for ( j = 0; j < values.length; j++ ) {
              out.print( ident + "  " );
              dumpBean( values[j], ident + "  ", out, done );
            }
            out.println( ident + " ]" );
          }
        }
        catch ( Exception e ) {
          out.println( e.toString() );
        }
      }
    }
  }
}
