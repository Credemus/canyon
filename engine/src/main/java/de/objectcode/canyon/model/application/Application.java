package de.objectcode.canyon.model.application;

import java.util.SortedSet;
import java.util.TreeSet;

import de.objectcode.canyon.model.BaseElement;
import de.objectcode.canyon.model.data.ExternalReference;
import de.objectcode.canyon.model.data.FormalParameter;
import de.objectcode.canyon.model.data.FormalParameterIndexComparator;

/**
 * @author    junglas
 * @created   20. November 2003
 */
public class Application extends BaseElement
{
	static final long serialVersionUID = 6864493848930499853L;
	
	private  SortedSet<FormalParameter>          m_formalParameters;
  private  ExternalReference  m_externalReference;


  /**
   *Constructor for the Application object
   */
  public Application()
  {
  	m_formalParameters = new TreeSet<FormalParameter>( new FormalParameterIndexComparator() );
  }


  /**
   * @param reference
   */
  public void setExternalReference( ExternalReference reference )
  {
    m_externalReference = reference;
  }


  /**
   * @return
   */
  public ExternalReference getExternalReference()
  {
    return m_externalReference;
  }


  /**
   * Gets the formalParameters attribute of the Application object
   *
   * @return   The formalParameters value
   */
  public FormalParameter[] getFormalParameters()
  {
    FormalParameter  ret[]  = new FormalParameter[m_formalParameters.size()];

    m_formalParameters.toArray( ret );

    return ret;
  }


  /**
   * Adds a feature to the FormalParameter attribute of the Application object
   *
   * @param formalParameter  The feature to be added to the FormalParameter attribute
   */
  public void addFormalParameter( FormalParameter formalParameter )
  {
    m_formalParameters.add( formalParameter );
  }
}
