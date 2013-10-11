package de.objectcode.canyon.wetdock.deadline.test;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import junit.framework.TestSuite;
import de.objectcode.canyon.api.worklist.WorkItemData;
import de.objectcode.canyon.wetdock.test.TestContext;
import de.objectcode.canyon.wetdock.test.WetdockTestBase;
import de.objectcode.canyon.wetdock.test.WetdockUserManagerHelper;

/**
 * Sync Deadline
 * @author junglas
 */
public class DynamicDeadlineTest extends WetdockTestBase
{
  public DynamicDeadlineTest( String testName, TestContext context )
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
    createPackage( "dynamic_deadline.xpdl" );
  }

  public void testStartProcess(String processId, String deadlineExpr)
      throws Exception
  {
	  HashMap parameters = new HashMap();
	  parameters.put("deadlineExpr",deadlineExpr);
      startProcess(processId, parameters );
  }


  public void testCompleteWorkItem1 ()
      throws Exception
  {
    WorkItemData workItem = findWorkItemByActivityId("Activity1", true);
    
    completeWorkItem ( workItem );
  }

  public void testCheckWorkItem1AndWait (int seconds)
      throws Exception
  {
    WorkItemData workItem = findWorkItemByActivityId("Activity1", true);

    Thread.sleep((seconds +10)* 1000L);
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
  	Thread.sleep(5000);
    WorkItemData workItem = findWorkItemByActivityId("Activity3", true);
    
    completeWorkItem ( workItem );
  }

  public void testSyncDeadline(String deadlineExpression, int seconds) throws Exception {
    testClearRepositories();
    testCreateUsersAndRoles();
    testCreatePackage();

    testStartProcess( "DynamicDeadline_Wor1", deadlineExpression);
    testCompleteWorkItem1();
    testCompleteWorkItem2();
    testCheckProcessCompleted();

    testStartProcess(  "DynamicDeadline_Wor1", deadlineExpression);
    testCheckWorkItem1AndWait(seconds);
    testCompleteWorkItem3();
    testCheckProcessCompleted();

  }

  public void testAsyncDeadline(String deadlineExpression, int seconds) throws Exception {
    testClearRepositories();
    testCreateUsersAndRoles();
    testCreatePackage();

    testStartProcess("DynamicAsync", deadlineExpression);
    testCompleteWorkItem1();
    testCompleteWorkItem2();
    testCheckProcessCompleted();

    testStartProcess("DynamicAsync", deadlineExpression);
    testCheckWorkItem1AndWait(seconds);
    testCompleteWorkItem3();
    testCompleteWorkItem1();
    testCompleteWorkItem2();
    testCheckProcessCompleted();

  }

  public void testRelDynSyncDeadline() throws Exception {
	  testSyncDeadline("10s",10);
  }
  
  public void testAbsDynSyncDeadline() throws Exception {
	  long deadline = System.currentTimeMillis() + 15*1000L;
	  SimpleDateFormat f = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
	  testSyncDeadline(f.format(new Date(deadline)),15);
  }

  public void testRelDynAsyncDeadline() throws Exception {
	  testAsyncDeadline("10s",10);
  }
  
  public void testAbsDynAsyncDeadline() throws Exception {
	  long deadline = System.currentTimeMillis() + 15*1000L;
	  SimpleDateFormat f = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
	  testAsyncDeadline(f.format(new Date(deadline)),15);
  }

  public static TestSuite suite()
  {
    TestSuite suite = new TestSuite( "DynamicDeadline Test" );
    TestContext context = new TestContext();

    suite.addTest( new DynamicDeadlineTest( "testRelDynSyncDeadline", context ) );
    suite.addTest( new DynamicDeadlineTest( "testAbsDynSyncDeadline", context ) );    
    suite.addTest( new DynamicDeadlineTest( "testRelDynAsyncDeadline", context ) );
    suite.addTest( new DynamicDeadlineTest( "testAbsDynAsyncDeadline", context ) );    
    return suite;
  }
}
