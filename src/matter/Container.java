package matter;

import entities.Player;
import game.Game;
import levels.LevelLayer;
import sprites.Animation;

/**
 * Written by Nicholas Cercos
 * Created on Dec 04 2023
 **/
public abstract class Container extends Matter {

	private Animation animation;

	public Container(Game game, double x, double y, double w, double h, double xDrawOffset, double yDrawOffset) {
		super(game, x, y, w, h, xDrawOffset, yDrawOffset);
	}

	/**
	 * Opens the container by starting its animation.
	 */
	public void open() {
		getAnimation().setRepeatable(false);
		getAnimation().setFrozen(false);
	}

	/**
	 * Removes the container tile from the world.
	 */
	public void remove() {
		game.getPlaying().getLevelManager().getCurrentLevel().removeContainer(this);
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

	/**
	 * @return The container's tile animation.
	 */
	public Animation getAnimation() {
		if(animation == null) {
			LevelLayer layer = LevelLayer.CONTAINERS;
			animation = game.getPlaying().getLevelManager().getTileAnimation(layer, getTileIndex(layer));
		}
		return animation;
	}
}
