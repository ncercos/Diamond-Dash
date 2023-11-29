package entities.items;

import entities.Matter;
import game.Game;

/**
 * Written by Nicholas Cercos
 * Created on Nov 21 2023
 **/
public class Gold extends Matter {

	/**
	 * Create a Gold item with an adjusted hitbox.
	 * The rectangle has an offset specific to the sprite.
	 *
	 * @param x The initial x-location.
	 * @param y The initial y-location.
	 */
	public Gold(Game game, double x, double y) {
		super(game, x, y, 9, 5, 3, 11);
	}

	@Override
	public void onCollide() {}

}
