package matter;

import entities.Player;
import game.states.Playing;

/**
 * Written by Nicholas Cercos
 * Created on Dec 04 2023
 **/
public abstract class Container extends Matter {

	private final static int DEFAULT_SPRITE_SIZE = 16;

	public Container(Playing playing, String name, double x, double y) {
		super(playing, "containers/" + name, x, y,
				DEFAULT_SPRITE_SIZE,
				DEFAULT_SPRITE_SIZE,
				0, 0,
				DEFAULT_SPRITE_SIZE, 4);

		animation.setFrozen(true)
						 .setRepeatable(false);
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
		playing.getLevelManager().getCurrentLevel()
				.destroyContainer(this);
	}

	/**
	 * @return True if the player is using an attack to open the container.
	 */
	public boolean isUsingAttack() {
		Player player = playing.getPlayer();
		return
				player.isAttacking() &&
				player.getCurrentAnimation().getCurrentIndex() == player.getAttackPoseIndex() &&
				player.getAttackBox().overlaps(this);
	}

	/**
	 * @return True if the player is using a roll to open the container.
	 */
	public boolean isUsingRoll() {
		Player player = playing.getPlayer();
		return
				player.isRolling() &&
				player.overlaps(this);
	}
}
