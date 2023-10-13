package levels;

import game.Game;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static game.Game.*;

/**
 * Written by Nicholas Cercos
 * Created on Oct 08 2023
 **/
public class LevelManager {

	// Level Data
	private final Game game;
	private final Map<LevelStyle, Image[]> tiles;
	private final Map<LevelStyle, BufferedImage[]> backgrounds;
	private Level currentLevel;

	public LevelManager(Game game) {
		this.game = game;
		this.tiles = new HashMap<>();
		this.backgrounds = new HashMap<>();

		try {
			loadResourcesForStyles();
			currentLevel = loadLevel(1, LevelStyle.LUSH);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Renders active level to screen.
	 *
	 * @param g The graphics context.
	 */
	public void draw(Graphics g) {
		if(currentLevel == null)return;
		currentLevel.draw(g);
	}

	/**
	 * Load tiles & backgrounds for all level styles and
	 * keeps them in memory to easily build worlds.
	 *
	 * @throws IOException If the image could not be accessed/found.
	 */
	public void loadResourcesForStyles() throws IOException {
		for(LevelStyle style : LevelStyle.values()) {
			// Load tiles
			BufferedImage sprite = ImageIO.read(new File(RESOURCE_URL + "tiles/" + style.getFileName()));
			final int WIDTH = sprite.getWidth() / TILES_DEFAULT_SIZE;
			final int HEIGHT = sprite.getHeight() / TILES_DEFAULT_SIZE;
			Image[] tiles = new Image[WIDTH * HEIGHT];

			for(int h = 0; h < HEIGHT; h++) {
				for(int w = 0; w < WIDTH; w++) {
					int index = h * WIDTH + w;
					tiles[index] = sprite.getSubimage(w * TILES_DEFAULT_SIZE, h * TILES_DEFAULT_SIZE, TILES_DEFAULT_SIZE, TILES_DEFAULT_SIZE);
				}
			}
			this.tiles.put(style, tiles);

			// Load background
			String backgroundDirectory = Game.RESOURCE_URL + "backgrounds/" + style.getName() + "/";
			backgrounds.put(style, new BufferedImage[] {
					ImageIO.read(new File(backgroundDirectory + "sky.png")),
					ImageIO.read(new File(backgroundDirectory + "shadow.png")),
					ImageIO.read(new File(backgroundDirectory + "mountains_fog_large.png")),
					ImageIO.read(new File(backgroundDirectory + "mountains_large.png")),
					ImageIO.read(new File(backgroundDirectory + "mountains_fog_small.png")),
					ImageIO.read(new File(backgroundDirectory + "mountains_small.png"))
			});
		}
	}

	/**
	 * @param style The style of the level.
	 * @return A background image based on the current level's style.
	 */
	public BufferedImage getBackgroundImage(LevelStyle style) {
		return backgrounds.getOrDefault(style, null)[0];
	}

	/**
	 * @param style The style of the level.
	 * @return A mountain shadow image based on the current level's style the level background.
	 */
	public BufferedImage getMountainShadowImage(LevelStyle style) {
		return backgrounds.getOrDefault(style, null)[1];
	}

	/**
	 * Obtain the mountain image for the background based on the current level's style.
	 *
	 * @param style The style of the level.
	 * @param large Do you want the large or small mountains?
	 * @param foggy Do you want foggy or clear skies?
	 * @return An image object, if it exists.
	 */
	public BufferedImage getMountainImage(LevelStyle style, boolean large, boolean foggy) {
		return backgrounds.getOrDefault(style, null)[large ? (foggy ? 2 : 3) : (foggy ? 4 : 5)];
	}

	/**
	 * Obtain the tiles for a specific style.
	 *
	 * @param style The style of tiles being used.
	 * @return A set of images from the sprite sheet, if it exists.
	 */
	public Image[] getTiles(LevelStyle style) {
		return tiles.getOrDefault(style, null);
	}

	/**
	 * Determine the tiles that will be used for a given level
	 * based on the RED color value of the pixel within the
	 * levels image and saves it to memory as a Level object.
	 *
	 * @return A Level object containing the tile type at a given x and y coordinate.
	 * @throws IOException If the image cannot be accessed/found.
	 */
	private Level loadLevel(int id, LevelStyle style) throws IOException {
		BufferedImage image = ImageIO.read(new File(RESOURCE_URL + "levels/" + id + ".png"));
		int[][] data = new int[image.getHeight()][image.getWidth()];

		for(int h = 0; h < image.getHeight(); h++) {
			for(int w = 0; w < image.getWidth(); w++) {
				Color color = new Color(image.getRGB(w, h));
				int value = color.getRed();
				data[h][w] = (value >= getTiles(style).length ? 0 : value);
			}
		}
		return new Level(this, id, style, data);
	}

	public Level getCurrentLevel() {
		return currentLevel;
	}
}
