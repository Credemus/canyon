package de.objectcode.canyon.persistent.process;

import java.util.Date;

import de.objectcode.canyon.model.PublicationStatus;
import de.objectcode.canyon.model.RedefinableHeader;
import de.objectcode.canyon.model.WorkflowPackage;
import de.objectcode.canyon.model.process.Duration;
import de.objectcode.canyon.model.process.DurationUnit;
import de.objectcode.canyon.model.process.ProcessHeader;
import de.objectcode.canyon.model.process.WorkflowProcess;

/**
 * @hibernate.class table="PPROCESSDEFINITIONS"
 *
 * @author    junglas
 * @created   18.06.2003
 * @version   $Id: PProcessDefinition.java,v 1.5 2004/03/30 08:13:09 ruth Exp $
 */
public class PProcessDefinition
{
  private  PProcessDefinitionID  m_id;
  private  PPackage              m_package;
  private  String                m_name;
  private  String                m_description;
  private  String                m_author;
  private  String                m_publicationStatus;
  private  Date                  m_createdDate;
  private  Date                  m_validFromDate;
  private  Date                  m_validToDate;
  private  int                   m_state;


  /**
   *Constructor for the HibProcessDefinition object
   */
  private PProcessDefinition() { }


  /**
   *Constructor for the HibProcessDefinition object
   *
   * @param workflow   Description of the Parameter
   * @param pkg        Description of the Parameter
   */
  public PProcessDefinition( PPackage pkg, WorkflowProcess workflow )
  {
    m_id = new PProcessDefinitionID( workflow.getId(), workflow.findWorkflowVersion() );
    m_package = pkg;

    update( workflow );
  }


  /**
   * @param string
   */
  public void setAuthor( String string )
  {
    m_author = string;
  }


  /**
   * @param date
   */
  public void setCreatedDate( Date date )
  {
    m_createdDate = date;
  }


  /**
   * @param string
   */
  public void setDescription( String string )
  {
    m_description = string;
  }


  /**
   * @param string
   */
  public void setName( String string )
  {
    m_name = string;
  }


  /**
   * @param i
   */
  public void setState( int i )
  {
    m_state = i;
  }


  /**
   * @param string
   */
  public void setPublicationStatus( String string )
  {
    m_publicationStatus = string;
  }


  /**
   * @param date
   */
  public void setValidFromDate( Date date )
  {
    m_validFromDate = date;
  }


  /**
   * @param date
   */
  public void setValidToDate( Date date )
  {
    m_validToDate = date;
  }



  /**
   * @param definitionID
   */
  public void setId( PProcessDefinitionID definitionID )
  {
    m_id = definitionID;
  }


  /**
   * @param package1
   */
  public void setPackage( PPackage package1 )
  {
    m_package = package1;
  }


  /**
   * @hibernate.property column="AUTHOR" type="string" length="32" not-null="false"
   *
   * @return
   */
  public String getAuthor()
  {
    return m_author;
  }


  /**
   * @hibernate.property column="CREATEDDATE" type="timestamp" not-null="false"
   *
   * @return
   */
  public Date getCreatedDate()
  {
    return m_createdDate;
  }


  /**
   * @hibernate.property column="DESCRIPTION" type="string" length="254" not-null="false"
   *
   * @return
   */
  public String getDescription()
  {
    return m_description;
  }


  /**
   * @hibernate.property column="NAME" type="string" length="64" not-null="false"
   *
   * @return
   */
  public String getName()
  {
    return m_name;
  }


  /**
   * @return   The processDefinitionId value
   */
  public String getProcessDefinitionId()
  {
    return m_id.getId();
  }


  /**
   * @hibernate.property column="STATE" type="integer" length="2" not-null="true"
   *
   * @return
   */
  public int getState()
  {
    return m_state;
  }


  /**
   * @hibernate.property column="STATUS" type="string length="32" not-null="false"
   *
   * @return
   */
  public String getPublicationStatus()
  {
    return m_publicationStatus;
  }


  /**
   * @hibernate.property column="VALIDFROMDATE" type="timestamp" not-null="false"
   *
   * @return
   */
  public Date getValidFromDate()
  {
    return m_validFromDate;
  }


  /**
   * @hibernate.property column="VALIDTODATE" type="timestamp" not-null="false"
   *
   * @return
   */
  public Date getValidToDate()
  {
    return m_validToDate;
  }


  /**
   * @return
   */
  public String getVersion()
  {
    return m_id.getVersion();
  }


  /**
   * @hibernate.id generator-class="assigned" unsaved-value="any"
   *
   * @return
   */
  public PProcessDefinitionID getId()
  {
    return m_id;
  }


  /**
   * @hibernate.many-to-one not-null="true" class="de.objectcode.canyon.persistent.process.PPackage"
   * @hibernate.column name="PACKAGEID"
   * @hibernate.column name="PACKAGEVERSION"
   *
   * @return
   */
  public PPackage getPackage()
  {
    return m_package;
  }


  /**
   * Description of the Method
   *
   * @param workflow  Description of the Parameter
   */
  public void update( WorkflowProcess workflow )
  {
    if ( !getProcessDefinitionId().equals( workflow.getId() ) ) {
      throw new IllegalArgumentException( "processDefinitionId mismatch" );
    }
    if ( !m_package.getPackageId().equals( workflow.getPackage().getId() ) ) {

      // If updating, package ID must match as well.
      throw new IllegalArgumentException( "packageId mismatch" );
    }

    WorkflowPackage    pkg        = workflow.getPackage();
    RedefinableHeader  redefHdr   = workflow.getRedefinableHeader();
    String             author     = null;
    String             pubStat    = null;
    if ( redefHdr != null ) {
      author = redefHdr.getAuthor();
      PublicationStatus  ps  = redefHdr.getPublicationStatus();
      if ( ps != null ) {
        pubStat = ps.toString();
      }
    }
    redefHdr = pkg.getRedefinableHeader();
    if ( redefHdr != null ) {
      if ( author == null ) {
        author = redefHdr.getAuthor();
      }
      if ( pubStat == null ) {
        PublicationStatus  ps  = redefHdr.getPublicationStatus();
        if ( ps != null ) {
          pubStat = ps.toString();
        }
      }
    }

    ProcessHeader      hdr        = workflow.getProcessHeader();
    DurationUnit       unit       = hdr.getDurationUnit();
    Date               created    = hdr.getCreated();
    if ( created == null ) {
      created = pkg.getPackageHeader().getCreated();
    }
    Date               validFrom  = null;
    Date               validTo    = null;
    if ( created != null ) {
      Duration  duration  = hdr.getValidFrom();
      if ( duration != null ) {
        long  interval  = duration.getMillis( unit );
        validFrom = new Date( created.getTime() + interval );
      } else {
        validFrom = new Date();
      }
      duration = hdr.getValidTo();
      if ( duration != null ) {
        long  interval  = duration.getMillis( unit );
        validTo = new Date( created.getTime() + interval );
      }
    } else {
      validFrom = new Date();
    }

    m_name = workflow.getName();
    m_description = hdr.getDescription();
    m_author = author;
    m_publicationStatus = pubStat;
    m_createdDate = created;
    m_validFromDate = validFrom;
    m_validToDate = validTo;
    m_state = workflow.getState();
  }

}
