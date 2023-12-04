package ui.overlays;

import entities.Player;
import game.Game;
import game.states.Playing;
import ui.Overlay;

import java.awt.*;

/**
 * Written by Nicholas Cercos
 * Created on Dec 01 2023
 **/
public class HudOverlay extends Overlay {

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

	public HudOverlay(Playing playing) {
		super(playing, "hud",
				(int) (5 * Game.SCALE),
				(int) (1 * Game.SCALE));
		currentHealthWidth = HEALTH_WIDTH;
	}

	@Override
	public boolean isActive() {
		return true;
	}

	/**
	 * Updates the bar width that will be rendered to display
	 * the current health and energy.
	 */
	@Override
	public void update() {
		Player player = playing.getPlayer();
		currentHealthWidth = (int) ((player.getHealth() / (float) player.getMaxHealth()) * HEALTH_WIDTH);
		currentEnergyWidth = (int) ((player.getEnergy() / (float) player.getMaxEnergy()) * ENERGY_WIDTH);
	}

	/**
	 * Draws the HUD and fills the status bars with their
	 * designated color and width (calculated within #update()).
	 *
	 * @param g The graphics context.
	 */
	@Override
	public void draw(Graphics g) {
		super.draw(g);

		// Health Bar
		g.setColor(Color.RED);
		g.fillRect(HEALTH_START_X + x, HEALTH_START_Y + y, currentHealthWidth, HEALTH_HEIGHT);

		// Energy Bar
		g.setColor(Color.YELLOW);
		g.fillRect(ENERGY_START_X + x, ENERGY_START_Y + y, currentEnergyWidth, ENERGY_HEIGHT);
	}
}
