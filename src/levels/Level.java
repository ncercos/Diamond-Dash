package levels;

import game.Game;

/**
 * Written by Nicholas Cercos
 * Created on Oct 08 2023
 **/
public class Level {

	private final int id;
	private final LevelStyle style;
	private final int[][] data;

	private int offsetX;
	private final int maxTilesOffset;
	public final int LT_BORDER = (int) (0.2 * Game.GAME_WIDTH);
	public final int RT_BORDER = (int) (0.8 * Game.GAME_WIDTH);

	public Level(int id, LevelStyle style, int[][] data) {
		this.id = id;
		this.style = style;
		this.data = data;
		int tilesWide = data[0].length;
		maxTilesOffset = (tilesWide - Game.TILES_IN_WIDTH) * Game.TILES_SIZE;
	}

	/**
	 * Determines if a specific location contains a solid tile.
	 *
	 * @param px The x-position.
	 * @param py The y-position.
	 * @return True if the tile is solid, otherwise, false.
	 */
	public boolean isSolid(double px, double py) {
		int x = (int)px;  int y = (int)py;
		int maxWidth = data[0].length * Game.TILES_SIZE;
		if(x < 0 || x >= maxWidth) return true;
		if(y < 0 || y >= Game.GAME_HEIGHT) return true;
		int index = getTileIndex(x / Game.TILES_SIZE, y / Game.TILES_SIZE);
		return !getStyle().isNonSolid(index);
	}

	/**
	 * Get the index of a tile at a specific location.
	 *
	 * @param x The x-coordinate.
	 * @param y The y-coordinate.
	 * @return An integer that determines which tile will be placed.
	 */
	public int getTileIndex(int x, int y) {
		return data[y][x];
	}

	public void addToOffsetX(int value) {
		offsetX += value;
		maintainOffsetConstraints();
	}

	private void maintainOffsetConstraints() {
		if(offsetX > maxTilesOffset) offsetX = maxTilesOffset;
		else if(offsetX < 0) offsetX = 0;
	}

	public int getOffsetX() {
		return offsetX;
	}

	public int getId() {
		return id;
	}

	public LevelStyle getStyle() {
		return style;
	}

	public int[][] getData() {
		return data;
	}
}
