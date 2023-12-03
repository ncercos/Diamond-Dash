import game.Game;

import java.util.Scanner;

/**
 * Written by Nicholas Cercos
 * Created on Oct 04 2023
 **/
public class Main {

	public static void main(String[] args) {
		if(false) {
			Scanner scanner = new Scanner(System.in);
			System.out.println("--> Please input data to be formatted below:");
			String data = scanner.nextLine();
			String[] tilesIndex = data.split(",");
			final int WIDTH = tilesIndex.length / Game.TILES_IN_HEIGHT;

			System.out.println("--> There are " + tilesIndex.length + " tiles to format!");

			StringBuilder builder = new StringBuilder();
			for (int i = 0, count = 1; i < tilesIndex.length; i++, count++) {
				boolean outOfBounds = count > WIDTH;
				if (outOfBounds) {
					count = 1;
					builder.deleteCharAt(builder.length() - 1);
					builder.append(";");
				}
				builder.append(tilesIndex[i].trim()).append(",");
			}

			String s = builder.toString();
			System.out.println("--> Complete!");
			System.out.println(s.substring(0, s.length() - 1));
			scanner.close();
		} else new Game();
	}
}
