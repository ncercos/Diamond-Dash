package entities;

import game.Game;
import levels.Level;
import sprites.Animation;
import sprites.Pose;

import java.awt.*;
import java.io.File;
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
	private double flipX = 0;
	private int flipW = 1;

	public Entity(Game game, String name, double x, double y, double w, double h, int spriteWidth) {
		super(x, y, w, h);
		this.game = game;
		this.name = name;
		animations = new HashMap<>();
		currentPose = Pose.IDLE;
		loadAllAnimations(spriteWidth);
	}

	@Override
	public void draw(Graphics g, Level level) {
		Animation animation = getCurrentAnimation();
		if(animation == null)return;
		int px = (int)(x - level.getOffsetX() + flipX);
		int width = (int)(w + 1) * flipW;
		int height = (int)h + 1;

		if(animation.isCycleCompleted() && !currentPose.isRepeated()) {
			currentPose = moving ? Pose.RUN : inAir ? Pose.JUMP : Pose.IDLE;
			System.out.println("cycle completed!");
		}

		g.drawImage(animation.getCurrentImage(), px, (int)y, width, height, null);
	}

	/**
	 * Loads all animations within the resource directory under
	 * the entity's given name. Sprites must be named after pose.
	 */
	private void loadAllAnimations(int spriteWidth) {
		File file = new File(Game.RESOURCE_URL + name + "/");
		if(!file.exists())return;
		File[] sprites = file.listFiles();
		if(sprites == null)return;

		for(File s : sprites) {
			String poseName = s.getName().split("\\.")[0];
			Pose pose = Pose.getPose(poseName);
			if(pose == null)continue;
			animations.put(pose, new Animation(name + "/" + pose.getName(), spriteWidth, pose.getDuration()));
		}
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
	 * Sets the pose for the entity.
	 *
	 * @param pose The new animation pose.
	 */
	public void setCurrentPose(Pose pose) {
		currentPose = pose;
		if(!pose.isRepeated())
			getCurrentAnimation().resetAnimation();
	}

	/**
	 * @return The current animation based on entity pose. (Defaults to null)
	 */
	public Animation getCurrentAnimation() {
		return animations.getOrDefault(currentPose, null);
	}

	/**
	 * @return True if the entity is not moving or in an action state.
	 */
	public boolean isIdling() {
		return !moving && !inAir && !isRolling();
	}

	/**
	 * @return True if the entity is in the roll animation.
	 */
	public boolean isRolling() {
		return currentPose.equals(Pose.ROLL);
	}

	@Override
	public void goLT(double dx) {
		super.goLT(dx);
		if(!isRolling())
			setCurrentPose(inAir ? Pose.JUMP : Pose.RUN);
		flipX = w;
		flipW = -1;
	}

	@Override
	public void goRT(double dx) {
		super.goRT(dx);
		if(!isRolling())
			setCurrentPose(inAir ? Pose.JUMP : Pose.RUN);
		flipX = 0;
		flipW = 1;
	}

	@Override
	public void goUP(double dy) {
		super.goUP(dy);
		if(!isRolling())
			setCurrentPose(Pose.JUMP);
	}

	@Override
	public void stopFalling() {
		super.stopFalling();
		if(!isRolling())
			setCurrentPose(Pose.RUN);
	}
}
