package entities;

import game.Game;

/**
 * Written by Nicholas Cercos
 * Created on Nov 29 2023
 **/
public abstract class Hostile extends Entity {

	private int tileY;
	private int attackDistance;
	private int sightDistance;

	public Hostile(Game game, String name, double x, double y, double w, double h, int spriteWidth, double xDrawOffset, double yDrawOffset) {
		super(game, name, x, y, w, h, spriteWidth, xDrawOffset, yDrawOffset);
		setAttackDistance(Game.TILES_SIZE / 2);
	}

	@Override
	public void update() {
		super.update();
		drop();
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
	 */
	public void setAttackDistance(int attackDistance) {
		this.attackDistance = attackDistance;
		this.sightDistance = attackDistance * 3;
	}

	/**
	 * Determines if a hostile mob has line of sight
	 * to a player.
	 *
	 * @param player The player object.
	 * @return True if player within range and accessible.
	 */
	protected boolean isInSight(Player player) {
		if(player.getTileY() == tileY) {
			if(getDistanceFrom(player) <= sightDistance)
				return isWalkable(player);
		}
		return false;
	}

	protected boolean isInAttackRange(Player player) {
		return getDistanceFrom(player) <= attackDistance;
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
		for(int i = 0; i < xEnd - xStart; i++) {
			if(getLevel().isSolid(xStart + i, tileY))
				return false;
			if(!getLevel().isSolid(xStart + i, tileY + 1))
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
	private int getDistanceFrom(Player player) {
		return (int) Math.abs(player.getX() - x);
	}
}
