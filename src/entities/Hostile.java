package entities;

import game.Game;
import game.states.Playing;
import sounds.Sound;
import sprites.Pose;

/**
 * Written by Nicholas Cercos
 * Created on Nov 29 2023
 **/
public abstract class Hostile extends Entity {

	private int tileY;
	protected int attackDistance;
	protected int sightDistance;
	protected int attackDelay;

	protected boolean facingLeft;

	public Hostile(Playing playing, String name, double x, double y, double w, double h, int spriteWidth, double xDrawOffset, double yDrawOffset, int maxHealth) {
		super(playing, name, x, y, w * Game.SCALE, h * Game.SCALE,
				spriteWidth, xDrawOffset, yDrawOffset, maxHealth);
		attackDelay = 0;
		setAttackDistance((int)w);
		setMaxHealth(maxHealth);
		reset();
	}

	@Override
	public void update() {
		super.update();
		drop();
		if(attackDelay > 0)
			attackDelay--;
	}

	@Override
	public boolean attack(Entity entity) {
		if(super.attack(entity)) {
			resetAttackDelay();
			return true;
		}
		return false;
	}

	@Override
	public void damage(int damage) {
		super.damage(damage);
		if(currentPose.equals(Pose.DIE))
			playing.getSoundManager().playSFX(Sound.DEATH);
	}

	/**
	 * Calls the native move method which uses physics
	 * to push the player down to solid ground.
	 */
	private void drop() {
		if(inAir) {
			move();
			tileY = getTileY();
		}
	}

	/**
	 * Sets the attack distance for a hostile mob.
	 * The sight distance will always be 3x more than attack.
	 *
	 * @param attackDistance The new distance the entity will attack within.
	 * @param sightMulti     The multiplier to calculate sight distance from the attack distance.
	 */
	public void setAttackDistance(int attackDistance, int sightMulti) {
		this.attackDistance = attackDistance;
		this.sightDistance = attackDistance * sightMulti;
	}

	public void setAttackDistance(int attackDistance) {
		setAttackDistance(attackDistance, 3);
	}

	/**
	 * Resets the attack delay so that the enemy has to wait in-between attacks.
	 */
	public void resetAttackDelay() {
		attackDelay = 60;
	}

	public boolean isAbleToAttack() {
		return attackDelay <= 0;
	}

	/**
	 * Determines if a hostile mob has line of sight
	 * to a player.
	 *
	 * @param player The player object.
	 * @return True if player within range and accessible.
	 */
	public boolean isInSight(Player player) {
		if(player.getTileY() == tileY && !playing.getLevelManager().getCurrentLevel().isComplete()) {
			if(getDistanceFrom(player) <= sightDistance)
				return isWalkable(player) && player.isActive() && !player.isDying();
		}
		return false;
	}

	public boolean isInAttackRange(Player player) {
		return !playing.getLevelManager().getCurrentLevel().isComplete() && getDistanceFrom(player) <= attackDistance;
	}

	/**
	 * Checks the bottom left and right corners of a player
	 * and determines if that tile is solid.
	 *
	 * @param player The player object.
	 * @return True if the path between a player and enemy is not obstructed.
	 */
	private boolean isWalkable(Player player) {
		int tileX = getTileX();
		int pTileX;

		if(getLevel().isSolid(player.getX(), player.getY() + player.getHeight() + 1))
			pTileX = player.getTileX();
		else pTileX = (int) ((player.getX() + player.getWidth()) / Game.TILES_SIZE);

		if(tileX < pTileX) return isPathClear(tileX, pTileX);
		else 							 return isPathClear(pTileX, tileX);
	}

	/**
	 * Checks all tiles between two points and
	 * determines if they are solid.
	 *
	 * @param xStart The starting x-tile.
	 * @param xEnd   The ending x-tile.
	 * @return True if there are no obstructions, otherwise, false.
	 */
	private boolean isPathClear(int xStart, int xEnd) {
		for (int i = 0; i < xEnd - xStart; i++) {
			if (getLevel().isSolid(xStart + i, tileY, false))
				return false;
			if (!getLevel().isSolid(xStart + i, tileY + 1, false))
				return false;
		}
		return true;
	}

	/**
	 * Finds the distance between a hostile mob
	 * and a player.
	 *
	 * @param player The player object.
	 * @return An integer distance. (will never be negative)
	 */
	protected double getDistanceFrom(Player player) {
		return Math.abs(player.getX() - x);
	}

	/**
	 * Faces the goblin in the correct direction
	 * based on the location of the player.
	 *
	 * @param player The player object.
	 */
	protected void turnTowards(Player player) {
		facingLeft = player.getX() < x;
	}
}
