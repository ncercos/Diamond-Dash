package game;

import javax.swing.*;

/**
 * Written by Nicholas Cercos
 * Created on Oct 04 2023
 **/
public class GameWindow {

	private final JFrame jframe;

	public GameWindow(GamePanel gamePanel) {
		jframe = new JFrame();
		jframe.setSize(400, 400);
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jframe.add(gamePanel);
		jframe.setLocationRelativeTo(null); /* spawn center stage */
		jframe.setResizable(false);
		jframe.pack();
		jframe.setVisible(true);
	}

	public JFrame getJFrame() {
		return jframe;
	}
}
