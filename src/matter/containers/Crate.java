package matter.containers;

import game.Game;
import game.states.Playing;
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

	public Crate(Playing playing, double x, double y) {
		super(playing, "crate", x, y);
	}

	@Override
	public void update() {
		if(animation.isCycleCompleted())
			remove();

		if(!animation.isFrozen())return;
		if(isUsingAttack() || isUsingRoll()) {
			open();
			playing.getLevelManager().getCurrentLevel()
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
			case 1 -> item = new Medicine   (playing, getX(), getY());
			case 2 -> item = new EnergyDrink(playing, getX(), getY());
		}
		return item;
	}
}
