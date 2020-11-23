package de.objectcode.canyon.spiImpl.parser;

import org.apache.commons.digester.Digester;
import org.apache.commons.digester.RuleSetBase;

import de.objectcode.canyon.model.ExtendedAttribute;
import de.objectcode.canyon.model.ExternalPackage;
import de.objectcode.canyon.model.PackageHeader;
import de.objectcode.canyon.model.RedefinableHeader;
import de.objectcode.canyon.model.Script;
import de.objectcode.canyon.model.activity.Activity;
import de.objectcode.canyon.model.activity.ActivitySet;
import de.objectcode.canyon.model.activity.AutomationMode;
import de.objectcode.canyon.model.activity.BlockActivity;
import de.objectcode.canyon.model.activity.Deadline;
import de.objectcode.canyon.model.activity.NoImplementation;
import de.objectcode.canyon.model.activity.Route;
import de.objectcode.canyon.model.activity.SimulationInformation;
import de.objectcode.canyon.model.activity.SubFlow;
import de.objectcode.canyon.model.activity.Tool;
import de.objectcode.canyon.model.application.Application;
import de.objectcode.canyon.model.data.ActualParameter;
import de.objectcode.canyon.model.data.DataType;
import de.objectcode.canyon.model.data.EnumerationType;
import de.objectcode.canyon.model.data.EnumerationValue;
import de.objectcode.canyon.model.data.FormalParameter;
import de.objectcode.canyon.model.data.TypeDeclaration;
import de.objectcode.canyon.model.participant.Participant;
import de.objectcode.canyon.model.process.DataField;
import de.objectcode.canyon.model.process.ProcessHeader;
import de.objectcode.canyon.model.process.TimeEstimation;
import de.objectcode.canyon.model.process.WorkflowProcess;
import de.objectcode.canyon.model.transition.Condition;
import de.objectcode.canyon.model.transition.Join;
import de.objectcode.canyon.model.transition.Split;
import de.objectcode.canyon.model.transition.Transition;
import de.objectcode.canyon.model.transition.TransitionRef;
import de.objectcode.canyon.model.transition.TransitionRestriction;
import de.objectcode.canyon.model.transition.Xpression;

/**
 * @author    junglas
 * @created   21. November 2003
 */
public class XPDLRuleSet extends RuleSetBase
{
  /**
   * Gets the namespaceURI attribute of the XPDLRuleSet object
   *
   * @return   The namespaceURI value
   */
  public String getNamespaceURI()
  {
    return "http://www.wfmc.org/2002/XPDL1.0";
  }


