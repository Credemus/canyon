package de.objectcode.canyon.wetdock.basic.test;

import junit.framework.TestSuite;
import de.objectcode.canyon.api.worklist.WorkItemData;
import de.objectcode.canyon.wetdock.test.TestContext;
import de.objectcode.canyon.wetdock.test.WetdockTestBase;
import de.objectcode.canyon.wetdock.test.WetdockUserManagerHelper;

/**
 * @author junglas
 */
public class TriforkTest extends WetdockTestBase {
  public TriforkTest(String testName, TestContext context) {
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

  public void testTrifork() throws Exception {
    testClearRepositories();
    testCreateUsersAndRoles();    
    createPackage("trifork.xpdl");
    startProcess("Trifork", null);
    WorkItemData workItem = findWorkItemByActivityId("Activity5", true);
    completeWorkItem(workItem);
    workItem = findWorkItemByActivityId("Activity1", true);
    workItem.getApplicationData()[0].getParameter("A")
    .setValue(Boolean.FALSE);
    workItem.getApplicationData()[0].getParameter("B")
    .setValue(Boolean.TRUE);
    completeWorkItem(workItem);
    workItem = findWorkItemByActivityId("Activity4", true);
    assertWorkItemDoesNotExistByActivityId("Activity3");
    assertWorkItemDoesNotExistByActivityId("Activity2");
    completeWorkItem(workItem);
    testCheckProcessCompleted();
  }

  public static TestSuite suite() {
    TestSuite suite = new TestSuite("Trifork Test");
    TestContext context = new TestContext();

    suite.addTest(new TriforkTest("testTrifork", context));

    return suite;
  }
}