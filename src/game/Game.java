package game;

import entities.Hitbox;
import entities.Player;
import levels.Level;
import levels.LevelManager;

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
	public final static float SCALE = 2.5f;
	public final static int TILES_IN_WIDTH = 39;
	public final static int TILES_IN_HEIGHT = 21;
	public final static int TILES_SIZE = (int) (TILES_DEFAULT_SIZE * SCALE);
	public final static int GAME_WIDTH = TILES_SIZE * TILES_IN_WIDTH;
	public final static int GAME_HEIGHT = TILES_SIZE * TILES_IN_HEIGHT;

	// Loop
	private Thread gameThread;
	private final static int MAX_FPS = 120;

	// Entities
	private final Player player;

	// Levels
	private final LevelManager levelManager;

	public Game() {
		levelManager = new LevelManager(this);
		player = new Player(0, 0, 16 * SCALE, 20 * SCALE);
		gamePanel = new GamePanel(this);
		gameWindow = new GameWindow(gamePanel);
		gamePanel.requestFocus();
		init();
	}

	/**
	 * Initializes the game loop and starts all services.
	 */
	private void init() {
		gameThread = new Thread(this);
		gameThread.start();
	}

	@Override
	public void run() {
		double timePerFrame = 1000000000.0 / MAX_FPS;
		long lastFrame = System.nanoTime();

		int frames = 0;
		long lastCheck = System.currentTimeMillis();

		while(true) {
			// Redraws the screen MAX_FPS times per second.
			long now = System.nanoTime();
			if(now - lastFrame >= timePerFrame) {
				Level level = levelManager.getCurrentLevel();
				player.handleMovement(player.standingOnAny(level.getHitboxes().toArray(new Hitbox[0])));

				for(Hitbox hb : level.getHitboxes()) {
					if(player.overlaps(hb)) {
						player.pushedOutOf(hb);
						player.applyFrictionWithFloor();
						player.stopFalling();
					}
				}

				gamePanel.repaint();
				lastFrame = now;
				frames++;
			}

			// Displays FPS in console once per second.
			if(System.currentTimeMillis() - lastCheck >= 1000) {
				lastCheck = System.currentTimeMillis();
				System.out.println("FPS: " + frames);
				frames = 0;
			}
		}
	}

	/**
	 * Renders assets to the scene.
	 *
	 * @param g The graphics context.
	 */
	public void draw(Graphics g) {
		levelManager.draw(g);
		player.draw(g);
	}

	public Player getPlayer() {
		return player;
	}
}
