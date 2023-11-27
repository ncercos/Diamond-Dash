package game;

/**
 * Written by Nicholas Cercos
 * Created on Nov 27 2023
 **/
public enum GameState {

	MENU,
	INGAME,
	HELP,
	QUIT;

	public static GameState current = MENU;

}
