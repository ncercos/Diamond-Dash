package levels;

import game.Game;
import sprites.Animation;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import static game.Game.*;

/**
 * Written by Nicholas Cercos
 * Created on Oct 08 2023
 **/
public class LevelManager {

	// Level Data
	private final Game game;
	private final Map<LevelStyle, BufferedImage[]> backgrounds;
	private final Map<LevelStyle, Image[]> foregroundTiles;
	private Image[] midGroundTiles, floraTiles, decorTiles;
	private Level currentLevel;

	public int MAX_TILES_PER_SHEET;

	private final Map<LevelLayer, TileAnimations> animations;

	public LevelManager(Game game) {
		this.game = game;
		this.foregroundTiles = new HashMap<>();
		this.backgrounds = new HashMap<>();
		this.animations = new HashMap<>();

		try {
			loadAllLevelResources();
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
	public void loadAllLevelResources() throws IOException {
		for(LevelStyle style : LevelStyle.values()) {
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

			// Load tiles
			final String TILE_DIRECTORY = RESOURCE_URL + "tiles/";
			BufferedImage tileSheet = ImageIO.read(new File(TILE_DIRECTORY + style.getFileName()));
			MAX_TILES_PER_SHEET = (tileSheet.getWidth() / TILES_DEFAULT_SIZE) * (tileSheet.getHeight() / TILES_DEFAULT_SIZE);
			foregroundTiles.put(style, importTiles(tileSheet));
			decorTiles = importTiles(ImageIO.read(new File(TILE_DIRECTORY + "decor.png")));
			midGroundTiles = importTiles(ImageIO.read(new File(TILE_DIRECTORY + "midground.png")));
			floraTiles = importTiles(ImageIO.read(new File(TILE_DIRECTORY + "flora.png")));
		}

		animations.put(LevelLayer.ITEMS, new TileAnimations(new String[]{
				"gold",
				"diamond" },
				new int[]{ 110, 25 }));
		animations.put(LevelLayer.WATER, new TileAnimations(new String[]{
				"water/some_bubbles",
				"water/many_bubbles",
				"water/no_bubbles" },
				new int[]{ 125, 100, 150 }));
	}

	/**
	 * Takes a sprite sheet and cuts out each tile and places
	 * them in an array.
	 *
	 * @param sprites The sprite sheet(s) in which tiles will be imported from.
	 * @return An array of tile images.
	 */
	private Image[] importTiles(BufferedImage... sprites) {
		List<Image> tiles = new ArrayList<>();
		for(BufferedImage sprite : sprites) {
			final int WIDTH = sprite.getWidth() / TILES_DEFAULT_SIZE;
			final int HEIGHT = sprite.getHeight() / TILES_DEFAULT_SIZE;

			for(int h = 0; h < HEIGHT; h++)
				for(int w = 0; w < WIDTH; w++)
					tiles.add(sprite.getSubimage(w * TILES_DEFAULT_SIZE, h * TILES_DEFAULT_SIZE, TILES_DEFAULT_SIZE, TILES_DEFAULT_SIZE));
		}
		return tiles.toArray(tiles.toArray(new Image[0]));
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
	 * Get a tile based on different factors.
	 *
	 * @param style The style of the level.
	 * @param layer The specific layer it is drawn for.
	 * @param index The index of the tile within the sprite sheet.
	 * @return An image object, if it exists.
	 */
	public Image getTile(LevelStyle style, LevelLayer layer, int index) {
		if(layer.equals(LevelLayer.FOREGROUND)) 		 return getForegroundTile(style, index);
		else if(layer.equals(LevelLayer.MIDGROUND))  return getMidGroundTile(index);
		else if(layer.equals(LevelLayer.FLORA))      return getFloraTile(index);
		else                                         return getDecorTile(index);
	}

	/**
	 * Obtain a foreground tile for a specific style.
	 *
	 * @param style The style of tiles being used.
	 * @param index The index of the tile within the sprite sheet.
	 * @return An image object, if it exists.
	 */
	private Image getForegroundTile(LevelStyle style, int index) {
		Image[] images = foregroundTiles.getOrDefault(style, null);
		if(images == null || (index >= images.length - 1))return null;
		return images[index];
	}

	/**
	 * Obtain a specific midground tile image.
	 * These are placed between the foreground and background.
	 *
	 * @param index The index of the tile within the sprite sheet.
	 * @return An image object, if it exists.
	 */
	private Image getMidGroundTile(int index) {
		if(index >= midGroundTiles.length - 1)return null;
		return midGroundTiles[index];
	}

	/**
	 * Obtain a specific flora tile image.
	 * These are placed between the foreground and midground.
	 *
	 * @param index The index of the tile within the sprite sheet.
	 * @return An image object, if it exists.
	 */
	private Image getFloraTile(int index) {
		if(index >= floraTiles.length - 1)return null;
		return floraTiles[index];
	}

	/**
	 * Obtain a specific decor tile image.
	 * These are placed between the flora and foreground.
	 *
	 * @param index The index of the tile within the sprite sheet.
	 * @return An image object, if it exists.
	 */
	private Image getDecorTile(int index) {
		if(index >= decorTiles.length - 1)return null;
		return decorTiles[index];
	}

	/**
	 * Get the animation of a dynamic tile.
	 *
	 * @param layer The layer the tile is located in.
	 * @param index The tile index within its sheet.
	 * @return An animation object, if it exists.
	 */
	public Animation getTileAnimation(LevelLayer layer, int index) {
		TileAnimations ta = animations.getOrDefault(layer, null);
		if(ta == null)return null;
		return ta.getAnimations().getOrDefault(index, null);
	}

	/**
	 * Loads a level's property file which contains all its data.
	 *
	 * @param path The path to the level file.
	 * @return A loaded properties file, so long as it exists.
	 */
	private Properties getLevelPropertyFile(String path) {
		path = path.concat(".properties");
		File levelFile = new File(path);
		Properties properties = new Properties();
		if(levelFile.exists()) {
			try {
				properties.load(new FileInputStream(path));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return properties;
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
		Properties levelProperties = getLevelPropertyFile(RESOURCE_URL + "levels/" + id);
		Map<LevelLayer, int[][]> data = new HashMap<>();
		for(LevelLayer layer : LevelLayer.values())
			data.put(layer, parseLevelData(levelProperties, layer));
		return new Level(this, id, style, data);
	}

	/**
	 * Takes a string of level data and makes it useful.
	 *
	 * @param properties The properties file of the level.
	 * @param layer      The layer that will be parsed.
	 * @return A 2D array of tile index for a specific layer.
	 */
	private int[][] parseLevelData(Properties properties, LevelLayer layer) {
		String data = properties.getProperty(layer.getName());
		int offset = Integer.parseInt(properties.getProperty(layer.getName() + "-offset"));
		String[] rows = data.split(";");

		int[][] levelData = null;
		for(int h = 0; h < rows.length; h++) {
			String[] columns = rows[h].split(",");
			for(int w = 0; w < columns.length; w++) {
				if(levelData == null) levelData = new int[rows.length][columns.length];
				int value = Integer.parseInt(columns[w].trim());
				if(value > 0) value -= offset;
				else if(value == 0 && !layer.equals(LevelLayer.FOREGROUND)) value = -1;
				levelData[h][w] = value;
			}
		}
		return levelData;
	}

	public Level getCurrentLevel() {
		return currentLevel;
	}

	public Game getGame() {
		return game;
	}

	class TileAnimations {

		String[] files;
		int[] durations;

		Map<Integer, Animation> animations;

		public TileAnimations(String[] files, int[] durations) {
			this.files = files;
			this.durations = durations;
			this.animations = new HashMap<>();

			for(int i = 0; i < files.length; i++) {
				Animation animation = new Animation("tiles/animations/" + files[i], TILES_DEFAULT_SIZE, durations[i]);
				animations.put(i * animation.getImages().length, animation);
			}
		}

		public Map<Integer, Animation> getAnimations() {
			return animations;
		}
	}
}
