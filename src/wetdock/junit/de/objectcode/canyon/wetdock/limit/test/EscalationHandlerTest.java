package de.objectcode.canyon.wetdock.limit.test;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import junit.framework.TestSuite;
import de.objectcode.canyon.api.worklist.ProcessInstanceData;
import de.objectcode.canyon.api.worklist.WorkItemData;
import de.objectcode.canyon.wetdock.test.TestContext;
import de.objectcode.canyon.wetdock.test.WetdockTestBase;
import de.objectcode.canyon.wetdock.test.WetdockUserManagerHelper;


/**
 * @author junglas
 */
public class EscalationHandlerTest extends WetdockTestBase {
  public EscalationHandlerTest(String testName, TestContext context) {
    super(testName, context);
  }

  public void testCreateUsersAndRoles() throws Exception {
    m_context.setClientId("client1");
    m_context.setUserId("user1");

    WetdockUserManagerHelper userManager = new WetdockUserManagerHelper();

    userManager.createUser("user1", "user1");
    userManager.createUser("user2", "user2");
    userManager.createUser("admin1", "admin1");
    userManager.createRole("role1");
    userManager.createRole("admins");
    userManager.createClient("client1");
    userManager.addUserRole("user1", "role1", "client1");
    userManager.addUserRole("admin1", "admins", "client1");
  }

  public void testCreatePackage() throws Exception {
    createPackage("escalationHandler.xpdl");
  }

  public void testStartProcess(String processId, String limitExpr) throws Exception {
    HashMap parameters = new HashMap();
    parameters.put("limitExpr", limitExpr);
    startProcess(processId, parameters);
  }


  public void testCompleteWorkItem1() throws Exception {
    WorkItemData workItem = findWorkItemByActivityId("Activity1", true);

    completeWorkItem(workItem);
  }

  public WorkItemData testCheckWorkItem1AndWait(int seconds) throws Exception {
    WorkItemData workItem = findWorkItemByActivityId("Activity1", true);

    Thread.sleep((seconds + 20) * 1000L);
    return workItem;
  }

  public void testCheckLimitExceededProcess(String escalationProcessId) throws Exception {
    ProcessInstanceData processInstance = findProcessInstanceByProcessId(escalationProcessId, true);

    m_context.setUserId("admin1");
    m_context.setProcessInstance(processInstance);
  }


  public void testCompleteWorkItemLimitActivity(Date retryDueDate, String retryPerformer, String retryState)
    throws Exception {
    WorkItemData workItem = findWorkItemByActivityId("LimitActivity", true);
    workItem.getApplicationData()[0].getParameter("state").setValue(retryState);
    if (retryDueDate != null) {
      workItem.getApplicationData()[0].getParameter("retryDueDate").setValue(retryDueDate);
    }
    if (retryPerformer != null) {
      workItem.getApplicationData()[0].getParameter("retryPerformer").setValue(retryPerformer);
    }

    completeWorkItem(workItem);
  }

  public void testDynamicLimit(String processId, String escalationProcessId, String limitExpr, int seconds,
    Date retryDueDate, String retryUser, String retryState) throws Exception {
    testClearRepositories();
    testCreateUsersAndRoles();
    testCreatePackage();

    //    testStartProcess(processId, limitExpr);
    //    testCompleteWorkItem1();
    //    testCheckProcessCompleted();
    testStartProcess(processId, limitExpr);

    ProcessInstanceData pi = m_context.getProcessInstance();
    WorkItemData workItem = testCheckWorkItem1AndWait(seconds);
    testCheckLimitExceededProcess(escalationProcessId);
    testCompleteWorkItemLimitActivity(retryDueDate, retryUser, retryState);
    testCheckProcessCompleted();
    m_context.setProcessInstance(pi);
    if (retryUser != null) {
      m_context.setUserId(retryUser);
    } else {
      m_context.setUserId("user1");
    }

    WorkItemData retryWorkItem = findWorkItemByActivityId("Activity1", true);
    if (retryDueDate == null) {
      assertEquals("Wrong Due Date", workItem.getDueDate().getTime() / 1000,
        retryWorkItem.getDueDate().getTime() / 1000);
    } else {
      assertEquals("Wrong Due Date", retryDueDate.getTime() / 1000, retryWorkItem.getDueDate().getTime() / 1000);
    }
    completeWorkItem(retryWorkItem);

    testCheckProcessCompleted();
  }

