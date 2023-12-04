package ui.overlays;

import game.Game;
import game.GameState;
import game.states.Playing;
import ui.Button;
import ui.Overlay;
import ui.buttons.SoundButton;
import ui.buttons.UtilButton;
import ui.buttons.VolumeButton;

/**
 * Written by Nicholas Cercos
 * Created on Nov 27 2023
 **/
public class PauseOverlay extends Overlay {

	public PauseOverlay(Playing playing) {
		super(playing, "pause_menu", 20);
		createSoundButtons();
		createUtilButtons();
		createVolumeButton();
	}

	/**
	 * Initializes all sound related buttons.
	 * Music & Sound Effects
	 */
	private void createSoundButtons() {
		int xPos   = (int) (210 * Game.SCALE);
		int musicY = (int) (65 * Game.SCALE);
		int sfxY   = (int) (89 * Game.SCALE);
		addButton(new SoundButton(xPos, musicY));
		addButton(new SoundButton(xPos, sfxY));
	}

	/**
	 * Initializes all utility buttons.
	 * Menu, Replay Level, Unpause
	 */
	private void createUtilButtons() {
		int yPos = (int) (158 * Game.SCALE);
		int menuX = (int) (140 * Game.SCALE);
		int replayX = (int) (178 * Game.SCALE);
		int unpauseX = (int) (216 * Game.SCALE);
		addButton(new UtilButton(menuX, yPos, UtilButton.Type.HOME));
		addButton(new UtilButton(replayX, yPos, UtilButton.Type.REPLAY));
		addButton(new UtilButton(unpauseX, yPos, UtilButton.Type.START));
	}

	/**
	 * Initializes the volume controls.
	 */
	private void createVolumeButton() {
		int xPos = (int) (139 * Game.SCALE);
		int yPos = (int) (135 * Game.SCALE);
		addButton(new VolumeButton(xPos, yPos));
	}

	@Override
	public boolean isActive() {
		return playing.isPaused();
	}

	@Override
	public void onButtonClick(Button button) {
		if(button instanceof SoundButton sb)
			sb.setMuted(!sb.isMuted());
		else if(button instanceof UtilButton ub) {
			if(ub.getType().equals(UtilButton.Type.HOME)) {
				playing.getLevelManager().getCurrentLevel().reset();
				GameState.current = GameState.MENU;
			} else if(ub.getType().equals(UtilButton.Type.REPLAY)) {
				playing.getLevelManager().getCurrentLevel().reset();
				playing.togglePause();
			} else if(ub.getType().equals(UtilButton.Type.START))
				playing.togglePause();
		}
	}
}
