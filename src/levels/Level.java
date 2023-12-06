package levels;

import entities.Hostile;
import entities.Player;
import entities.enemies.Flower;
import entities.enemies.Goblin;
import entities.enemies.Slime;
import game.states.Playing;
import matter.*;
import matter.Container;
import matter.containers.Crate;
import matter.items.Diamond;
import matter.items.EnergyDrink;
import matter.items.Medicine;
import matter.traps.ThornFence;
import game.Game;
import sprites.Animation;
import utils.Location;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import static game.Game.*;

/**
 * Written by Nicholas Cercos
 * Created on Oct 08 2023
 **/
public class Level {

	private final Playing playing;
	private final LevelManager levelManager;

	private final int id;
	private final LevelStyle style;
	private Map<LevelLayer, int[][]> data;

	private int offsetX;
	private final int maxTilesOffset;
	public final int LT_BORDER = (int) (0.2 * Game.GAME_WIDTH);
	public final int RT_BORDER = (int) (0.8 * Game.GAME_WIDTH);

	private final BufferedImage background, largeMountain, smallMountain, mountainShadow;
	private List<Item> items;
	private List<Trap> traps;
	private List<Container> containers;
	private List<Hostile> enemies;

	private Location spawn;
	private int totalDiamonds;
	private boolean complete;

	public Level(LevelManager levelManager, int id, LevelStyle style, Map<LevelLayer, int[][]> data) {
		this.levelManager = levelManager;
		this.playing = levelManager.getPlaying();
		this.id = id;
		this.style = style;
		this.data = data;

		int tilesWide = data.get(LevelLayer.FOREGROUND)[0].length;
		maxTilesOffset = (tilesWide - Game.TILES_IN_WIDTH) * Game.TILES_SIZE;

		boolean foggy = ThreadLocalRandom.current().nextBoolean();
		background     = levelManager.getBackgroundImage(style);
		largeMountain  = levelManager.getMountainImage(style, true, foggy);
		smallMountain  = levelManager.getMountainImage(style, false, foggy);
		mountainShadow = levelManager.getMountainShadowImage(style);

		initialize();
	}

	/**
	 * Sets all level attributes and details to their default values.
	 */
	private void initialize() {
		items      = new ArrayList<>();
		traps      = new ArrayList<>();
		containers = new ArrayList<>();
		enemies    = new ArrayList<>();
		offsetX = 0;
		totalDiamonds = 0;
		loadSpawns();
	}

	/**
	 * Updates all enemies in the level.
	 */
	public void update() {
		for(int i = 0; i < items.size(); i++)
			items.get(i).update();

		for(int i = 0; i < traps.size(); i++)
			traps.get(i).update();

		for(int i = 0; i < containers.size(); i++)
			containers.get(i).update();

		Iterator<Hostile> it = enemies.iterator();
		while(it.hasNext()) {
			Hostile enemy = it.next();
			if(enemy.isActive())
				enemy.update();
			else it.remove();
		}
	}

	/**
	 * Re-initializes level values & reloads tile data.
	 */
	public void reset() {
		data = levelManager.loadLevelData(id);
		initialize();
		Player player = playing.getPlayer();
		player.reset();
		if(spawn != null) player.teleport(spawn);
	}

	/**
	 * Draws necessary tiles and backgrounds to level.
	 *
	 * @param g The graphics context.
	 */
	public void draw(Graphics g) {
		// Background
		drawBackground(g, background, largeMountain, mountainShadow, smallMountain, offsetX);

		// Tiles, Matter, and Entities
		for (LevelLayer layer : LevelLayer.values()) {
			if (layer.equals(LevelLayer.SPAWNS)) {
				containers.forEach(c -> c.draw(g));
				items.forEach(i -> i.draw(g));
				traps.forEach(t -> t.draw(g));
				enemies.forEach(e -> e.draw(g));
				playing.getPlayer().draw(g);
			} else drawLayer(g, layer);
		}
	}

	/**
	 * Draws the complete background of a level.
	 *
	 * @param g  The graphics context.
	 * @param bg The background image.
	 * @param lm The large mountain image.
	 * @param ms The mountain shadow image.
	 * @param sm The small mountain image.
	 */
	public void drawBackground(Graphics g, BufferedImage bg, BufferedImage lm, BufferedImage ms, BufferedImage sm, int offsetX) {
		final int LM_WIDTH = (int) (lm.getWidth() * SCALE);
		final int MS_WIDTH = (int) (ms.getWidth() * SCALE);
		final int SM_WIDTH = (int) (sm.getWidth() * SCALE);

		g.drawImage(bg, 0, 0, GAME_WIDTH, GAME_HEIGHT, null);
		for(int i = 0; i < 3; i++) {
			g.drawImage(lm, i * LM_WIDTH - (int) (offsetX * 0.3), (int) (85 * SCALE),  LM_WIDTH, (int) (largeMountain.getHeight()  * SCALE), null);
			g.drawImage(ms, i * MS_WIDTH - (int) (offsetX * 0.5), (int) (70 * SCALE),  MS_WIDTH, (int) (mountainShadow.getHeight() * SCALE), null);
			g.drawImage(sm, i * SM_WIDTH - (int) (offsetX * 0.7), (int) (168 * SCALE), SM_WIDTH, (int) (smallMountain.getHeight()  * SCALE), null);
		}
	}

