package sprites;

import java.awt.*;

/**
 * Written by Nicholas Cercos
 * Created on Oct 07 2023
 **/
public class Animation {

	private final Image[] images;
	private final int duration;
	private int current, delay;

	public Animation(String name, int count, int duration) {
		this.duration = duration;
		delay = duration;
		images = new Image[count];
		for(int i = 0; i < count; i++)
			images[i] = Toolkit.getDefaultToolkit().getImage("./res/" + name + "_" + i + ".png");
	}

	/**
	 * @return The default sprite for still frame.
	 */
	public Image getStaticImage() {
		return images[0];
	}

	/**
	 * @return The current image within the animation.
	 */
	public Image getCurrentImage() {
		delay--;
		if(delay ==0) {
			current++;
			if(current == images.length) current = 0;
			delay = duration;
		}
		return images[current];
	}
}
