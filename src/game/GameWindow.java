package game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.io.File;

/**
 * Written by Nicholas Cercos
 * Created on Oct 04 2023
 **/
public class GameWindow {

	private final JFrame jframe;

	public GameWindow(GamePanel gamePanel) {
		jframe = new JFrame();
		jframe.setTitle("Diamond Dash");
		jframe.setIconImage(Toolkit.getDefaultToolkit().getImage(Game.RESOURCE_URL + "ui/icon.png"));
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jframe.add(gamePanel);
		jframe.setResizable(false);
		jframe.pack();
		jframe.setLocationRelativeTo(null); /* spawn center stage */
		jframe.setVisible(true);

		jframe.addWindowFocusListener(new WindowFocusListener() {
			@Override
			public void windowGainedFocus(WindowEvent e) {}

			@Override
			public void windowLostFocus(WindowEvent e) {
				gamePanel.getGame().lostFocus();
			}
		});
	}

	public JFrame getJFrame() {
		return jframe;
	}
}
