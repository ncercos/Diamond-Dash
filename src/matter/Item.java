package matter;

import game.Game;
import game.states.Playing;

/**
 * Written by Nicholas Cercos
 * Created on Dec 04 2023
 **/
public abstract class Item extends Matter {

	public Item(Game game, double x, double y, double w, double h, double xDrawOffset, double yDrawOffset) {
		super(game, x, y, w, h, xDrawOffset, yDrawOffset);
	}

	@Override
	public void update() {
		Playing playing = game.getPlaying();
		if(!overlaps(playing.getPlayer()))return;
		playing.getLevelManager().getCurrentLevel().removeItem(this);
		onCollide();
	}
}
