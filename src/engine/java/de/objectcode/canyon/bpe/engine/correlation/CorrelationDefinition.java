package de.objectcode.canyon.bpe.engine.correlation;

import de.objectcode.canyon.bpe.util.IDomSerializable;

import java.io.Serializable;

import org.dom4j.Element;

/**
 * @author    junglas
 * @created   15. Juni 2004
 */
public class CorrelationDefinition implements Serializable, IDomSerializable
{
  static final long serialVersionUID = 6023673569340681136L;
  
  private  CorrelationSet      m_correlationSet;
  private  boolean             m_initiate;
  private  CorrelationPattern  m_pattern;


  /**
   *Constructor for the CorrelationDefinition object
   *
   * @param correlationSet  Description of the Parameter
   * @param initiate        Description of the Parameter
   */
  public CorrelationDefinition( CorrelationSet correlationSet, boolean initiate )
  {
    this( correlationSet, initiate, null );
  }


  /**
   *Constructor for the CorrelationDefinition object
   *
   * @param correlationSet  Description of the Parameter
   * @param initiate        Description of the Parameter
   * @param pattern         Description of the Parameter
   */
  public CorrelationDefinition( CorrelationSet correlationSet, boolean initiate, CorrelationPattern pattern )
  {
    m_correlationSet = correlationSet;
    m_initiate = initiate;
    m_pattern = pattern;
  }


  /**
   * @return   Returns the correlationSet.
   */
  public CorrelationSet getCorrelationSet()
  {
    return m_correlationSet;
  }


  /**
   * @return   Returns the initiate.
   */
  public boolean isInitiate()
  {
    return m_initiate;
  }


  /**
   * @return   Returns the pattern.
   */
  public CorrelationPattern getPattern()
  {
    return m_pattern;
  }


  /**
   * @return   The elementName value
   * @see      de.objectcode.canyon.bpe.util.IDomSerializable#getElementName()
   */
  public String getElementName()
  {
    return "correlation";
  }


  /**
   * @param element  Description of the Parameter
   * @see            de.objectcode.canyon.bpe.util.IDomSerializable#toDom(org.dom4j.Element)
   */
  public void toDom( Element element )
  {
    m_correlationSet.toDom( element );

    element.addAttribute( "initiate", Boolean.toString( m_initiate ) );
    if ( m_pattern != null ) {
      element.addAttribute( "pattern", m_pattern.getTag() );
    }
  }
}
