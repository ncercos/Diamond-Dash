package matter;

import game.Game;
import game.states.Playing;
import levels.LevelLayer;
import utils.Hitbox;

/**
 * Written by Nicholas Cercos
 * Created on Nov 21 2023
 **/
public abstract class Matter extends Hitbox {

	protected final Game game;
	private final int tileX, tileY;
	private int tileIndex;

	public Matter(Game game, double x, double y, double w, double h, double xDrawOffset, double yDrawOffset) {
		super(x, y, w * Game.SCALE, h * Game.SCALE, xDrawOffset, yDrawOffset);
		this.game = game;
		this.x = x + xDrawOffset;
		this.y = y + yDrawOffset;

		tileX = (int)(x / Game.TILES_SIZE);
		tileY = (int)(y / Game.TILES_SIZE);
		tileIndex = -2;
	}

	public void update() {}

	public void onCollide() {}

	/**
	 * Finds a tile index within a specific layer.
	 *
	 * @param layer The layer to look through.
	 * @return An index for a tile on the sprite sheet.
	 */
	public int getTileIndex(LevelLayer layer) {
		if(tileIndex == -2)
			tileIndex = game.getPlaying().getLevelManager().getCurrentLevel().getTileIndex(layer, tileX, tileY);
		return tileIndex;
	}

	public int getTileX() {
		return tileX;
	}

	public int getTileY() {
		return tileY;
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
