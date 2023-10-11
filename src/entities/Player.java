package entities;

import game.Game;
import inputs.Input;
import levels.Level;
import sprites.Pose;

/**
 * Written by Nicholas Cercos
 * Created on Oct 04 2023
 **/
public class Player extends Entity {

	private final boolean[] pressing;

	public Player(Game game, double x, double y, double w, double h, int spriteWidth) {
		super(game, "player", x, y, w, h, spriteWidth);
		pressing = new boolean[1024];
	}

	/**
	 * The details behind making the player move.
	 *		1. Remove previous velocity
	 *	 	2. Handle inputs
	 *	 	3. Move according to speed
	 */
	public void update() {
		moving = false;
		double speed = 1.15;

		if (pressing[Input.LT]) goLT(speed);
		if (pressing[Input.RT] ) goRT(speed);
		if (pressing[Input.UP]) goUP(speed * 3.5);
		if (!moving && !inAir) currentPose = Pose.IDLE;
		move(game.getLevelManager().getCurrentLevel());
		checkCloseToLevelBorder();
	}

	/**
	 * When within 20% of the x-coordinate edges, the map will
	 * extend (if possible) by offsetting the value that was traveled.
	 */
	private void checkCloseToLevelBorder() {
		Level level = game.getLevelManager().getCurrentLevel();
		if(level == null)return;
		int currentXPos = (int)x;
		int diff = currentXPos - level.getOffsetX();

		if(diff > level.RT_BORDER)
			level.addToOffsetX(diff - level.RT_BORDER);
		else if(diff < level.LT_BORDER)
			level.addToOffsetX(diff - level.LT_BORDER);
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
