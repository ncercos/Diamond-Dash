package game;

import inputs.KeyboardInputs;
import inputs.MouseInputs;

import javax.swing.*;
import java.awt.*;

import static game.Game.GAME_HEIGHT;
import static game.Game.GAME_WIDTH;

/**
 * Written by Nicholas Cercos
 * Created on Oct 04 2023
 **/
public class GamePanel extends JPanel {

	private final Game game;
	private Image scene;
	private Graphics pen;

	public GamePanel(Game game) {
		this.game = game;
		MouseInputs mouseInputs = new MouseInputs(game);
		setPreferredSize(new Dimension(GAME_WIDTH, GAME_HEIGHT));
		addKeyListener(new KeyboardInputs(game));
		addMouseListener(mouseInputs);
		addMouseMotionListener(mouseInputs);
		setFocusable(true);
	}

	/**
	 * Renders visuals to display.
	 *
	 * @param g The graphics context.
	 */
	public void paintComponent(Graphics g) {
		if(scene == null) {
			scene = createImage(GAME_WIDTH, GAME_HEIGHT);
			pen = scene.getGraphics();
		}
		pen.clearRect(0, 0, GAME_WIDTH, GAME_HEIGHT);
		game.draw(pen);
		g.drawImage(scene, 0, 0, this);
	}

	public Game getGame() {
		return game;
	}
}
