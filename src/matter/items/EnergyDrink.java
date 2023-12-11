package matter.items;

import entities.Player;
import game.Game;
import game.states.Playing;
import matter.Item;
import sounds.Sound;
import utils.Location;

/**
 * Written by Nicholas Cercos
 * Created on Dec 04 2023
 **/
public class EnergyDrink extends Item {

	public EnergyDrink(Playing playing, double x, double y) {
		super(playing, "energy_drink", x, y, 8, 13, 3, 1, 9);
	}

	@Override
	public boolean onCollide() {
		Player player = playing.getPlayer();
		if(player.isBoosted())return false;
		player.setBoosted(true);
		playing.getSoundManager().playSFX(Sound.SPEED);
		return true;
	}
}
