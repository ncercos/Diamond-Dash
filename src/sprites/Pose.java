package sprites;

/**
 * Written by Nicholas Cercos
 * Created on Oct 07 2023
 **/
public enum Pose {

	WALK_LT("WLT"),
	WALK_RT("WRT"),

	SPRINT_LT("SLT"),
	SPRINT_RT("SRT"),

	JUMP_LT("JLT"),
	JUMP_RT("JRT"),

	CROUCH_LT("CLT"),
	CROUCH_RT("CRT"),

	DEAD("DEAD");

	private final String name;

	Pose(String name) {
		this.name = name;
	}

	/**
	 * Determines if a pose is drawn for left side.
	 *
	 * @param pose The pose in question.
	 * @return True if it is a left-sided pose.
	 */
	public static boolean isLeft(Pose pose) {
		String[] s = pose.name().split("_");
		if(s.length != 2) return false;
		return s[1].equals("LT");
	}

	public String getName() {
		return name;
	}

}
