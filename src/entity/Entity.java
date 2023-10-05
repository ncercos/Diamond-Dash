package entity;

/**
 * Written by Nicholas Cercos
 * Created on Oct 04 2023
 **/
public class Entity extends Hitbox {

	private final int MAX_HEALTH = 10;
	private int health = MAX_HEALTH;

	public Entity(double x, double y, double w, double h) {
		super(x, y, w, h);
	}

	public Entity(Hitbox hitbox) {
		this(hitbox.getX(), hitbox.getY(), hitbox.getWidth(), hitbox.getHeight());
	}

	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		if(health > MAX_HEALTH) health = MAX_HEALTH;
		this.health = health;
	}
}
