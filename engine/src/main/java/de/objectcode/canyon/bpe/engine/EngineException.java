package de.objectcode.canyon.bpe.engine;

/**
 * @author junglas
 */
public class EngineException extends Exception
{
	static final long serialVersionUID = -707352155782500550L;
	
	public EngineException ( String message )
  {
    super ( message );
  }
  
  public EngineException ( Throwable cause )
  {
    super ( cause );
  }
  
  public EngineException ( String message, Throwable cause )
  {
    super ( message, cause );
  }
  
}
