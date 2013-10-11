package de.objectcode.canyon.wetdock.tool.test;

import java.util.Date;

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
public class ToolScriptTest extends WetdockTestBase
{
  public ToolScriptTest( String testName, TestContext context )
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

  public void testScript() throws Exception  {
    testClearRepositories();
    testCreateUsersAndRoles();
    createPackage( "toolScript.xpdl" );
    startProcess( "ToolScript", null );
    testCompleteWorkItem1();
    testCompleteWorkItem3();
    testCheckProcessCompleted();
  }




  public void testCompleteWorkItem1 ()
      throws Exception
  {
    WorkItemData workItem = findWorkItemByActivityId("Activity1", true);
    
    ApplicationData appData[] = workItem.getApplicationData();
    
    assertEquals("Application data length", 1, appData.length);
    assertEquals("Application parameter length", 3, appData[0].getParameters().length);
    assertEquals("Application parameter 1 name", "paramString", appData[0].getParameters()[0].getName());
    assertEquals("Application parameter 2 name", "paramInteger", appData[0].getParameters()[1].getName());
    assertEquals("Application parameter 3 name", "paramDate", appData[0].getParameters()[2].getName());
    appData[0].getParameters()[0].setValue("42");
    appData[0].getParameters()[1].setValue(new Integer(1));
    appData[0].getParameters()[2].setValue(new Date());
    completeWorkItem ( workItem );
  }

  public void testCompleteWorkItem3 ()
      throws Exception
  {
    WorkItemData workItem = findWorkItemByActivityId("Activity3", true);
    
    ApplicationData appData[] = workItem.getApplicationData();
    
    assertEquals("Application data length", 1, appData.length);
    assertEquals("Application parameter length", 3, appData[0].getParameters().length);
    assertEquals("Application parameter 1 name", "paramString", appData[0].getParameters()[0].getName());
    assertEquals("Application parameter 2 name", "paramInt", appData[0].getParameters()[1].getName());
    assertEquals("Application parameter 3 name", "paramDate", appData[0].getParameters()[2].getName());
    assertEquals("Wrong parameter value for parameter 2",new Integer(42),appData[0].getParameters()[1].getValue());
    completeWorkItem ( workItem );
  }

  public static TestSuite suite()
  {
    TestSuite suite = new TestSuite( "Tool1 Test" );
    TestContext context = new TestContext();

    suite.addTest( new ToolScriptTest( "testScript", context ) );
    return suite;
  }
}
