package de.objectcode.canyon.drydock;

import java.io.File;

import de.objectcode.canyon.drydock.ui.MainFrame;

/**
 * @author junglas
 */
public class Main
{
  public static void main ( String[] args )
  {
    MainFrame frame = new MainFrame();
    
    if ( args.length > 0 ) {
      frame.loadXPDL(new File(args[0]));
    }
    
    frame.setVisible(true);
  }
}
