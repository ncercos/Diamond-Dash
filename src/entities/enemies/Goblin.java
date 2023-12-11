package entities.enemies;

import entities.Hostile;
import entities.Player;
import game.Game;
import game.states.Playing;
import sounds.Sound;
import sprites.Pose;

/**
 * Written by Nicholas Cercos
 * Created on Nov 29 2023
 **/
public class Goblin extends Hostile {

	private boolean scared;

	public Goblin(Playing playing, double x, double y) {
		super(playing, "goblin", x, y,
				8, 11,
				32,
				12, 20.5,
				15);
		attackDamage = 15;
		attackPoseIndex = 3;
		scared = false;
		setAttackDistance(Game.TILES_SIZE / 2);
	}

	@Override
	public void update() {
		super.update();
		if(isDying() || isHurt() || inAir)return;
		Player player = playing.getPlayer();

		// Attack a player, if possible.
		if(isInSight(player) && isAbleToAttack()) {
			turnTowards(player);

			if(!isAttacking() && isInAttackRange(player)) {
				scared = true;
				setCurrentPose(Pose.ATTACK);
				resetAttackDelay();
				return;
			}
		}

		// Run away after attacking.
		if(scared && !isAttacking()) {
			facingLeft = !facingLeft;
			scared = false;
		}

		// Patrol between two edges of a platform or floor.
		if(!isAttacking()) {
			double speed = 1;
			if (facingLeft) goLT(speed);
			else 						goRT(speed);

			if (canMoveTo(getKineticX(), y)) {
				if (getLevel().isSolid(getKineticX() + (facingLeft ? 0 : w), y + h + 1)) {
					move();
					return;
				}
			}
			facingLeft = !facingLeft;
		}
	}
}
