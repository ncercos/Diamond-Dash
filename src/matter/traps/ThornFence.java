package matter.traps;

import game.Game;
import matter.Trap;

/**
 * Written by Nicholas Cercos
 * Created on Nov 23 2023
 **/
public class ThornFence extends Trap {

	public ThornFence(Game game, double x, double y, boolean facesLeft) {
		super(game,
				x, y,
				facesLeft ? 12 : 15,
				facesLeft ? 7 : 9,
				facesLeft ? 4 : 0, 0,
				100);
	}

	@Override
	public void onCollide() {
		damage(game.getPlaying().getPlayer());
	}
}
