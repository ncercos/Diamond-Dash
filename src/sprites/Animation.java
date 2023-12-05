package sprites;

import game.Game;
import game.states.Playing;

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
	private int size;
	private final int duration;
	private int current, delay;
	private boolean cycleCompleted;
	private boolean frozen = false, repeatable = true;

	public Animation(String name, int size, int duration) {
		this.size = size;
		this.duration = duration;
		delay = duration;

		try {
			images = loadImages(name);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Animation(Image[] images, int duration, boolean frozen) {
		this.images = images;
		this.duration = duration;
		this.frozen = frozen;
		delay = duration;
	}

	/**
	 * Starts animation from the beginning.
	 */
	public void reset() {
		this.current = 0;
		this.delay = duration;
		cycleCompleted = false;
	}

	/**
	 * Loads all the frames for an animation.
	 *
	 * @param name The filePath for the animation sprite.
	 * @throws IOException If the file cannot be accessed/found.
	 */
	public Image[] loadImages(String name) throws IOException {
		BufferedImage sprite = ImageIO.read(new File(Game.RESOURCE_URL + name + ".png"));
		final int WIDTH = sprite.getWidth() / size;
		Image[] images = new Image[WIDTH];

		for(int i = 0; i < WIDTH; i++)
			images[i] = sprite.getSubimage(i * size, 0, size, size);
		return images;
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
	public Image getCurrentImage(Playing playing) {
		if(images == null) return null;
		if(frozen) return getStaticImage();

		if(!playing.isPaused()) delay--;
		if(delay == 0) {
			current++;
			if(current == images.length) {
				current = repeatable ? 0 : (images.length - 1);
				cycleCompleted = true;
			}
			delay = duration;
		}
		return images[current];
	}

	/**
	 * Sets the number of cycles for an animation.
	 *
	 * @param repeatable Should the animation cycle continuously?
	 * @return This animation object.
	 */
	public Animation setRepeatable(boolean repeatable) {
		this.repeatable = repeatable;
		return this;
	}

	/**
	 * @return True if all animation sprites have been shown at least once.
	 */
	public boolean isCycleCompleted() {
		return cycleCompleted;
	}

	public boolean isFrozen() {
		return frozen;
	}

	public Animation setFrozen(boolean frozen) {
		this.frozen = frozen;
		return this;
	}

	public int getCurrentIndex() {
		return current;
	}

	public Image[] getImages() {
		return images;
	}
}
