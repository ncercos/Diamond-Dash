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

	private final boolean entityDrawnHere;

	LevelLayer(boolean entityDrawnHere) {
		this.entityDrawnHere = entityDrawnHere;
	}

	LevelLayer() {
		this.entityDrawnHere = false;
	}

	public String getName() {
		return name().toLowerCase();
	}

	/**
	 * @return True if this layer will be where entities will be drawn.
	 */
	public boolean isEntityDrawnHere() {
		return entityDrawnHere;
	}
}
