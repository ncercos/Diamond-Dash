package sprites;

/**
 * Written by Nicholas Cercos
 * Created on Oct 07 2023
 **/
public enum Pose {

	WALK_UP("UP"),
	WALK_DN("DN"),
	WALK_LT("LT"),
	WALK_RT("RT");

	private final String name;

	Pose(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

}
