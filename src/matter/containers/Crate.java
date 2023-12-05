package matter.containers;

import game.Game;
import matter.Container;
import matter.Item;
import matter.items.EnergyDrink;
import matter.items.Medicine;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Written by Nicholas Cercos
 * Created on Dec 04 2023
 **/
public class Crate extends Container {

	public Crate(Game game, double x, double y) {
		super(game, "crate", x, y);
	}

	@Override
	public void update() {
		if(animation.isCycleCompleted())
			remove();

		if(!animation.isFrozen())return;
		if(isUsingAttack() || isUsingRoll()) {
			open();
			game.getPlaying().getLevelManager().getCurrentLevel()
					.spawnItem(getRandomUtilityItem());
		}
	}

	/**
	 * Gets a random item between the medicine and energy drink.
	 * This will spawn when the crate is broken.
	 *
	 * @return Medical-use item for utility.
	 */
	private Item getRandomUtilityItem() {
		Item item = null;
		int num = ThreadLocalRandom.current().nextInt(2) + 1;

		switch (num) {
			case 1 -> item = new Medicine   (game, getX(), getY());
			case 2 -> item = new EnergyDrink(game, getX(), getY());
		}
		return item;
	}
}
