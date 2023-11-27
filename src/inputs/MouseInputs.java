package inputs;

import game.Game;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

/**
 * Written by Nicholas Cercos
 * Created on Nov 27 2023
 **/
public class MouseInputs implements MouseListener, MouseMotionListener {

	private final Game game;

	public MouseInputs(Game game) {
		this.game = game;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		game.getCurrentState().mouseClicked(e);
	}

	@Override
	public void mousePressed(MouseEvent e) {
		game.getCurrentState().mousePressed(e);
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		game.getCurrentState().mouseReleased(e);
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		game.getCurrentState().mouseMoved(e);
	}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}

	@Override
	public void mouseDragged(MouseEvent e) {}
}
