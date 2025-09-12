package ui.overlays;

import entities.Player;
import game.Game;
import game.GameState;
import game.states.Playing;
import levels.LevelManager;
import sounds.Sound;
import ui.Button;
import ui.Overlay;
import ui.buttons.UtilButton;

/**
 * Written by Nicholas Cercos
 * Created on Dec 06 2023
 **/
public class CompleteOverlay extends Overlay {

	private final LevelManager lm;

	public CompleteOverlay(Playing playing) {
		super(playing, "complete_menu", 65);
		lm = playing.getLevelManager();
		createButtons();
	}

	private void createButtons() {
		int yPos = (int) (110 * Game.SCALE);
		addButton(new UtilButton((int)(150 * Game.SCALE), yPos, UtilButton.Type.HOME));
		addButton(new UtilButton((int)(205 * Game.SCALE), yPos, UtilButton.Type.START));
	}

	@Override
	public boolean isActive() {
		return lm.isCurrentLevelValid() && lm.getCurrentLevel().isComplete();
	}

	@Override
	public void onButtonClick(Button button) {
		super.onButtonClick(button);
		if(!(button instanceof UtilButton ub))return;
		playing.getSoundManager().stopSFX(Sound.LVL_COMPLETE);
		switch (ub.getType()) {
			case HOME -> handleHomeButton();
			case START -> handleStartButton();
		}
	}

	/**
	 * Handles the logic for the home button when it is clicked. This method resets the current level
	 * within the LevelManager and transitions the game's state to the main menu.
	 */
	private void handleHomeButton() {
		lm.getCurrentLevel().reset();
		playing.setState(GameState.MENU);
	}

	/**
	 * Handles the logic for the start button when it is clicked. This method either progresses
	 * the game to the next level or resets the current level based on the game's state.
	 *
	 * - If the last level is completed, the game resets to the first level, teleports the player
	 *   to the spawn point of the first level, and transitions the game state to the main menu.
	 * - If the current level is not the last, it progresses to the next level, teleports the
	 *   player to the spawn point of the new level, and resets the current level.
	 */
	private void handleStartButton() {
		Player player = playing.getPlayer();
		player.reset();

		if(lm.isLastLevel()) {
			lm.backToFirst();
			playing.setState(GameState.MENU);
		} else lm.nextLevel();

		player.teleport(lm.getCurrentLevel().getSpawn());
		lm.getCurrentLevel().reset();
	}
}