	/**
	 * Draw all tiles for a specific layer.
	 *
	 * @param g     The graphics context.
	 * @param layer The layer to be drawn.
	 */
	private void drawLayer(Graphics g, LevelLayer layer) {
		for(int h = 0; h < TILES_IN_HEIGHT; h++) {
			for(int w = 0; w < getWidth(); w++) {
				int x = w * TILES_SIZE - offsetX;
				int y = h * TILES_SIZE;

				int index = getTileIndex(layer, w, h);
				if(index < 0)continue;
				Image image = levelManager.getTile(style, layer, index);
				Animation animation = levelManager.getTileAnimation(layer, index);
				if(image == null && animation == null) continue;
				g.drawImage(animation != null ? animation.getCurrentImage(levelManager.getPlaying()) : image, x, y, TILES_SIZE, TILES_SIZE, null);
			}
		}
	}

	/**
	 * Determines location for all spawns, entities, traps
	 * and other types of matter.
	 */
	private void loadSpawns() {
		int[][] itemData = data.get(LevelLayer.SPAWNS);
		for(int h = 0; h < itemData.length; h++) {
			for(int w = 0; w < itemData[h].length; w++) {
				int index = itemData[h][w];
				if(index < 0)continue;
				int x = w * TILES_SIZE, y = h * TILES_SIZE;

				switch (index) {
					case 0 -> spawn = new Location(x, y);
					case 1 -> enemies.add(new Goblin(playing, x, y));
					case 2 -> enemies.add(new Slime(playing, x, y));
					case 3 -> enemies.add(new Flower(playing, x, y));
					case 4 -> containers.add(new Crate(playing, x, y));
					case 5 -> items.add(new Diamond(playing, this, x, y));
				}
			}
		}

		loadTraps();
	}

	/**
	 * The only traps are flora-based (natural), so they are
	 * loaded based on tiles within that layer.
	 */
	private void loadTraps() {
		int[][] trapData = data.get(LevelLayer.FLORA);
		for(int h = 0; h < trapData.length; h++) {
			for(int w = 0; w < trapData[h].length; w++) {
				int index = trapData[h][w];
				if(index == 71 || index == 79) {
					int x = w * TILES_SIZE, y = h * TILES_SIZE;
					traps.add(new ThornFence(playing, x, y, index == 71));
				}
			}
		}
	}

	/**
	 * Adds an item to the map.
	 *
	 * @param item The item to add.
	 */
	public void spawnItem(Item item) {
		items.add(item);
	}

	/**
	 * Removes an item from the map.
	 *
	 * @param item The item being removed.
	 */
	public void removeItem(Item item) {
		items.remove(item);
	}

	/**
	 * Removes a container from the map.
	 *
	 * @param container The container being destroyed.
	 */
	public void destroyContainer(Container container) {
		containers.remove(container);
	}

	public void addDiamond() {
		totalDiamonds++;
	}

	public int getTotalDiamonds() {
		return totalDiamonds;
	}

	public boolean isComplete() {
		return complete;
	}

	/**
	 * Called when the player clears the map.
	 */
	public void complete() {
		complete = true;
	}

	/**
	 * Determines if a specific location contains a solid tile.
	 *
	 * @param px The x-position.
	 * @param py The y-position.
	 * @param calcTile Is the tile-position provided or should it be calculated.
	 * @return True if the tile is solid, otherwise, false.
	 */
	public boolean isSolid(double px, double py, boolean calcTile) {
		int x = (int)px;  int y = (int)py;
		if(calcTile) {
			if(x < 0 || x >= (getWidth() * TILES_SIZE)) return true;
			if(y < 0 || y >= Game.GAME_HEIGHT) return true;
		}
		int index = getTileIndex(LevelLayer.FOREGROUND, calcTile ? x / Game.TILES_SIZE : x, calcTile ? y / Game.TILES_SIZE : y);
		return !getStyle().isNonSolid(index);
	}

	public boolean isSolid(double px, double py) {
		return isSolid(px, py, true);
	}

	/**
	 * Get the index of a tile at a specific location within a specific layer.
	 *
	 * @param x The x-coordinate.
	 * @param y The y-coordinate.
	 * @return An integer that determines which tile will be placed.
	 */
	public int getTileIndex(LevelLayer layer, int x, int y) {
		int index = -1;
		try {
			index = data.get(layer)[y][x];
		} catch (ArrayIndexOutOfBoundsException ignored) {}
		return index;
	}

	public void addToOffsetX(int value) {
		offsetX += value;
		maintainOffsetConstraints();
	}

	private void maintainOffsetConstraints() {
		if(offsetX > maxTilesOffset) offsetX = maxTilesOffset;
		else if(offsetX < 0) offsetX = 0;
	}

	public int getOffsetX() {
		return offsetX;
	}

	/**
	 * @return The max width of the level (includes offscreen).
	 */
	public int getWidth() {
		return data.get(LevelLayer.FOREGROUND)[0].length;
	}

	public LevelStyle getStyle() {
		return style;
	}

	public LevelManager getLevelManager() {
		return levelManager;
	}

	public Location getSpawn() {
		return spawn;
	}

	public List<Hostile> getEnemies() {
		return enemies;
	}
}
