package matter.items;

import entities.Player;
import game.states.Playing;
import levels.Level;
import matter.Item;

/**
 * Written by Nicholas Cercos
 * Created on Nov 21 2023
 **/
public class Diamond extends Item {

	public Diamond(Playing playing, Level level, double x, double y) {
		super(playing, "diamond", x, y, 8, 7, 3, 4, 10);
		level.addDiamond();
	}

	@Override
	public boolean onCollide() {
		Player player = playing.getPlayer();
		player.foundDiamond();
		return true;
	}
}
