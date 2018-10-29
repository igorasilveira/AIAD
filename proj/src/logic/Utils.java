package logic;

import java.util.HashMap;
import java.util.Random;

public class Utils {
	
	public static final HashMap<Integer, Integer> startingUnits = Utils.initUnits();

	/**
	 * Method that rolls the given amount of dice
	 * @param diceAmount amount of dice to be rolled
	 * @return array of length diceAmount with the values gotten from the dice rolls
	 */
	int[] rollDice(int diceAmount)
	{
		int[] results = new int[diceAmount];

		Random rand = new Random();

		for (int i = 0; i < results.length; i++) {
			results[i] = rand.nextInt(6) + 1;
		}

		return results;
	}

	private static HashMap<Integer, Integer> initUnits() {
		HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();
		
		map.put(3, 35);
		map.put(4, 30);
		map.put(5, 25);
		map.put(6, 20);
		
		return map;
	}
}
