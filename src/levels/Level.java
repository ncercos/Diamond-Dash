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

	public Level(LevelManager levelManager, int id, LevelStyle style) {
		this.id = id;
		this.style = style;

		try {
			data = levelManager.getLevelData(this);
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
		if(x < 0 || x >= Game.GAME_WIDTH) return true;
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
