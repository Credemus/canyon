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
public class Basic1Test extends WetdockTestBase
{
  public Basic1Test( String testName, TestContext context )
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
    createPackage( "basic1.xpdl" );
  }

  public void testUpdatePackage()
      throws Exception
  {
    updatePackage( "basic1.xpdl" );
  }
  
  public void testStartProcess()
      throws Exception
  {
    startProcess( "basic1_Wor1", null );
  }

  public void testStartProcesses()
  throws Exception
  {
  	for (int i = 0; i < 100; i++)
  		startProcess( "basic1_Wor1", null );
  	IFilter filter = new CompareFilter("state",CompareFilter.EQ,2);
  	WorkItemData[]	workItems = findWorkItems(0,20, filter, new String[] { "priority"}, new boolean[] { true } );
  	assertEquals("WRONG NUMBER OF WORKITEMS", 20, workItems.length);
  	int index = indexOfWorkItem(workItems[5], filter, new String[] { "priority"}, new boolean[] { true } );
  	assertEquals("WRONG INDEX", 5, index);
  	workItems = findWorkItems(90,20, null, new String[] { "priority"}, new boolean[] { true } );
  	assertEquals("WRONG NUMBER OF WORKITEMS", 10, workItems.length);
  	filter = new CompareFilter("entityOid",CompareFilter.EQ,Long.valueOf(workItems[0].getId()));
  	int c = countWorkItems(filter);
  	assertEquals("WRONG NUMBER OF WORKITEMS", 1, c);
  }


	public void testCompleteWorkItem1 ()
      throws Exception
  {
    WorkItemData workItem = findWorkItemByActivityId("Activity1", true);
    
    completeWorkItem ( workItem );
  }

  public void testCompleteWorkItem2 ()
      throws Exception
  {
    WorkItemData workItem = findWorkItemByActivityId("Activity2", true);
    
    completeWorkItem ( workItem );
  }
  
  public static TestSuite suite()
  {
    TestSuite suite = new TestSuite( "Basic1 Test" );
    TestContext context = new TestContext();

    suite.addTest( new Basic1Test( "testClearRepositories", context ) );
    suite.addTest( new Basic1Test( "testCreateUsersAndRoles", context ) );
    suite.addTest( new Basic1Test( "testCreatePackage", context ) );
    suite.addTest( new Basic1Test( "testStartProcesses", context ) );
    suite.addTest( new Basic1Test( "testClearRepositories", context ) );
    suite.addTest( new Basic1Test( "testCreateUsersAndRoles", context ) );
    suite.addTest( new Basic1Test( "testCreatePackage", context ) );
    suite.addTest( new Basic1Test( "testStartProcess", context ) );
    suite.addTest( new Basic1Test( "testCompleteWorkItem1", context ));
    suite.addTest( new Basic1Test( "testCompleteWorkItem2", context ));
    suite.addTest( new Basic1Test( "testCheckProcessCompleted", context ));
    suite.addTest( new Basic1Test( "testUpdatePackage", context ) );
    suite.addTest( new Basic1Test( "testStartProcess", context ) );
    suite.addTest( new Basic1Test( "testCompleteWorkItem1", context ));
    suite.addTest( new Basic1Test( "testCompleteWorkItem2", context ));
    suite.addTest( new Basic1Test( "testCheckProcessCompleted", context ));

    return suite;
  }
}
