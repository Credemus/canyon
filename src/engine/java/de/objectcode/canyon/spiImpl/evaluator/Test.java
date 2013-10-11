package de.objectcode.canyon.spiImpl.evaluator;

import org.apache.bsf.BSFManager;

/**
 * @author junglas
 */
public class Test {
	public static void main ( String[] args ){
		try {
			BSFManager      bsfMgr  = new BSFManager();
			
			bsfMgr.declareBean("startK", "nix", String.class);
			
			System.out.println(bsfMgr.eval( "beanshell", null, 0, 0, "startK" ));
		}catch (Exception e ) {
			e.printStackTrace();
		}
	}
}
