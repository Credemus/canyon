package de.objectcode.canyon.persistent.process;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.SerializationUtils;

import de.objectcode.canyon.model.RedefinableHeader;
import de.objectcode.canyon.model.WorkflowPackage;
import de.objectcode.canyon.model.process.WorkflowProcess;
import de.objectcode.canyon.spi.RepositoryException;

/**
 * @hibernate.class table="PPACKAGES"
 *
 * @author    junglas
 * @created   18.06.2003
 * @version   $Id: PPackage.java,v 1.6 2004/04/08 12:36:39 junglas Exp $
 */
public class PPackage
{
  private  PPackageID       m_id;
  private  String           m_name;
  private  String           m_description;
  private  String           m_author;
  private  String           m_vendor;
  private  Set              m_processDefinitionsSet;
  private  byte[]  m_packageData;


  /**
   *Constructor for the HibPackage object
   */
  private PPackage() { }


  /**
   *Constructor for the HibPackage object
   *
   * @param pkg                      Description of the Parameter
   * @exception RepositoryException  Description of the Exception
   */
  public PPackage( WorkflowPackage pkg )
    throws RepositoryException
  {
    m_processDefinitionsSet = new HashSet();

    RedefinableHeader  rhdr  = pkg.getRedefinableHeader();

    m_id = new PPackageID( pkg.getId(), pkg.findPackageVersion() );
    m_name = pkg.getName();

    update( pkg );
  }


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
   * @param string
   */
  public void setVendor( String string )
  {
    m_vendor = string;
  }


  /**
   * @param set  The new processDefinitions value
   */
  public void setProcessDefinitionsSet( Set set )
  {
    m_processDefinitionsSet = set;
  }


  /**
   * Sets the processDefinitionState attribute of the HibPackage object
   *
   * @param processDefinitionId      The new processDefinitionState value
   * @param newState                 The new processDefinitionState value
   * @exception RepositoryException  Description of the Exception
   */
  public void setProcessDefinitionState( String processDefinitionId, int newState )
    throws RepositoryException
  {
    WorkflowPackage workflowPackage = (WorkflowPackage)SerializationUtils.deserialize(m_packageData);
    WorkflowProcess workflow = workflowPackage.getWorklowProcess(processDefinitionId);
    
    if ( workflow == null )
      throw new RepositoryException( "Invalid process definition id" );
    
    workflow.setState(newState);
    m_packageData = SerializationUtils.serialize(workflowPackage);
  }


  /**
   * Sets the package attribute of the HibPackage object
   *
   * @param pkg  The new package value
   */
  public void setPackageData( byte[] pkg )
  {
    m_packageData = pkg;
  }


  /**
   * @param packageID
   */
  public void setId( PPackageID packageID )
  {
    m_id = packageID;
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
   * @hibernate.property column="DESCRIPTION" type="string" length="254" not-null="false"
   *
   * @return
   */
  public String getDescription()
  {
    return m_description;
  }


  /**
   * @hibernate.property column="NAME" type="string" length="64" not-null="true"
   *
   * @return
   */
  public String getName()
  {
    return m_name;
  }


  /**
   * @return
   */
  public String getPackageId()
  {
    return m_id.getId();
  }



  /**
   * @hibernate.property column="VENDOR" type="string" length="32" not-null="false"
   *
   * @return
   */
  public String getVendor()
  {
    return m_vendor;
  }


  /**
   * @return
   */
  public String getVersion()
  {
    return m_id.getVersion();
  }


  /**
   * @hibernate.set lazy="true" cascade="all"
   * @hibernate.collection-key
   * @hibernate.collection-key-column name="PACKAGEID"
   * @hibernate.collection-key-column name="PACKAGEVERSION"
   * @hibernate.collection-one-to-many class="de.objectcode.canyon.persistent.process.PProcessDefinition"
   *
   * @return
   */
  public Set getProcessDefinitionsSet()
  {
    return m_processDefinitionsSet;
  }


  /**
   * @hibernate.property column="SERPACKAGE" type="de.objectcode.canyon.persistent.util.BlobType" not-null="true"
   *
   * @return
   * @exception RepositoryException  Description of the Exception
   */
  public byte[] getPackageData()
    throws RepositoryException
  {
    return m_packageData;
  }


  /**
   * Gets the processDefinition attribute of the HibPackage object
   *
   * @param processDefinitionId      Description of the Parameter
   * @return                         The processDefinition value
   * @exception RepositoryException  Description of the Exception
   */
  public WorkflowProcess getProcessDefinition( String processDefinitionId )
    throws RepositoryException
  {
    WorkflowPackage workflowPackage = (WorkflowPackage)SerializationUtils.deserialize(getPackageData());
    
    WorkflowProcess workflow = workflowPackage.getWorklowProcess(processDefinitionId);
    
    if ( workflow == null )
      throw new RepositoryException( "Invalid process definition id" );
    
    return workflow;
  }


  /**
   * @hibernate.id generator-class="assigned" unsaved-value="any"
   *
   * @return
   */
  public PPackageID getId()
  {
    return m_id;
  }


  /**
   * Description of the Method
   *
   * @param pkg                      Description of the Parameter
   * @exception RepositoryException  Description of the Exception
   */
  public void update( WorkflowPackage pkg )
    throws RepositoryException
  {
    if ( !getPackageId().equals( pkg.getId() ) ) {
      throw new IllegalArgumentException( "Package ID mismatch: " + getPackageId() + " != " + pkg.getId() );
    }

    m_name = pkg.getName();
    m_description = pkg.getDescription();
    m_vendor = pkg.getPackageHeader().getVendor();
    RedefinableHeader  rhdr  = pkg.getRedefinableHeader();
    if ( rhdr != null ) {
      m_author = rhdr.getAuthor();
    }
    m_packageData = SerializationUtils.serialize(pkg);

    Iterator           it    = m_processDefinitionsSet.iterator();
    Map                byId  = new HashMap();

    while ( it.hasNext() ) {
      PProcessDefinition  procDef  = ( PProcessDefinition ) it.next();

      byId.put( procDef.getProcessDefinitionId(), procDef );
    }

    WorkflowProcess processes[] = pkg.getWorkflowProcesses();
    int i;
    
    for ( i = 0; i < processes.length; i++ ) {
      WorkflowProcess  workflow  = processes[i];

      if ( !byId.containsKey( workflow.getId() ) ) {
        PProcessDefinition  procDef  = new PProcessDefinition( this, workflow );

        m_processDefinitionsSet.add( procDef );
      } else {
        PProcessDefinition  procDef  = ( PProcessDefinition ) byId.get( workflow.getId() );

        procDef.update( workflow );
        byId.remove( workflow.getId() );
      }
    }

    it = m_processDefinitionsSet.iterator();

    while ( it.hasNext() ) {
      PProcessDefinition  procDef  = ( PProcessDefinition ) it.next();

      if ( byId.containsKey( procDef.getProcessDefinitionId() ) ) {
        it.remove();
      }
    }
  }

}
