package sprites;

/**
 * Written by Nicholas Cercos
 * Created on Oct 07 2023
 **/
public enum Pose {

	IDLE,
	RUN,
	JUMP;

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
}
