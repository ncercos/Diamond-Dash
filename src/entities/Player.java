package entities;

import game.Game;
import inputs.Input;
import levels.Level;
import sprites.Pose;

import java.awt.*;

/**
 * Written by Nicholas Cercos
 * Created on Oct 04 2023
 **/
public class Player extends Entity {

	private boolean[] pressing;
	private boolean[] clicking;

	private final int MAX_ENERGY = 100;
	private int energy = MAX_ENERGY;

	/**
	 * Constructs a player entity.
	 * 11w & 13h is the size of the hitbox (hb).
	 * Draw Offset is where the hb starts within sprite.
	 */
	public Player(Game game, double x, double y) {
		super(game, "player", x, y,
				6 * Game.SCALE,
				13 * Game.SCALE,
				32,
				13,
				18.5);
		attackDamage = 5;
		attackBox = new AttackBox();
		resetBinds();
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
		if(!isAttacking() && !isDying()) {
			if (pressing[Input.LT]) goLT(speed);
			if (pressing[Input.RT]) goRT(speed);
			if (pressing[Input.UP]) goUP(speed * 2.15);

			if (pressing[Input.ROLL] && !isRolling())
				setCurrentPose(Pose.ROLL);
			if(clicking[Input.ATTACK] && !isRolling())
				setCurrentPose(Pose.ATTACK);
		}

		move();
		checkCloseToLevelBorder();
		attackBox.update();

		Level level = game.getPlaying().getLevelManager().getCurrentLevel();
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

	@Override
	public void draw(Graphics g) {
		super.draw(g);
		attackBox.draw(g);
	}

	@Override
	public void modifyHealth(int health) {
		super.modifyHealth(health);
		if(health == 0) {
			// TODO: End the game
		}
	}

	/**
	 * Adds or removes energy.
	 *
	 * @param energy The amount to be added / subtracted.
	 */
	public void modifyEnergy(int energy) {
		this.energy += energy;
		if(energy > MAX_ENERGY) this.energy = MAX_ENERGY;
		else if(energy <= 0)    this.energy = 0;
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
	 * Initializes key and mouse bindings.
	 */
	public void resetBinds() {
		pressing = new boolean[1024];
		clicking = new boolean[4];
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

	/**
	 * Updates the value for a mouse click.
	 *
	 * @param buttonCode The index of the click.
	 * @param value      Whether the key was pressed or released.
	 */
	public void setClicking(int buttonCode, boolean value) {
		clicking[buttonCode] = value;
	}

	public int getEnergy() {
		return energy;
	}

	public int getMaxEnergy() {
		return MAX_ENERGY;
	}
}
