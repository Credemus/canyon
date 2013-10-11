package de.objectcode.canyon.wetdock.tool.test;

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
public class ToolRetryTest extends WetdockTestBase {
	public ToolRetryTest(String testName, TestContext context) {
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
		createPackage("toolRetry.xpdl");
	}

	public void testStartProcess() throws Exception {
		startProcess("ToolRetry_Wor1", null);
	}


	public void testStartProcessContext() throws Exception {
		startProcess("ToolRetry_Wor2", null);
	}

	public void testCompleteWorkItem1Off() throws Exception {
		WorkItemData workItem = findWorkItemByActivityId("Activity1", true);

		ApplicationData appData[] = workItem.getApplicationData();

		assertEquals("Application data length", 1, appData.length);
		assertEquals("Application parameter length", 1,
				appData[0].getParameters().length);
		assertEquals("Application parameter 1 name", "param", appData[0]
				.getParameters()[0].getName());
		assertNull("Application parameter 1 value", appData[0].getParameters()[0]
				.getValue());

		appData[0].getParameters()[0].setValue("test");

		new WetdockTestServiceHelper().setExceptionOn(false);
	    new WetdockTestServiceHelper().setWaitDuration(0);

		completeWorkItem(workItem);
	}

	public void testCompleteWorkItem1On() throws Exception {
		WorkItemData workItem = findWorkItemByActivityId("Activity1", true);

		ApplicationData appData[] = workItem.getApplicationData();

		assertEquals("Application data length", 1, appData.length);
		assertEquals("Application parameter length", 1,
				appData[0].getParameters().length);
		assertEquals("Application parameter 1 name", "param", appData[0]
				.getParameters()[0].getName());
		assertNull("Application parameter 1 value", appData[0].getParameters()[0]
				.getValue());

		appData[0].getParameters()[0].setValue("test");

		new WetdockTestServiceHelper().setExceptionOn(true);
	    new WetdockTestServiceHelper().setWaitDuration(0);

		completeWorkItem(workItem);
	}

	public void testCompleteWorkItem3() throws Exception {
		WorkItemData workItem = findWorkItemByActivityId("Activity3", true);

		ApplicationData appData[] = workItem.getApplicationData();

		assertEquals("Application data length", 1, appData.length);
		assertEquals("Application parameter length", 1,
				appData[0].getParameters().length);
		assertEquals("Application parameter 1 name", "param", appData[0]
				.getParameters()[0].getName());
		assertEquals("Application parameter 1 value", "test_test", appData[0]
				.getParameters()[0].getValue().toString());

		completeWorkItem(workItem);
	}

	public void testCompleteWorkItem3Ignore() throws Exception {
		WorkItemData workItem = findWorkItemByActivityId("Activity3", true);

		ApplicationData appData[] = workItem.getApplicationData();

		assertEquals("Application data length", 1, appData.length);
		assertEquals("Application parameter length", 1,
				appData[0].getParameters().length);
		assertEquals("Application parameter 1 name", "param", appData[0]
				.getParameters()[0].getName());
		assertNull("Application parameter 1 value", appData[0]
				.getParameters()[0].getValue());

		completeWorkItem(workItem);
	}

	
	public void testCheckRetryProcess() throws Exception {
		ProcessInstanceData processInstance = findProcessInstanceByProcessId(
				"ToolRetry_Wor1", true);

		m_context.setUserId("user1");
		m_context.setProcessInstance(processInstance);
	}

	
	public void testCheckProcess(String name) throws Exception {
		ProcessInstanceData processInstance = findProcessInstanceByProcessId(
				name, true);

		m_context.setUserId("user1");
		m_context.setProcessInstance(processInstance);
	}

	public void testCheckRetryContextProcess() throws Exception {
		ProcessInstanceData processInstance = findProcessInstanceByProcessId(
				"ToolRetry_Wor2", true);

		m_context.setUserId("user1");
		m_context.setProcessInstance(processInstance);
	}

	public void testCheckFaultProcess() throws Exception {
		Thread.sleep(1000);
		ProcessInstanceData processInstance = findProcessInstanceByProcessId(
				"FaultProcess", true);

		m_context.setUserId("admin1");
		m_context.setProcessInstance(processInstance);
	}

	public void testCheckFaultContextProcess() throws Exception {
		Thread.sleep(1000);
		ProcessInstanceData processInstance = findProcessInstanceByProcessId(
				"FaultProcessContext", true);

		m_context.setUserId("admin1");
		m_context.setProcessInstance(processInstance);
	}

	public void testCompleteWorkItem4Ignore() throws Exception {
		m_context.setUserId("admin1");

		WorkItemData workItem = findWorkItemByActivityId("Activity4", true);
		assertEquals("Wrong message", "testMethod1 exception as ordered", workItem
				.getApplicationData()[0].getParameter("message").getValue());
		workItem.getApplicationData()[0].getParameter("state").setValue("IGNORE");

		completeWorkItem(workItem);
	}

