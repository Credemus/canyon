package de.objectcode.canyon.wetdock.basic.test;

import junit.framework.TestSuite;
import de.objectcode.canyon.api.worklist.WorkItemData;
import de.objectcode.canyon.spi.filter.CompareFilter;
import de.objectcode.canyon.spi.filter.IFilter;
import de.objectcode.canyon.wetdock.test.TestContext;
import de.objectcode.canyon.wetdock.test.WetdockTestBase;
import de.objectcode.canyon.wetdock.test.WetdockUserManagerHelper;

/**
 * @author junglas
 */
public class DynaRoleTest extends WetdockTestBase {
	public DynaRoleTest(String testName, TestContext context) {
		super(testName, context);
	}

	public void testCreateUsersAndRoles() throws Exception {
		m_context.setClientId("client1");
		m_context.setUserId("user1");

		WetdockUserManagerHelper userManager = new WetdockUserManagerHelper();

		userManager.createUser("user1", "user1");
		userManager.createUser("user2", "user2");
		userManager.createUser("user3", "user3");
		userManager.createRole("role1");
		userManager.createRole("role2");
		userManager.createClient("client1");
		userManager.addUserRole("user1", "role1", "client1");
		userManager.addUserRole("user2", "role2", "client1");
		userManager.addUserRole("user3", "role2", "client1");
	}


	public void testCreatePackage() throws Exception {
		createPackage("dynaRole.xpdl");
	}

	public void testUpdatePackage() throws Exception {
		updatePackage("dynaRole.xpdl");
	}

	public void testStartProcess() throws Exception {
		startProcess("dynaRole_Wor1", null);
	}

	public void testRemoveStickyness() throws Exception {
		m_context.setUserId("user3");
		WorkItemData workItem = findWorkItemByActivityId("Activity1", true);

		acceptWorkItem(workItem);
		workItem = findWorkItemByActivityId("Activity1", true);

		// remove role. next workitem go to user2
		WetdockUserManagerHelper userManager = new WetdockUserManagerHelper();
		userManager.removeUserRole("user3", "role2", "client1");
		m_context.setUserId("user2");
		
		completeWorkItem(workItem);

		workItem = findWorkItemByActivityId("Activity2", true);

		completeWorkItem(workItem);
	}


	public void testStickyness() throws Exception {
		m_context.setUserId("user3");
		WorkItemData workItem = findWorkItemByActivityId("Activity1", true);

		acceptWorkItem(workItem);
		workItem = findWorkItemByActivityId("Activity1", true);

		completeWorkItem(workItem);

		workItem = findWorkItemByActivityId("Activity2", true);

		completeWorkItem(workItem);
	}


	public static TestSuite suite() {
		TestSuite suite = new TestSuite("DynaRole Test");
		TestContext context = new TestContext();

		suite.addTest(new DynaRoleTest("testClearRepositories", context));
		suite.addTest(new DynaRoleTest("testCreateUsersAndRoles", context));
		suite.addTest(new DynaRoleTest("testCreatePackage", context));
		suite.addTest(new DynaRoleTest("testClearRepositories", context));
		suite.addTest(new DynaRoleTest("testCreateUsersAndRoles", context));
		suite.addTest(new DynaRoleTest("testCreatePackage", context));
		suite.addTest(new DynaRoleTest("testStartProcess", context));
		suite.addTest(new DynaRoleTest("testStickyness", context));
		suite.addTest(new DynaRoleTest("testCheckProcessCompleted", context));
		suite.addTest(new DynaRoleTest("testClearRepositories", context));
		suite.addTest(new DynaRoleTest("testCreateUsersAndRoles", context));
		suite.addTest(new DynaRoleTest("testCreatePackage", context));
		suite.addTest(new DynaRoleTest("testStartProcess", context));
		suite.addTest(new DynaRoleTest("testRemoveStickyness", context));
		suite.addTest(new DynaRoleTest("testCheckProcessCompleted", context));

		return suite;
	}
}
