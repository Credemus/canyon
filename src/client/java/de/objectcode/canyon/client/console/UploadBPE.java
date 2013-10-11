package de.objectcode.canyon.client.console;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;

import javax.naming.InitialContext;
import javax.rmi.PortableRemoteObject;

import de.objectcode.canyon.api.bpe.admin.BPEProcessRepositoryAdmin;
import de.objectcode.canyon.api.bpe.admin.BPEProcessRepositoryAdminHome;
import de.objectcode.canyon.spi.parser.IParserFactory;

/**
 * @author junglas
 */
public class UploadBPE
{
  /**
   * The main program for the UploadXPDL class
   *
   * @param args  The command line arguments
   */
  public static void main( String[] args ) 
  {
    try {
      int i;
      boolean replace = false;
      String fileName = null;
      
      for ( i = 0; i < args.length; i++ ) {
        if ( "-replace".equals(args[i]) ) {
          replace = true;
        } else
          fileName = args[i];
      }
      
      System.out.println("Uploading: " + fileName);
      
      ByteArrayOutputStream xpdl = new ByteArrayOutputStream();
      FileInputStream in = new FileInputStream(fileName);
      byte[] buffer = new byte[8192];
      int readed;
      
      while ((readed = in.read(buffer)) > 0 ) {
        xpdl.write(buffer, 0, readed);
      }
      
      in.close();
      
      InitialContext ctx = new InitialContext();
      
      BPEProcessRepositoryAdminHome processRepositoryHome = (BPEProcessRepositoryAdminHome)PortableRemoteObject.narrow(ctx.lookup(BPEProcessRepositoryAdminHome.JNDI_NAME), BPEProcessRepositoryAdminHome.class);
      
      BPEProcessRepositoryAdmin processRepository = processRepositoryHome.create();

      if ( replace )
        processRepository.createOrReplacePackage(xpdl.toByteArray(), IParserFactory.XPDL);
      else
        processRepository.createPackage(xpdl.toByteArray(), IParserFactory.XPDL);
    }
    catch ( Throwable e ) {
      e.printStackTrace();
    }
  }
}
