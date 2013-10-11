package de.objectcode.canyon.wetdock.basic.test;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.apache.bsf.BSFException;
import org.apache.bsf.BSFManager;

import bsh.EvalError;
import bsh.Interpreter;
import de.objectcode.canyon.model.process.Duration;

public class BSFTest extends TestCase {

	public BSFTest(String name) {
		super(name);
	}

	private Object evalBSFScript(String script, HashMap variables) throws BSFException {
    BSFManager   bsfMgr     = new BSFManager();

    Iterator i = variables.keySet().iterator();
    while (i.hasNext()) {
    	String name = (String)i.next();
    	Object value = variables.get(name);
      bsfMgr.declareBean( name, value, Integer.TYPE);
    }
    
    return bsfMgr.eval("beanshell", null, 0,0, script);

	}

	private Object evalBSHScript(String script, HashMap variables) throws EvalError {
    Interpreter inter     = new Interpreter();

    Iterator i = variables.keySet().iterator();
    while (i.hasNext()) {
    	String name = (String)i.next();
    	Object value = variables.get(name);
      inter.set(name, value);
    }
    
    return inter.eval(script);

	}

	public void testBoxing() throws Exception {
		HashMap variables = new HashMap();
		Integer i = new Integer(1);
		variables.put("a",i);
		variables.put("b",i);
		Boolean b = (Boolean) evalBSFScript("a.equals(b)",variables);
		assertTrue("Should be true",b.booleanValue());		
		b = (Boolean) evalBSFScript("a==b",variables);
		assertTrue("Should be true",b.booleanValue());		
		variables.put("a",new Integer(2));
		b = (Boolean) evalBSFScript("a>b",variables);
		assertTrue("Should be true",b.booleanValue());		
	}

	public static String scoobeedoo(String a) {
		return a + "hoho";
	}
	public void testScript() throws Exception {
		HashMap variables = new HashMap();
		variables.put("a","HUHU");
		String b = (String) evalBSHScript("import de.objectcode.canyon.wetdock.basic.test.BSFTest;BSFTest.scoobeedoo(a)",variables);
		assertEquals("FunctionCall","HUHUhoho",b);
	}

	public void testMultilineScript() throws Exception {
		HashMap variables = new HashMap();
		variables.put("a","HUHU");
		String b = (String) evalBSHScript("c=a+\"hoho\";d=c+\"hihi\";",variables);
		assertEquals("FunctionCall","HUHUhohohihi",b);
	}

	public void testComplexScript() throws Exception {
		HashMap variables = new HashMap();
		variables.put("a","HUHU");
		String script = "if (a.equals(\"HUHU\")) result = \"HOHO\"; else result = \"HEHE\"; result;";
		String b = (String) evalBSFScript(script,variables);
		assertEquals("FunctionCall","HOHO",b);
		variables.put("a","GRRR");
		b = (String) evalBSFScript(script,variables);
		assertEquals("FunctionCall","HEHE",b);
	}
	

	public void testDueDateScript() throws Exception {
		HashMap variables = new HashMap();
		variables.put("a","HUHU");
		
		String script=
		"import java.text.SimpleDateFormat;"+
		"import java.util.Calendar;"+
		"import java.util.Date;"+
		"import java.util.GregorianCalendar;"+
		"SimpleDateFormat sdf = new SimpleDateFormat(\"dd.MM.yyyy HH:mm:ss\");"+
		"GregorianCalendar gc = new GregorianCalendar();"+
		"gc.set(Calendar.HOUR_OF_DAY,0);"+
		"gc.set(Calendar.MINUTE,0);"+
		"gc.set(Calendar.SECOND,0);"+
		"gc.add(Calendar.DATE,11);"+
		"sdf.format(gc.getTime());";
		String dd = (String) evalBSFScript(script,new HashMap());
		System.out.println(dd);
		Duration d= Duration.parse(dd);
	}
		public static	TestSuite suite() {
		TestSuite suite  = new TestSuite();
		suite.addTest(new BSFTest("testBoxing"));
		suite.addTest(new BSFTest("testScript"));
		suite.addTest(new BSFTest("testMultilineScript"));
		suite.addTest(new BSFTest("testComplexScript"));
		suite.addTest(new BSFTest("testDueDateScript"));
		return suite;
	}
	
}
