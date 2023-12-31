package entities;

import entities.enemies.Flower;
import game.Game;
import game.states.Playing;
import levels.Level;
import sounds.Sound;
import sprites.Animation;
import sprites.Pose;
import utils.Hitbox;
import utils.Location;

import java.awt.*;
import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Written by Nicholas Cercos
 * Created on Oct 04 2023
 **/
public abstract class Entity extends Hitbox {

	protected final Playing playing;
	protected final String name;

	/* Physics */
	private static final double GRAVITY = 0.15 * Game.SCALE;
	private static final double MAX_FALL_VELOCITY = 3.0 * Game.SCALE;
	protected double vx, vy;
	protected boolean moving, inAir = true;

	/* Health */
	protected int maxHealth;
	private int health = maxHealth;

	/* Damage */
	protected int attackDamage;
	protected Hitbox attackBox;
	protected boolean active;
	protected int attackPoseIndex;
	protected Entity attacker = null;
	protected boolean takesKnockback = true;

	/* Sprites & Animations */
	private final Map<Pose, Animation> animations;
	private final Set<Pose> poses;
	protected Pose currentPose;
	private final int spriteWidth;
	private double flipX;
	protected int flipW;

	public Entity(Playing playing, String name, double x, double y, double w, double h, int spriteWidth, double xDrawOffset, double yDrawOffset, int maxHealth) {
		super(x, y, w, h, xDrawOffset, yDrawOffset);
		this.playing = playing;
		this.name = name;
		this.spriteWidth = spriteWidth;
		this.maxHealth = maxHealth;
		animations = new HashMap<>();
		poses = new HashSet<>();
		attackBox = new Hitbox(x, y, w, h);
		attackPoseIndex = 0;
		loadAllAnimations(spriteWidth);
		reset();
	}

	/**
	 * Draws to the screen.
	 *
	 * @param g The graphics context.
	 */
	public void draw(Graphics g) {
		int lvlOffset = getLevel().getOffsetX();
		super.draw(g, lvlOffset);
		attackBox.draw(g, lvlOffset);

		Animation animation = getCurrentAnimation();
		if(!active || animation == null)return;
		int px = (int)(x - lvlOffset + flipX);
		int width  = (int) ((spriteWidth * Game.SCALE) * flipW);
		int height = (int)  (spriteWidth * Game.SCALE);

		if(animation.isCycleCompleted() && !currentPose.isRepeated())
			currentPose = moving ? Pose.RUN : inAir ? Pose.JUMP : Pose.IDLE;

		g.drawImage(animation.getCurrentImage(getLevel().getLevelManager().getPlaying()),
				(int)(px - xDrawOffset), (int) (y - yDrawOffset), width, height, null);
	}

	/**
	 * Variables to be set and modified
	 * per game draw.
	 */
	public void update() {
		if(!active)return;
		if(debug) attackBox.setDebug(true);
		moving = false;
		updateAttackBox();
		if(isDying() && getCurrentAnimation().isCycleCompleted()) active = false;
		else if (isIdling()) setCurrentPose(Pose.IDLE);
	}

	/**
	 * Resets player health & poses.
	 */
	public void reset() {
		active = true;
		inAir = true;
		moving = true;
		health = maxHealth;
		currentPose = Pose.IDLE;
		flipX = 0;
		flipW = 1;
	}

	public Level getLevel() {
		return playing.getLevelManager().getCurrentLevel();
	}

	// Movement

	/**
	 * Modifies the position of the rectangle
	 * by its given velocity.
	 */
	public void move() {
		if(!moving && !inAir)return;

		if(!inAir && !isOnTile())
			inAir = true;

		if(inAir)
			updateYPos();

		updateXPos();
	}

	/**
	 * Updates the current x-position to a location
	 * based on the constraints of the level.
	 */
	private void updateXPos() {
		if(canMoveTo(x + vx, y)) setX(x + vx);
		else setX(getXPosNextToTile());
	}

