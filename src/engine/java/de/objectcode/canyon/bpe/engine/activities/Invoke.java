package de.objectcode.canyon.bpe.engine.activities;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Element;

import de.objectcode.canyon.bpe.connector.IConnector;
import de.objectcode.canyon.bpe.engine.EngineException;
import de.objectcode.canyon.bpe.engine.correlation.Correlation;
import de.objectcode.canyon.bpe.engine.correlation.CorrelationDefinition;
import de.objectcode.canyon.bpe.engine.correlation.CorrelationPattern;
import de.objectcode.canyon.bpe.engine.correlation.CorrelationSet;
import de.objectcode.canyon.bpe.engine.correlation.Message;
import de.objectcode.canyon.bpe.engine.evaluator.IAssignableExpression;
import de.objectcode.canyon.bpe.engine.evaluator.IExpression;
import de.objectcode.canyon.bpe.engine.variable.ComplexType;
import de.objectcode.canyon.bpe.engine.variable.ComplexValue;

/**
 * @author    junglas
 * @created   8. Juni 2004
 */
public class Invoke extends Activity
{
  final static  long                   serialVersionUID    = -5380250353715769452L;

  private       IConnector             m_connector;
  private       ComplexType            m_outMessageType;
  private       ComplexType            m_inMessageType;
  private       String                 m_messageOperation;
  private       IAssignableExpression  m_inputExpression;
  private       IExpression            m_outputExpression;
  private       List                   m_correlations;


  /**
   *Constructor for the Invoke object
   *
   * @param name              Description of the Parameter
   * @param scope             Description of the Parameter
   * @param messageOperation  Description of the Parameter
   */
  public Invoke( String name, Scope scope, String messageOperation )
  {
    super( name, scope );

    m_correlations = new ArrayList();
    m_messageOperation = messageOperation;
  }


  /**
   * @param inMessageType  The inMessageType to set.
   */
  public void setInMessageType( ComplexType inMessageType )
  {
    m_inMessageType = inMessageType;
    m_scope.getProcess().registerType( inMessageType );
  }


  /**
   * @param outMessageType  The outMessageType to set.
   */
  public void setOutMessageType( ComplexType outMessageType )
  {
    m_outMessageType = outMessageType;
    m_scope.getProcess().registerType( outMessageType );
  }


  /**
   * @param messageOperation  The messageOperation to set.
   */
  public void setMessageOperation( String messageOperation )
  {
    m_messageOperation = messageOperation;
  }


  /**
   * @param connector  The connector to set.
   */
  public void setConnector( IConnector connector )
  {
    m_connector = connector;
  }


  /**
   * Sets the inputExpression attribute of the Invoke object
   *
   * @param expression  The new inputExpression value
   */
  public void setInputExpression( IAssignableExpression expression )
  {
    m_inputExpression = expression;
  }


  /**
   * Sets the outputExpression attribute of the Invoke object
   *
   * @param expression  The new outputExpression value
   */
  public void setOutputExpression( IExpression expression )
  {
    m_outputExpression = expression;
  }


  /**
   * @return   Returns the connector.
   */
  public IConnector getConnector()
  {
    return m_connector;
  }


  /**
   * Gets the elementName attribute of the Invoke object
   *
   * @return   The elementName value
   */
  public String getElementName()
  {
    return "invoke";
  }


  /**
   * Adds a feature to the Correlation attribute of the Invoke object
   *
   * @param correlationSet  The feature to be added to the Correlation attribute
   * @param initiate        The feature to be added to the Correlation attribute
   * @param pattern         The feature to be added to the Correlation attribute
   */
  public void addCorrelation( CorrelationSet correlationSet, boolean initiate, CorrelationPattern pattern )
  {
    m_correlations.add( new CorrelationDefinition( correlationSet, initiate, pattern ) );
    m_scope.addCorrelationSet( correlationSet );
  }


  /**
   * @exception EngineException  Description of the Exception
   * @see                        de.objectcode.canyon.bpe.engine.activities.Activity#start()
   */
  public void start()
    throws EngineException
  {
    super.start();

    if ( m_connector != null ) {
      ComplexValue  outContent  = null;

      if ( m_outputExpression != null ) {
        Object  value  = m_outputExpression.eval( this );

        if ( value instanceof ComplexValue ) {
          outContent = ( ComplexValue ) value;
        } else {
          outContent = new ComplexValue( null );

          outContent.set( "content", value );
        }
      } else {
        outContent = new ComplexValue(m_outMessageType);
      }
      
      Message       outMessage  = new Message( m_messageOperation, outContent );

      if ( !m_correlations.isEmpty() ) {
        Iterator  it  = m_correlations.iterator();

        while ( it.hasNext() ) {
          CorrelationDefinition  correlationDef  = ( CorrelationDefinition ) it.next();

          if ( correlationDef.getPattern() != CorrelationPattern.IN ) {
            if ( correlationDef.isInitiate() ) {
              m_scope.addCorrelation( correlationDef.getCorrelationSet().initiateCorrelation( outMessage ) );
            } else {
              Correlation correlation = m_scope.getCorrelation(correlationDef.getCorrelationSet().getName());
              
              correlation.correlate ( outMessage );
            }
          }
        }
      }

      Message       inMessage   = m_connector.invoke( getProcess(), outMessage );

      if ( inMessage != null ) {
        if ( m_inputExpression != null ) {
          m_inputExpression.assign( this, inMessage.getContent() );
        }

        if ( !m_correlations.isEmpty() ) {
          Iterator  it  = m_correlations.iterator();

          while ( it.hasNext() ) {
            CorrelationDefinition  correlation  = ( CorrelationDefinition ) it.next();

            if ( correlation.isInitiate() &&correlation.getPattern() != CorrelationPattern.OUT ) {
              m_scope.addCorrelation( correlation.getCorrelationSet().initiateCorrelation( inMessage ) );
            }
          }
        }
      }
    }

    complete();
  }


  /**
   * @param element  Description of the Parameter
   * @see            de.objectcode.canyon.bpe.util.IDomSerializable#toDom(org.dom4j.Element)
   */
  public void toDom( Element element )
  {
    super.toDom( element );

    element.addAttribute( "messageOperation", m_messageOperation );
    if ( m_inMessageType != null ) {
      element.addAttribute( "inMessageType", m_inMessageType.getName() );
    }
    if ( m_outMessageType != null ) {
      element.addAttribute( "outMessageType", m_outMessageType.getName() );
    }

    m_connector.toDom( element.addElement( m_connector.getElementName() ) );

    if ( m_outputExpression != null ) {
      Element  outputElement  = element.addElement( "output" );

      m_outputExpression.toDom( outputElement.addElement( m_outputExpression.getElementName() ) );
    }

    if ( m_inputExpression != null ) {
      Element  inputElement  = element.addElement( "input" );

      m_inputExpression.toDom( inputElement.addElement( m_inputExpression.getElementName() ) );
    }

    if ( !m_correlations.isEmpty() ) {
      Element   correlationsElement  = element.addElement( "correlations" );
      Iterator  it                   = m_correlations.iterator();

      while ( it.hasNext() ) {
        CorrelationDefinition  correlationDef  = ( CorrelationDefinition ) it.next();

        correlationDef.toDom( correlationsElement.addElement( correlationDef.getElementName() ) );
      }
    }
  }
}
