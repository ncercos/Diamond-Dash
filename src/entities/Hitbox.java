package entities;

import game.Game;
import levels.Level;

import java.awt.*;

/**
 * Written by Nicholas Cercos
 * Created on Oct 04 2023
 **/
public class Hitbox {

	/* Location & Size */
	protected double x, y;
	protected final double w, h;

	/* Physics */
	private static final double GRAVITY = 0.15 * Game.SCALE;
	private static final double MAX_FALL_VELOCITY = 2.0 * Game.SCALE;
	private double vx, vy;
	protected boolean moving, inAir = true;

	public Hitbox(double x, double y, double w, double h) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
	}

	// Movement

	/**
	 * Modifies the position of the rectangle
	 * by its given velocity.
	 */
	public void move(Level level) {
		if(!moving && !inAir)return;

		if(!inAir)
			if(!isOnTile(level))
				inAir = true;

		if(inAir) {
			if(canMoveTo(x, y + vy, level)) {
				y += vy;
				if(vy < MAX_FALL_VELOCITY)
					vy += GRAVITY;
				updateXPos(level);
			} else {
				y = getYPosAboveOrUnderTile();
				if(vy > 0) stopFalling();
				else vy = GRAVITY;
				updateXPos(level);
			}
		} else updateXPos(level);
	}

	/**
	 * Updates the current x-position to a location
	 * based on the constraints of the level.
	 *
	 * @param level The current level.
	 */
	private void updateXPos(Level level) {
		if(canMoveTo(x + vx, y, level)) x += vx;
		else x = getXPosNextToTile();
		applyFrictionWithFloor();
	}

	/**
	 * @return The x-coordinate closest to an adjacent tile, horizontally.
	 */
	private double getXPosNextToTile() {
		int currentTileIndex = (int) (x / Game.TILES_SIZE);
		int tileXPos = currentTileIndex * Game.TILES_SIZE;
		if(vx > 0) { /* moving to the right */
			int xOffset = (int)(Game.TILES_SIZE - w);
			return tileXPos + xOffset - 1;
		} else return tileXPos;
	}

	/**
	 * @return The y-coordinate closest to an adjacent tile, vertically.
	 */
	private double getYPosAboveOrUnderTile() {
		int currentTileIndex = (int) (y / Game.TILES_SIZE);
		int tileYPos = currentTileIndex * Game.TILES_SIZE;
		if(vy > 0) { /* falling */
			int yOffset = (int)(Game.TILES_SIZE - h);
			return tileYPos + yOffset - 1;
		} else return tileYPos;
	}

	/**
	 * Moves the rectangle to the left.
	 *
	 * @param dx The delta-x determines how far it will move horizontally.
	 */
	public void goLT(double dx) {
		vx = (int) (-dx * Game.SCALE);
		moving = true;
	}

	public void goRT(double dx) {
		vx = (int) (dx * Game.SCALE);
		moving = true;
	}

	/**
	 * Moves the rectangle up.
	 *
	 * @param dy The delta-y determines how far it will move vertically.
	 */
	public void goUP(double dy) {
		if(inAir)return;
		vy = (int) (-dy * Game.SCALE);
		moving = true;
		inAir = true;
	}

	/**
	 * Prevents infinite velocity glitch & stops
	 * horizontal movement from gravity on ground.
	 *
	 */
	public void applyFrictionWithFloor() {
		vx = 0;
	}

	/**
	 * Prevents infinite fall glitch due to gravity.
	 */
	public void stopFalling() {
		inAir = false;
		vy = 0;
	}

	/**
	 * Sets all velocity variables to their default; zero.
	 */
	public void resetVelocity() {
		vx = 0;
		vy = 0;
	}

	// Collision

	/**
	 * Checks if this can move to a new location based on
	 * the current game level.
	 *
	 * @param x 	  The new x-coordinate.
	 * @param y 	  The new y-coordinate.
	 * @param level The current level.
	 * @return True if the move is possible, otherwise, false.
	 */
	public boolean canMoveTo(double x, double y, Level level) {
		if(!level.isSolid(x, y))
			if(!level.isSolid(x + w, y + h))
				if(!level.isSolid(x + w, y))
					return !level.isSolid(x, y + h);
		return false;
	}

	/**
	 * Determines if this rectangle is standing on
	 * a solid tile.
	 *
	 * @param level The current level.
	 * @return True if tile below is solid.
	 */
	private boolean isOnTile(Level level) {
		if(!level.isSolid(x, y + h + 1))
			return level.isSolid(x + w, y + h + 1);
		return true;
	}

	/**
	 * Draws to the screen.
	 *
	 * @param g The graphics context.
	 */
	public void draw(Graphics g, Level level) {
		g.setColor(Color.BLACK);
		g.drawRect((int)x - level.getOffsetX(), (int)y, (int)w, (int)h);
	}
}
