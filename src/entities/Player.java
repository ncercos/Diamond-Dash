package entities;

import game.Game;
import game.states.Playing;
import inputs.Input;
import levels.Level;
import levels.LevelLayer;
import sounds.Sound;
import sprites.Pose;
import utils.Location;

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
	public Player(Playing playing, Location location) {
		super(playing, "player", location.getX(), location.getY(),
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
			if(isFacingLeft()) goLT(2.5);
			else 							 goRT(2.5);
		}

		replenishEnergy();
		regenerateHealth();

		double speed = 1.65;
		double xBoost = 0.75,
				   yBoost = 0.3;

		if(!isAttacking() && !isDying() && !getLevel().isComplete()) {
			if (pressing[Input.LT]) goLT(speed + (boosted ? xBoost : 0));
			if (pressing[Input.RT]) goRT(speed + (boosted ? xBoost : 0));
			if (pressing[Input.UP]) goUP(speed * 2.15 + (boosted ? yBoost : 0));

			if (pressing[Input.ROLL] && !isRolling() && isEnergetic()) {
				setCurrentPose(Pose.ROLL);
				consumeEnergy();
				playing.getSoundManager().playSFX(Sound.ROLL);
			} else if(clicking[Input.ATTACK] && !isAttacking() && isEnergetic()) {
				setCurrentPose(Pose.ATTACK);
				consumeEnergy();
			}
		}

		move();
		checkCloseToLevelBorder();
		checkInWater();
	}

	@Override
	public void reset() {
		super.reset();
		energy = MAX_ENERGY;
		boosted = false;
		regenerating = false;
		diamonds = 0;
	}

	@Override
	public boolean attack(Entity entity) {
		if(super.attack(entity)) {
			playing.getSoundManager().playSFX(Sound.ATTACK);
			return true;
		}
		return false;
	}

	@Override
	public void damage(int damage) {
		super.damage(damage);
		if(currentPose.equals(Pose.DIE)) {
			playing.getSoundManager().stopSong();
			playing.getSoundManager().playSFX(Sound.GAME_OVER);
		}
	}

	/**
	 * Called whenever a player finds a diamond. Handles the win check
	 * when the player collects all the diamonds within the level.
	 */
	public void foundDiamond() {
		diamonds++;
		Level level = playing.getLevelManager().getCurrentLevel();
		if(diamonds >= level.getTotalDiamonds()) {
			level.complete();
			playing.getSoundManager().stopSong();
			playing.getSoundManager().playSFX(Sound.LVL_COMPLETE);
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
		else if(boosted) {
			boosted = false;
			playing.getSoundManager().playSFX(Sound.DEBUFF);
		}

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
		Level level = playing.getLevelManager().getCurrentLevel();
		if(level == null)return;
		int currentXPos = (int)x;
		int diff = currentXPos - level.getOffsetX();

		if(diff > level.RT_BORDER)
			level.addToOffsetX(diff - level.RT_BORDER);
		else if(diff < level.LT_BORDER)
			level.addToOffsetX(diff - level.LT_BORDER);
	}

	/**
	 * Checks if player is standing in still water.
	 * If they are, they will drown instantly.
	 */
	private void checkInWater() {
		Level level = playing.getLevelManager().getCurrentLevel();
		LevelLayer layer = LevelLayer.WATER;
		if(level == null || isDying() || !active)return;

		int yPos = (int) (y + h);
		int indexLT = level.getTileIndex(layer, (int)x, yPos, true);
		int indexRT = level.getTileIndex(layer, (int)(x + w), yPos, true);
		int[] waterTiles = new int[] {0, 4, 8}; /* the still water tiles that will drown the player */

		for(int wt : waterTiles) {
			if(indexLT == wt || indexRT == wt) {
				if(active)playing.getSoundManager().playSFX(Sound.WATER);
				damage(100);
				break;
			}
		}
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
