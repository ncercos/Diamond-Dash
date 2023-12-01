package ui;

import entities.Player;
import game.Game;

import java.awt.*;

/**
 * Written by Nicholas Cercos
 * Created on Dec 01 2023
 **/
public class HudOverlay {

	private final Player player;
	private final Image hudImg;

	// Hud Dimensions
	private final int HUD_WIDTH = (int) (96 * Game.SCALE);
	private final int HUD_HEIGHT = (int) (29 * Game.SCALE);
	private final int HUD_X = (int) (5 * Game.SCALE);
	private final int HUD_Y = (int) (5 * Game.SCALE);

	// Health Bar Dimensions
	private final int HEALTH_WIDTH = (int) (75 * Game.SCALE);
	private final int HEALTH_HEIGHT = (int) (2 * Game.SCALE);
	private final int HEALTH_START_X = (int) (17 * Game.SCALE);
	private final int HEALTH_START_Y = (int) (7 * Game.SCALE);
	private int currentHealthWidth;

	// Energy Bar Dimensions
	private final int ENERGY_WIDTH = (int) (52 * Game.SCALE);
	private final int ENERGY_HEIGHT = (int) Game.SCALE;
	private final int ENERGY_START_X = (int) (22 * Game.SCALE);
	private final int ENERGY_START_Y = (int) (17 * Game.SCALE);
	private int currentEnergyWidth;

	public HudOverlay(Player player) {
		this.player = player;
		this.hudImg = Toolkit.getDefaultToolkit().getImage(Game.RESOURCE_URL + "ui/hud.png");
		this.currentHealthWidth = HEALTH_WIDTH;
	}

	/**
	 * Updates the bar width that will be rendered to display
	 * the current health and energy.
	 */
	public void update() {
		currentHealthWidth = (int) ((player.getHealth() / (float) player.getMaxHealth()) * HEALTH_WIDTH);
		currentEnergyWidth = (int) ((player.getEnergy() / (float) player.getMaxEnergy()) * ENERGY_WIDTH);
	}

	/**
	 * Draws the HUD and fills the status bars with their
	 * designated color and width (calculated within #update()).
	 *
	 * @param g The graphics context.
	 */
	public void draw(Graphics g) {
		g.drawImage(hudImg, HUD_X, HUD_Y, HUD_WIDTH, HUD_HEIGHT, null);

		// Health Bar
		g.setColor(Color.RED);
		g.fillRect(HEALTH_START_X + HUD_X, HEALTH_START_Y + HUD_Y, currentHealthWidth, HEALTH_HEIGHT);

		// Energy Bar
		g.setColor(Color.YELLOW);
		g.fillRect(ENERGY_START_X + HUD_X, ENERGY_START_Y + HUD_Y, currentEnergyWidth, ENERGY_HEIGHT);
	}
}
