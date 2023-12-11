package ui.overlays;

import game.Game;
import game.GameState;
import game.states.Playing;
import sounds.Sound;
import ui.Button;
import ui.Overlay;
import ui.buttons.UtilButton;

/**
 * Written by Nicholas Cercos
 * Created on Dec 04 2023
 **/
public class DeadOverlay extends Overlay {

	public DeadOverlay(Playing playing) {
		super(playing, "dead_menu", 50);
		createButtons();
	}

	private void createButtons() {
		int yPos = (int) (95 * Game.SCALE);
		addButton(new UtilButton((int)(150 * Game.SCALE), yPos, UtilButton.Type.HOME));
		addButton(new UtilButton((int)(205 * Game.SCALE), yPos, UtilButton.Type.START));
	}

	@Override
	public boolean isActive() {
		return !playing.getPlayer().isActive();
	}

	@Override
	public void onButtonClick(Button button) {
		super.onButtonClick(button);
		if(!(button instanceof UtilButton ub))return;
		playing.getSoundManager().stopSFX(Sound.GAME_OVER);
		if(ub.getType().equals(UtilButton.Type.HOME)) {
			playing.getLevelManager().getCurrentLevel().reset();
			playing.setState(GameState.MENU);
		} else if(ub.getType().equals(UtilButton.Type.START))
			playing.getLevelManager().getCurrentLevel().reset();
	}
}
