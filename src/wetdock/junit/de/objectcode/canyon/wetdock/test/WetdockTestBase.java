package de.objectcode.canyon.wetdock.test;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Map;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.wfmc.wapi.WMProcessInstanceState;

import de.objectcode.canyon.api.worklist.ProcessInstanceData;
import de.objectcode.canyon.api.worklist.WorkItemData;
import de.objectcode.canyon.spi.filter.IFilter;

/**
 * @author junglas
 */
public class WetdockTestBase extends TestCase {
	protected TestContext m_context;

	protected WetdockTestBase(String testName, TestContext context) {
		super(testName);

		m_context = context;
	}

	public void testClearRepositories() throws Exception {
		new WetdockSetupHelper().getWetdockSetup().clearRepositories(true,
				true, true);
	}

	public void testCheckProcessCompleted() throws Exception {
		checkProcessState(WMProcessInstanceState.CLOSED_TERMINATED_INT);
	}

	public void testCheckProcessTerminated() throws Exception {
		checkProcessState(WMProcessInstanceState.CLOSED_COMPLETED_INT);
	}

	public void testCheckProcessAborted() throws Exception {
		checkProcessState(WMProcessInstanceState.CLOSED_ABORTED_INT);
	}

	public void testCheckProcessCompleted(String processInstanceId)
			throws Exception {
		checkProcessState(processInstanceId,
				WMProcessInstanceState.CLOSED_TERMINATED_INT);
	}

	public void testCheckProcessTerminated(String processInstanceId)
			throws Exception {
		checkProcessState(processInstanceId,
				WMProcessInstanceState.CLOSED_COMPLETED_INT);
	}

	public void testCheckProcessAborted(String processInstanceId)
			throws Exception {
		checkProcessState(processInstanceId,
				WMProcessInstanceState.CLOSED_ABORTED_INT);
	}

	protected void assertWorkItemDoesNotExistByActivityId(String activityId)
			throws Exception {
		WorkItemData workItem = findWorkItemByActivityId(activityId, false);
		assertNull("Workitem with id '" + activityId + "' did exist", workItem);
	}

	protected void completeWorkItemByActivityId(String activityId)
			throws Exception {
		completeWorkItemByActivityId(m_context.getProcessInstance().getId(),
				activityId);
	}

	protected void completeWorkItemByActivityId(String processInstanceId,
			String activityId) throws Exception {
		WorkItemData workItem = findWorkItemByActivityId(processInstanceId,
				activityId, true);
		completeWorkItem(workItem);
	}

	protected WorkItemData findWorkItemByActivityId(String activityId,
			boolean assertExists) throws Exception {
		return findWorkItemByActivityId(m_context.getProcessInstance().getId(),
				activityId, assertExists);
	}

	protected WorkItemData findWorkItemByActivityId(String processInstanceId,
			String activityId, boolean assertExists) throws Exception {
		String context = "[activityId=" + activityId + ",processInstanceId="
				+ processInstanceId;
		WorklistHelper worklist = new WorklistHelper();
		WorkItemData workItem = null;

		int i = 0;
		while (workItem == null && ++i < (assertExists ? 10 : 2)) {
			workItem = worklist.findWorkItem(m_context.getUserId(), m_context
					.getClientId(), processInstanceId, activityId);
			if (workItem != null)
				break;
			Thread.sleep(1000);
		}
		if (assertExists)
			Assert.assertNotNull(
					"No matching workitem found [" + context + "]", workItem);
		return workItem;
	}

	protected WorkItemData waitForWorkItemByActivityId(String activityId,
			long duration) throws Exception {
		return waitForWorkItemByActivityId(m_context.getProcessInstance()
				.getId(), activityId, duration);
	}

	protected WorkItemData waitForWorkItemByActivityId(
			String processInstanceId, String activityId, long duration)
			throws Exception {
		String context = "[activityId=" + activityId + ",processInstanceId="
				+ processInstanceId;
		WorklistHelper worklist = new WorklistHelper();
		WorkItemData workItem = null;

		long stop = System.currentTimeMillis() + duration;
		while (workItem == null && System.currentTimeMillis() < stop) {
			workItem = worklist.findWorkItem(m_context.getUserId(), m_context
					.getClientId(), processInstanceId, activityId);
			if (workItem != null)
				break;
			Thread.sleep(1000);
		}
		if (workItem == null)
			Assert.assertNotNull("No matching workitem found after " + duration
					+ " ms [" + context + "]", workItem);
		return workItem;
	}

	protected WorkItemData findWorkItemByProcessIdAndActivityId(
			String processId, String activityId, boolean assertExists)
			throws Exception {
		String context = "[activityId=" + activityId + ",processId="
				+ processId;
		WorklistHelper worklist = new WorklistHelper();
		WorkItemData workItem = null;

		int i = 0;
		while (workItem == null && ++i < 10) {
			workItem = worklist.findWorkItemByProcessIdAndActivityId(m_context
					.getUserId(), m_context.getClientId(), processId,
					activityId);
			if (workItem != null)
				break;
			Thread.sleep(1000);
		}
		if (assertExists)
			Assert.assertNotNull(
					"No matching workitem found [" + context + "]", workItem);
		return workItem;
	}

