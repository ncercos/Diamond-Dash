package game.states;

import game.Game;
import game.GameState;
import levels.LevelStyle;
import ui.buttons.MenuButton;

import javax.imageio.ImageIO;
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

	private BufferedImage menuImg;
	private final Set<MenuButton> buttons;
	private int mX, mY, mW, mH;

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
			menuImg = ImageIO.read(new File(Game.RESOURCE_URL + "ui/menu.png"));
			mW = (int) (menuImg.getWidth() * Game.SCALE) / 2;
			mH = (int) (menuImg.getHeight() * Game.SCALE) / 2;
			mX = (Game.GAME_WIDTH / 2) - (mW / 2);
			mY = (int) (30 * Game.SCALE);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Loads all menu buttons based on their respective state.
	 * Each button will be placed 70px under the other. (adjusted to scale, ofc)
	 */
	private void loadButtons() {
		int yPos = 82;
		for(int i = 0; i < GameState.values().length; i++) {
			GameState state = GameState.values()[i];
			if(state.equals(GameState.MENU))continue;
			buttons.add(new MenuButton(Game.GAME_WIDTH / 2, (int) (yPos * Game.SCALE), i - 1, state));
			yPos += 35;
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

		game.getPlaying().getLevelManager().getCurrentLevel().drawBackground(g, bgImg, lmImg, msImg, smImg, bgOffsetX);
		g.drawImage(menuImg, mX, mY, mW, mH, null);
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
		for(MenuButton b : buttons) {
			if(isInteractingWith(e, b)) {
				b.setMousePressed(true);
				break;
			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		for(MenuButton b : buttons) {
			if(isInteractingWith(e, b)) {
				if(b.isMousePressed()) {
					b.applyState();

					if(b.getState().equals(GameState.PLAYING))
						game.getPlaying().unpause();
				}
				break;
			}
		}
		buttons.forEach(MenuButton::reset);
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		buttons.forEach(b -> b.setMouseOver(false));

		for(MenuButton b : buttons) {
			if(isInteractingWith(e, b)) {
				b.setMouseOver(true);
				break;
			}
		}
	}

	@Override
	public void mouseDragged(MouseEvent e) {}
}
