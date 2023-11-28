package game.states;

import entities.Player;
import game.Game;
import inputs.Input;
import levels.LevelManager;
import ui.PauseOverlay;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import static game.Game.SCALE;

/**
 * Written by Nicholas Cercos
 * Created on Nov 27 2023
 **/
public class Playing extends State {

	// Entities
	private final Player player;

	// Levels
	private final LevelManager levelManager;

	// Paused
	private boolean paused;
	private final PauseOverlay pauseOverlay;

	public Playing(Game game) {
		super(game);
		paused = false;
		levelManager = new LevelManager(game);
		player = new Player(game, 0, 0, 13 * SCALE, 13 * SCALE, 14);
		pauseOverlay = new PauseOverlay(this);
	}

	/**
	 * Resumes the game if it is paused.
	 * All key's pressed will be reset to prevent sticky keys.
	 */
	public void unpause() {
		player.initPressing();
		paused = false;
	}

	@Override
	public void update() {
		if(paused) pauseOverlay.update();
		else player.update();
	}

	@Override
	public void draw(Graphics g) {
		levelManager.draw(g);
		if(paused) pauseOverlay.draw(g);
	}

	@Override
	public void lostFocus() {
		player.resetVelocity();
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == Input.ESC) paused = !paused;
		if(paused)return;
		player.setPressing(e.getKeyCode(), true);
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if(paused)return;
		player.setPressing(e.getKeyCode(), false);
	}

	@Override
	public void mouseClicked(MouseEvent e) {}

	@Override
	public void mousePressed(MouseEvent e) {
		if(!paused)return;
		pauseOverlay.mousePressed(e);
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if(!paused)return;
		pauseOverlay.mouseReleased(e);
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		if(!paused)return;
		pauseOverlay.mouseMoved(e);
	}

	public Player getPlayer() {
		return player;
	}

	public LevelManager getLevelManager() {
		return levelManager;
	}

	public boolean isPaused() {
		return paused;
	}
}
