package matter;

import game.Game;
import levels.LevelLayer;
import sprites.Animation;
import utils.Hitbox;

import java.awt.*;

/**
 * Written by Nicholas Cercos
 * Created on Nov 21 2023
 **/
public abstract class Matter extends Hitbox {

	protected final Game game;
	private final int spriteSize;
	protected final Animation animation;

	public Matter(Game game, String name, double x, double y, double w, double h, double xDrawOffset, double yDrawOffset, int spriteSize, int duration) {
		super(x, y, w * Game.SCALE, h * Game.SCALE, xDrawOffset, yDrawOffset);
		this.game = game;
		this.x = x + xDrawOffset;
		this.y = y + yDrawOffset;
		this.spriteSize = spriteSize;
		animation = name == null ? null : new Animation(name, spriteSize, duration);
	}

	/**
	 * Creates an object of matter that will not have a texture or animation.
	 * It will only be used as a hitbox.
	 */
	public Matter(Game game, double x, double y, double w, double h, double xDrawOffset, double yDrawOffset) {
		this(game, null, x, y, w, h, xDrawOffset, yDrawOffset, 0, 0);
	}

	public void update() {}

	@Override
	public void draw(Graphics g) {
		int lvlOffset = game.getPlaying().getLevelManager().getCurrentLevel().getOffsetX();
		super.draw(g, lvlOffset);
		if(animation == null)return;

		// Draw image if animation exists.
		final int SIZE = (int) (spriteSize * Game.SCALE);
		int px = (int)(x - lvlOffset);
		g.drawImage(animation.getCurrentImage(game.getPlaying()), (int)(px - xDrawOffset),
				(int)(y - yDrawOffset), SIZE, SIZE, null);
	}

	public boolean onCollide() {
		return false;
	}
}
