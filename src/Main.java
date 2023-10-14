import game.Game;

/**
 * Written by Nicholas Cercos
 * Created on Oct 04 2023
 **/
public class Main {

	public static void main(String[] args) {
		new Game();

		// WIP: Make it easier to translate level data using this scanner system.
		/*
		Scanner scanner = new Scanner(System.in);
		System.out.println("Input Data to Be Formatted: ");
		List<String> data = new ArrayList<>();
		while(scanner.hasNextLine())  {
			if(scanner.next().equals("exit"))break;
			data.add(scanner.nextLine());
			System.out.println(">> Added data");
		}


		System.out.println(">> Formatting...");
		StringBuilder builder = new StringBuilder();
		for(String d : data) builder.append(d).append(";");
		String s = builder.toString();
		System.out.println("DONE!");
		System.out.println(s);
		 */
	}
}
