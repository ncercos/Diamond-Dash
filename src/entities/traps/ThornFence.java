package entities.traps;

import entities.Matter;
import entities.Player;
import game.Game;

/**
 * Written by Nicholas Cercos
 * Created on Nov 23 2023
 **/
public class ThornFence extends Matter {

	public ThornFence(Game game, double x, double y, boolean facesLeft) {
		super(game,
				x, y,
				facesLeft ? 12 : 15,
				facesLeft ? 7 : 9,
				facesLeft ? 4 : 0, 0);
		attackDamage = 100;
	}

	@Override
	public void onCollide() {
		Player player = game.getPlaying().getPlayer();
		if(player.isDying() || !player.isActive())return;
		attack(game.getPlaying().getPlayer());
	}
}
