package levels;

import game.Game;
import game.states.Playing;
import sprites.Animation;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.*;
import java.util.List;
import java.util.*;

import static game.Game.TILES_DEFAULT_SIZE;

/**
 * Written by Nicholas Cercos
 * Created on Oct 08 2023
 **/
public class LevelManager {

	private final Playing playing;
	private final Map<LevelStyle, BufferedImage[]> backgrounds;
	private final Map<LevelStyle, Image[]> foregroundTiles;
	public Image[] midGroundTiles, floraTiles, decorTiles;

	public int MAX_TILES_PER_SHEET;
	private final Map<LevelLayer, TileAnimations> animations;

	private List<Level> levels;
	private int currentLevel;

	public LevelManager(Playing playing) {
		this.playing = playing;
		this.foregroundTiles = new HashMap<>();
		this.backgrounds = new HashMap<>();
		this.animations = new HashMap<>();

		loadResources();
		loadLevels();
		currentLevel = 0;
	}

	/**
	 * Renders active level to screen.
	 *
	 * @param g The graphics context.
	 */
	public void draw(Graphics g) {
		getCurrentLevel().draw(g);
	}

	/**
	 * Loads all levels for the game.
	 */
	public void loadLevels(){
		levels = new ArrayList<>();
		URL url = getClass().getResource("/levels/");
		if (url == null) return;

		if (url.getProtocol().equals("file")) {
			File levelsDir = new File(url.getPath());
			File[] files = levelsDir.listFiles();
			if (files != null) loadLevelFiles(files);
		} else {
			try {
				URI uri = url.toURI();
				FileSystem fileSystem = FileSystems.newFileSystem(uri, Collections.emptyMap());
				Path levelsPath = fileSystem.getPath("/levels");
				try (DirectoryStream<Path> stream = Files.newDirectoryStream(levelsPath, "*.properties")) {
					for (Path path : stream) {
						String fileName = path.getFileName().toString();
						int id = Integer.parseInt(fileName.split("\\.")[0]);
						levels.add(loadLevel(id));
					}
				}
				Collections.reverse(levels);
			} catch (URISyntaxException | IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Loads level files from the specified array of files and adds the levels to the levels list.
	 * Only files with a ".properties" extension are processed.
	 *
	 * @param files An array of files representing potential level files.
	 */
	private void loadLevelFiles(File[] files) {
		for (File file : files) {
			String[] fn = file.getName().split("\\.");
			if (!fn[1].equals("properties")) continue;
			levels.add(loadLevel(Integer.parseInt(fn[0])));
		}
	}

	/**
	 * Load tiles & backgrounds for all level styles and
	 * keeps them in memory to easily build worlds.
	 */
	public void loadResources() {

		for(LevelStyle style : LevelStyle.values()) {
			// Load background
			loadBackground(style);


			// Load tiles
			final String TILE_DIRECTORY = "tiles/";
			BufferedImage tileSheet = Game.loadSprite(TILE_DIRECTORY + style.getFileName());
			if(tileSheet == null)return;

			MAX_TILES_PER_SHEET = (tileSheet.getWidth() / TILES_DEFAULT_SIZE) * (tileSheet.getHeight() / TILES_DEFAULT_SIZE);
			foregroundTiles.put(style, importTiles(tileSheet));
		}

		decorTiles     = loadLayerTiles(LevelLayer.DECOR);
		midGroundTiles = loadLayerTiles(LevelLayer.MIDGROUND);
		floraTiles     = loadLayerTiles(LevelLayer.FLORA);

		// Animated tiles
		addLayerAnimations(LevelLayer.WATER, 4,
				80, 30, 15, 										    // still, some, many bubbles
								30,           											// main water flow
								7, 7, 7, 7, 7, 7, 7, 							  // water fall lush rock
								7, 7, 7, 7, 7, 										  // split water, split water crash, water crack, water leak, water leak crash
								7, 7, 7, 7, 7, 7, 7, 							  // water fall dry rock
								0, 0);
	}

	/**
	 * Loads all images needed to create the background of a style.
	 *
	 * @param style The style of background.
	 */
	private void loadBackground(LevelStyle style) {
		String[] files = new String[] { "sky", "shadow", "mountains_fog_large", "mountains_large", "mountains_fog_small", "mountains_small" };
		BufferedImage[] images = new BufferedImage[files.length];

		for(int i = 0; i < files.length; i++)
			images[i] = Game.loadSprite("backgrounds/" + style.getName() + "/" + files[i] + ".png");

		backgrounds.put(style, images);
	}

	private Image[] loadLayerTiles(LevelLayer layer) {
		return importTiles(Game.loadSprite("tiles/" + layer.getName() + ".png"));
	}

	private void addLayerAnimations(LevelLayer layer, int count, int... duration) {
		animations.put(layer, new TileAnimations(Game.loadSprite("tiles/" + layer.getName() + ".png"), count, duration));
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
		if(images == null || (index >= images.length))return null;
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
		if(index >= midGroundTiles.length)return null;
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
		if(index >= floraTiles.length)return null;
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
		if(index >= decorTiles.length)return null;
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
		Properties properties = new Properties();
		try (InputStream input = getClass().getResourceAsStream("/" + path)) {
			if (input != null)
				properties.load(input);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return properties;
	}


	/**
	 * Loads a level and all of its data.
	 *
	 * @param id The level number.
	 * @return A level object filled with the provided data.
	 */
	private Level loadLevel(int id) {
		Properties levelProperties = getLevelPropertyFile("levels/" + id);
		LevelStyle style = LevelStyle.getStyle(levelProperties.getProperty("style"));
		return new Level(this, id, style, loadLevelData(id));
	}

	/**
	 * Determines all tile information for a level based on the
	 * tile index given within each level's properties file.
	 *
	 * @param id The level number.
	 * @return A map consisting of a key layer with tile index values.
	 */
	public Map<LevelLayer, int[][]> loadLevelData(int id) {
		Properties levelProperties = getLevelPropertyFile("levels/" + id);
		Map<LevelLayer, int[][]> data = new HashMap<>();
		for(LevelLayer layer : LevelLayer.values())
			data.put(layer, parseLevelData(levelProperties, layer));
		return data;
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
		String[] rows = data.split(";");

		int[][] levelData = null;
		for(int h = 0; h < rows.length; h++) {
			String[] columns = rows[h].split(",");
			for(int w = 0; w < columns.length; w++) {
				if(levelData == null) levelData = new int[rows.length][columns.length];
				int value = Integer.parseInt(columns[w].trim());
				value = (value > 0 ? (value - 1) : -1);
				levelData[h][w] = value;
			}
		}
		return levelData;
	}

	/**
	 * Updates current level to the next.
	 */
	public void nextLevel() {
		if(currentLevel == (levels.size() - 1))
			backToFirst();
		else currentLevel++;
		playing.getSoundManager().startSong();
	}

	/**
	 * Resets the current level to the first level of the game.
	 */
	public void backToFirst() {
		currentLevel = 0;
	}

	/**
	 * @return True if the current level is the last level, false otherwise.
	 */
	public boolean isLastLevel() {
		return currentLevel == (levels.size() - 1);
	}

	/**
	 * @return True if the current level is valid, false otherwise.
	 */
	public boolean isCurrentLevelValid() {
		return levels != null && !levels.isEmpty() && currentLevel >= 0 && currentLevel < levels.size();
	}

	public Level getCurrentLevel() {
		return levels.get(currentLevel);
	}

	public List<Level> getLevels() {
		return levels;
	}

	public Playing getPlaying() {
		return playing;
	}

	class TileAnimations {

		Map<Integer, Animation> animations;

		/**
		 * Used to initialize animated tile sheets.
		 *
		 * @param sprite  	The sprite that will be cut into different animations.
		 * @param count   	The number of images per animation.
		 * @param duration 	An array of durations for each animation.
		 */
		public TileAnimations(BufferedImage sprite, int count, int... duration) {
			this.animations = new HashMap<>();

			Image[] allAnimations = importTiles(sprite);

			for(int i = 0; i < allAnimations.length; i += count) {
				Image[] groupedAnimations = new Image[count];
				System.arraycopy(allAnimations, i, groupedAnimations, 0, count);
				Animation animation = new Animation(groupedAnimations, duration[animations.size()], false);
				int index = animations.size() * animation.getImages().length;
				animations.put(index, animation);
			}
		}

		public Map<Integer, Animation> getAnimations() {
			return animations;
		}
	}
}
