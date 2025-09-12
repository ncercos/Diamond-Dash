package ui.buttons;

import game.Game;
import ui.Button;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Written by Nicholas Cercos
 * Created on Nov 27 2023
 **/
public class VolumeButton extends Button {

	private BufferedImage[] buttons;
	private BufferedImage slider;
	private int bX;
	private final int minX, maxX, sX;
	private float value = 1f;

	// Dimensions
	private static final int DEFAULT_BUTTON_WIDTH = 28;
	private static final int DEFAULT_BUTTON_HEIGHT = 44;
	private static final int BUTTON_WIDTH = (int) (DEFAULT_BUTTON_WIDTH * Game.SCALE) / 2;
	private static final int BUTTON_HEIGHT = (int) (DEFAULT_BUTTON_HEIGHT * Game.SCALE) / 2;
	private static final int BUTTON_CENTER = BUTTON_WIDTH / 2;

	private static final int DEFAULT_SLIDER_WIDTH = 215;
	private static final int SLIDER_WIDTH = (int) (DEFAULT_SLIDER_WIDTH * Game.SCALE) / 2;

	public VolumeButton(int sX, int y) {
		super((sX + SLIDER_WIDTH) - BUTTON_CENTER, y, BUTTON_WIDTH, BUTTON_HEIGHT);
		this.sX = sX;
		minX =  sX + (BUTTON_WIDTH / 2);
		maxX = (sX + SLIDER_WIDTH) - (BUTTON_WIDTH / 2);
		bX   = maxX;
		loadSprites();
	}

	/**
	 * Loads the 3 different volume button animations
	 * and the slider separately.
	 */
	private void loadSprites() {
		BufferedImage sheet = Game.loadSprite("ui/volume_buttons.png");
		if(sheet == null) return;
		buttons = new BufferedImage[3];
		for(int i = 0; i < buttons.length; i++)
			buttons[i] = sheet.getSubimage(i * DEFAULT_BUTTON_WIDTH, 0, DEFAULT_BUTTON_WIDTH, DEFAULT_BUTTON_HEIGHT);
		slider = sheet.getSubimage(buttons.length * DEFAULT_BUTTON_WIDTH, 0, DEFAULT_SLIDER_WIDTH, DEFAULT_BUTTON_HEIGHT);
	}

	/**
	 * Moves the volume button position and hitbox.
	 *
	 * @param x The new x-coordinate.
	 */
	public void move(int x) {
		if(x < minX) 			bX = minX;
		else bX = Math.min(x, maxX);
		this.x = bX - BUTTON_WIDTH / 2.0;
		updateValue();
	}

	private void updateValue() {
		double range = maxX - minX;
		this.value = (float) ((bX - minX) / range);
	}

	public float getValue() {
		return value;
	}

	@Override
	public void draw(Graphics g) {
		g.drawImage(slider, sX, (int) y, SLIDER_WIDTH, (int) h, null);
		g.drawImage(buttons[currentIndex], bX - (int) w / 2, (int) y, (int) w, (int) h, null);
	}
}
