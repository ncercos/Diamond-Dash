package matter;

import entities.Player;
import game.Game;
import sprites.Animation;

import java.awt.*;

/**
 * Written by Nicholas Cercos
 * Created on Dec 04 2023
 **/
public abstract class Container extends Matter {

	protected Animation animation;
	private final static int DEFAULT_SPRITE_SIZE = 16;

	public Container(Game game, String name, double x, double y) {
		super(game, x, y, DEFAULT_SPRITE_SIZE, DEFAULT_SPRITE_SIZE, 0, 0);
		animation = new Animation("containers/" + name, DEFAULT_SPRITE_SIZE, 4)
				.setFrozen(true)
				.setRepeatable(false);
		debug = false;
	}

	@Override
	public void draw(Graphics g) {
		int lvlOffset = game.getPlaying().getLevelManager().getCurrentLevel().getOffsetX();
		super.draw(g, lvlOffset);
		if(animation == null)return;

		// Draw image if animation exists.
		int SPRITE_SIZE = (int) (DEFAULT_SPRITE_SIZE * Game.SCALE);
		int cx = (int)(x - lvlOffset);
		g.drawImage(animation.getCurrentImage(game.getPlaying()), (int)(cx - xDrawOffset),
				(int)(y - yDrawOffset), SPRITE_SIZE, SPRITE_SIZE, null);
	}

	/**
	 * Opens the container by starting its animation.
	 */
	public void open() {
		animation.setRepeatable(false);
		animation.setFrozen(false);
	}

	/**
	 * Removes the container from the world.
	 */
	public void remove() {
		game.getPlaying().getLevelManager().getCurrentLevel()
				.destroyContainer(this);
	}

	/**
	 * @return True if the player is using an attack to open the container.
	 */
	public boolean isUsingAttack() {
		Player player = game.getPlaying().getPlayer();
		return
				player.isAttacking() &&
				player.getCurrentAnimation().getCurrentIndex() == player.getAttackPoseIndex() &&
				player.getAttackBox().overlaps(this);
	}

	/**
	 * @return True if the player is using a roll to open the container.
	 */
	public boolean isUsingRoll() {
		Player player = game.getPlaying().getPlayer();
		return
				player.isRolling() &&
				player.overlaps(this);
	}
}
