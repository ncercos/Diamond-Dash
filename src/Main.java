import game.Game;

import java.util.Scanner;

/**
 * Written by Nicholas Cercos
 * Created on Oct 04 2023
 **/
public class Main {

	public static void main(String[] args) {
		if(false) {
			final String PREFIX = "--> ";
			Scanner scanner = new Scanner(System.in);

			// Offsets
			System.out.println(PREFIX + " Please input the tile sheet offset below:");
			int offset = scanner.nextInt() - 1; /* all indexes will be translated 1 index up (to be reverted down 1 index when loaded) */
			if(offset < 0) {
				System.out.println(PREFIX + "Offset must be greater than 0!");
				return;
			}
			scanner.nextLine();

			// Tiles
			System.out.println(PREFIX + " Please input data to be formatted below:");
			String data = scanner.nextLine();
			String[] tilesIndex = data.split(",");
			final int WIDTH = tilesIndex.length / Game.TILES_IN_HEIGHT;

			System.out.println("--> There are " + tilesIndex.length + " tiles to format! (" + offset + ")");

			// Format
			StringBuilder builder = new StringBuilder();
			for (int i = 0, count = 1; i < tilesIndex.length; i++, count++) {
				boolean outOfBounds = count > WIDTH;
				if (outOfBounds) {
					count = 1;
					builder.deleteCharAt(builder.length() - 1);
					builder.append(";");
				}
				int tileIndex = Integer.parseInt(tilesIndex[i].trim());
				if(tileIndex > 0) tileIndex -= offset;
				builder.append(tileIndex).append(",");
			}

			// Send data
			String s = builder.toString();
			System.out.println("--> Complete!");
			System.out.println(s.substring(0, s.length() - 1));
			scanner.close();
		} else new Game();
	}
}