  public void testProcessAbsDynamicLimit() throws Exception {
    testDynamicLimit("EscalationHanderTest_Wor1", "LimitExceeded", "10s", 10, null, null, "RETRY");
  }

  public void testProcessRelDynamicLimit() throws Exception {
    long deadline = System.currentTimeMillis() + (15 * 1000L);
    SimpleDateFormat f = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
    testDynamicLimit("EscalationHanderTest_Wor1", "LimitExceeded", f.format(new Date(deadline)), 15, null, null,
      "RETRY");
  }

  public void testActivityAbsDynamicLimit() throws Exception {
    testDynamicLimit("EscalationHanderTest_Wor2", "LimitExceeded2", "10s", 10,
      new Date(System.currentTimeMillis() + (1000L * 3600)), null, "RETRY");
  }

  public void testActivityRelDynamicLimit() throws Exception {
    long deadline = System.currentTimeMillis() + (15 * 1000L);
    SimpleDateFormat f = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
    testDynamicLimit("EscalationHanderTest_Wor2", "LimitExceeded2", f.format(new Date(deadline)), 15,
      new Date(System.currentTimeMillis() + (1000L * 3600)), "user2", "RETRY");
  }

  public void testActivityIgnore() throws Exception {
    testDynamicLimit("EscalationHanderTest_Wor2", "LimitExceeded2", "10s", 10,
      new Date(System.currentTimeMillis() + (1000L * 3600)), "user2", "IGNORE");
  }

  public void testBug() throws Exception {
    testClearRepositories();
    testCreateUsersAndRoles();
    testCreatePackage();

    testStartProcess("EscalationHanderTest_Wor3", "10s");

    ProcessInstanceData rootpi = m_context.getProcessInstance();
    ProcessInstanceData pi = findProcessInstanceByProcessId("EscalationHanderTest_Wor4", true);
    m_context.setProcessInstance(pi);

    WorkItemData workItem = testCheckWorkItem1AndWait(10);
    testCheckLimitExceededProcess("LimitExceeded");
    testCompleteWorkItemLimitActivity(null, null, "RETRY");
    testCheckProcessCompleted();
    m_context.setProcessInstance(pi);
    m_context.setUserId("user1");

    WorkItemData retryWorkItem = findWorkItemByActivityId("Activity1", true);
    assertEquals("Wrong Due Date", workItem.getDueDate().getTime() / 1000,
      retryWorkItem.getDueDate().getTime() / 1000);
    completeWorkItem(retryWorkItem);

    testCheckProcessCompleted();
    m_context.setProcessInstance(rootpi);
    workItem = findWorkItemByActivityId("Show", true);
    assertEquals("Wrong value",
      ((Boolean) workItem.getApplicationData()[0].getParameter("angebotAngenommen").getValue()).booleanValue(), true);

    completeWorkItem(workItem);
    testCheckProcessCompleted();

  }

  public static TestSuite suite() {
    TestSuite suite = new TestSuite("EscalationHandlerTest");
    TestContext context = new TestContext();

    suite.addTest(new EscalationHandlerTest("testProcessAbsDynamicLimit", context));
    suite.addTest(new EscalationHandlerTest("testProcessRelDynamicLimit", context));
    suite.addTest(new EscalationHandlerTest("testActivityAbsDynamicLimit", context));

    suite.addTest(new EscalationHandlerTest("testActivityRelDynamicLimit", context));
    //        suite.addTest(new EscalationHandlerTest("testActivityIgnore", context));
    suite.addTest(new EscalationHandlerTest("testBug", context));
    return suite;
  }
}
