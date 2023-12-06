package entities.enemies;

import entities.Hostile;
import entities.Player;
import game.states.Playing;
import sprites.Pose;
import utils.Location;

/**
 * Written by Nicholas Cercos
 * Created on Dec 05 2023
 **/
public class Flower extends Hostile {

	public Flower(Playing playing, double x, double y) {
		super(playing, "flower", x, y,
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
		Player player = playing.getPlayer();

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
