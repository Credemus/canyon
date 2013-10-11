package de.objectcode.canyon.wetdock.basic.test;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestSuite;
import de.objectcode.canyon.api.worklist.ProcessInstanceData;
import de.objectcode.canyon.api.worklist.WorkItemData;
import de.objectcode.canyon.wetdock.test.ProcessHelper;
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
      WorkItemData workItem = findWorkItemByActivityId("Activity1", true);
      completeWorkItem(workItem);
      workItem = findWorkItemByActivityId("Activity3", true);
      assertNotNull("No workItem found at loop " + i, workItem);
      Integer counter = (Integer) workItem.getApplicationData()[0]
          .getParameter("Counter").getValue();
      assertEquals("Counter wrong",counter.intValue(),i+1);
      counter = new Integer(counter.intValue() + 1);
      workItem.getApplicationData()[0].getParameter("Counter")
          .setValue(counter);
      completeWorkItem(workItem);
    }
    testCheckProcessCompleted();
  }

  private int getCounter(WorkItemData workItem) {
      Integer counter = (Integer) workItem.getApplicationData()[0]
                                                  	          .getParameter("Counter").getValue();
      return counter.intValue();
  }
  
  private void sendEvent(int counter) throws Exception {
		ProcessHelper process = new ProcessHelper();

		Map parameters = new HashMap();
		parameters.put("Counter", new Integer(counter));
		ProcessInstanceData processInstance = process.startProcess(m_context
				.getUserId(), m_context.getClientId(), "FireEvent", parameters);
  }
  
  
  public void testDualLoop() throws Exception {
	    testClearRepositories();
	    testCreateUsersAndRoles();    
	    createPackage("loop.xpdl");
	    startProcess("DualLoop", null);
	      WorkItemData workItem1 = findWorkItemByActivityId(null,"Activity1", true);
	      assertNotNull("No workItem 1 found at first loop ", workItem1);
	      assertEquals("Wrong counter",getCounter(workItem1),1);
	      completeWorkItem(workItem1);
	      WorkItemData workItem2 = findWorkItemByActivityId("Activity2", true);
	      assertNotNull("No workItem 2 found at first loop ", workItem2);
	      assertEquals("Wrong counter",getCounter(workItem2),1);
	      assertNull("Found workitem 3",findWorkItemByActivityId("Activity3", false));
	      assertNull("Found workitem 4",findWorkItemByActivityId("Activity4", false));
	      completeWorkItem(workItem2);
	      sendEvent(2);
	      workItem1 = findWorkItemByActivityId(null,"Activity1", true);
	      assertNotNull("No workItem 1 found at second loop ", workItem1);
	      assertEquals("Wrong counter",getCounter(workItem1),2);
	      completeWorkItem(workItem1);
	      WorkItemData workItem3 = findWorkItemByActivityId("Activity3", true);
	      assertNotNull("No workItem 3 found at second loop ", workItem3);
	      assertEquals("Wrong counter",getCounter(workItem3),2);
	      WorkItemData workItem4 = findWorkItemByActivityId("Activity4", true);
	      assertNotNull("No workItem 4 found at second loop ", workItem4);
	      assertEquals("Wrong counter",getCounter(workItem4),2);
	      completeWorkItem(workItem4);
	      workItem2 = findWorkItemByActivityId("Activity2", true);
	      assertNotNull("No workItem 2 found at second loop ", workItem2);
	      assertEquals("Wrong counter",getCounter(workItem2),2);
	      completeWorkItem(workItem2);
	      sendEvent(3);
	      workItem1 = findWorkItemByActivityId(null,"Activity1", true);
	      assertNotNull("No workItem 1 found at third loop ", workItem1);
	      assertEquals("Wrong counter",getCounter(workItem1),3);
	      completeWorkItem(workItem1);
	      Thread.sleep(1000);
	      workItem3 = findWorkItemByActivityId("Activity3", true);
	      assertNotNull("No workItem 3 found at third loop ", workItem3);
	      assertEquals("Wrong counter",getCounter(workItem3),3);
	      workItem4 = findWorkItemByActivityId("Activity4", true);
	      assertNotNull("No workItem 4 found at third loop ", workItem4);
	      assertEquals("Wrong counter",getCounter(workItem4),3);
	      completeWorkItem(workItem4);
	      workItem2 = findWorkItemByActivityId("Activity2", true);
	      assertNotNull("No workItem 2 found at third loop ", workItem2);
	      assertEquals("Wrong counter",getCounter(workItem2),3);
	      completeWorkItem(workItem3);
	      completeWorkItem(workItem2);
	      testCheckProcessCompleted();
	  }

  public static TestSuite suite() {
    TestSuite suite = new TestSuite("BasicLoop Test");
    TestContext context = new TestContext();

    //suite.addTest(new LoopTest("testLoop", context));
    suite.addTest(new LoopTest("testDualLoop", context));

    return suite;
  }
}