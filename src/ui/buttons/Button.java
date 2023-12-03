package ui.buttons;

import utils.Hitbox;

import java.awt.*;

/**
 * Written by Nicholas Cercos
 * Created on Nov 27 2023
 **/
public abstract class Button extends Hitbox {

	protected boolean mouseOver, mousePressed;
	protected int currentIndex;

	public Button(int x, int y, int w, int h) {
		super(x, y, w, h);
	}

	/**
	 * Any attributes that need to update alongside
	 * the game loop. (Button Animations)
	 */
	public void update() {
		 								 currentIndex = 0;
		if(mouseOver)    currentIndex = 1;
		if(mousePressed) currentIndex = 2;
	}

	/**
	 * Draws button to screen.
	 *
	 * @param g The graphics context.
	 */
	public abstract void draw(Graphics g);

	/**
	 * Resets button attributes.
	 */
	public void reset() {
		mouseOver = false;
		mousePressed = false;
	}

	public void setMouseOver(boolean mouseOver) {
		this.mouseOver = mouseOver;
	}

	public boolean isMousePressed() {
		return mousePressed;
	}

	public void setMousePressed(boolean mousePressed) {
		this.mousePressed = mousePressed;
	}
}
