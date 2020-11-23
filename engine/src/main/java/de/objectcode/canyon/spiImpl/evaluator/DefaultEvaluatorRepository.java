package de.objectcode.canyon.spiImpl.evaluator;

import java.util.HashMap;
import java.util.Map;

import de.objectcode.canyon.spi.ObjectNotFoundException;
import de.objectcode.canyon.spi.RepositoryException;
import de.objectcode.canyon.spi.evaluator.IEvaluator;
import de.objectcode.canyon.spi.evaluator.IEvaluatorRepository;

/**
 * @author    junglas
 * @created   27. Oktober 2003
 */
public class DefaultEvaluatorRepository implements IEvaluatorRepository
{
  private  Map  m_evaluators;


  /**
   *Constructor for the DefaultEvaluatorRepository object
   */
  public DefaultEvaluatorRepository()
  {
    m_evaluators = new HashMap();

    m_evaluators.put( "text/xpath", new JXPathEvaluator() );
    m_evaluators.put( "text/netrexx", new BSFEvaluator("netrexx"));
    m_evaluators.put( "text/perl", new BSFEvaluator("perl"));    
    m_evaluators.put( "text/vbscript", new BSFEvaluator("vbscript"));
    m_evaluators.put( "text/javaclass", new BSFEvaluator("javaclass"));
    m_evaluators.put( "text/bml", new BSFEvaluator("bml"));
    m_evaluators.put( "text/javascript", new BSFEvaluator("javascript"));
    m_evaluators.put( "text/jscript", new BSFEvaluator("jscript"));
    m_evaluators.put( "text/jpython", new BSFEvaluator("jpython"));
    m_evaluators.put( "text/jython", new BSFEvaluator("jython"));
    m_evaluators.put( "text/beanshell", new BSFEvaluator("beanshell"));
  }


  /**
   * @param contentType              Description of the Parameter
   * @return                         Description of the Return Value
   * @exception RepositoryException  Description of the Exception
   * @see                            de.objectcode.canyon.spi.evaluator.IEvaluatorRepository#findEvaluator(java.lang.String)
   */
  public IEvaluator findEvaluator( String contentType )
    throws RepositoryException
  {
    if ( !m_evaluators.containsKey( contentType ) ) {
      throw new ObjectNotFoundException( "No evaluator for contentType '" + contentType + "'" );
    }

    return ( IEvaluator ) m_evaluators.get( contentType );
  }

}
