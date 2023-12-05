package matter.items;

import entities.Player;
import game.Game;
import matter.Item;

/**
 * Written by Nicholas Cercos
 * Created on Nov 21 2023
 **/
public class Diamond extends Item {

	public Diamond(Game game, double x, double y) {
		super(game, "diamond", x, y, 8, 7, 3, 4, 10);
	}

	@Override
	public boolean onCollide() {
		Player player = game.getPlaying().getPlayer();
		player.foundDiamond();
		return true;
	}
}
