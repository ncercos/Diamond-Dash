package matter.items;

import game.Game;
import matter.Item;

/**
 * Written by Nicholas Cercos
 * Created on Nov 21 2023
 **/
public class Diamond extends Item {

	/**
	 * Create a Diamond item with an adjusted hitbox.
	 * The rectangle has an offset specific to the sprite.
	 *
	 * @param x The initial x-location.
	 * @param y The initial y-location.
	 */
	public Diamond(Game game, double x, double y) {
		super(game, x, y, 8, 7, 4, 5);
	}

}
