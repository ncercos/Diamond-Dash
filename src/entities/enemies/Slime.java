package entities.enemies;

import entities.Hostile;
import entities.Player;
import game.Game;

/**
 * Written by Nicholas Cercos
 * Created on Dec 05 2023
 **/
public class Slime extends Hostile {
	public Slime(Game game, double x, double y) {
		super(game, "slime", x, y,
				14, 11,
				32,
				9, 20,
				10);
		attackDamage = 10;
		attackDistance = 5;
		sightDistance = Game.TILES_SIZE * 5;
	}

	@Override
	public void update() {
		super.update();
		if(isDying() || isHurt() || inAir)return;
		Player player = game.getPlaying().getPlayer();

		if(isInSight(player)) {

			if(!isInAttackRange(player)) {
				turnTowards(player);

				double speed = 1;
				if (facingLeft) goLT(speed);
				else goRT(speed);

				if (canMoveTo(getKineticX(), y)) {
					if (getLevel().isSolid(getKineticX() + (facingLeft ? 0 : w), y + h + 1)) {
						move();
						return;
					}
				}
				facingLeft = !facingLeft;
			} else if(isAbleToAttack()) {
				if(player.overlaps(this))
					attack(player);
			}
		}
	}
}
