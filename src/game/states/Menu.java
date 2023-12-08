package game.states;

import game.Game;
import game.GameState;
import levels.LevelStyle;
import ui.Button;
import ui.buttons.MenuButton;
import ui.buttons.UtilButton;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Written by Nicholas Cercos
 * Created on Nov 27 2023
 **/
public class Menu extends State {

	// Logo & Buttons
	private BufferedImage logoImg;
	private int x, y, w, h;
	private final Set<MenuButton> buttons;

	// Background
	private final BufferedImage bgImg, lmImg, smImg, msImg;
	private int bgOffsetX;
	private boolean bgMoveLeft;

	public Menu(Game game) {
		super(game);
		buttons = new HashSet<>();
		LevelStyle style = LevelStyle.values()[ThreadLocalRandom.current().nextInt(LevelStyle.values().length)];
		bgImg = game.getPlaying().getLevelManager().getBackgroundImage(style);
		lmImg = game.getPlaying().getLevelManager().getMountainImage(style, true, true);
		smImg = game.getPlaying().getLevelManager().getMountainImage(style, false, true);
		msImg = game.getPlaying().getLevelManager().getMountainShadowImage(style);
		loadMenu();
		loadButtons();
	}

	private void loadMenu() {
		try {
			logoImg = ImageIO.read(new File(Game.RESOURCE_URL + "ui/logo.png"));
			w = (int) (logoImg.getWidth() / 3 * Game.SCALE);
			h = (int) (logoImg.getHeight() / 3 * Game.SCALE);
			x = (Game.GAME_WIDTH / 2) - (w / 2);
			y = (int) (20 * Game.SCALE);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Loads all menu buttons based on their respective state.
	 * Each button will be placed 70px under the other. (adjusted to scale, ofc)
	 */
	private void loadButtons() {
		int xPos = 140;
		for(int i = 0; i < GameState.values().length; i++) {
			GameState state = GameState.values()[i];
			if(state.equals(GameState.MENU))continue;
			buttons.add(new MenuButton((int)(xPos * Game.SCALE), (int) (115 * Game.SCALE), i - 1, state));
			xPos += 100;
		}
	}


	@Override
	public void update() {
		buttons.forEach(MenuButton::update);
	}

	@Override
	public void draw(Graphics g) {
		int BG_SCROLL_LIMIT = (int) (766 * Game.SCALE);
		if(bgOffsetX == BG_SCROLL_LIMIT && bgMoveLeft) bgMoveLeft = false;
		else if(bgOffsetX == 0 && !bgMoveLeft) bgMoveLeft = true;
		bgOffsetX = bgMoveLeft ? (bgOffsetX + 1) : (bgOffsetX - 1);

		game.getPlaying().getLevelManager().getCurrentLevel()
				.drawBackground(g, bgImg, lmImg, msImg, smImg, bgOffsetX);
		g.drawImage(logoImg, x, y, w, h, null);
		buttons.forEach(b -> b.draw(g));
	}

	@Override
	public void lostFocus() {}

	@Override
	public void keyPressed(KeyEvent e) {}

	@Override
	public void keyReleased(KeyEvent e) {}

	@Override
	public void mouseClicked(MouseEvent e) {}

	@Override
	public void mousePressed(MouseEvent e) {
		for(Button b : buttons) {
			if(b.contains(e.getX(), e.getY())) {
				b.setMousePressed(true);
				break;
			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		for(MenuButton b : buttons) {
			if(b.contains(e.getX(), e.getY())) {
				if(b.isMousePressed()) {
					b.applyState();

					if(b.getState().equals(GameState.PLAYING) &&
							game.getPlaying().isPaused())
						game.getPlaying().togglePause();
				}
				break;
			}
		}
		buttons.forEach(Button::reset);
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		buttons.forEach(b -> b.setMouseOver(false));

		for(Button b : buttons) {
			if(b.contains(e.getX(), e.getY())) {
				b.setMouseOver(true);
				break;
			}
		}
	}

	@Override
	public void mouseDragged(MouseEvent e) {}
}
