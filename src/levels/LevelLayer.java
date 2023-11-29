package levels;

/**
 * Written by Nicholas Cercos
 * Created on Oct 14 2023
 **/
public enum LevelLayer {

	MIDGROUND,
	DECOR,
	ITEMS,
	FLORA(true),
	FOREGROUND,
	WATER;

	private final boolean mobSpawn;

	LevelLayer(boolean mobSpawn) {
		this.mobSpawn = mobSpawn;
	}

	LevelLayer() {
		this.mobSpawn = false;
	}

	public String getName() {
		return name().toLowerCase();
	}

	/**
	 * @return True if this layer will be where mobs will be drawn.
	 */
	public boolean isMobSpawn() {
		return mobSpawn;
	}
}
