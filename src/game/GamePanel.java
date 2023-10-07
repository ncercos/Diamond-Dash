package game;

import inputs.KeyboardInputs;

import javax.swing.*;
import java.awt.*;

/**
 * Written by Nicholas Cercos
 * Created on Oct 04 2023
 **/
public class GamePanel extends JPanel {

	private final Game game;

	public GamePanel(Game game) {
		this.game = game;
		setPreferredSize(new Dimension(1280, 800));
		addKeyListener(new KeyboardInputs(game.getPlayer()));
	}

	/**
	 * Renders visuals to display.
	 *
	 * @param g The graphics context.
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		game.getPlayer().draw(g);
	}
}
