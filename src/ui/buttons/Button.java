package ui.buttons;

import entities.Entity;

import java.awt.*;

/**
 * Written by Nicholas Cercos
 * Created on Nov 27 2023
 **/
public abstract class Button extends Entity {

	protected boolean mouseOver, mousePressed;
	protected int currentIndex;

	public Button(int x, int y, int w, int h, int xDrawOffset, int yDrawOffset) {
		super(x, y, w, h, xDrawOffset, yDrawOffset);
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

	public boolean isMouseOver() {
		return mouseOver;
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