	protected WorkItemData[] findWorkItems(int offset, int length, IFilter filter, String[] sortAttrs, boolean[] asc)
			throws Exception {
		WorklistHelper worklist = new WorklistHelper();
		return worklist.findWorkItems(m_context
				.getUserId(), m_context.getClientId(),filter, offset,length,sortAttrs, asc);
	}

  protected int indexOfWorkItem(WorkItemData wd, IFilter filter, String[] sortAttrs, boolean[] asc) throws Exception  {
		WorklistHelper worklist = new WorklistHelper();
		return worklist.indexOfWorkItem(wd, m_context
				.getUserId(), m_context.getClientId(), filter, sortAttrs, asc);
	}

	protected int countWorkItems(IFilter filter)
	throws Exception {
WorklistHelper worklist = new WorklistHelper();
return worklist.countWorkItems(m_context
		.getUserId(), m_context.getClientId(), true,filter);
}

	protected ProcessInstanceData findProcessInstanceByProcessId(
			String processId, boolean assertExists) throws Exception {
		ProcessHelper process = new ProcessHelper();
		ProcessInstanceData[] processInstances = process.listProcessInstances(
				processId, m_context.getClientId(), true);

		if (assertExists)
			Assert.assertTrue("Process Instance " + processId
					+ " does not exist", processInstances.length > 0);

		return processInstances[0];
	}

	  protected ProcessInstanceData findProcessInstanceByProcessInstanceIdReadOnly(
			String processInstanceId) throws Exception {
		ProcessHelper process = new ProcessHelper();
		ProcessInstanceData processInstance = process
				.getProcessInstanceReadOnly(processInstanceId);
		Assert.assertTrue("Process Instance with piid" + processInstanceId
				+ " does not exist", processInstance != null);

		return processInstance;
	}

	  protected ProcessInstanceData findProcessInstanceByProcessInstanceId(
				String processInstanceId) throws Exception {
			ProcessHelper process = new ProcessHelper();
			ProcessInstanceData processInstance = process
					.getProcessInstance(processInstanceId);
			Assert.assertTrue("Process Instance with piid" + processInstanceId
					+ " does not exist", processInstance != null);

			return processInstance;
		}

	  protected String startProcess(String processId, Map parameters)
			throws Exception {
		ProcessHelper process = new ProcessHelper();

		ProcessInstanceData processInstance = process.startProcess(m_context
				.getUserId(), m_context.getClientId(), processId, parameters);

		m_context.setProcessInstance(processInstance);
		return processInstance.getId();
	}

	protected void completeWorkItem(WorkItemData workItem) throws Exception {
		WorklistHelper worklist = new WorklistHelper();
		worklist.completeWorkItem(workItem);
	}

	protected void acceptWorkItem(WorkItemData workItem) throws Exception {
		WorklistHelper worklist = new WorklistHelper();
		worklist.acceptWorkItem(workItem);
	}

	protected void checkProcessState(int state) throws Exception {
		checkProcessState(m_context.getProcessInstance().getId(), state);
	}

	protected void checkProcessState(String processInstanceId, int state)
			throws Exception {
		ProcessHelper process = new ProcessHelper();

		ProcessInstanceData processInstance = process
				.getProcessInstance(processInstanceId);

		m_context.setProcessInstance(processInstance);

		assertEquals("Process Instance State [" + processInstance.getId(),
				state, processInstance.getState());
	}

	protected void createPackage(String name) throws Exception {
		int i;

		InputStream in = this.getClass().getResourceAsStream(name);
		ByteArrayOutputStream xpdl = new ByteArrayOutputStream();
		byte[] buffer = new byte[8192];
		int readed;

		while ((readed = in.read(buffer)) > 0) {
			xpdl.write(buffer, 0, readed);
		}

		in.close();

		new ProcessAdminHelper().createPackage(xpdl.toByteArray());
	}

	protected void updatePackage(String name) throws Exception {
		int i;

		InputStream in = this.getClass().getResourceAsStream(name);
		ByteArrayOutputStream xpdl = new ByteArrayOutputStream();
		byte[] buffer = new byte[8192];
		int readed;

		while ((readed = in.read(buffer)) > 0) {
			xpdl.write(buffer, 0, readed);
		}

		in.close();

		new ProcessAdminHelper().updatePackage(xpdl.toByteArray());
	}

	static {
		System.setProperty("java.naming.factory.initial", System.getProperty(
				"java.naming.factory.initial",
				"org.jnp.interfaces.NamingContextFactory"));
		System.setProperty("java.naming.provider.url", System.getProperty(
				"java.naming.provider.url", "jnp://localhost:1099"));
		System.setProperty("java.naming.factory.url.pkgs", System.getProperty(
				"java.naming.factory.url.pkgs",
				"org.jboss.naming:org.jnp.interfaces"));
	}
}
