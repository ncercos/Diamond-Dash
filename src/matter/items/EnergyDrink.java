package matter.items;

import entities.Player;
import game.Game;
import matter.Item;
import utils.Location;

/**
 * Written by Nicholas Cercos
 * Created on Dec 04 2023
 **/
public class EnergyDrink extends Item {

	public EnergyDrink(Game game, double x, double y) {
		super(game, "energy_drink", x, y, 8, 13, 3, 1, 9);
	}

	@Override
	public boolean onCollide() {
		Player player = game.getPlaying().getPlayer();
		if(player.isBoosted())return false;
		player.setBoosted(true);
		return true;
	}
}
