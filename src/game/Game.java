package game;

import entities.Player;

import java.awt.*;

/**
 * Written by Nicholas Cercos
 * Created on Oct 04 2023
 **/
public class Game implements Runnable {

	// Screen
	private final GameWindow gameWindow;
	private final GamePanel gamePanel;

	// Loop
	private Thread gameThread;
	private final int MAX_FPS = 120;

	// Entities
	private final Player player;

	public Game() {
		player = new Player(0, 0, 48, 60);
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
				player.handleMovement();

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
	 * Draws assets to the scene.
	 *
	 * @param g The graphics context.
	 */
	public void render(Graphics g) {
		player.draw(g);
	}

	public Player getPlayer() {
		return player;
	}
}
