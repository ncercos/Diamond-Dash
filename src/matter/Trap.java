package matter;

import entities.Entity;
import game.Game;

/**
 * Written by Nicholas Cercos
 * Created on Dec 03 2023
 **/
public abstract class Trap extends Matter {

	protected int damage;

	public Trap(Game game, double x, double y, double w, double h, double xDrawOffset, double yDrawOffset, int damage) {
		super(game, x, y, w, h, xDrawOffset, yDrawOffset);
		this.damage = damage;
	}

	/**
	 * Damage an entity based on trap's damage.
	 *
	 * @param entity The entity that was hurt.
	 */
	public void damage(Entity entity) {
		if(entity.isDying() || !entity.isActive())return;
		entity.damage(damage);
	}
}
