package levels;

import entities.Hitbox;
import game.Game;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * Written by Nicholas Cercos
 * Created on Oct 08 2023
 **/
public class Level {

	private final int id;
	private final LevelStyle style;
	private int[][] data;
	private final Set<Hitbox> hitboxes;

	public Level(LevelManager levelManager, int id, LevelStyle style) {
		this.id = id;
		this.style = style;
		this.hitboxes = new HashSet<>();

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

	public void addHitbox(Hitbox hitbox) {
		hitboxes.add(hitbox);
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

	public Set<Hitbox> getHitboxes() {
		return hitboxes;
	}
}
