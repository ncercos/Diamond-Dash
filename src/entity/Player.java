package entity;

import inputs.Input;

/**
 * Written by Nicholas Cercos
 * Created on Oct 04 2023
 **/
public class Player extends Entity {

	private final boolean[] pressing;

	public Player(double x, double y, double w, double h) {
		super(x, y, w, h);
		pressing = new boolean[1024];
	}

	public Player(Hitbox hitbox) {
		this(hitbox.getX(), hitbox.getY(), hitbox.getWidth(), hitbox.getHeight());
	}

	/**
	 * The details behind making the player move.
	 *		1. Remove previous velocity
	 *	 	2. Handle inputs
	 *	 	3. Move according to speed
	 */
	public void handleMovement() {
		int speed = 3;

		physicsOFF();
		if(pressing[Input.UP]) goUP(speed);
		if(pressing[Input.DN]) goDN(speed);
		if(pressing[Input.LT]) goLT(speed);
		if(pressing[Input.RT]) goRT(speed);
		move();
	}

	/**
	 * Updates the value for a key interaction.
	 *
	 * @param keyCode The code of the interacted key.
	 * @param value   Whether the key was pressed or released.
	 */
	public void setPressing(int keyCode, boolean value) {
		pressing[keyCode] = value;
	}
}
