package entities.enemies;

import entities.Hostile;
import entities.Player;
import game.Game;
import sprites.Pose;
import utils.Location;

/**
 * Written by Nicholas Cercos
 * Created on Dec 05 2023
 **/
public class Flower extends Hostile {

	public Flower(Game game, double x, double y) {
		super(game, "flower", x, y,
				10, 13,
				32,
				11, 18.5,
				20);
		attackDamage = 25;
		attackPoseIndex = 4;
		takesKnockback = false;
	}

	@Override
	public void update() {
		super.update();
		if(isDying() || isHurt() || inAir)return;
		Player player = game.getPlaying().getPlayer();

		if(!isAttacking() && isAbleToAttack() && isInAttackRange(player)) {
			setCurrentPose(Pose.ATTACK);
			resetAttackDelay();
		}
	}

	@Override
	public void updateAttackBox() {
		attackBox.teleport(new Location(x, y - h));
	}
}