	/**
	 * Updates the current y-position to a location
	 * based on the constraints of the level and physics.
	 */
	private void updateYPos() {
		if(canMoveTo(x, y + vy)) {
			y += vy;
			if(vy < MAX_FALL_VELOCITY)
				vy += GRAVITY;
		} else {
			y = getYPosAboveOrUnderTile();
			if(vy > 0) stopFalling();
			else vy = GRAVITY;
		}
	}

	/**
	 * @return The x-coordinate closest to an adjacent tile, horizontally.
	 */
	protected double getXPosNextToTile() {
		int tileXPos = getTileX() * Game.TILES_SIZE;
		if(vx > 0) { /* moving to the right */
			int xOffset = (int)(Game.TILES_SIZE - w);
			return tileXPos + xOffset - 1;
		} else return tileXPos;
	}

	/**
	 * @return The y-coordinate closest to an adjacent tile, vertically.
	 */
	private double getYPosAboveOrUnderTile() {
		int tileYPos = getTileY() * Game.TILES_SIZE;
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
		if(!isRolling() && !isAttacking()) setCurrentPose(inAir ? Pose.JUMP : Pose.RUN);
		flipX = spriteWidth * Game.SCALE;
		flipW = -1;
	}

	public void goRT(double dx) {
		vx = (int) (dx * Game.SCALE);
		moving = true;
		if(!isRolling() && !isAttacking()) setCurrentPose(inAir ? Pose.JUMP : Pose.RUN);
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
		if(!isRolling() && !isAttacking()) {
			setCurrentPose(Pose.JUMP);
			playing.getSoundManager().playSFX(Sound.JUMP);
		}
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
		if(!isRolling() && !isAttacking()) setCurrentPose(Pose.RUN);
	}

	/**
	 * Sets all velocity variables to their default; zero.
	 */
	public void resetVelocity() {
		vx = 0;
		vy = 0;
	}

	public void setX(double x) {
		this.x = x;
		applyFrictionWithFloor();
	}

	/**
	 * Modifies the x value by the x-velocity to determine
	 * an entity's kinetic energy on the x-axis.
	 *
	 * @return The value of x + vx.
	 */
	public double getKineticX() {
		return x + vx;
	}

	/**
	 * @return True if the player is facing left, false if facing right.
	 */
	public boolean isFacingLeft() {
		return flipW == -1;
	}

	@Override
	public void teleport(Location location) {
		super.teleport(location);
		applyFrictionWithFloor();
	}

	// Collision

	/**
	 * Checks if this can move to a new location based on
	 * the current game level.
	 *
	 * @param x 	  The new x-coordinate.
	 * @param y 	  The new y-coordinate.
	 * @return True if the move is possible, otherwise, false.
	 */
	public boolean canMoveTo(double x, double y) {
		if(!getLevel().isSolid(x, y))
			if(!getLevel().isSolid(x + w, y + h))
				if(!getLevel().isSolid(x + w, y))
					return !getLevel().isSolid(x, y + h);
		return false;
	}

	/**
	 * Determines if this rectangle is standing on
	 * a solid tile.
	 *
	 * @return True if tile below is solid.
	 */
	public boolean isOnTile() {
		if(!getLevel().isSolid(x, y + h + 1))
			return getLevel().isSolid(x + w, y + h + 1);
		return true;
	}

	/**
	 * @return The level tile at the current x-location.
	 */
	public int getTileX() {
		return (int) (x / Game.TILES_SIZE);
	}

