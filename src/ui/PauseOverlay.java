package ui;

import game.Game;
import game.GameState;
import game.states.Playing;
import ui.buttons.Button;
import ui.buttons.SoundButton;
import ui.buttons.UtilButton;
import ui.buttons.VolumeButton;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Written by Nicholas Cercos
 * Created on Nov 27 2023
 **/
public class PauseOverlay {

	private final Playing playing;
	private BufferedImage pmImg;
	private int pmX, pmY, pmW, pmH;

	private List<SoundButton> soundButtons;
	private List<UtilButton> utilButtons;
	private List<VolumeButton> volumeButtons;

	public PauseOverlay(Playing playing) {
		this.playing = playing;
		loadOverlay();
		createSoundButtons();
		createUtilButtons();
		createVolumeButton();
	}

	/**
	 * Loads the main overlay that will be used as a background
	 * for all pause menu buttons.
	 */
	private void loadOverlay() {
		try {
			pmImg = ImageIO.read(new File(Game.RESOURCE_URL + "ui/pause_menu.png"));
			pmW = (int) (pmImg.getWidth() * Game.SCALE) / 2;
			pmH = (int) (pmImg.getHeight() * Game.SCALE) / 2;
			pmX = (Game.GAME_WIDTH / 2) - (pmW / 2);
			pmY = (int) (20 * Game.SCALE);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Initializes all sound related buttons.
	 * Music & Sound Effects
	 */
	private void createSoundButtons() {
		soundButtons = new ArrayList<>();
		int xPos   = (int) (210 * Game.SCALE);
		int musicY = (int) (65 * Game.SCALE);
		int sfxY   = (int) (89 * Game.SCALE);
		soundButtons.add(new SoundButton(xPos, musicY));
		soundButtons.add(new SoundButton(xPos, sfxY));
	}

	/**
	 * Initializes all utility buttons.
	 * Menu, Replay Level, Unpause
	 */
	private void createUtilButtons() {
		utilButtons = new ArrayList<>();
		int yPos = (int) (158 * Game.SCALE);
		int menuX = (int) (140 * Game.SCALE);
		int replayX = (int) (178 * Game.SCALE);
		int unpauseX = (int) (216 * Game.SCALE);
		utilButtons.add(new UtilButton(menuX, yPos, UtilButton.Type.HOME));
		utilButtons.add(new UtilButton(replayX, yPos, UtilButton.Type.REPLAY));
		utilButtons.add(new UtilButton(unpauseX, yPos, UtilButton.Type.START));
	}

	/**
	 * Initializes the volume controls.
	 */
	private void createVolumeButton() {
		volumeButtons = new ArrayList<>();
		int xPos = (int) (139 * Game.SCALE);
		int yPos = (int) (135 * Game.SCALE);
		volumeButtons.add(new VolumeButton(xPos, yPos));
	}

	/**
	 * Resets all pause related buttons and their attributes.
	 */
	private void resetButtons() {
		for(Button button : getButtons())
			button.reset();
	}

	/**
	 * @return An arraylist that contains both sound and utility buttons via superclass.
	 */
	private List<Button> getButtons() {
		List<Button> buttons = new ArrayList<>();
		buttons.addAll(soundButtons);
		buttons.addAll(utilButtons);
		buttons.addAll(volumeButtons);
		return buttons;
	}

	public void update() {
		for(Button button : getButtons())
			button.update();
	}

	public void draw(Graphics g) {
		g.drawImage(pmImg, pmX, pmY, pmW, pmH, null);

		for(Button button : getButtons())
			button.draw(g);
	}

	public void mousePressed(MouseEvent e) {
		for(Button button : getButtons()) {
			if(button.contains(e.getX(), e.getY())) {
				button.setMousePressed(true);
				break;
			}
		}
	}

	public void mouseReleased(MouseEvent e) {
		for(Button button : getButtons()) {
			if(button.contains(e.getX(), e.getY())) {
				if(button.isMousePressed()) {
					if(button instanceof SoundButton sb) sb.setMuted(!sb.isMuted());
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
					break;
				}
			}
		}
		resetButtons();
	}

	public void mouseMoved(MouseEvent e) {
		for(Button button : getButtons()) {
			button.reset();

			if(button.contains(e.getX(), e.getY())) {
				button.setMouseOver(true);
				break;
			}
		}
	}

	public void mouseDragged(MouseEvent e) {
		for(VolumeButton vb : volumeButtons) {
			if(vb.isMousePressed())
				vb.move(e.getX());
		}
	}
}
