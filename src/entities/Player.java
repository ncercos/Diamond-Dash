package entities;

import game.Game;
import inputs.Input;
import levels.Level;
import sprites.Pose;
import utils.Location;

import java.awt.*;

/**
 * Written by Nicholas Cercos
 * Created on Oct 04 2023
 **/
public class Player extends Entity {

	private boolean[] pressing;
	private boolean[] clicking;

	private final int MAX_ENERGY = 100;
	private double energy = MAX_ENERGY;
	private final int ENERGY_CONSUMPTION = 25; /* 3 moves */

	private boolean regenerating = false;
	private boolean boosted = false;
	private int boostedDuration;

	private int diamonds = 0;

	/**
	 * Constructs a player entity.
	 * 11w & 13h is the size of the hitbox (hb).
	 * Draw Offset is where the hb starts within sprite.
	 */
	public Player(Game game, Location location) {
		super(game, "player", location.getX(), location.getY(),
				6 * Game.SCALE,
				13 * Game.SCALE,
				32,
				13,
				18.5,
				100);
		attackDamage = 5;
		attackPoseIndex = 3;
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

		if(isRolling()) {
			if(isFacingLeft()) goLT(0.5 * Game.SCALE);
			else 							 goRT(0.5 * Game.SCALE);
		}

		replenishEnergy();
		regenerateHealth();

		double speed = 1.65;
		double xBoost = 0.75,
				   yBoost = 0.3;

		if(!isAttacking() && !isDying()) {
			if (pressing[Input.LT]) goLT(speed + (boosted ? xBoost : 0));
			if (pressing[Input.RT]) goRT(speed + (boosted ? xBoost : 0));
			if (pressing[Input.UP]) goUP(speed * 2.15 + (boosted ? yBoost : 0));

			if (pressing[Input.ROLL] && !isRolling() && isEnergetic()) {
				setCurrentPose(Pose.ROLL);
				consumeEnergy();
			} else if(clicking[Input.ATTACK] && !isRolling() && isEnergetic()) {
				setCurrentPose(Pose.ATTACK);
				consumeEnergy();
			}
		}

		move();
		checkCloseToLevelBorder();
	}

	@Override
	public void draw(Graphics g) {
		super.draw(g);
	}

	@Override
	public void reset() {
		super.reset();
		energy = MAX_ENERGY;
		boosted = false;
		regenerating = false;
		diamonds = 0;
	}

	public void foundDiamond() {
		diamonds++;
	}

	@Override
	public void modifyHealth(int value) {
		super.modifyHealth(value);
		if(getHealth() == 0) {
			// TODO: End the game
		}
	}

	/**
	 * Consumes energy for power moves (attack & roll).
	 */
	public void consumeEnergy() {
		if(boosted)return;
		energy -= ENERGY_CONSUMPTION;
		if(energy <= 0)
			energy = 0;
	}

	/**
	 * Replenishes energy after a power move.
	 * Bar will completely drain when dead.
	 */
	public void replenishEnergy() {
		if(isDying() || !active) {
			if(energy > 0)
				energy -= 1;
			return;
		}

		if(boostedDuration > 0) boostedDuration--;
		else boosted = false;

		if(energy >= MAX_ENERGY)return;
		if(!boosted && !getCurrentAnimation().isCycleCompleted())return;
		energy += boosted ? 1 : 0.5;
	}

	/**
	 * Regenerates health when medicine is picked up.
	 */
	public void regenerateHealth() {
		if(isDying() || !active || !regenerating)return;
		if(getHealth() >= getMaxHealth()) {
			regenerating = false;
			return;
		}
		modifyHealth(1);
	}

	/**
	 * @return True if the player has enough energy to perform a power move.
	 */
	public boolean isEnergetic() {
		return boosted || energy >= ENERGY_CONSUMPTION;
	}

	public boolean isRegenerating() {
		return regenerating;
	}

	public boolean isBoosted() {
		return boosted;
	}

	public void setRegenerating(boolean regenerating) {
		this.regenerating = regenerating;
	}

	public void setBoosted(boolean boosted) {
		this.boosted = boosted;
		boostedDuration = boosted ? 600 : 0;
	}

	public double getEnergy() {
		return energy;
	}

	public int getMaxEnergy() {
		return MAX_ENERGY;
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
}
