package game;

import inputs.KeyboardInputs;

import javax.swing.*;
import java.awt.*;

/**
 * Written by Nicholas Cercos
 * Created on Oct 04 2023
 **/
public class GamePanel extends JPanel {

	private final int WIDTH = 1280;
	private final int HEIGHT = 800;

	private final Game game;
	private Image scene;
	private Graphics pen;

	public GamePanel(Game game) {
		this.game = game;
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		addKeyListener(new KeyboardInputs(game.getPlayer()));
		setFocusable(true);
	}

	/**
	 * Renders visuals to display.
	 *
	 * @param g The graphics context.
	 */
	public void paintComponent(Graphics g) {
		if(scene == null) {
			scene = createImage(WIDTH, HEIGHT);
			pen = scene.getGraphics();
		}
		pen.clearRect(0, 0, WIDTH, HEIGHT);
		game.render(pen);
		g.drawImage(scene, 0, 0, this);
	}
}
