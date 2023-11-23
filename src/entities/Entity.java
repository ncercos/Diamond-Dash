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
public class Entity {

	/* Location & Size */
	protected double x, y;
	protected final double w, h;

	/* Physics */
	private static final double GRAVITY = 0.15 * Game.SCALE;
	private static final double MAX_FALL_VELOCITY = 3.0 * Game.SCALE;
	private double vx, vy;
	protected boolean moving, inAir = true;

	/* Settings */
	private final String name;
	private final int MAX_HEALTH = 10;
	private int health = MAX_HEALTH;

	/* Sprites & Animations */
	private final Map<Pose, Animation> animations;
	protected Pose currentPose;
	private double flipX = 0;
	private int flipW = 1;

	public Entity(String name, double x, double y, double w, double h, int spriteWidth) {
		this.name = name;
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		animations = new HashMap<>();
		currentPose = Pose.IDLE;
		loadAllAnimations(spriteWidth);
	}

	public Entity(double x, double y, double w, double h) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		name = null;
		animations = null;
		currentPose = null;
	}

	/**
	 * Draws to the screen.
	 *
	 * @param g The graphics context.
	 */
	public void draw(Graphics g, Level level) {
		g.drawRect((int)x - level.getOffsetX(), (int)y, (int)w, (int)h);
		if(animations == null || currentPose == null)return;

		Animation animation = getCurrentAnimation();
		if(animation == null)return;
		int px = (int)(x - level.getOffsetX() + flipX);
		int width = (int)(w + 1) * flipW;
		int height = (int)h + 1;

		if(animation.isCycleCompleted() && !currentPose.isRepeated())
			currentPose = moving ? Pose.RUN : inAir ? Pose.JUMP : Pose.IDLE;

		g.drawImage(animation.getCurrentImage(), px, (int)y, width, height, null);
	}

	/**
	 * Variables to be set and modified
	 * per game draw.
	 */
	public void update() {
		moving = false;
	}

	// Movement

	/**
	 * Modifies the position of the rectangle
	 * by its given velocity.
	 */
	public void move(Level level) {
		if(!moving && !inAir)return;

		if(!inAir && !isOnTile(level))
			inAir = true;

		if(inAir) {
			if(canMoveTo(x, y + vy, level)) {
				y += vy;
				if(vy < MAX_FALL_VELOCITY)
					vy += GRAVITY;
			} else {
				y = getYPosAboveOrUnderTile();
				if(vy > 0) stopFalling();
				else vy = GRAVITY;
			}
			updateXPos(level);
		} else updateXPos(level);
	}

	/**
	 * Updates the current x-position to a location
	 * based on the constraints of the level.
	 *
	 * @param level The current level.
	 */
	private void updateXPos(Level level) {
		if(canMoveTo(x + vx, y, level)) x += vx;
		else x = getXPosNextToTile();
		applyFrictionWithFloor();
	}

	/**
	 * @return The x-coordinate closest to an adjacent tile, horizontally.
	 */
	private double getXPosNextToTile() {
		int currentTileIndex = (int) (x / Game.TILES_SIZE);
		int tileXPos = currentTileIndex * Game.TILES_SIZE;
		if(vx > 0) { /* moving to the right */
			int xOffset = (int)(Game.TILES_SIZE - w);
			return tileXPos + xOffset - 1;
		} else return tileXPos;
	}

	/**
	 * @return The y-coordinate closest to an adjacent tile, vertically.
	 */
	private double getYPosAboveOrUnderTile() {
		int currentTileIndex = (int) (y / Game.TILES_SIZE);
		int tileYPos = currentTileIndex * Game.TILES_SIZE;
		if(vy > 0) { /* falling */
			int yOffset = (int)(Game.TILES_SIZE - h);
			return tileYPos + yOffset - 1;
		} else return tileYPos;
	}

	/**
	 * Moves the rectangle to the left.
	 *
	 * @param dx The delta-x determines how far it will move horizontally.
	 */
	public void goLT(double dx) {
		vx = (int) (-dx * Game.SCALE);
		moving = true;
		if(!isRolling()) setCurrentPose(inAir ? Pose.JUMP : Pose.RUN);
		flipX = w;
		flipW = -1;
	}

	public void goRT(double dx) {
		vx = (int) (dx * Game.SCALE);
		moving = true;
		if(!isRolling()) setCurrentPose(inAir ? Pose.JUMP : Pose.RUN);
		flipX = 0;
		flipW = 1;
	}

	/**
	 * Moves the rectangle up.
	 *
	 * @param dy The delta-y determines how far it will move vertically.
	 */
	public void goUP(double dy) {
		if(inAir)return;
		vy = (int) (-dy * Game.SCALE);
		moving = true;
		inAir = true;
		if(!isRolling()) setCurrentPose(Pose.JUMP);
	}

	/**
	 * Prevents infinite velocity glitch & stops
	 * horizontal movement from gravity on ground.
	 *
	 */
	public void applyFrictionWithFloor() {
		vx = 0;
	}

	/**
	 * Prevents infinite fall glitch due to gravity.
	 */
	public void stopFalling() {
		inAir = false;
		vy = 0;
		if(!isRolling()) setCurrentPose(Pose.RUN);
	}

	/**
	 * Sets all velocity variables to their default; zero.
	 */
	public void resetVelocity() {
		vx = 0;
		vy = 0;
	}

	// Collision

	/**
	 * Checks if this can move to a new location based on
	 * the current game level.
	 *
	 * @param x 	  The new x-coordinate.
	 * @param y 	  The new y-coordinate.
	 * @param level The current level.
	 * @return True if the move is possible, otherwise, false.
	 */
	public boolean canMoveTo(double x, double y, Level level) {
		if(!level.isSolid(x, y))
			if(!level.isSolid(x + w, y + h))
				if(!level.isSolid(x + w, y))
					return !level.isSolid(x, y + h);
		return false;
	}

	/**
	 * Determines if this rectangle is standing on
	 * a solid tile.
	 *
	 * @param level The current level.
	 * @return True if tile below is solid.
	 */
	private boolean isOnTile(Level level) {
		if(!level.isSolid(x, y + h + 1))
			return level.isSolid(x + w, y + h + 1);
		return true;
	}

	/**
	 * Determines if two entity hitboxes are overlapping.
	 *
	 * @param e The other entity in question.
	 * @return True if both entities are colliding.
	 */
	public boolean overlaps(Entity e) {
		return (x     <= e.x + e.w) &&
				   (x + w >= e.x      ) &&
				   (y     <= e.y + e.h) &&
				   (y + h >= e.y      );
	}

	// Animations

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

	// Settings

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
}
