package ui.buttons;

import game.Game;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Written by Nicholas Cercos
 * Created on Nov 27 2023
 **/
public class VolumeButton extends Button {

	private BufferedImage[] buttons;
	private BufferedImage slider;
	private int bX;
	private final int minX, maxX;

	// Dimensions
	private static final int DEFAULT_WIDTH = 28;
	private static final int DEFAULT_HEIGHT = 44;
	private static final int WIDTH = (int) (DEFAULT_WIDTH * Game.SCALE) / 2;
	private static final int HEIGHT = (int) (DEFAULT_HEIGHT * Game.SCALE) / 2;

	private static final int DEFAULT_SLIDER_WIDTH = 215;
	private static final int SLIDER_WIDTH = (int) (DEFAULT_SLIDER_WIDTH * Game.SCALE) / 2;
	private static final int SLIDER_CENTER = SLIDER_WIDTH / 2;

	public VolumeButton(int x, int y) {
		super(x, y, WIDTH, HEIGHT);
		hitbox.x = x + SLIDER_CENTER;
		bX   =  x + SLIDER_CENTER;
		minX =  x + (WIDTH / 2);
		maxX = (x + SLIDER_WIDTH) - (WIDTH / 2);
		loadSprites();
	}

	/**
	 * Loads the 3 different volume button animations
	 * and the slider separately.
	 */
	private void loadSprites() {
		try {
			BufferedImage sheet = ImageIO.read(new File(Game.RESOURCE_URL + "ui/volume_buttons.png"));
			buttons = new BufferedImage[3];
			for(int i = 0; i < buttons.length; i++)
				buttons[i] = sheet.getSubimage(i * DEFAULT_WIDTH, 0, DEFAULT_WIDTH, DEFAULT_HEIGHT);
			slider = sheet.getSubimage(buttons.length * DEFAULT_WIDTH, 0, DEFAULT_SLIDER_WIDTH, DEFAULT_HEIGHT);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Moves the volume button position and hitbox.
	 *
	 * @param x The new x-coordinate.
	 */
	public void move(int x) {
		if(x < minX) 			bX = minX;
		else bX = Math.min(x, maxX);
		hitbox.x = bX - WIDTH / 2;
	}

	@Override
	public void draw(Graphics g) {
		g.drawImage(slider, x, y, SLIDER_WIDTH, h, null);
		g.drawImage(buttons[currentIndex], bX - w / 2, y, w, h, null);
	}
}
