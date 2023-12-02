package game.states;

import entities.Hostile;
import entities.Player;
import entities.enemies.Goblin;
import game.Game;
import inputs.Input;
import levels.LevelManager;
import ui.HudOverlay;
import ui.PauseOverlay;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Written by Nicholas Cercos
 * Created on Nov 27 2023
 **/
public class Playing extends State {

	// Entities
	private final Player player;
	private final List<Hostile> enemies;

	// Levels
	private final LevelManager levelManager;

	// Paused
	private boolean paused;
	private final PauseOverlay pauseOverlay;

	// HUD
	private final HudOverlay hudOverlay;

	public Playing(Game game) {
		super(game);
		paused = false;
		levelManager = new LevelManager(game);
		player = new Player(game, 0, 0);
		pauseOverlay = new PauseOverlay(this);
		hudOverlay = new HudOverlay(player);
		enemies = new ArrayList<>();
		enemies.add(new Goblin(game, 50, 0));
	}

	/**
	 * Renders all mobile entities that will spawn
	 * on the set "mob" layer.
	 *
	 * @param g The graphics context.
	 */
	public void drawMobs(Graphics g) {
		enemies.forEach(e -> e.draw(g));
		player.draw(g);
	}

	/**
	 * Updates all mobile entities in the world.
	 */
	private void updateMobs() {
		Iterator<Hostile> it = enemies.iterator();
		while(it.hasNext()) {
			Hostile enemy = it.next();
			if(enemy.isActive())
				enemy.update();
			else it.remove();
		}
		player.update();
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
			updateMobs();
			hudOverlay.update();

			// Player attacks enemy
			if(player.isAttacking() && player.getCurrentAnimation().isCycleCompleted()) {
				for(Hostile e : enemies) {
					if(player.getAttackBox().overlaps(e)) {
						player.attack(e);
						return;
					}
				}
			}

			// Enemy attacks player

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
