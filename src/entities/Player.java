package entities;

import inputs.Input;
import sprites.Pose;

import java.util.HashMap;
import java.util.Map;

/**
 * Written by Nicholas Cercos
 * Created on Oct 04 2023
 **/
public class Player extends Entity {

	private final boolean[] pressing;
	private boolean sprinting;

	public Player(double x, double y, double w, double h) {
		super("player", x, y, w, h);
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

	public Player(Hitbox hitbox) {
		this(hitbox.getX(), hitbox.getY(), hitbox.getWidth(), hitbox.getHeight());
	}

	/**
	 * The details behind making the player move.
	 *		1. Remove previous velocity
	 *	 	2. Handle inputs
	 *	 	3. Move according to speed
	 *
	 * @param allowed Is the player allowed to move?
	 */
	public void handleMovement(boolean allowed) {
		if(allowed) {
			moving = false;
			sprinting = pressing[Input.SPRINT];
			int speed = sprinting ? 4 : 2;

			if (pressing[Input.UP]) goUP(speed * (sprinting ? 3 : 5));
			if (pressing[Input.LT]) goLT(speed);
			if (pressing[Input.RT]) goRT(speed);
			if(!sprinting || !moving) stopSprinting();
		}
		move();
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
	public void goLT(int dx) {
		super.goLT(dx);
		if(sprinting && !jumping)
			currentPose = Pose.SPRINT_LT;
	}

	@Override
	public void goRT(int dx) {
		super.goRT(dx);
		if(sprinting && !jumping)
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
