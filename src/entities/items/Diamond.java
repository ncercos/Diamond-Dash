package entities.items;

import levels.Level;

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
	public Diamond(Level level, double x, double y) {
		super(level, x, y, 8, 7, 4, 5);
	}

	@Override
	public void onCollect() {}

}
