package de.objectcode.canyon.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import de.objectcode.canyon.model.application.Application;
import de.objectcode.canyon.model.data.TypeDeclaration;
import de.objectcode.canyon.model.participant.Participant;
import de.objectcode.canyon.model.process.DataField;
import de.objectcode.canyon.model.process.WorkflowProcess;

/**
 * @author    junglas
 * @created   20. November 2003
 */
public class WorkflowPackage extends BaseElement implements IValidatable
{
	static final long serialVersionUID = -3402381146408849123L;
	
	private  PackageHeader      m_packageHeader;
  private  RedefinableHeader  m_redefinableHeader;
  private  GraphConformance   m_graphConformance;
  private  Script             m_script;
  private  List               m_externalPackages;
  private  Map                m_participants;
  private  Map                m_applications;
  private  Map                m_workflowProcesses;
  private  Map                m_dataFields;
  private  Map                m_typeDeclarations;


  /**
   *Constructor for the WorkflowPackage object
   */
  public WorkflowPackage()
  {
    m_externalPackages = new ArrayList();
    m_participants = new HashMap();
    m_applications = new HashMap();
    m_workflowProcesses = new LinkedHashMap();
    m_dataFields = new HashMap();
    m_typeDeclarations = new HashMap();
  }


  /**
   * @param conformance
   */
  public void setGraphConformance( GraphConformance conformance )
  {
    m_graphConformance = conformance;
  }


  /**
   * @param header
   */
  public void setPackageHeader( PackageHeader header )
  {
    m_packageHeader = header;
  }


  /**
   * @param header
   */
  public void setRedefinableHeader( RedefinableHeader header )
  {
    m_redefinableHeader = header;
  }


  /**
   * @param script
   */
  public void setScript( Script script )
  {
    m_script = script;
  }


  /**
   * Gets the typeDeclarations attribute of the WorkflowPackage object
   *
   * @return   The typeDeclarations value
   */
  public TypeDeclaration[] getTypeDeclarations()
  {
    TypeDeclaration  ret[]  = new TypeDeclaration[m_typeDeclarations.size()];

    m_typeDeclarations.values().toArray( ret );

    return ret;
  }


  /**
   * Gets the typeDeclaration attribute of the WorkflowPackage object
   *
   * @param id  Description of the Parameter
   * @return    The typeDeclaration value
   */
  public TypeDeclaration getTypeDeclaration( String id )
  {
    return ( TypeDeclaration ) m_typeDeclarations.get( id );
  }


  /**
   * Gets the dataField attribute of the WorkflowPackage object
   *
   * @param id  Description of the Parameter
   * @return    The dataField value
   */
  public DataField getDataField( String id )
  {
    return ( DataField ) m_dataFields.get( id );
  }


  /**
   * Gets the dataFields attribute of the WorkflowPackage object
   *
   * @return   The dataFields value
   */
  public DataField[] getDataFields()
  {
    DataField  ret[]  = new DataField[m_dataFields.size()];

    m_dataFields.values().toArray( ret );

    return ret;
  }


  /**
   * @return
   */
  public GraphConformance getGraphConformance()
  {
    return m_graphConformance;
  }


  /**
   * @return
   */
  public PackageHeader getPackageHeader()
  {
    return m_packageHeader;
  }


  /**
   * @return
   */
  public RedefinableHeader getRedefinableHeader()
  {
    return m_redefinableHeader;
  }


  /**
   * @return
   */
  public Script getScript()
  {
    return m_script;
  }


  /**
   * Gets the externalPackages attribute of the WorkflowPackage object
   *
   * @return   The externalPackages value
   */
  public ExternalPackage[] getExternalPackages()
  {
    ExternalPackage[]  ret  = new ExternalPackage[m_externalPackages.size()];

    m_externalPackages.toArray( ret );

    return ret;
  }


