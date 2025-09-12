package ui.buttons;

import game.Game;
import ui.Button;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Written by Nicholas Cercos
 * Created on Nov 27 2023
 **/
public class UtilButton extends Button {

	private BufferedImage[] buttons;
	private final Type type;
	private int currentIndex;

	// Dimensions
	private static final int DEFAULT_SIZE = 56;
	private static final int SIZE = (int) (DEFAULT_SIZE * Game.SCALE) / 2;

	public UtilButton(int x, int y, double size, Type type) {
		super(x, y, (int) (SIZE * size), (int) (SIZE * size));
		this.type = type;
		loadButtons();
	}

	public UtilButton(int x, int y, Type type) {
		this(x, y, 1, type);
	}

	/**
	 * Loads a specific utility button based on the given type.
	 */
	private void loadButtons() {
		BufferedImage sheet = Game.loadSprite("ui/util_buttons.png");
		if(sheet == null) return;
		buttons = new BufferedImage[3];
		for(int i = 0; i < buttons.length; i++) {
			buttons[i] = sheet.getSubimage(i * DEFAULT_SIZE, type.getRowIndex() * DEFAULT_SIZE, DEFAULT_SIZE, DEFAULT_SIZE);
		}
	}

	/**
	 * Changes the animation of button based on mouse action.
	 * 0 - Default Button
	 * 1 - Hover Button
	 * 2 - Pressed Button
	 */
	@Override
	public void update() {
		                 currentIndex = 0;
		if(mouseOver) 	 currentIndex = 1;
		if(mousePressed) currentIndex = 2;
	}

	@Override
	public void draw(Graphics g) {
		g.drawImage(buttons[currentIndex], (int) x, (int) y, SIZE, SIZE, null);
	}

	public Type getType() {
		return type;
	}

	public enum Type {

		START(0),
		REPLAY(1),
		HOME(2);

		private final int rowIndex;

		Type(int rowIndex) {
			this.rowIndex = rowIndex;
		}

		public int getRowIndex() {
			return rowIndex;
		}
	}
}
