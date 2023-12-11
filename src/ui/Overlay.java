package ui;

import game.Game;
import game.states.Playing;
import sounds.Sound;
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
 * Created on Dec 04 2023
 **/
public abstract class Overlay {

	protected final Playing playing;
	private BufferedImage img;
	protected int x, y, w, h;
	private final boolean centerDisplay;

	private final List<Button> buttons;

	public Overlay(Playing playing, String fileName, int x, int y, boolean centerDisplay) {
		this.playing = playing;
		this.x = x;
		this.y = y;
		this.centerDisplay = centerDisplay;
		buttons = new ArrayList<>();
		load(fileName);
	}

	public Overlay(Playing playing, String fileName, int x, int y) {
		this(playing, fileName, x, y, false);
	}

	public Overlay(Playing playing, String fileName, int y) {
		this(playing, fileName, 0, y, true);
	}

	public abstract boolean isActive();

	/**
	 * Called when a specific button is clicked.
	 *
	 * @param button The button being clicked.
	 */
	public void onButtonClick(Button button) {
		playing.getSoundManager().playSFX(Sound.CLICK);
	}

	/**
	 * Updates all button animations.
	 */
	public void update() {
		if(isActive())
			buttons.forEach(Button::update);
	}

	public void draw(Graphics g) {
		if(isActive()) {
			g.drawImage(img, x, y, w, h, null);
			buttons.forEach(b -> b.draw(g));
		}
	}

	/**
	 * Adds a new button.
	 *
	 * @param button The button to be added.
	 */
	public void addButton(Button button) {
		buttons.add(button);
	}

	/**
	 * Resets all attributes within a button.
	 */
	public void resetButtons() {
		buttons.forEach(Button::reset);
	}

	/**
	 * Loads the main overlay image.
	 *
	 * @param fileName The name of the image file.
	 */
	private void load(String fileName) {
		try {
			img = ImageIO.read(new File(Game.RESOURCE_URL + "ui/" + fileName + ".png"));
			w = (int) (img.getWidth()  * Game.SCALE) / 2;
			h = (int) (img.getHeight() * Game.SCALE) / 2;
			y = (int) (y * Game.SCALE);
			if(centerDisplay) x = (Game.GAME_WIDTH / 2) - (w / 2);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * When a button is initially pressed.
	 */
	public void mousePressed(MouseEvent e) {
		if(!isActive())return;
		for(Button button : buttons) {
			if(button.contains(e.getX(), e.getY())) {
				button.setMousePressed(true);
				break;
			}
		}
	}

	/**
	 * Handles button clicks.
	 */
	public void mouseReleased(MouseEvent e) {
		if(!isActive())return;
		for(Button button : buttons) {
			if(button.contains(e.getX(), e.getY())) {
				if(button.isMousePressed()) {
					onButtonClick(button);
					break;
				}
			}
		}
		resetButtons();
	}

	/**
	 * Updates button hover animation when mouse is over button.
	 */
	public void mouseMoved(MouseEvent e) {
		if(!isActive())return;
		for(Button button : buttons) {
			button.reset();

			if(button.contains(e.getX(), e.getY())) {
				button.setMouseOver(true);
				break;
			}
		}
	}

	/**
	 * Moves volume button slider as the mouse is dragged.
	 */
	public void mouseDragged(MouseEvent e) {
		if(!isActive())return;
		for(Button button : buttons) {
			if(!(button instanceof VolumeButton vb))continue;
			if(vb.isMousePressed()) {
				float previous = vb.getValue();
				vb.move(e.getX());
				float current = vb.getValue();
				if(previous != current)
					playing.getGame().getSoundManager().setVolume(current);
			}
		}
	}
}
