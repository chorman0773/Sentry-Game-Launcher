package github.chorman0773.sentry.launcher;

public interface ILoadingStep {
	public LoadingPhase phase();
	public void load(GameLauncher launcher);
}
