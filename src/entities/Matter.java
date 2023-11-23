package entities;

import game.Game;
import levels.Level;

/**
 * Written by Nicholas Cercos
 * Created on Nov 21 2023
 **/
public abstract class Matter extends Entity {

	private final Level level;
	private final int tileX, tileY;

	public Matter(Level level, double x, double y, double w, double h, double xHitboxOffset, double yHitboxOffset) {
		super(x, y, w * Game.SCALE, h * Game.SCALE);
		this.x = x + (xHitboxOffset * Game.SCALE);
		this.y = y + (yHitboxOffset * Game.SCALE);
		this.level = level;

		tileX = (int)(x / Game.TILES_SIZE);
		tileY = (int)(y / Game.TILES_SIZE);
	}

	public abstract void onCollide();

	public int getTileX() {
		return tileX;
	}

	public int getTileY() {
		return tileY;
	}

	public Level getLevel() {
		return level;
	}

	public enum Type {

		// Items
		GOLD(0),
		DIAMOND(4),

		// Traps
		THORN_FENCE_LEFT(71, true),
		THORN_FENCE_RIGHT(79, true);

		private final int tileID;
		private final boolean trap;

		Type(int tileID) {
			this.tileID = tileID;
			this.trap = false;
		}

		Type(int tileID, boolean trap) {
			this.tileID = tileID;
			this.trap = trap;
		}

		/**
		 * Determines if a specific matter tile is a trap.
		 *
		 * @param index The index of the tile.
		 * @return True if the tile index is a trap tile.
		 */
		public static boolean isTrap(int index) {
			for(Type type : values()) {
				if(type.isTrap() && type.getTileID() == index)
					return true;
			}
			return false;
		}

		public int getTileID() {
			return tileID;
		}

		public boolean isTrap() {
			return trap;
		}
	}
}
