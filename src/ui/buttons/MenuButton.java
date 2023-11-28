package ui.buttons;

import game.Game;
import game.GameState;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Written by Nicholas Cercos
 * Created on Nov 27 2023
 **/
public class MenuButton extends Button {

	private final int rowIndex;
	private int currentIndex;
	private final GameState state;
	private BufferedImage[] sprites;

	// Dimensions
	private static final int DEFAULT_WIDTH = 140;
	private static final int DEFAULT_HEIGHT = 56;
	private static final int WIDTH = (int) (DEFAULT_WIDTH * Game.SCALE) / 2;
	private static final int HEIGHT = (int) (DEFAULT_HEIGHT * Game.SCALE) / 2;
	private final int X_CENTER_OFFSET = WIDTH / 2;

	public MenuButton(int x, int y, int rowIndex, GameState state) {
		super(x, y, WIDTH, HEIGHT);
		this.x = x;
		this.y = y;
		this.rowIndex = rowIndex;
		this.state = state;

		loadSprites();
		hitbox = new Rectangle(x - X_CENTER_OFFSET, y, WIDTH, HEIGHT);
	}

	/**
	 * Loads all menu buttons from its sprite sheet.
	 */
	private void loadSprites() {
		sprites = new BufferedImage[3];
		try {
			BufferedImage sprite = ImageIO.read(new File(Game.RESOURCE_URL + "ui/menu_buttons.png"));
			for (int i = 0; i < sprites.length; i++)
				sprites[i] = sprite.getSubimage(i * DEFAULT_WIDTH, rowIndex * DEFAULT_HEIGHT,
						DEFAULT_WIDTH, DEFAULT_HEIGHT);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * The state that will be applied when the button is pressed.
	 */
	public void applyState() {
		GameState.current = state;
	}

	/**
	 * Updates a button's animation based on the
	 * actions of the mouse.
	 */
	@Override
	public void update() {
		                 currentIndex = 0;
		if(mouseOver)    currentIndex = 1;
		if(mousePressed) currentIndex = 2;
	}

	@Override
	public void draw(Graphics g) {
		g.drawImage(sprites[currentIndex], x - X_CENTER_OFFSET, y, WIDTH, HEIGHT, null);
	}

	public GameState getState() {
		return state;
	}
}
