package levels;

/**
 * Written by Nicholas Cercos
 * Created on Oct 09 2023
 **/
public enum LevelStyle {

	LUSH,
	DRY;

	private final int[] nonSolidIndex;

	LevelStyle(int... nonSolidIndex) {
		this.nonSolidIndex = nonSolidIndex;
	}

	/**
	 * Find style based on name.
	 *
	 * @param name The name of the file.
	 * @return A level style under the given name, if it exists.
	 */
	public static LevelStyle getStyle(String name) {
		for(LevelStyle style : LevelStyle.values())
			if(style.getName().equalsIgnoreCase(name))
				return style;
		return null;
	}

	/**
	 * Checks if a tile's index is non-solid.
	 *
	 * @param index The index of the tile.
	 * @return True if not solid.
	 */
	public boolean isNonSolid(int index) {
		if(index < 1)return true;
		for (int solidIndex : nonSolidIndex)
			if (index == solidIndex)
				return true;
		return false;
	}

	public String getName() {
		return name().toLowerCase();
	}

	/**
	 * @return The file name for a specific style.
	 */
	public String getFileName() {
		return getName() + ".png";
	}
}
