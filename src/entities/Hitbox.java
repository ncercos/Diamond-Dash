package entities;

import java.awt.*;

/**
 * Written by Nicholas Cercos
 * Created on Oct 04 2023
 **/
public class Hitbox {

	/* Location & Size */
	private double x, y;
	private final double w, h;

	/* Physics */
	private static final double GRAVITY = 0.4;
	private double vx, vy;
	private double ay = GRAVITY;

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
	public void move() {
		x += vx;
		y += vy;
		vy += ay;
	}

	/**
	 * Sets the velocity of the rectangle.
	 *
	 * @param vx The x-velocity.
	 * @param vy The y-velocity.
	 */
	public void setVelocity(int vx, int vy) {
		this.vx = vx;
		this.vy = vy;
	}

	/**
	 * Moves the rectangle to the left.
	 *
	 * @param dx The delta-x determines how far it will move horizontally.
	 */
	public void goLT(int dx) {
		vx = -dx;
	}

	public void goRT(int dx) {
		vx = dx;
	}

	/**
	 * Moves the rectangle up.
	 *
	 * @param dy The delta-y determines how far it will move vertically.
	 */
	public void goUP(int dy) {
		vy = -dy;
	}

	public void goDN(int dy) {
		vy = dy;
	}

	/**
	 * Disables physics by resetting the velocity.
	 */
	public void physicsOFF() {
		setVelocity (0, 0);
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
		vy = 0;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	// Collision

	/**
	 * Collision detection between two rectangles.
	 *
	 * @param r The rectangle involved in collision.
	 * @return True if the given rectangle is within the constraints of this rectangle.
	 */
	public boolean overlaps(Hitbox r) {
		return (x     <= r.x + r.w) &&
				   (x + w >= r.x)       &&
				   (y     <= r.y + r.h) &&
				   (y + h >= r.y);
	}

	/**
	 * Determines if two points are within rectangle constraints.
	 *
	 * @param px The x-value being checked.
	 * @param py The y-value being checked.
	 * @return True if both points are within the rectangle.
	 */
	public boolean contains(int px, int py) {
		return (px >= x)   &&
					 (px <= x+w) &&
					 (py >= y)   &&
					 (py <= y+h);
	}

	/**
	 * Determines if the current rectangle is left of
	 * a different rectangle.
	 *
	 * @param r The rectangle in question.
	 * @return True if on the left side of the given rectangle.
	 */
	public boolean isLeftOf(Hitbox r) {
		return x < r.x - w + 1;
	}

	public boolean isRightOf(Hitbox r) {
		return x > r.w + r.w - 1;
	}

	public boolean isAbove(Hitbox r) {
		return y < r.y - h + 1;
	}

	public boolean isBelow(Hitbox r) {
		return y > r.y + r.h - 1;
	}

	/**
	 * Determines if the previous location was
	 * to the left of the given rectangle.
	 *
	 * @param r The rectangle in question.
	 * @return True if this rectangle was on the left.
	 */
	public boolean wasLeftOf(Hitbox r) {
		return x - vx < r.x - w + 1;
	}

	public boolean wasRightOf(Hitbox r) {
		return x - vx > r.x + r.w - 1;
	}

	public boolean wasAbove(Hitbox r) {
		return y - vy < r.y - h + 1;
	}

	public boolean wasBelow(Hitbox r) {
		return y - vy > r.y + r.h - 1;
	}

	/**
	 * Changes the position of this rectangle to be
	 * adjacent to the given rectangle.
	 *
	 * @param r The rectangle involved in collision.
	 */
	public void pushedOutOf(Hitbox r) {
		if(wasLeftOf(r))  pushLeftOf(r);
		if(wasRightOf(r)) pushRightOf(r);
		if(wasAbove(r))   pushAbove(r);
		if(wasBelow(r))   pushBelow(r);
	}

	public void pushLeftOf(Hitbox r) {
		x = r.x - w - 1;
	}

	public void pushRightOf(Hitbox r) {
		x = r.x + r.w + 1;
	}

	public void pushAbove(Hitbox r) {
		y = r.y - h - 1;
	}

	public void pushBelow(Hitbox r) {
		y = r.y + r.h + 1;
	}

	/**
	 * Checks if this rectangle is standing on another rectangle.
	 *
	 * @param r An array of rectangles.
	 * @return True if this rectangle is "standing" above another rectangle.
	 */
	public boolean standingOnAny(Hitbox[] r) {
		for(int i = 0; i < r.length; i++) {
			if(y + h == r[i].y - 1)
				return true;
		}
		return false;
	}

	public double getWidth() {
		return w;
	}

	public double getHeight() {
		return h;
	}

	/**
	 * Draws to the screen.
	 *
	 * @param g The graphics context.
	 */
	public void draw(Graphics g) {
		g.setColor(Color.BLUE);
		g.drawRect((int)x, (int)y, (int)w, (int)h);
	}
}
