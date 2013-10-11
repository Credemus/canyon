package de.objectcode.canyon.wetdock.deadline.test;

import junit.framework.TestSuite;
import de.objectcode.canyon.api.worklist.WorkItemData;
import de.objectcode.canyon.wetdock.test.TestContext;
import de.objectcode.canyon.wetdock.test.WetdockTestBase;
import de.objectcode.canyon.wetdock.test.WetdockUserManagerHelper;

/**
 * Sync Deadline
 * @author junglas
 */
public class Deadline1Test extends WetdockTestBase
{
  public Deadline1Test( String testName, TestContext context )
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
    createPackage( "deadline1.xpdl" );
  }

  public void testStartProcess()
      throws Exception
  {
    startProcess( "Deadline1_Wor1", null );
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
      
  
  public void testCompleteWorkItem2 ()
      throws Exception
  {
    WorkItemData workItem = findWorkItemByActivityId("Activity2", true);
    
    completeWorkItem ( workItem );
  }
  
  public void testCompleteWorkItem3 ()
      throws Exception
  {
    WorkItemData workItem = findWorkItemByActivityId("Activity3", true);
    
    completeWorkItem ( workItem );
  }

  public void testSyncDeadline() throws Exception {
    testClearRepositories();
    testCreateUsersAndRoles();
    testCreatePackage();

    testStartProcess();
    testCompleteWorkItem1();
    testCompleteWorkItem2();
    testCheckProcessCompleted();

    testStartProcess();
    testCheckWorkItem1AndWait();
    testCompleteWorkItem3();
    testCheckProcessCompleted();

  }
  
  public static TestSuite suite()
  {
    TestSuite suite = new TestSuite( "Deadline1 Test" );
    TestContext context = new TestContext();

    suite.addTest( new Deadline1Test( "testSyncDeadline", context ) );
    return suite;
  }
}
