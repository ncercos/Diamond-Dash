package levels;

import entities.Hitbox;
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

	private final Game game;
	private final Map<LevelStyle, Image[]> tiles;
	private Level currentLevel;

	public LevelManager(Game game) {
		this.game = game;
		this.tiles = new HashMap<>();

		try {
			loadAllTiles();
			currentLevel = new Level(this, 1, LevelStyle.NATURE);
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
		for(int h = 0; h < TILES_IN_HEIGHT; h++) {
			for(int w = 0; w < TILES_IN_WIDTH; w++) {
				int index = currentLevel.getTileIndex(w, h);
				int x = w * TILES_SIZE;
				int y = h * TILES_SIZE;
				g.drawImage(getTiles(currentLevel.getStyle())[index], x, y, TILES_SIZE, TILES_SIZE, null);
			}
		}
	}

	/**
	 * Load tiles for all level styles and keep them
	 * in memory to build world.
	 *
	 * @throws IOException If the image could not be accessed/found.
	 */
	public void loadAllTiles() throws IOException {
		for(LevelStyle style : LevelStyle.values()) {
			BufferedImage sprite = ImageIO.read(new File("./res/tiles/" + style.getFileName()));
			final int WIDTH = sprite.getWidth() / TILES_DEFAULT_SIZE;
			final int HEIGHT = sprite.getHeight() / TILES_DEFAULT_SIZE;
			Image[] tiles = new Image[WIDTH * HEIGHT];

			for(int h = 0; h < HEIGHT; h++) {
				for(int w = 0; w < WIDTH; w++) {
					int index = h * WIDTH + w;
					tiles[index] = sprite.getSubimage(w * TILES_DEFAULT_SIZE, h * TILES_DEFAULT_SIZE, TILES_DEFAULT_SIZE, TILES_DEFAULT_SIZE);
				}
			}

			if(tiles.length == 0)continue;
			this.tiles.put(style, tiles);
		}
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
	 * levels image.
	 *
	 * @return A 2D array containing the tile type at a given x and y coordinate.
	 * @throws IOException If the image cannot be accessed/found.
	 */
	public int[][] getLevelData(Level level) throws IOException {
		int[][] lvlData = new int[TILES_IN_HEIGHT][TILES_IN_WIDTH];
		BufferedImage lvlImage =ImageIO.read(new File("./res/levels/" + level.getId() + ".png"));

		for(int h = 0; h < lvlImage.getHeight(); h++) {
			for(int w = 0; w < lvlImage.getWidth(); w++) {
				Color color = new Color(lvlImage.getRGB(w, h));
				int value = color.getRed();
				lvlData[h][w] = (value >= getTiles(level.getStyle()).length ? 0 : value);
				if(level.getStyle().isNonSolid(value)) continue;
				level.addHitbox(new Hitbox(w * TILES_SIZE, h * TILES_SIZE, TILES_SIZE, TILES_SIZE));
			}
		}
		return lvlData;
	}

	public Level getCurrentLevel() {
		return currentLevel;
	}
}
