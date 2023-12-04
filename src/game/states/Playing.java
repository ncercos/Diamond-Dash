package game.states;

import entities.Hostile;
import entities.Player;
import game.Game;
import inputs.Input;
import levels.Level;
import levels.LevelManager;
import ui.Overlay;
import ui.overlays.DeadOverlay;
import ui.overlays.HudOverlay;
import ui.overlays.PauseOverlay;

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

	private boolean paused;
	private final Overlay[] overlays;

	public Playing(Game game) {
		super(game);
		paused = false;
		levelManager = new LevelManager(game);
		player = new Player(game, levelManager.getCurrentLevel().getSpawn());
		overlays = new Overlay[] {
				new PauseOverlay(this),
				new HudOverlay(this),
				new DeadOverlay(this)
		};
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
		for(Overlay overlay : overlays)
			overlay.update();

		if(paused)return;
		Level level = levelManager.getCurrentLevel();
		level.update();
		player.update();

		// Combat between mobs
		for(Hostile e : level.getEnemies()) {
			if(player.isAttacking() && player.getAttackBox().overlaps(e))
				player.attack(e);

			if(e.isAttacking() && e.getAttackBox().overlaps(player) && !player.isRolling())
				e.attack(player);
		}
	}

	@Override
	public void draw(Graphics g) {
		levelManager.draw(g);
		for(Overlay overlay : overlays)
			overlay.draw(g);
	}

	@Override
	public void lostFocus() {
		player.resetVelocity();
		player.resetBinds();
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(player.isActive() && e.getKeyCode() == Input.ESC) togglePause();
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
		for(Overlay overlay : overlays)
			overlay.mousePressed(e);

		if(!paused)
			player.setClicking(e.getButton(), true);
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		for(Overlay overlay : overlays)
			overlay.mouseReleased(e);

		if(!paused)
			player.setClicking(e.getButton(), false);
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		for(Overlay overlay : overlays)
			overlay.mouseMoved(e);
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		for(Overlay overlay : overlays)
			overlay.mouseDragged(e);
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
