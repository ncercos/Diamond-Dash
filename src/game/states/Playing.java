package game.states;

import entities.Hostile;
import entities.Player;
import game.Game;
import inputs.Input;
import levels.Level;
import levels.LevelManager;
import ui.HudOverlay;
import ui.PauseOverlay;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

/**
 * Written by Nicholas Cercos
 * Created on Nov 27 2023
 **/
public class Playing extends State {

	private final Player player;
	private final LevelManager levelManager;
	private final PauseOverlay pauseOverlay;
	private final HudOverlay hudOverlay;

	private boolean paused;

	public Playing(Game game) {
		super(game);
		paused = false;
		levelManager = new LevelManager(game);
		player = new Player(game, levelManager.getCurrentLevel().getSpawn());
		pauseOverlay = new PauseOverlay(this);
		hudOverlay = new HudOverlay(player);
	}

	/**
	 * Resumes / Stops the game immediately.
	 * All key's pressed will be reset to prevent sticky keys.
	 */
	public void togglePause() {
		player.resetBinds();
		paused = !paused;
	}

	@Override
	public void update() {
		if(paused) pauseOverlay.update();
		else {
			Level level = levelManager.getCurrentLevel();
			level.update();
			player.update();
			hudOverlay.update();

			// Combat between mobs
			for(Hostile e : level.getEnemies()) {
				if(player.isAttacking() && player.getAttackBox().overlaps(e))
					player.attack(e);

				if(e.isAttacking() && e.getAttackBox().overlaps(player) && !player.isRolling())
					e.attack(player);
			}
		}
	}

	@Override
	public void draw(Graphics g) {
		levelManager.draw(g);
		hudOverlay.draw(g);
		if(paused) pauseOverlay.draw(g);
	}

	@Override
	public void lostFocus() {
		player.resetVelocity();
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == Input.ESC) togglePause();
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
		if(paused) pauseOverlay.mousePressed(e);
		else player.setClicking(e.getButton(), true);
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if(paused) pauseOverlay.mouseReleased(e);
		else player.setClicking(e.getButton(), false);
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		if(!paused)return;
		pauseOverlay.mouseMoved(e);
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		if(!paused)return;
		pauseOverlay.mouseDragged(e);
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
