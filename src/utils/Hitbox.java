package utils;

import game.Game;

import java.awt.*;

/**
 * Written by Nicholas Cercos
 * Created on Dec 03 2023
 **/
public class Hitbox {

	protected double x, y;
	protected final double w, h;
	protected final double xDrawOffset, yDrawOffset;
	protected boolean debug;

	public Hitbox(double x, double y, double w, double h, double xDrawOffset, double yDrawOffset) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.xDrawOffset = xDrawOffset * Game.SCALE;
		this.yDrawOffset = yDrawOffset * Game.SCALE;
		debug = false;
	}

	public Hitbox(double x, double y, double w, double h) {
		this(x, y, w, h, 0, 0);
	}

	/**
	 * Draws the hitbox for debugging purposes.
	 *
	 * @param g         The graphics context.
	 * @param lvlOffset The offset for the level camera.
	 */
	public void draw(Graphics g, int lvlOffset) {
		if(debug) {
			g.setColor(Color.PINK);
			g.drawRect((int)x - lvlOffset, (int)y, (int)w, (int)h);
		}
	}

	public void draw(Graphics g) {
		draw(g, 0);
	}

	/**
	 * Determines if two hitboxes are overlapping.
	 *
	 * @param hb The other hitbox in question.
	 * @return True if both hitboxes are colliding.
	 */
	public boolean overlaps(Hitbox hb) {
		return (x     <= hb.x + hb.w) &&
				   (x + w >= hb.x      ) &&
				   (y     <= hb.y + hb.h) &&
				   (y + h >= hb.y      );
	}

	/**
	 * Checks if the location of a mouse is within
	 * the constraints of the hitbox.
	 *
	 * @param mx The x-coordinate.
	 * @param my The y-coordinate.
	 * @return True if within the point is within hitbox.
	 */
	public boolean contains(int mx, int my) {
		return (mx >= x  )   &&
				   (mx <= x+w)   &&
				   (my >= y  )   &&
				   (my <= y+h);
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	/**
	 * Teleports hitbox to a specific location.
	 *
	 * @param location The location to teleport to.
	 */
	public void teleport(Location location) {
		x = location.getX();
		y = location.getY();
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

	public double getWidth() {
		return w;
	}

	public double getHeight() {
		return h;
	}
}
