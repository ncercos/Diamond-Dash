package ui;

import game.Game;
import game.GameState;
import game.states.Playing;
import ui.buttons.Button;
import ui.buttons.SoundButton;
import ui.buttons.UtilButton;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Stream;

/**
 * Written by Nicholas Cercos
 * Created on Nov 27 2023
 **/
public class PauseOverlay {

	private final Playing playing;
	private BufferedImage pmImg;
	private int pmX, pmY, pmW, pmH;

	private SoundButton[] soundButtons;
	private UtilButton[] utilButtons;

	public PauseOverlay(Playing playing) {
		this.playing = playing;
		loadOverlay();
		createSoundButtons();
		createUtilButtons();
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
		soundButtons = new SoundButton[2];
		int xPos   = (int) (210 * Game.SCALE);
		int musicY = (int) (65 * Game.SCALE);
		int sfxY   = (int) (89 * Game.SCALE);
		soundButtons[0] = new SoundButton(xPos, musicY);
		soundButtons[1] = new SoundButton(xPos, sfxY);
	}

	/**
	 * Initializes all utility buttons.
	 * Menu, Replay Level, Unpause
	 */
	private void createUtilButtons() {
		utilButtons = new UtilButton[3];
		int yPos = (int) (158 * Game.SCALE);
		int menuX = (int) (140 * Game.SCALE);
		int replayX = (int) (178 * Game.SCALE);
		int unpauseX = (int) (216 * Game.SCALE);
		utilButtons[0] = new UtilButton(menuX, yPos, UtilButton.Type.HOME);
		utilButtons[1] = new UtilButton(replayX, yPos, UtilButton.Type.REPLAY);
		utilButtons[2] = new UtilButton(unpauseX, yPos, UtilButton.Type.START);
	}

	/**
	 * Resets all pause related buttons and their attributes.
	 */
	private void resetButtons() {
		for(Button button : getButtons())
			button.reset();
	}

	/**
	 * @return An array that contains both sound and utility buttons via superclass.
	 */
	private Button[] getButtons() {
		return Stream.concat(Arrays.stream(soundButtons), Arrays.stream(utilButtons)).toArray(Button[]::new);
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

	public void mouseMoved() {}

	public void mousePressed(MouseEvent e) {
		for(Button button : getButtons()) {
			if(playing.isInteractingWith(e, button)) {
				button.setMousePressed(true);
				break;
			}
		}
	}

	public void mouseReleased(MouseEvent e) {
		for(Button button : getButtons()) {
			if(playing.isInteractingWith(e, button)) {
				if(button.isMousePressed()) {
					if(button instanceof SoundButton sb) sb.setMuted(!sb.isMuted());
					else if(button instanceof UtilButton ub) {
						if(ub.getType().equals(UtilButton.Type.HOME))
							GameState.current = GameState.MENU;
						else if(ub.getType().equals(UtilButton.Type.REPLAY))
							System.out.println("replay level");
						else if(ub.getType().equals(UtilButton.Type.START))
							playing.unpause();
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

			if(playing.isInteractingWith(e, button)) {
				button.setMouseOver(true);
				break;
			}
		}
	}

	public void mouseDragged(MouseEvent e) {}
}
