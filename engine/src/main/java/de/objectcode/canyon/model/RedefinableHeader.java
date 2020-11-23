package de.objectcode.canyon.model;

import java.io.Serializable;

/**
 * @author    junglas
 * @created   20. November 2003
 */
public class RedefinableHeader implements Serializable
{
	static final long serialVersionUID = -1430378860275749074L;
	
	private  String             m_author;
  private  String             m_version;
  private  String             m_codepage;
  private  String             m_countrykey;
  private  String[]           m_responsibles;
  private  PublicationStatus  m_publicationStatus;


  /**
   * @param string
   */
  public void setAuthor( String string )
  {
    m_author = string;
  }


  /**
   * @param string
   */
  public void setCodepage( String string )
  {
    m_codepage = string;
  }


  /**
   * @param string
   */
  public void setCountrykey( String string )
  {
    m_countrykey = string;
  }


  /**
   * @param status
   */
  public void setPublicationStatus( PublicationStatus status )
  {
    m_publicationStatus = status;
  }


  /**
   * @param strings
   */
  public void setResponsibles( String[] strings )
  {
    m_responsibles = strings;
  }


  /**
   * @param string
   */
  public void setVersion( String string )
  {
    m_version = string;
  }


  /**
   * @return
   */
  public String getAuthor()
  {
    return m_author;
  }


  /**
   * @return
   */
  public String getCodepage()
  {
    return m_codepage;
  }


  /**
   * @return
   */
  public String getCountrykey()
  {
    return m_countrykey;
  }


  /**
   * @return
   */
  public PublicationStatus getPublicationStatus()
  {
    return m_publicationStatus;
  }


  /**
   * @return
   */
  public String[] getResponsibles()
  {
    return m_responsibles;
  }


  /**
   * @return
   */
  public String getVersion()
  {
    return m_version;
  }

}
