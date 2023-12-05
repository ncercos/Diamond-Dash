package matter;

import game.Game;
import game.states.Playing;
import sprites.Animation;

import java.awt.*;

/**
 * Written by Nicholas Cercos
 * Created on Dec 04 2023
 **/
public abstract class Item extends Matter {

	private final Animation animation;
	private final int DEFAULT_SPRITE_SIZE = 15;

	public Item(Game game, String name, double x, double y, double w, double h, double xDrawOffset, double yDrawOffset, int duration) {
		super(game, x, y, w, h, xDrawOffset, yDrawOffset);
		this.x += Game.TILES_SIZE / 4.0;
		animation = new Animation("items/" + name, DEFAULT_SPRITE_SIZE, duration);
	}

	@Override
	public void update() {
		Playing playing = game.getPlaying();
		if(!overlaps(playing.getPlayer()))return;
		if(onCollide())
			playing.getLevelManager().getCurrentLevel()
					.removeItem(this);
	}

	@Override
	public void draw(Graphics g) {
		int lvlOffset = game.getPlaying().getLevelManager().getCurrentLevel().getOffsetX();
		super.draw(g, lvlOffset);
		if(animation == null)return;

		// Draw image if animation exists.
		final int SPRITE_SIZE = (int) (DEFAULT_SPRITE_SIZE * Game.SCALE);
		int ix = (int)(x - lvlOffset);
		g.drawImage(animation.getCurrentImage(game.getPlaying()), (int)(ix - xDrawOffset),
				(int)(y - yDrawOffset), SPRITE_SIZE, SPRITE_SIZE, null);
	}
}
