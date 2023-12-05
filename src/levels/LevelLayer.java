package levels;

/**
 * Written by Nicholas Cercos
 * Created on Oct 14 2023
 **/
public enum LevelLayer {

	MIDGROUND,
	DECOR,
	FLORA,
	SPAWNS,
	FOREGROUND,
	WATER;

	public String getName() {
		return name().toLowerCase();
	}

}
