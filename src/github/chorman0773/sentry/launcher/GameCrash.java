package github.chorman0773.sentry.launcher;

public class GameCrash extends RuntimeException {
	private final LoadingPhase phase;
	public GameCrash(LoadingPhase phase) {
		this(phase,"An unexpected Error has occured");
	}

	public GameCrash(LoadingPhase phase,String message) {
		super(message);
		this.phase = phase;
	}

	public GameCrash(LoadingPhase phase,Throwable cause) {
		this(phase,cause.getMessage(),cause);
		// TODO Auto-generated constructor stub
	}

	public GameCrash(LoadingPhase phase,String message, Throwable cause) {
		super(message, cause);
		this.phase = phase;
	}

	public String toString() {
		return "A Game Crash has Occurred during "+phase+": "+getMessage();
	}
}
