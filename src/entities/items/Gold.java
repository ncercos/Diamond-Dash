package entities.items;

import levels.Level;

/**
 * Written by Nicholas Cercos
 * Created on Nov 21 2023
 **/
public class Gold extends Item {

	/**
	 * Create a Gold item with an adjusted hitbox.
	 * The rectangle has an offset specific to the sprite.
	 *
	 * @param x The initial x-location.
	 * @param y The initial y-location.
	 */
	public Gold(Level level, double x, double y) {
		super(level, x, y, 9, 5, 3, 11);
	}

	@Override
	public void onCollect() {}

}
