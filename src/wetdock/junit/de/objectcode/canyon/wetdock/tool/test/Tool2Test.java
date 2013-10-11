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
public class Tool2Test extends WetdockTestBase
{
  public Tool2Test( String testName, TestContext context )
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
    userManager.createUser( "admin1", "admin1" );
    userManager.createRole( "role1" );
    userManager.createRole( "admins" );
    userManager.createClient( "client1" );
    userManager.addUserRole( "user1", "role1", "client1" );
    userManager.addUserRole( "admin1", "admins", "client1" );
  }

  public void testCreatePackage()
      throws Exception
  {
    createPackage( "tool2.xpdl" );
  }

  public void testStartProcess()
      throws Exception
  {
    startProcess( "Tool2_Wor1", null );
  }

  public void testCompleteWorkItem1Off()
      throws Exception
  {
    WorkItemData workItem = findWorkItemByActivityId( "Activity1", true );

    ApplicationData appData[] = workItem.getApplicationData();

    assertEquals( "Application data length", 1, appData.length );
    assertEquals( "Application parameter length", 1, appData[0].getParameters().length );
    assertEquals( "Application parameter 1 name", "param", appData[0].getParameters()[0].getName() );
    assertNull( "Application parameter 1 value", appData[0].getParameters()[0].getValue() );

    appData[0].getParameters()[0].setValue( "test" );

    new WetdockTestServiceHelper().setExceptionOn( false );
    new WetdockTestServiceHelper().setWaitDuration(0);

    completeWorkItem( workItem );
  }

  public void testCompleteWorkItem1Wait() throws Exception {
		WorkItemData workItem = findWorkItemByActivityId("Activity1", true);

		ApplicationData appData[] = workItem.getApplicationData();

		assertEquals("Application data length", 1, appData.length);
		assertEquals("Application parameter length", 1, appData[0]
				.getParameters().length);
		assertEquals("Application parameter 1 name", "param", appData[0]
				.getParameters()[0].getName());
		assertNull("Application parameter 1 value",
				appData[0].getParameters()[0].getValue());

		appData[0].getParameters()[0].setValue("test");

	    new WetdockTestServiceHelper().setExceptionOn( false );
		new WetdockTestServiceHelper().setWaitDuration(10000);

		completeWorkItem(workItem);
	}

  public void testCompleteWorkItem1On()
      throws Exception
  {
    WorkItemData workItem = findWorkItemByActivityId( "Activity1", true );

    ApplicationData appData[] = workItem.getApplicationData();

    assertEquals( "Application data length", 1, appData.length );
    assertEquals( "Application parameter length", 1, appData[0].getParameters().length );
    assertEquals( "Application parameter 1 name", "param", appData[0].getParameters()[0].getName() );
    assertNull( "Application parameter 1 value", appData[0].getParameters()[0].getValue() );

    appData[0].getParameters()[0].setValue( "test" );

    new WetdockTestServiceHelper().setExceptionOn( true );
    new WetdockTestServiceHelper().setWaitDuration(0);

    completeWorkItem( workItem );
  }

  public void testCompleteWorkItem3()
      throws Exception
  {
    WorkItemData workItem = findWorkItemByActivityId( "Activity3", true );

    ApplicationData appData[] = workItem.getApplicationData();

    assertEquals( "Application data length", 1, appData.length );
    assertEquals( "Application parameter length", 1, appData[0].getParameters().length );
    assertEquals( "Application parameter 1 name", "param", appData[0].getParameters()[0].getName() );
    assertEquals( "Application parameter 1 value", "test_test", appData[0].getParameters()[0]
        .getValue().toString() );

    completeWorkItem( workItem );
  }

  public void testWaitAndCompleteWorkItem3() throws Exception {
	    // Test for Lock
	    long start = System.currentTimeMillis();
	    findProcessInstanceByProcessInstanceIdReadOnly(m_context.getProcessInstance().getId());
	    assertTrue("LOCK still active",(System.currentTimeMillis()-start)<500);
	    Thread.sleep(10000);
		WorkItemData workItem = findWorkItemByActivityId("Activity3", true);

		ApplicationData appData[] = workItem.getApplicationData();

		assertEquals("Application data length", 1, appData.length);
		assertEquals("Application parameter length", 1, appData[0]
				.getParameters().length);
		assertEquals("Application parameter 1 name", "param", appData[0]
				.getParameters()[0].getName());
		assertEquals("Application parameter 1 value", "test_test", appData[0]
				.getParameters()[0].getValue().toString());

		completeWorkItem(workItem);
	}

  public void testCompleteWorkItem4()
      throws Exception
  {
    m_context.setUserId( "admin1" );

    WorkItemData workItem = findWorkItemByActivityId( "Activity4", true );
//  message is no longer available
//    assertEquals("Wrong message","testMethod1 exception as ordered",workItem.getApplicationData()[0].getParameter("errorMessage").getValue());

    completeWorkItem( workItem );
  }

  public void testNoFailure() throws Exception {
    testClearRepositories();
    testCreateUsersAndRoles();
    testCreatePackage();

    testStartProcess();
    testCompleteWorkItem1Off();
    testCompleteWorkItem3();
    testCheckProcessCompleted();
  	
  }
  
  public void testNoFailureWait() throws Exception {
	    testClearRepositories();
	    testCreateUsersAndRoles();
	    testCreatePackage();

	    testStartProcess();
	    testCompleteWorkItem1Wait();
	    testWaitAndCompleteWorkItem3();
	    testCheckProcessCompleted();
	  	
	  }

  public void testFailure() throws Exception {
    testClearRepositories();
    testCreateUsersAndRoles();
    testCreatePackage();
  	
    testStartProcess();
    testCompleteWorkItem1On();
    testCompleteWorkItem4();
    testCheckProcessCompleted();
  }
  
  public static TestSuite suite()
  {
    TestSuite suite = new TestSuite( "Tool2 Test" );
    TestContext context = new TestContext();

    suite.addTest( new Tool2Test( "testNoFailure", context ) );
    suite.addTest( new Tool2Test( "testNoFailureWait", context ) );
    suite.addTest( new Tool2Test( "testFailure", context ) );
    return suite;
  }
}
