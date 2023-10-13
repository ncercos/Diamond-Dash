package levels;

import game.Game;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.concurrent.ThreadLocalRandom;

import static game.Game.*;

/**
 * Written by Nicholas Cercos
 * Created on Oct 08 2023
 **/
public class Level {

	private final LevelManager levelManager;

	private final int id;
	private final LevelStyle style;
	private final int[][] data;

	private int offsetX;
	private final int maxTilesOffset;
	public final int LT_BORDER = (int) (0.2 * Game.GAME_WIDTH);
	public final int RT_BORDER = (int) (0.8 * Game.GAME_WIDTH);

	private final BufferedImage background, largeMountain, smallMountain, mountainShadow;
	private final int LARGE_MOUNTAIN_WIDTH, SMALL_MOUNTAIN_WIDTH, MOUNTAIN_SHADOW_WIDTH;

	public Level(LevelManager levelManager, int id, LevelStyle style, int[][] data) {
		this.levelManager = levelManager;
		this.id = id;
		this.style = style;
		this.data = data;
		int tilesWide = data[0].length;
		maxTilesOffset = (tilesWide - Game.TILES_IN_WIDTH) * Game.TILES_SIZE;

		boolean foggy = ThreadLocalRandom.current().nextBoolean();
		background     = levelManager.getBackgroundImage(style);
		largeMountain  = levelManager.getMountainImage(style, true, foggy);
		smallMountain  = levelManager.getMountainImage(style, false, foggy);
		mountainShadow = levelManager.getMountainShadowImage(style);

		LARGE_MOUNTAIN_WIDTH  = (int) (largeMountain.getWidth()  * SCALE);
		SMALL_MOUNTAIN_WIDTH  = (int) (smallMountain.getWidth()  * SCALE);
		MOUNTAIN_SHADOW_WIDTH = (int) (mountainShadow.getWidth() * SCALE);
	}

	/**
	 * Draws necessary tiles and backgrounds to level.
	 *
	 * @param g The graphics context.
	 */
	public void draw(Graphics g) {
		// Background
		g.drawImage(background, 0, 0, GAME_WIDTH, GAME_HEIGHT, null);
		for(int i = 0; i < 2; i++) {
			g.drawImage(largeMountain,  i * LARGE_MOUNTAIN_WIDTH  - (int) (offsetX * 0.3), (int) (85 * SCALE),  LARGE_MOUNTAIN_WIDTH,  (int) (largeMountain.getHeight() * SCALE), null);
			g.drawImage(mountainShadow, i * MOUNTAIN_SHADOW_WIDTH - (int) (offsetX * 0.5), (int) (50 * SCALE),  MOUNTAIN_SHADOW_WIDTH, (int) (mountainShadow.getHeight() * SCALE), null);
			g.drawImage(smallMountain,  i * SMALL_MOUNTAIN_WIDTH  - (int) (offsetX * 0.7), (int) (135 * SCALE), SMALL_MOUNTAIN_WIDTH,  (int) (smallMountain.getHeight() * SCALE), null);
		}

		// Tiles
		for(int h = 0; h < TILES_IN_HEIGHT; h++) {
			for(int w = 0; w < data[0].length; w++) {
				int index = getTileIndex(w, h);
				int x = w * TILES_SIZE - offsetX;
				int y = h * TILES_SIZE;
				g.drawImage(levelManager.getTiles(style)[index], x, y, TILES_SIZE, TILES_SIZE, null);
			}
		}
	}

	/**
	 * Determines if a specific location contains a solid tile.
	 *
	 * @param px The x-position.
	 * @param py The y-position.
	 * @return True if the tile is solid, otherwise, false.
	 */
	public boolean isSolid(double px, double py) {
		int x = (int)px;  int y = (int)py;
		if(x < 0 || x >= getMaxWidth()) return true;
		if(y < 0 || y >= Game.GAME_HEIGHT) return true;
		int index = getTileIndex(x / Game.TILES_SIZE, y / Game.TILES_SIZE);
		return !getStyle().isNonSolid(index);
	}

	/**
	 * Get the index of a tile at a specific location.
	 *
	 * @param x The x-coordinate.
	 * @param y The y-coordinate.
	 * @return An integer that determines which tile will be placed.
	 */
	public int getTileIndex(int x, int y) {
		return data[y][x];
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
	public int getMaxWidth() {
		return data[0].length * Game.TILES_SIZE;
	}

	public LevelStyle getStyle() {
		return style;
	}
}