  /**
   * Adds a feature to the RuleInstances attribute of the XPDLRuleSet object
   *
   * @param digester  The feature to be added to the RuleInstances attribute
   */
  public void addRuleInstances( Digester digester )
  {
    digester.addRule( "Package", new SetLowerPropertiesRule() );
    digester.addRule("Package/ConformanceClass", new SetLowerPropertiesRule() );

    addStdMultiChild( digester, "*/ExtendedAttribute", "ExtendedAttribute", ExtendedAttribute.class );

    addStdSingleChild( digester, "Package/PackageHeader", "PackageHeader", PackageHeader.class );
    digester.addRule( "Package/PackageHeader/?", new BeanLowerPropertySetterRule() );
    
    addStdSingleChild( digester, "Package/Script", "Script", Script.class);

    addStdSingleChild( digester, "*/RedefinableHeader", "RedefinableHeader", RedefinableHeader.class );
    digester.addRule( "*/RedefinableHeader/?", new BeanLowerPropertySetterRule() );

    addStdMultiChild( digester, "Package/ExternalPackages/ExternalPackage", "ExternalPackage", ExternalPackage.class);

    addStdMultiChild( digester, "*/Participant", "Participant", Participant.class );
    digester.addRule( "*/Participant/ParticipantType", new SetLowerPropertiesRule() );

    addStdMultiChild( digester, "*/Application", "Application", Application.class );
    addStdMultiChild( digester, "*/DataField", "DataField", DataField.class);
    digester.addRule( "*/DataField/InitialValue", new BeanLowerPropertySetterRule() );
    
    addStdMultiChild( digester, "*/TypeDeclaration", "TypeDeclaration", TypeDeclaration.class);

    addStdMultiChild( digester, "*/FormalParameter", "FormalParameter", FormalParameter.class );
    digester.addRule( "*/FormalParameter/Description", new BeanLowerPropertySetterRule() );
    digester.addFactoryCreate("*/BasicType", BasicTypeFactory.class);
    digester.addSetNext( "*/BasicType", "setDataType", DataType.class.getName());
    digester.addObjectCreate("*/EnumerationType", EnumerationType.class);
    digester.addSetNext("*/EnumerationType", "setDataType", DataType.class.getName());
    addStdMultiChild( digester, "*/EnumerationType/EnumerationValue", "EnumerationValue", EnumerationValue.class);

    addStdMultiChild( digester, "*/WorkflowProcess", "WorkflowProcess", WorkflowProcess.class );
    addStdSingleChild( digester, "*/WorkflowProcess/ProcessHeader", "ProcessHeader", ProcessHeader.class);
    digester.addRule( "*/WorkflowProcess/ProcessHeader/?", new BeanLowerPropertySetterRule() );
    
    addStdMultiChild( digester, "*/ActivitySet", "ActivitySet", ActivitySet.class );
    addStdMultiChild( digester, "*/Transition", "Transition", Transition.class );
    addStdSingleChild( digester, "*/Transition/Condition", "Condition", Condition.class);
    digester.addBeanPropertySetter("*/Transition/Condition", "value");
    addStdMultiChild( digester, "*/Transition/Condition/Xpression", "Xpression", Xpression.class);
    digester.addBeanPropertySetter("*/Transition/Condition/Xpression", "value");
        
    addStdMultiChild( digester, "*/Activity", "Activity", Activity.class );
    
    digester.addRule( "*/Activity/Description", new BeanLowerPropertySetterRule() );
    digester.addRule( "*/Activity/Limit", new BeanLowerPropertySetterRule() );
    addStdSingleChild( digester, "*/Activity/BlockActivity", "BlockActivity", BlockActivity.class);
    addStdSingleChild( digester, "*/Activity/Route", "Route", Route.class);
    addStdSingleChild( digester, "*/Activity/Implementation/No", "Implementation", NoImplementation.class);
    addStdSingleChild( digester, "*/Activity/Implementation/SubFlow", "Implementation", SubFlow.class);
    addStdMultiChild( digester, "*/Activity/Implementation/SubFlow/ActualParameters/ActualParameter", "ActualParameter", ActualParameter.class);
    digester.addBeanPropertySetter("*/Activity/Implementation/SubFlow/ActualParameters/ActualParameter", "text");
    addStdMultiChild( digester, "*/Activity/Implementation/Tool", "Tool", Tool.class);
    addStdMultiChild( digester, "*/Activity/Implementation/Tool/ActualParameters/ActualParameter", "ActualParameter", ActualParameter.class);
    digester.addBeanPropertySetter("*/Activity/Implementation/Tool/ActualParameters/ActualParameter", "text");
    addStdMultiChild( digester, "*/Activity/Deadline", "Deadline", Deadline.class);
    digester.addRule( "*/Activity/Deadline/?", new BeanLowerPropertySetterRule() );
    digester.addRule( "*/Activity/Priority", new BeanLowerPropertySetterRule() );
    digester.addRule( "*/Activity/Icon", new BeanLowerPropertySetterRule() );
    digester.addRule( "*/Activity/Performer", new BeanLowerPropertySetterRule() );
    digester.addRule( "*/Activity/StartMode/?", new AutomationModeRule() );
    digester.addSetNext("*/Activity/StartMode/?", "setStartMode", AutomationMode.class.getName());
    digester.addRule( "*/Activity/FinishMode/?", new AutomationModeRule() );
    digester.addSetNext("*/Activity/FinishMode/?", "setFinishMode", AutomationMode.class.getName());
    digester.addRule( "*/Activity/Documentation", new BeanLowerPropertySetterRule() );
    
    addStdMultiChild( digester, "*/Activity/TransitionRestrictions/TransitionRestriction", "TransitionRestriction", TransitionRestriction.class);
    addStdSingleChild( digester, "*/Activity/TransitionRestrictions/TransitionRestriction/Join", "Join", Join.class);
    addStdSingleChild( digester, "*/Activity/TransitionRestrictions/TransitionRestriction/Split", "Split", Split.class);
    addStdMultiChild( digester, "*/Activity/TransitionRestrictions/TransitionRestriction/Split/TransitionRefs/TransitionRef", "TransitionRef", TransitionRef.class);
    
    
    addStdSingleChild( digester, "*/Activity/SimulationInformation", "SimulationInformation", SimulationInformation.class);
    digester.addRule( "*/Activity/SimulationInformation/Cost", new BeanLowerPropertySetterRule() ); 
    addStdSingleChild( digester, "*/TimeEstimation", "TimeEstimation", TimeEstimation.class);
    digester.addRule( "*/TimeEstimation/?", new BeanLowerPropertySetterRule() ); 
  }


  /**
   * Adds a feature to the StdSingleChild attribute of the XPDLRuleSet object
   *
   * @param digester  The feature to be added to the StdSingleChild attribute
   * @param pattern   The feature to be added to the StdSingleChild attribute
   * @param name      The feature to be added to the StdSingleChild attribute
   * @param clazz     The feature to be added to the StdSingleChild attribute
   */
  public void addStdSingleChild( Digester digester, String pattern, String name, Class clazz )
  {
    digester.addObjectCreate( pattern, clazz );
    digester.addRule(pattern, new SetLowerPropertiesRule());
    digester.addSetNext( pattern, "set" + name, clazz.getName() );
  }


  /**
   * Adds a feature to the StdMultiChild attribute of the XPDLRuleSet object
   *
   * @param digester  The feature to be added to the StdMultiChild attribute
   * @param pattern   The feature to be added to the StdMultiChild attribute
   * @param name      The feature to be added to the StdMultiChild attribute
   * @param clazz     The feature to be added to the StdMultiChild attribute
   */
  public void addStdMultiChild( Digester digester, String pattern, String name, Class clazz )
  {
    digester.addObjectCreate( pattern, clazz );
    digester.addRule(pattern, new SetLowerPropertiesRule());
    digester.addSetNext( pattern, "add" + name, clazz.getName() );
  }
}
