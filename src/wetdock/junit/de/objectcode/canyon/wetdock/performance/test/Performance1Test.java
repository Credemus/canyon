package de.objectcode.canyon.wetdock.performance.test;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.naming.NamingException;

import org.wfmc.wapi.WMWorkflowException;

import junit.framework.TestSuite;
import de.objectcode.canyon.api.worklist.ProcessData;
import de.objectcode.canyon.wetdock.test.ProcessHelper;
import de.objectcode.canyon.wetdock.test.TestContext;
import de.objectcode.canyon.wetdock.test.WetdockTestBase;
import de.objectcode.canyon.wetdock.test.WetdockUserManagerHelper;

/**
 * @author junglas
 */
public class Performance1Test extends WetdockTestBase
{
  public Performance1Test( String testName, TestContext context )
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

  public void testCreatePackage(int i)
      throws Exception
  {
  	long start = System.currentTimeMillis();
    createPackage( "performance1.xpdl" );
  	System.out.println("CreatePackage#"+i+"="+(System.currentTimeMillis()-start)+" ms");
  }

  public void testLoadProcesses(int i, ProcessHelper ph ) throws RemoteException, NamingException, CreateException, WMWorkflowException {
  	long start = System.currentTimeMillis();
  	ProcessData[] pds = ph.getActiveProcesses();
  	System.out.println("GetActiveProcesses#"+i+"="+(System.currentTimeMillis()-start)+" ms");
  	assertEquals("Wrong number of active processes",19,pds.length);
  }
  
  public void testProcessUpload()
      throws Exception
  {
    testClearRepositories();
    ProcessHelper ph = new ProcessHelper();
     for (int i =0; i < 10; i++) {
    	testCreatePackage(i);
    	testLoadProcesses(i,ph);
    }
  }
  public static TestSuite suite()
  {
    TestSuite suite = new TestSuite( "Performance1 Test" );
    TestContext context = new TestContext();
    suite.addTest( new Performance1Test( "testProcessUpload", context ) );

    
    return suite;
  }
}
