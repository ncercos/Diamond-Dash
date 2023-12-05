package matter.items;

import entities.Player;
import game.Game;
import matter.Item;

/**
 * Written by Nicholas Cercos
 * Created on Dec 04 2023
 **/
public class Medicine extends Item {
	public Medicine(Game game, double x, double y) {
		super(game, "medicine", x, y, 7, 13, 4, 1, 7);
	}

	@Override
	public boolean onCollide() {
		Player player = game.getPlaying().getPlayer();
		if(player.getHealth() >= player.getMaxHealth() || player.isRegenerating())
			return false;

		player.setRegenerating(true);
		return true;
	}
}
