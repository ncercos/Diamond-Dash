package entities;

import game.Game;
import levels.Level;
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

	protected Game game;
	private final String name;
	private final int MAX_HEALTH = 10;
	private int health = MAX_HEALTH;

	private final Map<Pose, Animation> animations;
	protected Pose currentPose;

	public Entity(Game game, String name, double x, double y, double w, double h) {
		super(x, y, w, h);
		this.game = game;
		this.name = name;
		animations = new HashMap<>();
		currentPose = Pose.WALK_RT;
	}

	@Override
	public void draw(Graphics g) {
		Animation animation = getCurrentAnimation();
		if(animation == null)return;
		Level level = game.getLevelManager().getCurrentLevel();
		if(level == null)return;
		if(moving) g.drawImage(animation.getCurrentImage(), (int)x - level.getOffsetX(), (int)y, (int)w + 1, (int)h + 1, null);
		else g.drawImage(animation.getStaticImage(), (int)x - level.getOffsetX(), (int)y, (int)w + 1, (int)h + 1, null);
	}

	/**
	 * Loads in all animations based on the given poses.
	 *
	 * @param poses A hashmap of poses and the number of sprites within the animation (per pose).
	 */
	protected void loadAnimations(Map<Pose, Integer> poses) {
		poses.forEach((k,v) -> animations.put(k, new Animation(name + "/" + k.getName(), v, 18)));
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
	public void goLT(double dx) {
		super.goLT(dx);
		if(!inAir) currentPose = Pose.WALK_LT;
	}

	@Override
	public void goRT(double dx) {
		super.goRT(dx);
		if(!inAir) currentPose = Pose.WALK_RT;
	}

	@Override
	public void goUP(double dy) {
		super.goUP(dy);
		if(Pose.isLeft(currentPose))
			currentPose = Pose.JUMP_LT;
		else currentPose = Pose.JUMP_RT;
	}

	@Override
	public void stopFalling() {
		super.stopFalling();
		if(currentPose.equals(Pose.JUMP_RT))
			currentPose = Pose.WALK_RT;
		else if(currentPose.equals(Pose.JUMP_LT))
			currentPose = Pose.WALK_LT;
	}
}
