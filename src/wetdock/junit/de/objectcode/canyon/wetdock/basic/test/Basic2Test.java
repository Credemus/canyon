package de.objectcode.canyon.wetdock.basic.test;

import junit.framework.TestSuite;
import de.objectcode.canyon.api.worklist.ApplicationData;
import de.objectcode.canyon.api.worklist.ProcessInstanceData;
import de.objectcode.canyon.api.worklist.WorkItemData;
import de.objectcode.canyon.wetdock.test.ProcessHelper;
import de.objectcode.canyon.wetdock.test.TestContext;
import de.objectcode.canyon.wetdock.test.WetdockTestBase;
import de.objectcode.canyon.wetdock.test.WetdockUserManagerHelper;

/**
 * @author junglas
 */
public class Basic2Test extends WetdockTestBase
{

  public Basic2Test( String testName, TestContext context )
  {
    super( testName, context );
  }

  public void testCreateUsersAndRoles()
      throws Exception
  {
    m_context.setClientId( "client1" );
    m_context.setUserId( "user1" );

    WetdockUserManagerHelper userManager = new WetdockUserManagerHelper();

    userManager.createUser( "user1", "user1" );
    userManager.createRole( "role1" );
    userManager.createClient( "client1" );
    userManager.addUserRole( "user1", "role1", "client1" );
  }

  public void testCreatePackage()
      throws Exception
  {
    createPackage( "basic2.xpdl" );
  }

  public void testStartProcess()
      throws Exception
  {
    startProcess( "basic2_Wor1", null );
  }
  
  public void testCheckProcessData1()
      throws Exception
  {
    ProcessInstanceData processInstance = new ProcessHelper().getProcessInstance(m_context.getProcessInstance().getId());
    
    ProcessInstanceData.Attribute attrs[] = processInstance.getAttributes();
    
    assertEquals("ProcessAttrs length", 3, attrs.length);    
    assertEquals("ProcessAttr 1 name", "param1", attrs[0].getName());
    assertEquals("ProcessAttr 1 value", "Initial Param1", attrs[0].getValue().toString());
    assertEquals("ProcessAttr 2 name", "param2", attrs[1].getName());
    assertEquals("ProcessAttr 2 value", "Initial Param2", attrs[1].getValue().toString());
    assertEquals("ProcessAttr 3 name", "param3", attrs[2].getName());    
    assertEquals("ProcessAttr 3 value", "Initial Param3", attrs[2].getValue().toString());
  }

  public void testCompleteWorkItem1()
      throws Exception
  {
    WorkItemData workItem = findWorkItemByActivityId( "Activity1", true );

    ApplicationData appData[] = workItem.getApplicationData();
    
    assertEquals("Application data length", 1, appData.length);
    assertEquals("Application parameter length", 3, appData[0].getParameters().length);
    assertEquals("Application parameter 1 name", "testApp1Param1", appData[0].getParameters()[0].getName());
    assertEquals("Application parameter 1 value", "Initial Param1", appData[0].getParameters()[0].getValue().toString());
    assertEquals("Application parameter 2 name", "testApp1Param2", appData[0].getParameters()[1].getName());
    assertEquals("Application parameter 2 value", "Initial Param2", appData[0].getParameters()[1].getValue().toString());
    assertEquals("Application parameter 3 name", "testApp1Param3", appData[0].getParameters()[2].getName());

    appData[0].getParameters()[2].setValue("Out Test3");
    
    completeWorkItem( workItem );
  }

  public void testCheckProcessData2()
      throws Exception
  {
    ProcessInstanceData processInstance = new ProcessHelper().getProcessInstance(m_context.getProcessInstance().getId());
    
    ProcessInstanceData.Attribute attrs[] = processInstance.getAttributes();
    
    assertEquals("ProcessAttrs length", 3, attrs.length);    
    assertEquals("ProcessAttr 1 name", "param1", attrs[0].getName());
    assertEquals("ProcessAttr 1 value", "Initial Param1", attrs[0].getValue().toString());
    assertEquals("ProcessAttr 2 name", "param2", attrs[1].getName());
    assertEquals("ProcessAttr 2 value", "Initial Param2", attrs[1].getValue().toString());
    assertEquals("ProcessAttr 3 name", "param3", attrs[2].getName());    
    assertEquals("ProcessAttr 3 value", "Out Test3", attrs[2].getValue().toString());
  }

  public void testCompleteWorkItem2()
      throws Exception
  {
    WorkItemData workItem = findWorkItemByActivityId( "Activity2", true );

    completeWorkItem( workItem );
  }

  public static TestSuite suite()
  {
    TestSuite suite = new TestSuite( "Basic2 Test" );
    TestContext context = new TestContext();

    suite.addTest( new Basic2Test( "testClearRepositories", context ) );
    suite.addTest( new Basic2Test( "testCreateUsersAndRoles", context ) );
    suite.addTest( new Basic2Test( "testCreatePackage", context ) );
    suite.addTest( new Basic2Test( "testStartProcess", context ) );
    suite.addTest( new Basic2Test( "testCheckProcessData1", context ) );
    suite.addTest( new Basic2Test( "testCompleteWorkItem1", context ) );
    suite.addTest( new Basic2Test( "testCheckProcessData2", context ) );
    suite.addTest( new Basic2Test( "testCompleteWorkItem2", context ) );
    suite.addTest( new Basic2Test( "testCheckProcessCompleted", context ) );

    return suite;
  }
}
