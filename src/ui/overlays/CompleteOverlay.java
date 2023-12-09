package ui.overlays;

import entities.Player;
import game.Game;
import game.GameState;
import game.states.Playing;
import levels.LevelManager;
import ui.Button;
import ui.Overlay;
import ui.buttons.UtilButton;

/**
 * Written by Nicholas Cercos
 * Created on Dec 06 2023
 **/
public class CompleteOverlay extends Overlay {

	public CompleteOverlay(Playing playing) {
		super(playing, "complete_menu", 65);
		createButtons();
	}

	private void createButtons() {
		int yPos = (int) (110 * Game.SCALE);
		addButton(new UtilButton((int)(150 * Game.SCALE), yPos, UtilButton.Type.HOME));
		addButton(new UtilButton((int)(205 * Game.SCALE), yPos, UtilButton.Type.START));
	}

	@Override
	public boolean isActive() {
		return playing.getLevelManager().getCurrentLevel().isComplete();
	}

	@Override
	public void onButtonClick(Button button) {
		if(!(button instanceof UtilButton ub))return;
		LevelManager lm = playing.getLevelManager();
		if(ub.getType().equals(UtilButton.Type.HOME)) {
			lm.getCurrentLevel().reset();
			GameState.current = GameState.MENU;
		} else if(ub.getType().equals(UtilButton.Type.START)) {
			Player player = playing.getPlayer();
			lm.nextLevel();
			player.reset();
			player.teleport(lm.getCurrentLevel().getSpawn());
		}
	}
}
