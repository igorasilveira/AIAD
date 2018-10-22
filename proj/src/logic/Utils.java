package logic;

import java.util.Random;

public class Utils {
	public enum Army {
		Cavalry, Infantry, Artillery;
	}

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
}
