package sprites;

/**
 * Written by Nicholas Cercos
 * Created on Oct 07 2023
 **/
public enum Pose {

	IDLE,
	RUN,
	JUMP,
	ROLL(false, 4);

	private final boolean repeated;
	private final int duration;

	Pose(boolean repeated, int duration) {
		this.repeated = repeated;
		this.duration = duration;
	}

	Pose() {
		repeated = true;
		duration = 12;
	}

	/**
	 * Obtain a specific pose via its name.
	 *
	 * @param name The name of the pose.
	 * @return The enum pose value.
	 */
	public static Pose getPose(String name) {
		for(Pose pose : values())
			if(pose.getName().equalsIgnoreCase(name))
				return pose;
		return null;
	}

	public String getName() {
		return name().toLowerCase();
	}

	/**
	 * @return True if this pose will loop until it is told not to.
	 * If false, the pose will only loop through animations once.
	 */
	public boolean isRepeated() {
		return repeated;
	}

	/**
	 * @return The length in which each sprite of a specific pose
	 * will be shown on the screen. (Animation Speed)
	 */
	public int getDuration() {
		return duration;
	}
}
