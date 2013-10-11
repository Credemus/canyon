package de.objectcode.canyon.wetdock.basic.test;

import junit.framework.TestSuite;
import de.objectcode.canyon.api.worklist.ApplicationData;
import de.objectcode.canyon.api.worklist.ProcessInstanceData;
import de.objectcode.canyon.api.worklist.WorkItemData;
import de.objectcode.canyon.wetdock.test.TestContext;
import de.objectcode.canyon.wetdock.test.WetdockTestBase;
import de.objectcode.canyon.wetdock.test.WetdockTestServiceHelper;
import de.objectcode.canyon.wetdock.test.WetdockUserManagerHelper;

/**
 * @author junglas
 */
public class ExceptionTest extends WetdockTestBase {
	public ExceptionTest(String testName, TestContext context) {
		super(testName, context);
	}

	public void testCreateUsersAndRoles() throws Exception {
		m_context.setClientId("client1");
		m_context.setUserId("user1");

		WetdockUserManagerHelper userManager = new WetdockUserManagerHelper();

		userManager.createUser("user1", "user1");
		userManager.createUser("admin1", "admin1");
		userManager.createRole("role1");
		userManager.createRole("admins");
		userManager.createClient("client1");
		userManager.addUserRole("user1", "role1", "client1");
		userManager.addUserRole("admin1", "admins", "client1");
	}

	public void testCreatePackage() throws Exception {
		createPackage("exception.xpdl");
	}

	public void testStartProcess() throws Exception {
		startProcess("Exception_Wor1", null);
	}

	public void testStartProcessNoExceptionHandler() throws Exception {
		startProcess("Exception_wp1", null);
	}

	
	public void testStartProcessInfinite() throws Exception {
		startProcess("Exception_Wor2", null);
	}

	public void testCompleteWorkItem1NOK() throws Exception {
		WorkItemData workItem = findWorkItemByActivityId("Activity1", true);

		ApplicationData appData[] = workItem.getApplicationData();

		assertEquals("Application data length", 1, appData.length);
		assertEquals("Application parameter length", 1,
				appData[0].getParameters().length);
		assertEquals("Application parameter 1 name", "param", appData[0]
				.getParameters()[0].getName());
		assertNull("Application parameter 1 value", appData[0].getParameters()[0]
				.getValue());

		appData[0].getParameters()[0].setValue("NOK");

		completeWorkItem(workItem);
	}

	public void testCompleteWorkItem1OK() throws Exception {
		WorkItemData workItem = findWorkItemByActivityId("Activity1", true);

		ApplicationData appData[] = workItem.getApplicationData();

		assertEquals("Application data length", 1, appData.length);
		assertEquals("Application parameter length", 1,
				appData[0].getParameters().length);
		assertEquals("Application parameter 1 name", "param", appData[0]
				.getParameters()[0].getName());
		assertNull("Application parameter 1 value", appData[0].getParameters()[0]
				.getValue());

		appData[0].getParameters()[0].setValue("OK");

		completeWorkItem(workItem);
	}

	public void testCompleteWorkItem2() throws Exception {
		WorkItemData workItem = findWorkItemByActivityId("Activity2", true);

		ApplicationData appData[] = workItem.getApplicationData();

		assertEquals("Application data length", 1, appData.length);
		assertEquals("Application parameter length", 1,
				appData[0].getParameters().length);
		assertEquals("Application parameter 1 name", "param", appData[0]
				.getParameters()[0].getName());
		completeWorkItem(workItem);
	}

	public void testCompleteWorkItem4() throws Exception {
		m_context.setUserId("admin1");

		WorkItemData workItem = findWorkItemByActivityId("Activity4", true);

		completeWorkItem(workItem);
	}

	public void testCheckFaultProcess() throws Exception {
		ProcessInstanceData processInstance = findProcessInstanceByProcessId(
				"FaultProcess", true);

		m_context.setUserId("admin1");
		m_context.setProcessInstance(processInstance);
	}

	public void testCheckFaultProcessInfinite() throws Exception {
		ProcessInstanceData processInstance = findProcessInstanceByProcessId(
				"InfiniteFaultProcess", true);

		m_context.setUserId("admin1");
		m_context.setProcessInstance(processInstance);
	}

	
	public void testExceptionHandlerWithoutException() throws Exception  {
		testClearRepositories();         
		testCreateUsersAndRoles();         
		testCreatePackage();               
		                                
		testStartProcess();                
		testCompleteWorkItem1OK();         
		testCompleteWorkItem2();           
		testCheckProcessCompleted();       		
	}
	
	public void testExceptionHandlerWithException() throws Exception  {
		testClearRepositories();         
		testCreateUsersAndRoles();         
		testCreatePackage();               
		                                
		testStartProcess();                       
		testCompleteWorkItem1NOK();               
		testCheckProcessTerminated();                
		testCheckFaultProcess();                  
		testCompleteWorkItem4();                  
		testCheckProcessCompleted();              
	}

	public void testExceptionHandlerWithInfiniteLoop() throws Exception  {
		testClearRepositories();         
		testCreateUsersAndRoles();         
		testCreatePackage();               
		                                
		testStartProcessInfinite();      
		testCompleteWorkItem1NOK();      
		testCheckFaultProcessInfinite(); 
	}

	public void testNoExceptionHandlerWithException() throws Exception  {
		testClearRepositories();         
		testCreateUsersAndRoles();         
		testCreatePackage();               
		                                
		testStartProcessNoExceptionHandler();       
		testCompleteWorkItem1NOK();                 
		testCheckProcessTerminated();                  
	}
	
	public static TestSuite suite() {
		TestSuite suite = new TestSuite("Exception Test");
		TestContext context = new TestContext();
		suite.addTest(new ExceptionTest("testExceptionHandlerWithoutException", context));
		suite.addTest(new ExceptionTest("testExceptionHandlerWithException", context));
//		suite.addTest(new ExceptionTest("testExceptionHandlerWithInfiniteException", context));
		suite.addTest(new ExceptionTest("testNoExceptionHandlerWithException", context));
		return suite;
	}
}
