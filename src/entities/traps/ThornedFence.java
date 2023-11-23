package entities.traps;

import entities.Trap;
import levels.Level;

/**
 * Written by Nicholas Cercos
 * Created on Nov 23 2023
 **/
public class ThornedFence extends Trap {

	public ThornedFence(Level level, double x, double y, boolean facesLeft) {
		super(level,
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
