package game;

/**
 * Written by Nicholas Cercos
 * Created on Oct 04 2023
 **/
public class Game implements Runnable {

	private GameWindow gameWindow;
	private GamePanel gamePanel;

	private Thread gameThread;
	private final int MAX_FPS = 60;

	private boolean[] pressing = new boolean[1024];

	public Game() {
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

			long now = System.nanoTime();
			if(now - lastFrame >= timePerFrame) {
				gamePanel.repaint();
				lastFrame = now;
				frames++;
			}

			if(System.currentTimeMillis() - lastCheck >= 1000) {
				lastCheck = System.currentTimeMillis();
				System.out.println("FPS: " + frames);
				frames = 0;
			}
		}

	}

	/**
	 * Updates the value for a key interaction.
	 *
	 * @param keyCode The code of the interacted key.
	 * @param value   Whether the key was pressed or released.
	 */
	public void setPressing(int keyCode, boolean value) {
		pressing[keyCode] = value;
	}
}
