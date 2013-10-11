package de.objectcode.canyon.wetdock.basic.test;

import junit.framework.TestSuite;
import de.objectcode.canyon.api.worklist.WorkItemData;
import de.objectcode.canyon.wetdock.test.TestContext;
import de.objectcode.canyon.wetdock.test.WetdockTestBase;
import de.objectcode.canyon.wetdock.test.WetdockUserManagerHelper;

/**
 * @author junglas
 */
public class SubprocessTest extends WetdockTestBase {
  public SubprocessTest(String testName, TestContext context) {
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

  public void testSyncSubprocess() throws Exception {
    testClearRepositories();
    testCreateUsersAndRoles();    
    createPackage("subprocess.xpdl");
    String processInstanceId = startProcess("Sync", null);
    String subProcessInstanceId = findProcessInstanceByProcessId("Sub",true).getId();
    WorkItemData subWorkItem = findWorkItemByActivityId(subProcessInstanceId,"SubActivity1", true);
    WorkItemData workItem = findWorkItemByActivityId(processInstanceId, "SyncActivity1", false);
    assertNull("WorkItem found",workItem);
    completeWorkItem(subWorkItem);
    testCheckProcessCompleted(subProcessInstanceId);
    workItem = findWorkItemByActivityId(processInstanceId, "SyncActivity1", true);
    completeWorkItem(workItem);
    testCheckProcessCompleted();
  }

  public void testAsyncSubprocessFirstCompleteSubProcess() throws Exception {
	    testClearRepositories();
	    testCreateUsersAndRoles();    
	    createPackage("subprocess.xpdl");
	    String processInstanceId = startProcess("Async", null);
	    String subProcessInstanceId = findProcessInstanceByProcessId("Sub",true).getId();
	    WorkItemData subWorkItem = findWorkItemByActivityId(subProcessInstanceId,"SubActivity1", true);
	    WorkItemData workItem = findWorkItemByActivityId(processInstanceId, "AsyncActivity1", true);
	    completeWorkItem(subWorkItem);
	    testCheckProcessCompleted(subProcessInstanceId);
	    completeWorkItem(workItem);
	    testCheckProcessCompleted(processInstanceId);
	  }

  public void testAsyncSubprocessFirstCompleteParentProcess() throws Exception {
	    testClearRepositories();
	    testCreateUsersAndRoles();    
	    createPackage("subprocess.xpdl");
	    String processInstanceId = startProcess("Async", null);
	    String subProcessInstanceId = findProcessInstanceByProcessId("Sub",true).getId();
	    WorkItemData subWorkItem = findWorkItemByActivityId(subProcessInstanceId,"SubActivity1", true);
	    WorkItemData workItem = findWorkItemByActivityId(processInstanceId, "AsyncActivity1", true);
	    completeWorkItem(workItem);
	    testCheckProcessCompleted(processInstanceId);
	    completeWorkItem(subWorkItem);
	    testCheckProcessCompleted(subProcessInstanceId);
	  }

  public static TestSuite suite() {
    TestSuite suite = new TestSuite("Subprocess Test");
    TestContext context = new TestContext();

    suite.addTest(new SubprocessTest("testSyncSubprocess", context));
    suite.addTest(new SubprocessTest("testAsyncSubprocessFirstCompleteSubProcess", context));
    suite.addTest(new SubprocessTest("testAsyncSubprocessFirstCompleteParentProcess", context));

    return suite;
  }
}