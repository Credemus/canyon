package de.objectcode.canyon.bpe.engine;

/**
 * @author junglas
 */
public class InvalidStateException extends EngineException
{

	static final long serialVersionUID = 3316390254772037073L;
	
	public InvalidStateException ( String message )
  {
    super ( message );
  }
}
