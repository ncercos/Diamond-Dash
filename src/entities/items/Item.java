package entities.items;

import entities.Entity;
import game.Game;
import levels.Level;

/**
 * Written by Nicholas Cercos
 * Created on Nov 21 2023
 **/
public abstract class Item extends Entity {

	private final Level level;
	private final int tileX, tileY;

	public Item(Level level, double x, double y, double w, double h, double xHitboxOffset, double yHitboxOffset) {
		super(x, y, w * Game.SCALE, h * Game.SCALE);
		this.x = x + (xHitboxOffset * Game.SCALE);
		this.y = y + (yHitboxOffset * Game.SCALE);
		this.level = level;

		tileX = (int)(x / Game.TILES_SIZE);
		tileY = (int)(y / Game.TILES_SIZE);
	}

	protected abstract void onCollect();

	/**
	 * Handles everything that is done when an item
	 * is picked up by a player.
	 */
	public void collected() {
		level.removeItem(this);
		onCollect();
	}

	public int getTileX() {
		return tileX;
	}

	public int getTileY() {
		return tileY;
	}
}
