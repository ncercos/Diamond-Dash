package entities.items;

import entities.Matter;
import game.Game;

/**
 * Written by Nicholas Cercos
 * Created on Nov 21 2023
 **/
public class Diamond extends Matter {

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

	@Override
	public void onCollide() {}

}
