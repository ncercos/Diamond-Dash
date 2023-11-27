package game.states;

import entities.Player;
import game.Game;
import game.GameState;
import inputs.Input;
import levels.LevelManager;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import static game.Game.SCALE;

/**
 * Written by Nicholas Cercos
 * Created on Nov 27 2023
 **/
public class InGame extends State {

	// Entities
	private final Player player;

	// Levels
	private final LevelManager levelManager;

	public InGame(Game game) {
		super(game);
		levelManager = new LevelManager(game);
		player = new Player(game, 0, 0, 13 * SCALE, 13 * SCALE, 14);
	}

	@Override
	public void update() {
		player.update();
	}

	@Override
	public void draw(Graphics g) {
		levelManager.draw(g);
	}

	@Override
	public void lostFocus() {
		player.resetVelocity();
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == Input.ESC) {
			GameState.current = GameState.MENU;
			return;
		}
		player.setPressing(e.getKeyCode(), true);
	}

	@Override
	public void keyReleased(KeyEvent e) {
		player.setPressing(e.getKeyCode(), false);
	}

	@Override
	public void mouseClicked(MouseEvent e) {}

	@Override
	public void mousePressed(MouseEvent e) {}

	@Override
	public void mouseReleased(MouseEvent e) {}

	@Override
	public void mouseMoved(MouseEvent e) {}

	public Player getPlayer() {
		return player;
	}

	public LevelManager getLevelManager() {
		return levelManager;
	}
}
