package entities;

import sprites.Animation;
import sprites.Pose;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Written by Nicholas Cercos
 * Created on Oct 04 2023
 **/
public class Entity extends Hitbox {

	private final String name;
	private final int MAX_HEALTH = 10;
	private int health = MAX_HEALTH;

	private final Map<Pose, Animation> animations;
	private Pose currentPose;
	private boolean moving;

	public Entity(String name, double x, double y, double w, double h) {
		super(x, y, w, h);
		this.name = name;
		animations = new HashMap<>();
		currentPose = Pose.WALK_RT;
	}

	public Entity(String name, Hitbox hitbox) {
		this(name, hitbox.getX(), hitbox.getY(), hitbox.getWidth(), hitbox.getHeight());
	}
	@Override
	public void draw(Graphics g) {
		Animation animation = getCurrentAnimation();
		if(animation == null)return;
		if(moving) g.drawImage(animation.getCurrentImage(), (int)getX(), (int)getY(), (int)getWidth(), (int)getHeight(), null);
		else g.drawImage(animation.getStaticImage(), (int)getX(), (int)getY(), (int)getWidth(), (int)getHeight(), null);
	}

	/**
	 * Loads in all animations based on the given poses.
	 *
	 * @param poses A hashmap of poses and the number of sprites within the animation (per pose).
	 */
	protected void loadAnimations(Map<Pose, Integer> poses) {
		poses.forEach((k,v) -> animations.put(k, new Animation(name + "_" + k.getName(), v, 18)));
	}

	/**
	 * Sets the health.
	 * Value cannot be higher than pre-defined max health.
	 *
	 * @param health The amount of health to be given.
	 */
	public void setHealth(int health) {
		if(health > MAX_HEALTH) health = MAX_HEALTH;
		this.health = health;
	}

	public int getHealth() {
		return health;
	}

	/**
	 * @return The current animation based on entity pose. (Defaults to null)
	 */
	public Animation getCurrentAnimation() {
		return animations.getOrDefault(currentPose, null);
	}

	@Override
	public void physicsOFF() {
		super.physicsOFF();
		moving = false;
	}

	@Override
	public void goLT(int dx) {
		super.goLT(dx);
		currentPose = Pose.WALK_LT;
		moving = true;
	}

	@Override
	public void goRT(int dx) {
		super.goRT(dx);
		currentPose = Pose.WALK_RT;
		moving = true;
	}

	@Override
	public void goUP(int dy) {
		super.goUP(dy);
		currentPose = Pose.WALK_UP;
		moving = true;
	}

	@Override
	public void goDN(int dy) {
		super.goDN(dy);
		currentPose = Pose.WALK_DN;
		moving = true;
	}
}
