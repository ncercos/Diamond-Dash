package game;

import game.states.Playing;
import game.states.Menu;
import game.states.State;
import sounds.SoundManager;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Written by Nicholas Cercos
 * Created on Oct 04 2023
 **/
public class Game implements Runnable {

	// Screen
	private final GameWindow gameWindow;
	private final GamePanel gamePanel;

	public final static int TILES_DEFAULT_SIZE = 16;
	public final static float SCALE = 3f;
	public final static int TILES_IN_WIDTH = 24;
	public final static int TILES_IN_HEIGHT = 14;
	public final static int TILES_SIZE = (int) (TILES_DEFAULT_SIZE * SCALE);
	public final static int GAME_WIDTH = TILES_SIZE * TILES_IN_WIDTH;
	public final static int GAME_HEIGHT = TILES_SIZE * TILES_IN_HEIGHT;

	// States
	private final Menu menu;
	private final Playing playing;

	// Utils
	private SoundManager soundManager;

	public Game() {
		soundManager = new SoundManager();
		playing = new Playing(this);
		menu = new Menu(this);
		gamePanel = new GamePanel(this);
		gameWindow = new GameWindow(gamePanel);
		gamePanel.requestFocus();
		init();
	}

	/**
	 * Loads any sprite within the game's resource directory.
	 *
	 * @param path The path starting from the res folder.
	 * @return An image, if it exists, otherwise null.
	 */
	public static BufferedImage loadSprite(String path) {
		try {
			return ImageIO.read(Game.class.getResourceAsStream("/" + path));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Initializes the game loop and starts all services.
	 */
	private void init() {
		new Thread(this).start();
	}

	/**
	 * Everything that needs to be updated and re-rendered.
	 */
	private void update() {
		State state = getCurrentState();
		if(state == null) {
			System.exit(0);
			return;
		}
		state.update();
	}

	/**
	 * Renders assets to the scene.
	 *
	 * @param g The graphics context.
	 */
	public void draw(Graphics g) {
		getCurrentState().draw(g);
	}

	/**
	 * Handler for when the game window loses focus.
	 */
	public void lostFocus() {
		getCurrentState().lostFocus();
	}

	/**
	 * The game loop.
	 */
	@Override
	public void run() {
		double timePerFrame = 1000000000.0 / 60;
		long lastFrame = System.nanoTime();
		long now;

		while(true) {
			now = System.nanoTime();
			if(now - lastFrame >= timePerFrame) {
				update();
				gamePanel.repaint();
				lastFrame = now;
			}
		}
	}

	/**
	 * @return The game's current state.
	 */
	public State getCurrentState() {
		switch (GameState.current) {
			case MENU ->   {  return menu;   }
			case PLAYING -> { return playing; }
		}
		return null;
	}

	public Playing getPlaying() {
		return playing;
	}

	public SoundManager getSoundManager() {
		return soundManager;
	}
}
