package de.objectcode.canyon.wetdock.basic.test;

import junit.framework.TestSuite;
import de.objectcode.canyon.api.worklist.WorkItemData;
import de.objectcode.canyon.wetdock.test.TestContext;
import de.objectcode.canyon.wetdock.test.WetdockTestBase;
import de.objectcode.canyon.wetdock.test.WetdockUserManagerHelper;

/**
 * @author junglas
 */
public class VersionTest extends WetdockTestBase {
  public VersionTest(String testName, TestContext context) {
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

  public void testVersion1() throws Exception {
    testClearRepositories();
    testCreateUsersAndRoles();    
    createPackage("version_1.xpdl");
    startProcess("version_1_Wor1", null);
      WorkItemData workItem = findWorkItemByActivityId("Activity1", true);
      completeWorkItem(workItem);
      workItem = findWorkItemByProcessIdAndActivityId("version_Wor2","Activity1",true);
      assertEquals("Wrong Version","VERSION1",workItem.getApplicationData()[0].getParameter("version_Wor2_App1_For1").getValue());
      completeWorkItem(workItem);
    testCheckProcessCompleted();
    startProcess("version_1_Wor1", null);
    workItem = findWorkItemByActivityId("Activity1", true);
    // upload now new version
    createPackage("version_2.xpdl");
      completeWorkItem(workItem);
      workItem = findWorkItemByProcessIdAndActivityId("version_Wor2","Activity1",true);
      assertEquals("Wrong Version","VERSION1",workItem.getApplicationData()[0].getParameter("version_Wor2_App1_For1").getValue());
      completeWorkItem(workItem);
    testCheckProcessCompleted();
    startProcess("version_1_Wor1", null);
    workItem = findWorkItemByActivityId("Activity1", true);
    completeWorkItem(workItem);
    workItem = findWorkItemByProcessIdAndActivityId("version_Wor2","Activity1",true);
    assertEquals("Wrong Version","VERSION2",workItem.getApplicationData()[0].getParameter("version_Wor2_App1_For1").getValue());
    completeWorkItem(workItem);
  testCheckProcessCompleted();
  }

  public static TestSuite suite() {
    TestSuite suite = new TestSuite("Version Test");
    TestContext context = new TestContext();

    suite.addTest(new VersionTest("testVersion1", context));

    return suite;
  }
}