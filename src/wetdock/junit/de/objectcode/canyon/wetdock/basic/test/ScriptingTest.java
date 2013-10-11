package de.objectcode.canyon.wetdock.basic.test;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;

import junit.framework.TestSuite;
import de.objectcode.canyon.api.worklist.WorkItemData;
import de.objectcode.canyon.model.process.Duration;
import de.objectcode.canyon.model.process.DurationUnit;
import de.objectcode.canyon.wetdock.test.TestContext;
import de.objectcode.canyon.wetdock.test.WetdockTestBase;
import de.objectcode.canyon.wetdock.test.WetdockUserManagerHelper;

/**
 * @author junglas
 */
public class ScriptingTest extends WetdockTestBase {
  public ScriptingTest(String testName, TestContext context) {
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

  public void testStaticNames() throws Exception {
    testClearRepositories();
    testCreateUsersAndRoles();    
    createPackage("Scripting.xpdl");
    startProcess("StaticNames", null);
    WorkItemData workItem = findWorkItemByActivityId("Activity1", true);
    completeWorkItem(workItem);
    assertEquals("Wrong act name","Activity1", workItem.getName());
    assertTrue("Wrong proc name",workItem.getProcessInstanceName().startsWith("StaticNames"));
    testCheckProcessCompleted();
  }

  public void testDynamicNames() throws Exception {
    testClearRepositories();
    testCreateUsersAndRoles();    
    HashMap params = new HashMap();
    params.put("actName","DynamicActName");
    params.put("procName","DynamicProcName");
    createPackage("Scripting.xpdl");
    startProcess("DynamicNames", params);
    WorkItemData workItem = findWorkItemByActivityId("Activity1", true);
    completeWorkItem(workItem);
    assertEquals("Wrong act name","DynamicActName",workItem.getName());
    assertEquals("Wrong proc name","DynamicProcName",workItem.getProcessInstanceName());
    testCheckProcessCompleted();
  }

  public void testBusinessCalendar() throws Exception {
    testClearRepositories();
    testCreateUsersAndRoles();    
    HashMap params = new HashMap();
    createPackage("Scripting.xpdl");
    startProcess("BusinessCalendar", params);
    WorkItemData workItem = findWorkItemByActivityId("Activity1", true);
    GregorianCalendar gc = new GregorianCalendar();
    gc.add(Calendar.DATE,10);
    GregorianCalendar gc2 = new GregorianCalendar();
    gc2.setTime(workItem.getDueDate());
    assertEquals("Wrong due date", gc.get(Calendar.DATE), gc2.get(Calendar.DATE));
    assertEquals("Wrong due date", gc.get(Calendar.MONTH), gc2.get(Calendar.MONTH));
    assertEquals("Wrong due date", gc.get(Calendar.YEAR), gc2.get(Calendar.YEAR));
    completeWorkItem(workItem);
    testCheckProcessCompleted();
  }

  public void testBusinessCalendarDefaultDurationUnit() throws Exception {
    testClearRepositories();
    testCreateUsersAndRoles();    
    HashMap params = new HashMap();
    createPackage("Scripting.xpdl");
    startProcess("BusinessCalendarDefaultDurationUnit", params);
    WorkItemData workItem = findWorkItemByActivityId("Activity1", true);
    GregorianCalendar gc = new GregorianCalendar();
    Duration d = new Duration(1,DurationUnit.MONTH);
    gc.setTimeInMillis(System.currentTimeMillis()+d.getMillis());
    GregorianCalendar gc2 = new GregorianCalendar();
    gc2.setTime(workItem.getDueDate());
    assertEquals("Wrong due date", gc.get(Calendar.DATE), gc2.get(Calendar.DATE));
    assertEquals("Wrong due date", gc.get(Calendar.MONTH), gc2.get(Calendar.MONTH));
    assertEquals("Wrong due date", gc.get(Calendar.YEAR), gc2.get(Calendar.YEAR));
    completeWorkItem(workItem);
    testCheckProcessCompleted();
  }

  public static TestSuite suite() {
    TestSuite suite = new TestSuite("ScriptingTest");
    TestContext context = new TestContext();

    suite.addTest(new ScriptingTest("testStaticNames", context));
    suite.addTest(new ScriptingTest("testDynamicNames", context));
    suite.addTest(new ScriptingTest("testBusinessCalendar", context));
    suite.addTest(new ScriptingTest("testBusinessCalendarDefaultDurationUnit", context));

    return suite;
  }
}