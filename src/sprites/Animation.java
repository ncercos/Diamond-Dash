package sprites;

import game.Game;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Written by Nicholas Cercos
 * Created on Oct 07 2023
 **/
public class Animation {

	private Image[] images;
	private final int size, duration;
	private int current, delay;

	public Animation(String name, int size, int duration) {
		this.size = size;
		this.duration = duration;
		delay = duration;

		try {
			loadImages(name);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Loads all the frames for an animation.
	 *
	 * @param name The filePath for the animation sprite.
	 * @throws IOException If the file cannot be accessed/found.
	 */
	public void loadImages(String name) throws IOException {
		BufferedImage sprite = ImageIO.read(new File(Game.RESOURCE_URL + name + ".png"));
		final int WIDTH = sprite.getWidth() / size;
		images = new Image[WIDTH];

		for(int i = 0; i < WIDTH; i++)
			images[i] = sprite.getSubimage(i * size, 0, size, size);

	}

	/**
	 * @return The default sprite for still frame.
	 */
	public Image getStaticImage() {
		return images != null ? images[0] : null;
	}

	/**
	 * @return The current image within the animation.
	 */
	public Image getCurrentImage() {
		if(images == null) return null;
		delay--;
		if(delay ==0) {
			current++;
			if(current == images.length) current = 0;
			delay = duration;
		}
		return images[current];
	}
}
