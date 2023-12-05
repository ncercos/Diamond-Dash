package matter;

import game.Game;
import levels.LevelLayer;
import utils.Hitbox;

/**
 * Written by Nicholas Cercos
 * Created on Nov 21 2023
 **/
public abstract class Matter extends Hitbox {

	protected final Game game;

	public Matter(Game game, double x, double y, double w, double h, double xDrawOffset, double yDrawOffset) {
		super(x, y, w * Game.SCALE, h * Game.SCALE, xDrawOffset, yDrawOffset);
		this.game = game;
		this.x = x + xDrawOffset;
		this.y = y + yDrawOffset;
	}

	public void update() {}

	public boolean onCollide() {
		return false;
	}

	public enum Tile {

		// Traps
		THORN_FENCE_LEFT  (71, LevelLayer.FLORA),
		THORN_FENCE_RIGHT (79, LevelLayer.FLORA);

		private final int tileID;
		private final LevelLayer layer;

		Tile(int tileID, LevelLayer layer) {
			this.tileID = tileID;
			this.layer = layer;
		}

		/**
		 * Determines if a specific matter tile is of a specific group / layer.
		 *
		 * @param layer  The layer in which this tile is found.
		 * @param index The index of the tile.
		 * @return True if the tile index is of the given layer.
		 */
		public static boolean isType(LevelLayer layer, int index) {
			for(Tile tile : values()) {
				if(tile.getLayer().equals(layer) && tile.getTileID() == index)
					return true;
			}
			return false;
		}

		public int getTileID() {
			return tileID;
		}

		public LevelLayer getLayer() {
			return layer;
		}
	}
}
