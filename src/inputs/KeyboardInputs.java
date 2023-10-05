package inputs;

import entity.Player;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Written by Nicholas Cercos
 * Created on Oct 04 2023
 **/
public class KeyboardInputs implements KeyListener {

	private final Player player;

	public KeyboardInputs(Player player) {
		this.player = player;
	}

	@Override
	public void keyTyped(KeyEvent e) {}

	@Override
	public void keyPressed(KeyEvent e) {
		player.setPressing(e.getKeyCode(), true);
	}

	@Override
	public void keyReleased(KeyEvent e) {
		player.setPressing(e.getKeyCode(), false);
	}
}
