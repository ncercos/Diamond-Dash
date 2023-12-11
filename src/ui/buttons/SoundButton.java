package ui.buttons;

import game.Game;
import ui.Button;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Written by Nicholas Cercos
 * Created on Nov 27 2023
 **/
public class SoundButton extends Button {

	private BufferedImage[][] buttonImgs;
	private boolean muted;
	private int rowIndex;
	private final boolean music;

	// Dimensions
	private static final int DEFAULT_SIZE = 42;
	private static final int SIZE = (int) (DEFAULT_SIZE * Game.SCALE) / 2;

	public SoundButton(int x, int y, boolean music) {
		super(x, y, SoundButton.SIZE, SoundButton.SIZE);
		this.music = music;
		loadButtonImgs();
	}

	/**
	 * Loads all individual sound buttons with their animation.
	 */
	private void loadButtonImgs() {
		try {
			BufferedImage sheet = ImageIO.read(new File(Game.RESOURCE_URL + "ui/sound_buttons.png"));
			buttonImgs = new BufferedImage[2][3];
			for(int h = 0; h < buttonImgs.length; h++) {
				for(int w = 0; w < buttonImgs[h].length; w++) {
					buttonImgs[h][w] = sheet.getSubimage(w * DEFAULT_SIZE, h * DEFAULT_SIZE, DEFAULT_SIZE, DEFAULT_SIZE);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Update the button's animation and texture based on mouse action.
	 * Shows whether sound is enabled or disabled.
	 */
	@Override
	public void update() {
		super.update();
		if(muted) rowIndex = 1;
		else      rowIndex = 0;
	}

	public void draw(Graphics g) {
		g.drawImage(buttonImgs[rowIndex][currentIndex], (int) x, (int) y, (int) w, (int) h, null);
	}

	public boolean isMuted() {
		return muted;
	}

	public void setMuted(boolean muted) {
		this.muted = muted;
	}

	public boolean isMusic() {
		return music;
	}
}
