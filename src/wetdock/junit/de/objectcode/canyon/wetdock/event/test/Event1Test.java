package de.objectcode.canyon.wetdock.event.test;

import junit.framework.TestSuite;
import de.objectcode.canyon.api.worklist.WorkItemData;
import de.objectcode.canyon.wetdock.deadline.test.DynamicDeadlineTest;
import de.objectcode.canyon.wetdock.test.TestContext;
import de.objectcode.canyon.wetdock.test.WetdockTestBase;
import de.objectcode.canyon.wetdock.test.WetdockUserManagerHelper;

/**
 * @author junglas
 */
public class Event1Test extends WetdockTestBase
{
  public Event1Test( String testName, TestContext context )
  {
    super( testName, context );
  }

  public void testCreateUsersAndRoles()
      throws Exception
  {
    m_context.setClientId( "client1" );
    m_context.setUserId( "user1" );
    
    WetdockUserManagerHelper userManager = new WetdockUserManagerHelper();

    userManager.createUser( "user1", "user1" );
    userManager.createRole( "role1" );
    userManager.createClient( "client1" );
    userManager.addUserRole( "user1", "role1", "client1" );
  }

  public void testCreatePackage()
      throws Exception
  {
    createPackage( "event1.xpdl" );
  }

  
  public void testStartStopByEvent()
      throws Exception
  {
    testClearRepositories();
    testCreateUsersAndRoles();
    testCreatePackage();
    String processInstanceId = startProcess( "Event1_Wor1", null );
    Thread.sleep(500);
    String otherProcessInstanceId = findProcessInstanceByProcessId("Event1_Wor2",true).getId();
    WorkItemData workItem = findWorkItemByActivityId(otherProcessInstanceId, "Start", true);
    completeWorkItem ( workItem );
    workItem = findWorkItemByActivityId("BeforeStop", true);
    WorkItemData dummy = findWorkItemByActivityId(otherProcessInstanceId, "Dummy", true);
    String subProcessInstanceId = findProcessInstanceByProcessId("Event1_wp1",true).getId();
    WorkItemData dummyDummy = findWorkItemByActivityId(subProcessInstanceId, "DummyDummy", true);
    completeWorkItem ( workItem );
    Thread.sleep(500);
    dummy = findWorkItemByActivityId(otherProcessInstanceId, "Dummy", false);
    assertNull("Dummy not removed",dummy);
    testCheckProcessTerminated(otherProcessInstanceId);
    dummyDummy = findWorkItemByActivityId(subProcessInstanceId, "DummyDummy", false);
    assertNull("DummyDummy not removed",dummyDummy);
    testCheckProcessTerminated(subProcessInstanceId);
    testCheckProcessCompleted(processInstanceId);
  }


//  public void testCompleteWorkItem1 ()
//      throws Exception
//  {
//    WorkItemData workItem = findWorkItemByActivityId("Activity1", true);
//    
//    completeWorkItem ( workItem );
//  }
//
//  public void testCheckWorkItem1AndWait ()
//      throws Exception
//  {
//    WorkItemData workItem = findWorkItemByActivityId("Activity1", true);
//
//    Thread.sleep(40 * 1000L);
//  }
//      
//  
//  public void testCompleteWorkItem2 ()
//      throws Exception
//  {
//    WorkItemData workItem = findWorkItemByActivityId("Activity2", true);
//    
//    completeWorkItem ( workItem );
//  }
//  
//  public void testCompleteWorkItem3 ()
//      throws Exception
//  {
//    WorkItemData workItem = findWorkItemByActivityId("Activity3", true);
//    
//    completeWorkItem ( workItem );
//  }
  
  public static TestSuite suite()
  {
    TestSuite suite = new TestSuite( "Event1 Test" );
    TestContext context = new TestContext();
    suite.addTest( new Event1Test( "testStartStopByEvent", context ) );

    
    return suite;
  }
}
