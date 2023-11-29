package game;

import game.states.Playing;
import game.states.Menu;
import game.states.State;

import java.awt.*;

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
	public static final String RESOURCE_URL = "./res/";

	public Game() {
		playing = new Playing(this);
		menu = new Menu(this);
		gamePanel = new GamePanel(this);
		gameWindow = new GameWindow(gamePanel);
		gamePanel.requestFocus();
		init();
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
		while(true) {

			update();
			gamePanel.repaint();

			try {
				Thread.sleep(15);
			} catch (InterruptedException ignored) {}
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
}
