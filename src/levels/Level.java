package levels;

import game.Game;

import java.io.IOException;

/**
 * Written by Nicholas Cercos
 * Created on Oct 08 2023
 **/
public class Level {

	private final int id;
	private final LevelStyle style;
	private int[][] data;

	private int offsetX, maxTilesOffset;
	public final int LT_BORDER = (int) (0.2 * Game.GAME_WIDTH);
	public final int RT_BORDER = (int) (0.8 * Game.GAME_WIDTH);

	public Level(LevelManager levelManager, int id, LevelStyle style) {
		this.id = id;
		this.style = style;

		try {
			data = levelManager.getLevelData(this);
			int tilesWide = data[0].length;
			maxTilesOffset = (tilesWide - Game.TILES_IN_WIDTH) * Game.TILES_SIZE;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Determines if a specific location contains a solid tile.
	 *
	 * @param x The x-coordinate.
	 * @param y The y-coordinate.
	 * @return True if the tile is solid, otherwise, false.
	 */
	public boolean isSolid(double x, double y) {
		int maxWidth = data[0].length * Game.TILES_SIZE;
		if(x < 0 || x >= maxWidth) return true;
		if(y < 0 || y >= Game.GAME_HEIGHT) return true;
		int index = getTileIndex((int)x / Game.TILES_SIZE, (int)y / Game.TILES_SIZE);
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

	public void setOffsetX(int offsetX) {
		this.offsetX = offsetX;
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
