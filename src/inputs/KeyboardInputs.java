package inputs;

import game.Game;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Written by Nicholas Cercos
 * Created on Oct 04 2023
 **/
public class KeyboardInputs implements KeyListener {

	private final Game game;

	public KeyboardInputs(Game game) {
		this.game = game;
	}

	@Override
	public void keyTyped(KeyEvent e) {}

	@Override
	public void keyPressed(KeyEvent e) {
		game.setPressing(e.getKeyCode(), true);
	}

	@Override
	public void keyReleased(KeyEvent e) {
		game.setPressing(e.getKeyCode(), false);
	}
}
