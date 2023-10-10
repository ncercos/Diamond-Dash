package entities;

import game.Game;
import inputs.Input;
import sprites.Pose;

import java.util.HashMap;
import java.util.Map;

/**
 * Written by Nicholas Cercos
 * Created on Oct 04 2023
 **/
public class Player extends Entity {

	private final Game game;
	private final boolean[] pressing;
	private boolean sprinting;

	public Player(Game game, double x, double y, double w, double h) {
		super("player", x, y, w, h);
		this.game = game;
		pressing = new boolean[1024];

		// Establish poses and load animations
		Map<Pose, Integer> poses = new HashMap<>();
		poses.put(Pose.WALK_LT, 4);
		poses.put(Pose.WALK_RT, 4);
		poses.put(Pose.SPRINT_LT, 4);
		poses.put(Pose.SPRINT_RT, 4);
		poses.put(Pose.JUMP_LT, 2);
		poses.put(Pose.JUMP_RT, 2);
		loadAnimations(poses);
	}

	/**
	 * The details behind making the player move.
	 *		1. Remove previous velocity
	 *	 	2. Handle inputs
	 *	 	3. Move according to speed
	 */
	public void handleMovement() {
		moving = false;
		sprinting = pressing[Input.SPRINT];
		double speed = sprinting ? 1.8 : 1.0;

		if (pressing[Input.UP]) goUP(speed * (sprinting ? 2.3 : 3.5));
		if (pressing[Input.LT]) goLT(speed);
		if (pressing[Input.RT]) goRT(speed);
		if (!sprinting || !moving) stopSprinting();
		move(game.getLevelManager().getCurrentLevel());
	}

	/**
	 * Stops the sprint pose & returns to idle (static walking).
	 */
	private void stopSprinting() {
		if(currentPose.equals(Pose.SPRINT_RT))
			currentPose = Pose.WALK_RT;
		else if(currentPose.equals(Pose.SPRINT_LT))
			currentPose = Pose.WALK_LT;
	}

	@Override
	public void goLT(double dx) {
		super.goLT(dx);
		if(sprinting && !inAir)
			currentPose = Pose.SPRINT_LT;
	}

	@Override
	public void goRT(double dx) {
		super.goRT(dx);
		if(sprinting && !inAir)
			currentPose = Pose.SPRINT_RT;
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
