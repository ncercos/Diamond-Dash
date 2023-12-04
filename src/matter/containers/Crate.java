package matter.containers;

import entities.Player;
import game.Game;
import levels.LevelLayer;
import levels.LevelManager;
import matter.Matter;
import sprites.Animation;

/**
 * Written by Nicholas Cercos
 * Created on Dec 04 2023
 **/
public class Crate extends Matter {

	private final LevelLayer layer = LevelLayer.CONTAINERS;

	public Crate(Game game, double x, double y) {
		super(game, x, y, 16, 11, 0, 5);
	}

	@Override
	public void update() {
		Player player = game.getPlaying().getPlayer();
		LevelManager lm = game.getPlaying().getLevelManager();

		Animation animation = lm.getTileAnimation(layer, getTileIndex(layer));
		if(animation == null)return;

		// Removes crate after it's broken
		if(animation.isCycleCompleted())
			lm.getCurrentLevel().removeContainer(this);

		// Breaks crate
		if(!animation.isFrozen())return;
		boolean attacking = player.isAttacking() && player.getCurrentAnimation().getCurrentIndex() == player.getAttackPoseIndex() && player.getAttackBox().overlaps(this);
		boolean rolling = player.isRolling() && player.overlaps(this);
		if(attacking || rolling) {
			animation.setRepeatable(false);
			animation.setFrozen(false);
		}
	}
}
