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

	private final Game game;
	private boolean[] pressing;
	private boolean[] clicking;

	public Player(Game game, double x, double y, double w, double h, int spriteWidth) {
		super("player", x, y, w, h, spriteWidth, 10, 18.5);
		this.game = game;
		initPressing();
		initClicking();
	}

	/**
	 * The details behind making the player move.
	 *		1. Remove previous velocity
	 *	 	2. Handle inputs
	 *	 	3. Move according to speed
	 */
	@Override
	public void update() {
		super.update();

		double speed = 1.65;
		if(!isAttacking()) {
			if (pressing[Input.LT]) goLT(speed);
			if (pressing[Input.RT]) goRT(speed);
			if (pressing[Input.UP]) goUP(speed * 2.15);

			if (pressing[Input.ROLL] && !isRolling())
				setCurrentPose(Pose.ROLL);
			if(clicking[Input.ATTACK] && !isRolling())
				setCurrentPose(Pose.ATTACK);
		}
		if (isIdling()) setCurrentPose(Pose.IDLE);

		Level level = game.getPlaying().getLevelManager().getCurrentLevel();
		move(level);
		checkCloseToLevelBorder();

		for(Matter item : level.getItems()) {
			if(overlaps(item)) {
				level.removeItem(item);
				item.onCollide();
				break;
			}
		}

		for(Matter trap : level.getTraps()) {
			if(overlaps(trap)) {
				trap.onCollide();
				break;
			}
		}
	}

	/**
	 * When within 20% of the x-coordinate edges, the map will
	 * extend (if possible) by offsetting the value that was traveled.
	 */
	private void checkCloseToLevelBorder() {
		Level level = game.getPlaying().getLevelManager().getCurrentLevel();
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

	public void initPressing() {
		 pressing = new boolean[1024];
	}

	/**
	 * Updates the value for a mouse click.
	 *
	 * @param buttonCode The index of the click.
	 * @param value      Whether the key was pressed or released.
	 */
	public void setClicking(int buttonCode, boolean value) {
		clicking[buttonCode] = value;
	}

	public void initClicking() {
		clicking = new boolean[4];
	}
}