	public void testCompleteWorkItem4ContextIgnore() throws Exception {
		m_context.setUserId("admin1");

		WorkItemData workItem = findWorkItemByActivityId("Activity4", true);
		assertEquals("Wrong message", "testMethod1 exception as ordered", workItem
				.getApplicationData()[0].getParameter("message").getValue());
		assertEquals("Wrong context", "4711", workItem
				.getApplicationData()[0].getParameter("contextId").getValue());
		workItem.getApplicationData()[0].getParameter("state").setValue("IGNORE");

		completeWorkItem(workItem);
	}

	public void testCompleteWorkItem4RetryOn() throws Exception {
		m_context.setUserId("admin1");

		WorkItemData workItem = findWorkItemByActivityId("Activity4", true);
		assertEquals("Wrong message", "testMethod1 exception as ordered", workItem
				.getApplicationData()[0].getParameter("message").getValue());
		workItem.getApplicationData()[0].getParameter("state").setValue("RETRY");
		new WetdockTestServiceHelper().setExceptionOn(true);
	    new WetdockTestServiceHelper().setWaitDuration(0);

		completeWorkItem(workItem);
	}

	public void testCompleteWorkItem4RetryOff() throws Exception {
		m_context.setUserId("admin1");

		Thread.sleep(500);
		WorkItemData workItem = findWorkItemByActivityId("Activity4", true);
		assertEquals("Wrong message", "testMethod1 exception as ordered", workItem
				.getApplicationData()[0].getParameter("message").getValue());
		workItem.getApplicationData()[0].getParameter("state").setValue("RETRY");
		new WetdockTestServiceHelper().setExceptionOn(false);
	    new WetdockTestServiceHelper().setWaitDuration(0);
		completeWorkItem(workItem);
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
	
	public void testIgnore() throws Exception {
		testClearRepositories();
		testCreateUsersAndRoles();
		testCreatePackage();

		testStartProcess();
		testCompleteWorkItem1On();
		testCheckFaultProcess();
		testCompleteWorkItem4Ignore();
		testCheckProcessCompleted();
		testCheckRetryProcess();
		testCompleteWorkItem3Ignore();
		testCheckProcessCompleted();
	}

	public void testRetry() throws Exception {
		testClearRepositories();
		testCreateUsersAndRoles();
		testCreatePackage();
		testStartProcess();
		testCompleteWorkItem1On();
		testCheckFaultProcess();
		testCompleteWorkItem4RetryOn();
		testCheckProcessCompleted();
		testCheckFaultProcess();
		testCompleteWorkItem4RetryOff();
		testCheckProcessCompleted();
		testCheckRetryProcess();
		testCompleteWorkItem3();
		testCheckProcessCompleted();		
	}
	
	public void testContext() throws Exception {
		testClearRepositories();
		testCreateUsersAndRoles();
		testCreatePackage();
		testStartProcessContext();
		testCompleteWorkItem1On();
		testCheckFaultContextProcess();
		testCompleteWorkItem4ContextIgnore();
		testCheckProcessCompleted();
		testCheckRetryContextProcess();
		testCompleteWorkItem3Ignore();
		testCheckProcessCompleted();
	}
	
	public void testToolSet() throws Exception {
		testClearRepositories();
		testCreateUsersAndRoles();
		testCreatePackage();
		startProcess("ToolRetry_Wor3", null);
		testCompleteWorkItem1On();
		testCheckFaultProcess();
		testCompleteWorkItem4RetryOn();
		testCheckProcessCompleted();
		testCheckFaultProcess();
		testCompleteWorkItem4RetryOff();
		testCheckProcessCompleted();
		testCheckProcess("ToolRetry_Wor3");
		testCompleteWorkItem3();
		testCheckProcessCompleted();		
	}

	public void testNoFailureToolSet() throws Exception {
		testClearRepositories();
		testCreateUsersAndRoles();
		testCreatePackage();

		startProcess("ToolRetry_Wor3", null);
		testCompleteWorkItem1Off();
		testCompleteWorkItem3();
		testCheckProcessCompleted();
	}

	public static TestSuite suite() {
		TestSuite suite = new TestSuite("ToolRetry Test");
		TestContext context = new TestContext();
		suite.addTest(new ToolRetryTest("testNoFailure",context));
		suite.addTest(new ToolRetryTest("testIgnore",context));
		suite.addTest(new ToolRetryTest("testRetry",context));
		suite.addTest(new ToolRetryTest("testContext",context));
		suite.addTest(new ToolRetryTest("testNoFailureToolSet",context));
		suite.addTest(new ToolRetryTest("testToolSet",context));
		return suite;
	}
}
