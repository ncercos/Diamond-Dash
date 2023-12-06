package entities.enemies;

import entities.Hostile;
import game.Game;

/**
 * Written by Nicholas Cercos
 * Created on Dec 05 2023
 **/
public class Slime extends Hostile {
	public Slime(Game game, String name, double x, double y, double w, double h, int spriteWidth, double xDrawOffset, double yDrawOffset, int maxHealth) {
		super(game, name, x, y, w, h, spriteWidth, xDrawOffset, yDrawOffset, maxHealth);
	}
}
