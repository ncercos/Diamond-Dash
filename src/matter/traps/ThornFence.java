package matter.traps;

import game.states.Playing;
import matter.Trap;
import sounds.Sound;

/**
 * Written by Nicholas Cercos
 * Created on Nov 23 2023
 **/
public class ThornFence extends Trap {

	public ThornFence(Playing playing, double x, double y, boolean facesLeft) {
		super(playing,
				x, y,
				facesLeft ? 12 : 15,
				facesLeft ? 7 : 9,
				facesLeft ? 4 : 0, 0,
				100);
	}

	@Override
	public boolean onCollide() {
		playing.getSoundManager().playSFX(Sound.BRANCH);
		damage(playing.getPlayer());
		return true;
	}
}
