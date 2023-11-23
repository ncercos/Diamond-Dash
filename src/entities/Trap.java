package entities;

import game.Game;
import levels.Level;

/**
 * Written by Nicholas Cercos
 * Created on Nov 23 2023
 **/
public abstract class Trap extends Entity {

	private final Level level;

	public Trap(Level level, double x, double y, double w, double h, double xHitboxOffset, double yHitboxOffset) {
		super(x, y, w * Game.SCALE, h * Game.SCALE);
		this.x = x + (xHitboxOffset * Game.SCALE);
		this.y = y + (yHitboxOffset * Game.SCALE);
		this.level = level;
	}

	public abstract void onCollide();

	public Level getLevel() {
		return level;
	}

	public enum TrapType {

		STICK_RIGHT(79),
		STICK_LEFT(71);

		private final int tileID;

		TrapType(int tileID) {
			this.tileID = tileID;
		}

		/**
		 * Determines if a specific tile is a trap.
		 *
		 * @param index The index of the tile.
		 * @return True if the tile index is a trap tile.
		 */
		public static boolean isTrap(int index) {
			for(TrapType type : values()) {
				if(type.getTileID() == index)
					return true;
			}
			return false;
		}

		public int getTileID() {
			return tileID;
		}
	}
}
