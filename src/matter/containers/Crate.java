package matter.containers;

import game.Game;
import matter.Container;

/**
 * Written by Nicholas Cercos
 * Created on Dec 04 2023
 **/
public class Crate extends Container {

	public Crate(Game game, double x, double y) {
		super(game, x, y, 16, 11, 0, 5);
	}

	@Override
	public void update() {
		if(getAnimation().isCycleCompleted())
			remove();

		if(!getAnimation().isFrozen())return;
		if(isUsingAttack() || isUsingRoll())
			open();
	}
}
