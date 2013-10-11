package de.objectcode.canyon.wetdock.event.test;

import java.util.HashMap;

import junit.framework.TestSuite;
import de.objectcode.canyon.api.worklist.WorkItemData;
import de.objectcode.canyon.wetdock.test.TestContext;
import de.objectcode.canyon.wetdock.test.WetdockTestBase;
import de.objectcode.canyon.wetdock.test.WetdockUserManagerHelper;

/**
 * @author junglas
 */
public class MultiplicityTest extends WetdockTestBase {
	public MultiplicityTest(String testName, TestContext context) {
		super(testName, context);
	}

	public void testCreateUsersAndRoles() throws Exception {
		m_context.setClientId("client1");
		m_context.setUserId("user1");

		WetdockUserManagerHelper userManager = new WetdockUserManagerHelper();

		userManager.createUser("user1", "user1");
		userManager.createRole("role1");
		userManager.createClient("client1");
		userManager.addUserRole("user1", "role1", "client1");
	}

	public void testCreatePackage() throws Exception {
		createPackage("multiplicity.xpdl");
	}

	public void testStartStopByEventWait() throws Exception {
		testClearRepositories();
		testCreateUsersAndRoles();
		testCreatePackage();
		HashMap params = new HashMap();
		params.put("doWait",Boolean.TRUE);
		startProcess("Event1_Wor1", params);
		Thread.sleep(500);
		String otherProcessInstanceId = findProcessInstanceByProcessId(
				"Event1_Wor2", true).getId();
		WorkItemData workItem = findWorkItemByActivityId(otherProcessInstanceId,
				"Start", true);
		completeWorkItem(workItem);
		workItem = findWorkItemByActivityId("BeforeStop", true);
		WorkItemData dummy = findWorkItemByActivityId(otherProcessInstanceId,
				"Dummy", true);
		completeWorkItem(workItem);
		Thread.sleep(2000);
		dummy = findWorkItemByActivityId(otherProcessInstanceId, "Dummy", false);
		assertNull("Dummy not removed", dummy);
	}

	public void testStartStopByEventNoWait() throws Exception {
		testClearRepositories();
		testCreateUsersAndRoles();
		testCreatePackage();
		HashMap params = new HashMap();
		params.put("doWait",Boolean.FALSE);
		startProcess("Event1_Wor1", params);
		Thread.sleep(2000);
		String otherProcessInstanceId = findProcessInstanceByProcessId(
				"Event1_Wor2", true).getId();
		WorkItemData workItem = findWorkItemByActivityId(otherProcessInstanceId,
				"Start", true);
		completeWorkItem(workItem);
		Thread.sleep(500);
		String errorHandlerProcessInstanceId = findProcessInstanceByProcessId(
				"ErrorHandler", true).getId();
		workItem = findWorkItemByActivityId(errorHandlerProcessInstanceId,
				"Error", true);
	}


	public static TestSuite suite() {
		TestSuite suite = new TestSuite("Multiplicity Test");
		TestContext context = new TestContext();
		suite.addTest(new MultiplicityTest("testStartStopByEventNoWait", context));
		suite.addTest(new MultiplicityTest("testStartStopByEventWait", context));

		return suite;
	}
}
