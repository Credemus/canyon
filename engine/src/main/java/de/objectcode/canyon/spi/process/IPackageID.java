package de.objectcode.canyon.spi.process;

/**
 * @author junglas
 */
public interface IPackageID 
{
  /**
   * Get the identifier of the package.
   * I.e. the package name.
   */
  public String getId ();
  
  /**
   * Get the version of the package.
   */
  public String getVersion();
}