  /**
   * Gets the participant attribute of the WorkflowPackage object
   *
   * @param id  Description of the Parameter
   * @return    The participant value
   */
  public Participant getParticipant( String id )
  {
    return ( Participant ) m_participants.get( id );
  }


  /**
   * Gets the participants attribute of the WorkflowPackage object
   *
   * @return   The participants value
   */
  public Participant[] getParticipants()
  {
    Participant[]  ret  = new Participant[m_participants.size()];

    m_participants.values().toArray( ret );

    return ret;
  }


  /**
   * Gets the worklowProcess attribute of the WorkflowPackage object
   *
   * @param id  Description of the Parameter
   * @return    The worklowProcess value
   */
  public WorkflowProcess getWorklowProcess( String id )
  {
    return ( WorkflowProcess ) m_workflowProcesses.get( id );
  }


  /**
   * Gets the workflowProcesses attribute of the WorkflowPackage object
   *
   * @return   The workflowProcesses value
   */
  public WorkflowProcess[] getWorkflowProcesses()
  {
    WorkflowProcess  ret[]  = new WorkflowProcess[m_workflowProcesses.size()];

    m_workflowProcesses.values().toArray( ret );

    return ret;
  }


  /**
   * Gets the application attribute of the WorkflowProcess object
   *
   * @param id  Description of the Parameter
   * @return    The application value
   */
  public Application getApplication( String id )
  {
    return ( Application ) m_applications.get( id );
  }

  public String findPackageVersion()
  {
    if ( m_redefinableHeader != null && m_redefinableHeader.getVersion() != null ) {
      return m_redefinableHeader.getVersion();
    }

    return "1.0";
  }

  /**
   * Gets the applications attribute of the WorkflowProcess object
   *
   * @return   The applications value
   */
  public Application[] getApplications()
  {
    Application[]  ret  = new Application[m_applications.size()];

    m_applications.values().toArray( ret );

    return ret;
  }


  /**
   * Adds a feature to the TypeDeclaration attribute of the WorkflowPackage object
   *
   * @param typeDeclaration  The feature to be added to the TypeDeclaration attribute
   */
  public void addTypeDeclaration( TypeDeclaration typeDeclaration )
  {
    m_typeDeclarations.put( typeDeclaration.getId(), typeDeclaration );
  }


  /**
   * Adds a feature to the DataField attribute of the WorkflowPackage object
   *
   * @param dataField  The feature to be added to the DataField attribute
   */
  public void addDataField( DataField dataField )
  {
    m_dataFields.put( dataField.getId(), dataField );
  }


  /**
   * Adds a feature to the ExternalPackage attribute of the WorkflowPackage object
   *
   * @param externalPackage  The feature to be added to the ExternalPackage attribute
   */
  public void addExternalPackage( ExternalPackage externalPackage )
  {
    m_externalPackages.add( externalPackage );
  }


  /**
   * Adds a feature to the Participant attribute of the WorkflowPackage object
   *
   * @param participant  The feature to be added to the Participant attribute
   */
  public void addParticipant( Participant participant )
  {
    m_participants.put( participant.getId(), participant );
  }


  /**
   * Adds a feature to the Application attribute of the WorkflowProcess object
   *
   * @param application  The feature to be added to the Application attribute
   */
  public void addApplication( Application application )
  {
    m_applications.put( application.getId(), application );
  }


  /**
   * Adds a feature to the WorkflowProcess attribute of the WorkflowPackage object
   *
   * @param workflowProcess  The feature to be added to the WorkflowProcess attribute
   */
  public void addWorkflowProcess( WorkflowProcess workflowProcess )
  {
    m_workflowProcesses.put( workflowProcess.getId(), workflowProcess );
    workflowProcess.setPackage( this );
  }


  /**
   * Description of the Method
   *
   * @return   Description of the Return Value
   */
  public ValidationErrors validate()
  {
    ValidationErrors  errors  = new ValidationErrors();

    errors.check( m_participants.values() );
    errors.check( m_workflowProcesses.values() );

    return errors;
  }
}
