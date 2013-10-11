package de.objectcode.canyon.wetdock.limit.test;

import junit.framework.TestSuite;
import de.objectcode.canyon.api.worklist.ProcessInstanceData;
import de.objectcode.canyon.api.worklist.WorkItemData;
import de.objectcode.canyon.wetdock.test.TestContext;
import de.objectcode.canyon.wetdock.test.WetdockTestBase;
import de.objectcode.canyon.wetdock.test.WetdockUserManagerHelper;

/**
 * @author junglas
 */
public class Limit1Test extends WetdockTestBase
{
  public Limit1Test( String testName, TestContext context )
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
    createPackage( "limit1.xpdl" );
  }

  public void testStartProcess()
      throws Exception
  {
    startProcess( "Limit1_Wor1", null );
  }


  public void testCompleteWorkItem1 ()
      throws Exception
  {
    WorkItemData workItem = findWorkItemByActivityId("Activity1", true);
    
    completeWorkItem ( workItem );
  }

  public void testCheckWorkItem1AndWait ()
      throws Exception
  {
    WorkItemData workItem = findWorkItemByActivityId("Activity1", true);

    Thread.sleep(40 * 1000L);
  }
  
  public void testCheckLimitExceededProcess ()
    throws Exception
  {
    ProcessInstanceData processInstance = findProcessInstanceByProcessId ( "LimitExceeded", true);
    
    m_context.setUserId("admin1");
    m_context.setProcessInstance(processInstance);
  }
  

  public void testCompleteWorkItemLimitActivity ()
      throws Exception
  {
    WorkItemData workItem = findWorkItemByActivityId("LimitActivity", true);
    
    completeWorkItem ( workItem );
  }
  
  public static TestSuite suite()
  {
    TestSuite suite = new TestSuite( "Limit1 Test" );
    TestContext context = new TestContext();

    suite.addTest( new Limit1Test( "testClearRepositories", context ) );
    suite.addTest( new Limit1Test( "testCreateUsersAndRoles", context ) );
    suite.addTest( new Limit1Test( "testCreatePackage", context ) );

    suite.addTest( new Limit1Test( "testStartProcess", context ) );
    suite.addTest( new Limit1Test( "testCompleteWorkItem1", context ) );
    suite.addTest( new Limit1Test( "testCheckProcessCompleted", context ));
    
    suite.addTest( new Limit1Test( "testStartProcess", context ) );
    suite.addTest( new Limit1Test( "testCheckWorkItem1AndWait", context ));
    suite.addTest( new Limit1Test( "testCheckProcessCompleted", context ));
    suite.addTest( new Limit1Test( "testCheckLimitExceededProcess", context ));
    suite.addTest( new Limit1Test( "testCompleteWorkItemLimitActivity", context ) );
    suite.addTest( new Limit1Test( "testCheckProcessCompleted", context ));

    return suite;
  }  
}