	/**
	 * @return The level tile at the current y-location.
	 */
	public int getTileY() {
		return (int) (y / Game.TILES_SIZE);
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
			animations.put(pose, new Animation(name + "/" + pose.getName(), spriteWidth, pose.getDuration()).setRepeatable(pose.isRepeated()));
			poses.add(pose);
		}
	}

	/**
	 * Sets the pose for the entity.
	 *
	 * @param pose The new animation pose.
	 */
	public void setCurrentPose(Pose pose) {
		if(!poses.contains(pose) || isDying() || !active)return;
		currentPose = pose;
		if(!pose.isRepeated())
			getCurrentAnimation().reset();
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
		return !moving && !inAir && !isRolling() && !isAttacking() && !isHurt() && !isDying();
	}

	/**
	 * @return True if the entity is in the jump animation.
	 */
	public boolean isJumping() {
		return currentPose.equals(Pose.JUMP);
	}

	/**
	 * @return True if the entity is in the roll animation.
	 */
	public boolean isRolling() {
		return currentPose.equals(Pose.ROLL);
	}

	/**
	 * @return True if the entity is in the attack animation.
	 */
	public boolean isAttacking() {
		return currentPose.equals(Pose.ATTACK);
	}

	/**
	 * @return True if the entity was hit and is hurt.
	 */
	public boolean isHurt() {
		return currentPose.equals(Pose.HURT);
	}

	/**
	 * @return True if the entity is dying.
	 */
	public boolean isDying() {
		return currentPose.equals(Pose.DIE);
	}

	// Health

	/**
	 * Sets the health.
	 * Value cannot be higher than pre-defined max health.
	 *
	 * @param health The amount of health to be given.
	 */
	public void setHealth(int health) {
		if(health > maxHealth) health = maxHealth;
		this.health = health;
	}

	/**
	 * Sets a new value for the max health.
	 *
	 * @param health The new integer value to represent max health.
	 */
	public void setMaxHealth(int health) {
		this.maxHealth = health;
		this.health = maxHealth;
	}

	/**
	 * Adds or removes health.
	 *
	 * @param value The amount to be added / subtracted.
	 */
	public void modifyHealth(int value) {
		this.health += value;
		if(health > maxHealth) health = maxHealth;
		else if(health <= 0)   health = 0;
	}

	public int getHealth() {
		return health;
	}

	public int getMaxHealth() {
		return maxHealth;
	}

	// Damage

	/**
	 * Updates the location of the hitbox relative to the host entity location.
	 */
	public void updateAttackBox() {
		double xPos = isFacingLeft() ?
				((x - w - (w / 2))) :
				((x + w + (w / 2)));
		attackBox.teleport(new Location(xPos, y));
	}

	/**
	 * Attacks an entity and damages them.
	 *
	 * @param entity The entity being attacked.
	 */
	public boolean attack(Entity entity) {
		if(animations != null && getCurrentAnimation().getCurrentIndex() != attackPoseIndex)return false;
		if(currentPose != null && (entity.isHurt() || entity.isDying()))return false;

		System.out.println(name + " attacked " + entity.name);
		entity.attacker = this;
		entity.damage(attackDamage);
		return true;
	}

	/**
	 * Damage the entity.
	 *
	 * @param damage The amount of damage to be taken.
	 */
	public void damage(int damage) {
		modifyHealth(-damage);

		// Apply pose
		if(getHealth() <= 0)
			setCurrentPose(Pose.DIE);
		else {
			setCurrentPose(Pose.HURT);
			playing.getSoundManager().playSFX(Sound.HURT);
		}


		// Apply knockback
		if(takesKnockback) {
			if(attacker != null && attacker instanceof Flower)
				knockbackY();
			else knockbackX();
		}

		attacker = null;
	}

	/**
	 * Knocks a player back on the x-dimension.
	 */
	private void knockbackX() {
		boolean facingLeft = attacker == null ? isFacingLeft() : !attacker.isFacingLeft();
		int kb = (int) (2 * Game.SCALE);
		if (!facingLeft)
			vx = -kb * Game.SCALE;
		else vx = kb * Game.SCALE;
		updateXPos();
	}

	/**
	 * Knocks a player back on the y-dimension.
	 */
	private void knockbackY() {
		vy = (-3 * Game.SCALE);
		inAir = true;
		updateYPos();
	}

	/**
	 * @return True if the entity is alive.
	 */
	public boolean isActive() {
		return active;
	}

	public int getAttackPoseIndex() {
		return attackPoseIndex;
	}

	public Hitbox getAttackBox() {
		return attackBox;
	}
}
