package github.chorman0773.sentry.launcher.asm;



import java.lang.System.Logger;

import github.chorman0773.sentry.annotation.Game.ClassInitFailBehavior;
import github.chorman0773.sentry.launcher.GameCrash;
import github.chorman0773.sentry.launcher.GameLauncher;
import github.chorman0773.sentry.launcher.ILoadingStep;
import github.chorman0773.sentry.launcher.LoadingPhase;

public class ClassInitializer implements ILoadingStep {
	
	private final String type;
	private Class<?> cl;
	private final ClassInitFailBehavior failBehavior;
	
	private static final Logger LOGGER = System.getLogger("SentryLauncher/ClassInitializer");
	
	public ClassInitializer(String type,ClassInitFailBehavior failBehavior) {
		this.type = type;
		this.failBehavior = failBehavior;
	}

	@Override
	public LoadingPhase phase() {
		// TODO Auto-generated method stub
		return LoadingPhase.PHASE2_1;
	}

	@Override
	public void load(GameLauncher launcher) {
		try {
			cl = Class.forName(type.replace('/', '.'), true, launcher.getClassLoader());
		}catch(ClassNotFoundException | ExceptionInInitializerError e) {
			if(failBehavior==ClassInitFailBehavior.CRASH)
				throw new GameCrash(launcher.getLoadingPhase(),"Game Crash Occured while initializing class "+type,e);
		}
	}

}
