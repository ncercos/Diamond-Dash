package entities.enemies;

import entities.Hostile;
import entities.Player;
import game.Game;
import sprites.Pose;

/**
 * Written by Nicholas Cercos
 * Created on Nov 29 2023
 **/
public class Goblin extends Hostile {

	private boolean facingLeft;
	public Goblin(Game game, double x, double y) {
		super(game, "goblin", x, y,
				11 * Game.SCALE,
				11 * Game.SCALE,
				32,
				10,
				21);
	}

	@Override
	public void update() {
		super.update();
		Player player = game.getPlaying().getPlayer();

		if(isInSight(player)) {
			turnTowards(player);

			if(!isAttacking() && isInAttackRange(player))
				setCurrentPose(Pose.ATTACK);
		}

		if(!isAttacking()) {
			double speed = 1;
			if (facingLeft) goLT(speed);
			else 						goRT(speed);

			if (canMoveTo(getKineticX(), y)) {
				if (getLevel().isSolid(getKineticX() + (facingLeft ? 0 : w), y + h + 1)) {
					setX(getKineticX());
					return;
				}
			}
			facingLeft = !facingLeft;
		}
	}

	/**
	 * Faces the goblin in the correct direction
	 * based on the location of the player.
	 *
	 * @param player The player object.
	 */
	private void turnTowards(Player player) {
		facingLeft = player.getX() < x;
	}
}
