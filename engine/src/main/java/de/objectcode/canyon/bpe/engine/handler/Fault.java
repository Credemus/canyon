package de.objectcode.canyon.bpe.engine.handler;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;

import de.objectcode.canyon.bpe.engine.activities.Activity;

public class Fault {
	
	private Throwable fThrowable;
	private String fMessage;
	private String fName;
	private String fStackTrace;
	private String fActivityId;
	private String fActivityName;
	private String fProcessDefinitionId;
	private String fProcessDefinitionVersion;
	private String fProcessDefinitionName;
	private String fProcessInstanceIdPath;

	public Fault(Throwable throwable) {
		super();
		Throwable base = getBaseThrowable(throwable);
		fName = base.getClass().getName();
		fMessage = extractMessage(base);
		fStackTrace = extractStackTrace(throwable);
	}

	public Fault(Throwable throwable, Activity activity) {
		super();
		Throwable base = getBaseThrowable(throwable);
		fName = base.getClass().getName();
		fMessage = extractMessage(base);
		fStackTrace = extractStackTrace(throwable);
		fActivityId = activity.getId();
		fActivityName = activity.getName();
		fProcessDefinitionId = activity.getProcess().getId();
		fProcessDefinitionName = activity.getProcess().getName();
		fProcessDefinitionVersion = activity.getProcess().getVersion();
		StringBuffer buffy = new StringBuffer();
		if (activity.getProcess().getParentProcessInstanceIdPath()!=null)
			buffy.append(activity.getProcess().getParentProcessInstanceIdPath()).append("_");
		buffy.append(activity.getProcess().getProcessInstanceId());
		fProcessInstanceIdPath = buffy.toString();
	}
	
	/**
	 * Try to find a usefull cause/target
	 * @param e
	 * @return
	 */
	private Throwable getBaseThrowable(Throwable e) {
  	Throwable t = e;
  	if (e.getCause()!=null) {
  		t = e.getCause(); // cascade here?
  	}
  	if (t instanceof InvocationTargetException) {
  		InvocationTargetException ite = (InvocationTargetException) t;
  		if (ite.getTargetException()!=null)
  			t = ite.getTargetException(); 
  	}		
  	return t;
	}
	
	private String extractStackTrace(Throwable e) {
		StringWriter  buffy = new StringWriter();
		
		PrintWriter p = new PrintWriter(buffy);
		e.printStackTrace(p);
		return buffy.toString();
	}
	
	private String extractMessage(Throwable t) {
  	return t.getMessage();
	}
	
	public Fault(String name) {
		super();
		fName = name;
	}

	public String getMessage() {
		return fMessage;
	}

	public String getName() {
		return fName;
	}


	public String getStackTrace() {
		return fStackTrace;
	}

	public String getProcessDefinitionId() {
		return fProcessDefinitionId;
	}

	public String getProcessDefinitionName() {
		return fProcessDefinitionName;
	}

	public String getProcessDefinitionVersion() {
		return fProcessDefinitionVersion;
	}

	public String getProcessInstanceIdPath() {
		return fProcessInstanceIdPath;
	}

	public String getActivityId() {
		return fActivityId;
	}

	public String getActivityName() {
		return fActivityName;
	}
	

}
