package de.objectcode.canyon.wetdock.deadline.test;

import junit.framework.TestSuite;
import de.objectcode.canyon.api.worklist.WorkItemData;
import de.objectcode.canyon.wetdock.test.TestContext;
import de.objectcode.canyon.wetdock.test.WetdockTestBase;
import de.objectcode.canyon.wetdock.test.WetdockUserManagerHelper;

/**
 * @author junglas
 */
public class LoopTest extends WetdockTestBase {
  public LoopTest(String testName, TestContext context) {
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

  public void testLoop() throws Exception {
    testClearRepositories();
    testCreateUsersAndRoles();
    createPackage("loop.xpdl");
    startProcess("Loop", null);
    for (int i = 0; i < 2; i++) {
      Thread.sleep(40 * 1000L);
      WorkItemData workItem = findWorkItemByActivityId("Activity3", true);
      assertNotNull("No workItem found at loop " + i, workItem);
      Integer counter = (Integer) workItem.getApplicationData()[0]
	  .getParameter("Counter").getValue();
      counter = new Integer(counter.intValue() + 1);
      workItem.getApplicationData()[0].getParameter("Counter")
	  .setValue(counter);
      completeWorkItem(workItem);
    }
    WorkItemData workItem = findWorkItemByActivityId("Activity4", true);
    completeWorkItem(workItem);
    testCheckProcessCompleted();
  }

  public void testLoopSkippedAsync() throws Exception {
    testClearRepositories();
    testCreateUsersAndRoles();
    createPackage("loop.xpdl");
    startProcess("Loop2", null);
    WorkItemData workItem = findWorkItemByActivityId("Deadline1_Wor1_Act2",
	true);
    completeWorkItem(workItem);
    workItem = findWorkItemByActivityId("Deadline1_Wor1_Act3", true);
    completeWorkItem(workItem);
    testCheckProcessCompleted();
  }

  public void testLoopSkippedSync() throws Exception {
    testClearRepositories();
    testCreateUsersAndRoles();
    createPackage("loop.xpdl");
    startProcess("Loop3", null);
    WorkItemData workItem = findWorkItemByActivityId("Activity2", true);
    completeWorkItem(workItem);
    assertWorkItemDoesNotExistByActivityId("Activity1");
    workItem = findWorkItemByActivityId("Activity3", true);
    completeWorkItem(workItem);
    testCheckProcessCompleted();
  }

  public void testLoopSplit1() throws Exception {
    testLoopSplit1("Loop4");
  }

  public void testLoopSplit4() throws Exception {
    testLoopSplit1("Loop6");
  }

  public void testLoopSplit1(String processId) throws Exception {
    testClearRepositories();
    testCreateUsersAndRoles();
    createPackage("loop.xpdl");
    startProcess(processId, null);
    WorkItemData warning = null;
    WorkItemData alert = null;
    for (int i = 0; i < 4; i++) {
      WorkItemData workItem = null;
      int counter = -1;
      do {
	if (counter!=-1)
	  Thread.sleep(5000L);
	workItem = findWorkItemByActivityId("Activity1", false);
	if (workItem != null) {
	  counter = ((Integer) workItem.getApplicationData()[0].getParameter(
	      "Counter").getValue()).intValue();
	}
	  System.out.println("COUNTER="+counter+"=?"+(i+1));

      } while (workItem == null || counter < i + 1);
	  System.out.println("COUNTER="+counter);
      if (counter == 2) {
	warning = findWorkItemByActivityId(
	    "Deadline1_wp1_act1", true);
//	completeWorkItem(printWorkItem);
      } else if (counter == 3) {
	alert = findWorkItemByActivityId(
	    "Deadline1_wp1_act2", true);
//	completeWorkItem(printWorkItem);
      } else if (counter == 4) {
	  System.out.println("COMPLETING");
	completeWorkItem(workItem);
      }
    }
    completeWorkItem(warning);
    completeWorkItem(alert);
    testCheckProcessCompleted();
  }

  public void testLoopSplit2() throws Exception {
    testLoopSplit2("Loop4");
  }

  public void testLoopSplit5() throws Exception {
    testLoopSplit2("Loop6");
  }
  
  public void testLoopSplit2(String processId) throws Exception {
    testClearRepositories();
    testCreateUsersAndRoles();
    createPackage("loop.xpdl");
    startProcess(processId, null);
    WorkItemData warning = null;
    WorkItemData alert = null;
    for (int i = 0; i < 4; i++) {
      WorkItemData workItem = null;
      int counter = -1;
      do {
	if (counter!=-1)
	  Thread.sleep(5000L);
	workItem = findWorkItemByActivityId("Activity1", false);
	if (workItem != null) {
	  counter = ((Integer) workItem.getApplicationData()[0].getParameter(
	      "Counter").getValue()).intValue();
	}
	  System.out.println("COUNTER="+counter+"=?"+(i+1));

      } while (workItem == null || counter < i + 1);
	  System.out.println("COUNTER="+counter);
      if (counter == 2) {
	warning = findWorkItemByActivityId(
	    "Deadline1_wp1_act1", true);
	completeWorkItem(warning);
      } else if (counter == 3) {
	alert = findWorkItemByActivityId(
	    "Deadline1_wp1_act2", true);
	completeWorkItem(alert);
      } else if (counter == 4) {
	  System.out.println("COMPLETING");
	completeWorkItem(workItem);
      }
    }
    testCheckProcessCompleted();
  }

  public void testLoopSplit3() throws Exception {
    testClearRepositories();
    testCreateUsersAndRoles();
    createPackage("loop.xpdl");
    startProcess("Loop5", null);
    WorkItemData warning = null;
    WorkItemData alert = null;
    for (int i = 0; i < 4; i++) {
      WorkItemData workItem = null;
      int counter = -1;
      do {
	if (counter!=-1)
	  Thread.sleep(5000L);
	workItem = findWorkItemByActivityId("Activity1", false);
	if (workItem != null) {
	  counter = ((Integer) workItem.getApplicationData()[0].getParameter(
	      "Counter").getValue()).intValue();
	}
	  System.out.println("COUNTER="+counter+"=?"+(i+1));

      } while (workItem == null || counter < i + 1);
	  System.out.println("COUNTER="+counter);
      if (counter == 2) {
	warning = findWorkItemByActivityId(
	    "Deadline1_wp1_act1", true);
      } else if (counter == 3) {
	alert = findWorkItemByActivityId(
	    "Deadline1_wp1_act2", true);
      } else if (counter == 4) {
	  System.out.println("COMPLETING");
	completeWorkItem(workItem);
      }
    }
    completeWorkItem(warning);
    completeWorkItem(alert);
    testCheckProcessCompleted();
  }

  public static TestSuite suite() {
    TestSuite suite = new TestSuite("Deadline1 Test");
    TestContext context = new TestContext();

     suite.addTest(new LoopTest("testLoop", context));
     suite.addTest(new LoopTest("testLoopSkippedSync", context));
     suite.addTest(new LoopTest("testLoopSkippedAsync", context));
     suite.addTest(new LoopTest("testLoopSplit1", context));
     suite.addTest(new LoopTest("testLoopSplit2", context));
//     suite.addTest(new LoopTest("testLoopSplit3", context));
     suite.addTest(new LoopTest("testLoopSplit4", context));
     suite.addTest(new LoopTest("testLoopSplit5", context));

    return suite;
  }
}