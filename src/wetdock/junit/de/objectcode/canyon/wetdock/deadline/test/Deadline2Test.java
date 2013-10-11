package de.objectcode.canyon.wetdock.deadline.test;

import junit.framework.TestSuite;
import de.objectcode.canyon.api.worklist.WorkItemData;
import de.objectcode.canyon.wetdock.test.TestContext;
import de.objectcode.canyon.wetdock.test.WetdockTestBase;
import de.objectcode.canyon.wetdock.test.WetdockUserManagerHelper;

/**
 * Async Deadline
 * 
 * @author junglas
 */
public class Deadline2Test extends WetdockTestBase {
  public Deadline2Test(String testName, TestContext context) {
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
    createPackage("deadline2.xpdl");
  }

  public void testStartProcess(String processId) throws Exception {
    startProcess(processId, null);
  }

  public void testCompleteWorkItem1() throws Exception {
    WorkItemData workItem = findWorkItemByActivityId("Activity1", true);

    completeWorkItem(workItem);
  }

  public void testCheckWorkItem1AndWait() throws Exception {
    WorkItemData workItem = findWorkItemByActivityId("Activity1", true);

    Thread.sleep(40 * 1000L);
  }

  public void testCheckWorkItem3AndWait() throws Exception {
    WorkItemData workItem = findWorkItemByActivityId("Activity3", true);

    Thread.sleep(40 * 1000L);
  }


  public void testCompleteWorkItem2() throws Exception {
    WorkItemData workItem = findWorkItemByActivityId("Activity2", true);

    completeWorkItem(workItem);
  }

  public void testCompleteWorkItem4() throws Exception {
    WorkItemData workItem = findWorkItemByActivityId("Activity4", true);

    completeWorkItem(workItem);
  }

  public void testCompleteWorkItem3() throws Exception {
    WorkItemData workItem = findWorkItemByActivityId("Activity3", true);

    completeWorkItem(workItem);
  }

  public void testDeadlineNoLimit() throws Exception {
    testDeadline("Deadline2_Wor1");
  }

  public void testDeadlineWithLongLimit() throws Exception {
    testDeadline("DeadlineWithLongLimit");
  }

  public void testDeadline(String processId) throws Exception {
    testClearRepositories();
    testCreateUsersAndRoles();
    testCreatePackage();

    testStartProcess(processId);
    testCompleteWorkItem1();
    testCompleteWorkItem2();
    testCheckProcessCompleted();

    testStartProcess(processId);
    testCheckWorkItem1AndWait();
    testCompleteWorkItem3();
    testCompleteWorkItem1();
    testCompleteWorkItem2();
    testCheckProcessCompleted();

  }

  public void testMultipleDeadlines() throws Exception {
    testClearRepositories();
    testCreateUsersAndRoles();
    testCreatePackage();

    testStartProcess("Deadline2_wp1");
    testCompleteWorkItem1();
    testCompleteWorkItem2();
    testCheckProcessCompleted();

    testStartProcess("Deadline2_wp1");
    testCheckWorkItem1AndWait();
    testCheckWorkItem3AndWait();
    testCompleteWorkItem4();
    testCompleteWorkItem3();
    testCompleteWorkItem1();
    testCompleteWorkItem2();
    testCheckProcessCompleted();

    testStartProcess("Deadline2_wp1");
    testCheckWorkItem1AndWait();
    WorkItemData workItem = waitForWorkItemByActivityId("Activity5", 120000);
//    assertWorkItemDoesNotExistByActivityId("Activity3");
//    assertWorkItemDoesNotExistByActivityId("Activity4");
    completeWorkItem(workItem);
    testCheckWorkItem3AndWait();
    testCompleteWorkItem4();
    testCompleteWorkItem3();
    testCompleteWorkItem1();
    testCompleteWorkItem2();
    testCheckProcessCompleted();
    
  }

  public void testDeadlineWithLimit() throws Exception {
    testClearRepositories();
    testCreateUsersAndRoles();
    testCreatePackage();

    testStartProcess("DeadlineWithLimit");
    testCompleteWorkItem1();
    testCompleteWorkItem2();
    testCheckProcessCompleted();

    testStartProcess("Deadline2_Wor1");
    testCheckWorkItem1AndWait();
    testCompleteWorkItem3();
    testCompleteWorkItem1();
    testCompleteWorkItem2();
    testCheckProcessCompleted();

  }

  public static TestSuite suite() {
    TestSuite suite = new TestSuite("Deadline2 Test");
    TestContext context = new TestContext();

//    suite.addTest(new Deadline2Test("testDeadlineNoLimit", context));
//    suite.addTest(new Deadline2Test("testDeadlineWithLongLimit", context));
    suite.addTest(new Deadline2Test("testMultipleDeadlines", context));
    return suite;
  }

}
