package matter;

import game.Game;
import game.states.Playing;

/**
 * Written by Nicholas Cercos
 * Created on Dec 04 2023
 **/
public abstract class Item extends Matter {

	public Item(Playing playing, String name, double x, double y, double w, double h, double xDrawOffset, double yDrawOffset, int duration) {
		super(playing, "items/" + name, x, y, w, h, xDrawOffset, yDrawOffset, 15, duration);
		this.x += Game.TILES_SIZE / 4.0;
	}

	@Override
	public void update() {
		if(!overlaps(playing.getPlayer()))return;
		if(onCollide())
			playing.getLevelManager().getCurrentLevel()
					.removeItem(this);
	}
}
