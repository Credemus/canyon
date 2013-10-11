package de.objectcode.canyon.wetdock.tool.test;

import junit.framework.TestSuite;
import de.objectcode.canyon.api.worklist.ApplicationData;
import de.objectcode.canyon.api.worklist.WorkItemData;
import de.objectcode.canyon.wetdock.test.TestContext;
import de.objectcode.canyon.wetdock.test.WetdockTestBase;
import de.objectcode.canyon.wetdock.test.WetdockTestServiceHelper;
import de.objectcode.canyon.wetdock.test.WetdockUserManagerHelper;

/**
 * @author junglas
 */
public class Tool1Test extends WetdockTestBase
{
  public Tool1Test( String testName, TestContext context )
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
    userManager.createUser( "admin1", "admin1");
    userManager.createRole( "role1" );
    userManager.createRole( "admins" );
    userManager.createClient( "client1" );
    userManager.addUserRole( "user1", "role1", "client1" );
    userManager.addUserRole( "admin1", "admins", "client1" );
  }

  public void testCreatePackage()
      throws Exception
  {
    createPackage( "tool1.xpdl" );
  }

  public void testStartProcess()
      throws Exception
  {
    startProcess( "Tool1_Wor1", null );
  }


  public void testCompleteWorkItem1 ()
      throws Exception
  {
    WorkItemData workItem = findWorkItemByActivityId("Activity1", true);
    
    ApplicationData appData[] = workItem.getApplicationData();
    
    assertEquals("Application data length", 1, appData.length);
    assertEquals("Application parameter length", 1, appData[0].getParameters().length);
    assertEquals("Application parameter 1 name", "param", appData[0].getParameters()[0].getName());
    assertNull("Application parameter 1 value", appData[0].getParameters()[0].getValue());
    
    appData[0].getParameters()[0].setValue("test");
    
    new WetdockTestServiceHelper().setExceptionOn(false);
    new WetdockTestServiceHelper().setWaitDuration(0);

    completeWorkItem ( workItem );
  }

  public void testCompleteWorkItem3 ()
      throws Exception
  {
    WorkItemData workItem = findWorkItemByActivityId("Activity3", true);
    
    ApplicationData appData[] = workItem.getApplicationData();
    
    assertEquals("Application data length", 1, appData.length);
    assertEquals("Application parameter length", 1, appData[0].getParameters().length);
    assertEquals("Application parameter 1 name", "param", appData[0].getParameters()[0].getName());
    assertEquals("Application parameter 1 value", "test_test", appData[0].getParameters()[0].getValue().toString());
    
    completeWorkItem ( workItem );
  }

  public static TestSuite suite()
  {
    TestSuite suite = new TestSuite( "Tool1 Test" );
    TestContext context = new TestContext();

    suite.addTest( new Tool1Test( "testClearRepositories", context ) );
    suite.addTest( new Tool1Test( "testCreateUsersAndRoles", context ) );
    suite.addTest( new Tool1Test( "testCreatePackage", context ) );
    
    suite.addTest( new Tool1Test( "testStartProcess", context ) );
    suite.addTest( new Tool1Test( "testCompleteWorkItem1", context ) );
    suite.addTest( new Tool1Test( "testCompleteWorkItem3", context ) );
    suite.addTest( new Tool1Test( "testCheckProcessCompleted", context ));

    return suite;
  }
}
