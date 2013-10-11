package de.objectcode.canyon.wetdock.limit.test;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import junit.framework.TestSuite;
import de.objectcode.canyon.api.worklist.ProcessInstanceData;
import de.objectcode.canyon.api.worklist.WorkItemData;
import de.objectcode.canyon.wetdock.test.TestContext;
import de.objectcode.canyon.wetdock.test.WetdockTestBase;
import de.objectcode.canyon.wetdock.test.WetdockUserManagerHelper;

/**
 * @author junglas
 */
public class DynamicLimitTest extends WetdockTestBase
{
  public DynamicLimitTest( String testName, TestContext context )
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
    createPackage( "dynamic_limit.xpdl" );
  }

  public void testStartProcess(String limitExpr)
      throws Exception
  {
	HashMap parameters = new HashMap();
	parameters.put("limitExpr",limitExpr);
    startProcess( "DynamicLimit_Wor1", parameters );
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

    Thread.sleep((seconds+15) * 1000L);
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

  public void testDynamicLimit(String limitExpr, int seconds) throws Exception {
	    testClearRepositories();
	    testCreateUsersAndRoles();
	    testCreatePackage();

	    testStartProcess(limitExpr);
	    testCompleteWorkItem1();
	    testCheckProcessCompleted();
	    testStartProcess(limitExpr);
	    testCheckWorkItem1AndWait(seconds);
	    testCheckProcessCompleted();
	    testCheckLimitExceededProcess();
	    testCompleteWorkItemLimitActivity();
	    testCheckProcessCompleted();
  }

  public void testAbsDynamicLimit() throws Exception {
	  testDynamicLimit("10s",10);
  }
  
  public void testRelDynamicLimit() throws Exception {
	  long deadline = System.currentTimeMillis() + 15*1000L;
	  SimpleDateFormat f = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
	  testDynamicLimit(f.format(new Date(deadline)),15);
  }

  public static TestSuite suite()
  {
    TestSuite suite = new TestSuite( "Limit1 Test" );
    TestContext context = new TestContext();

    suite.addTest( new DynamicLimitTest( "testAbsDynamicLimit", context ) );
    suite.addTest( new DynamicLimitTest( "testRelDynamicLimit", context ) );
    return suite;
  }  
}
