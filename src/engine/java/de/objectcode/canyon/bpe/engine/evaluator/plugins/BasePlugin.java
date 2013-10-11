package de.objectcode.canyon.bpe.engine.evaluator.plugins;

import de.objectcode.canyon.bpe.engine.activities.Activity;

public class BasePlugin {
	
	private Activity fActivity;
	
	public BasePlugin(Activity activity) {
		fActivity = activity;
	}

	public Activity getActivity() {
		return fActivity;
	}
	
	
}
