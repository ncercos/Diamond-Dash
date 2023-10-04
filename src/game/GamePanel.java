package game;

import inputs.KeyboardInputs;

import javax.swing.*;
import java.awt.*;

/**
 * Written by Nicholas Cercos
 * Created on Oct 04 2023
 **/
public class GamePanel extends JPanel {

	public GamePanel(Game game) {
		addKeyListener(new KeyboardInputs(game));
	}

	/**
	 * Renders visuals to display.
	 *
	 * @param g The graphics context.
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		g.fillRect(100, 100, 200, 50);
	}
}
