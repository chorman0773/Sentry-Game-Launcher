package github.chorman0773.sentry.launcher;

public enum LoadingPhase {
	
	PHASE0_1("Prelaunch","Prelaunch",1,"Idle"),
	PHASE0_2("Setting Up","Prelaunch",2,"Download Game Info"),
	PHASE0_3("Setting Up","Prelaunch",3,"Updating Game"),
	PHASE1_1("Game Launch","Loading",1,"Object Resolution"),
	PHASE1_2("Game Launch","Loading",2,"Launch Artifact Lookup"),
	PHASE1_3("Game Launch","Loading",3,"Class-loading"),
	PHASE2_1("Game Launch","Initialization",1,"Launch Artifact Instantiation"),
	PHASE2_2("Game Launch","Initialization",2,"Preinit"),
	PHASE2_3("Game Launch","Initialization",3,"Startup"),
	PHASE2_4("Game Launch","Initialization",4,"Launch"),
	PHASE3_1("Game Launch","Runtime",1,"Game Handoff"),
	PHASE3_2("Game","Runtime",2,"Execution"),
	
	PHASE4_1("Game","Shutdown",1,"Stop Game"),
	PHASE4_2("Game Termination","Shutdown",2,"Reset State"),
	PHASE4_3("Game Terminiation","Shutdown",3,"Unload Game");
	
	private final String supergroup;
	private final String group;
	private final int innerId;
	private final String qualifier;
	private LoadingPhase(String supergroup,String group,int innerId,String qualifier) {
		this.supergroup = supergroup;
		this.group = group;
		this.innerId = innerId;
		this.qualifier = qualifier;
	}
	
	public String toString() {
		return String.format("%s: %s Phase %d -> %s",supergroup, group,innerId,qualifier);
	}
	
	public LoadingPhase nextPhase() {
		switch(this) {
		case PHASE0_1:
			return PHASE0_2;
		case PHASE0_2:
			return PHASE0_3;
		case PHASE0_3:
			return PHASE1_1;
		case PHASE1_1:
			return PHASE1_2;
		case PHASE1_2:
			return PHASE1_3;
		case PHASE1_3:
			return PHASE2_1;
		case PHASE2_1:
			return PHASE2_2;
		case PHASE2_2:
			return PHASE2_3;
		case PHASE2_3:
			return PHASE2_4;
		case PHASE2_4:
			return PHASE3_1;
		case PHASE3_1:
			return PHASE3_2;
		case PHASE3_2:
			return PHASE4_1;
		case PHASE4_1:
			return PHASE4_2;
		case PHASE4_2:
			return PHASE4_3;
		case PHASE4_3:
			return PHASE0_1;
		default:
			throw new Error();
		
		}
	}
	
}
