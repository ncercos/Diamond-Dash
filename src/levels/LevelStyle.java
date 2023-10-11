package levels;

/**
 * Written by Nicholas Cercos
 * Created on Oct 09 2023
 **/
public enum LevelStyle {

	NATURE(35, 36);

	private final int[] nonSolidIndex;

	LevelStyle(int... nonSolidIndex) {
		this.nonSolidIndex = nonSolidIndex;
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

	/**
	 * @return The file name for a specific style.
	 */
	public String getFileName() {
		return name().toLowerCase() + ".png";
	}
}
