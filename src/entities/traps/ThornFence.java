package entities.traps;

import entities.Matter;
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
	}

	@Override
	public void onCollide() {
		// TODO: Damage the player
	}
}
