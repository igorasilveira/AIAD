package logic;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import logic.Utils.Army;

public class Game {

	/**
	 * Name of the file used to store the board territories adjacency data
	 */
	private static final String territoriesFileName = "territories.dat";

	/**
	 * Name of the file used to store the board continents value data
	 */
	private static final String continentsFileName = "continents.dat";

	/**
	 * Name of the file used to store the card data
	 */
	private static final String cardsFileName = "cards.dat";

	/**
	 * List of all the territories in the board
	 */
	private ArrayList<Continent> continents;

	/**
	 * List of all the cards
	 */
	private ArrayList<Card> cards;

	/**
	 * Constructs a game class with no territories
	 */
	public Game() {
		continents = new ArrayList<Continent>();
		cards = new ArrayList<Card>();

		loadContinents();
		loadTerritories();
		loadCards();

		shuffleCards();
	}

	public static void main(String[] args) {
		Game g = new Game();

		for(int i = 0; i < g.continents.size(); i++) {
			g.continents.get(i).dump();
		}

		for(int i = 0; i < g.cards.size(); i++) {
			g.cards.get(i).dump();
		}
	}

	/**
	 * Shuffles cards
	 */
	private void shuffleCards() {
		Collections.shuffle(this.cards);
	}

	/**
	 * Reads the asset containing the territories data
	 * @return true if successful, false otherwise
	 */
	private boolean loadTerritories(){
		ArrayList<Territory> territories = new ArrayList<Territory>();
		try{

			File file = new File("src/assets/" + Game.territoriesFileName);

			if(file.exists()){

				FileReader fr = new FileReader(file);
				BufferedReader reader = new BufferedReader(fr);

				String line = reader.readLine();
				int lineCount = 1;
				while(line != null)
				{
					String[] territoryInfo = line.split(";");
					if(!processTerritoryLine(territories, lineCount, territoryInfo[0],  territoryInfo[1].split(",")))
					{
						System.out.println("Line format for territories.dat asset should be integers separated by commas");
						reader.close();
						return false;
					}
					lineCount++;
					line = reader.readLine();
				}

				reader.close();


			}else{
				System.out.println("Couldn't find " + Game.territoriesFileName + " in \"assets\" folder!");
				return false;
			}

		}catch(IOException e){
			return false;
		}


		System.out.println("Territories successfully loaded!");
		return true;
	}


	/**
	 * Reads the asset containing the continents' data
	 * @return true if successful, false otherwise
	 */
	private boolean loadContinents(){
		try{

			File file = new File("src/assets/" + Game.continentsFileName);

			if(file.exists()){

				FileReader fr = new FileReader(file);
				BufferedReader reader = new BufferedReader(fr);

				String line = reader.readLine();
				int lineCount = 1;
				while(line != null)
				{
					try {

						int currentContinentVal = Integer.parseInt(line);
						this.continents.add(new Continent(lineCount, currentContinentVal));	
					} catch (Exception e) {
						System.out.println("Line format for continents.dat asset should be an integer");
						reader.close();
						return false;
					}

					lineCount++;
					line = reader.readLine();
				}

				reader.close();


			}else{
				System.out.println("Couldn't find " + Game.continentsFileName + " in \"assets\" folder!");
				return false;
			}

		}catch(IOException e){
			return false;
		}


		System.out.println("Territories successfully loaded!");
		return true;
	}

	/**
	 * Function that processes lines from the board asset
	 * @param currentTerritoryID line number that corresponds to current territory id
	 * @param neighbours line content (array with neighbours' id's)
	 * @return true if successfull, false otherwise
	 */
	private boolean processTerritoryLine(ArrayList<Territory> territories, int currentTerritoryID, String continent, String[] neighbours) {
		int continentID;

		try {
			continentID = Integer.parseInt(continent);	
		} catch (NumberFormatException e) { //not an integer program should shut down
			return false;
		}

		if(currentTerritoryID > territories.size())
		{
			fillTerritories(territories, currentTerritoryID);
		}
		Territory current = territories.get(currentTerritoryID-1);
		for (int i = 0; i < neighbours.length; i++) {
			int currentNeighbourID;
			try {
				currentNeighbourID = Integer.parseInt(neighbours[i]);	
			} catch (NumberFormatException e) { //not an integer program should shut down
				return false;
			}

			if(currentNeighbourID > territories.size())
			{
				fillTerritories(territories, currentNeighbourID);
			}
			Territory neighbour = territories.get(currentNeighbourID-1);
			current.addNeighbour(neighbour);
		}
		this.continents.get(continentID - 1).addTerritory(current);
		return true;
	}



	/**
	 * Function that creates the needed amount of territories to match the total of territories given as a parameter
	 * @param totalGoal goal amount of territories
	 */
	private void fillTerritories(ArrayList<Territory> territories, int totalGoal)
	{
		while(territories.size() < totalGoal)
		{
			territories.add(new Territory(territories.size() + 1));
		}
	}



	/**
	 * Reads the asset containing the cards data
	 * @return true if successful, false otherwise
	 */
	private boolean loadCards(){
		try{

			File file = new File("src/assets/" + Game.cardsFileName);

			if(file.exists()){

				FileReader fr = new FileReader(file);
				BufferedReader reader = new BufferedReader(fr);

				String line = reader.readLine();
				int lineCount = 1;
				while(line != null)
				{
					if(!processCardLine(lineCount, line))
					{
						System.out.println("Line format for cards.dat asset should be: \"a\" for artillery, \"c\" for cavalry, \"i\" for infantry and \"w\" for wildcard");
						reader.close();
						return false;
					}
					lineCount++;
					line = reader.readLine();
				}

				reader.close();


			}else{
				System.out.println("Couldn't find " + Game.cardsFileName + " in \"assets\" folder!");
				return false;
			}

		}catch(IOException e){
			return false;
		}


		System.out.println("Cards successfully loaded!");
		return true;
	}

	/**
	 * Method that processes a line from the asset containing cards' info
	 * @param territoryID id of the territory the card refers to
	 * @param line line from the asset
	 * @return true if successful, false otherwise
	 */
	private boolean processCardLine(int territoryID, String line) {
		switch(line) {
		case "i":
			this.cards.add(new Card(territoryID, Army.Infantry));
			break;
		case "c":
			this.cards.add(new Card(territoryID, Army.Cavalry));
			break;
		case "a":
			this.cards.add(new Card(territoryID, Army.Artillery));
			break;
		case "w":
			this.cards.add(new Card(-1, null));
			break;
		default:
			return false;
		}

		return true;
	}

	/**
	 * Method that decides the battle given the attacker's and the defender's dice rolls
	 * @param attacker array of dice rolls the attacker got
	 * @param defender array of dice rolls the defender got
	 * @return array with results from battle each true means attacker wins and each false means defender wins
	 */
	public static boolean[] decideBattle(int[] attacker, int[] defender)
	{
		boolean[] results = new boolean[Math.min(attacker.length, defender.length)];

		Arrays.sort(attacker); Arrays.sort(defender);
		int offset = 1;
		while(attacker.length >= offset && defender.length >= offset)
		{
			if(attacker[attacker.length - offset] > defender[defender.length - offset])
			{ //attacker wins
				results[offset - 1] = true;
			} else
			{ //defender wins (including in case of a tie)
				results[offset - 1] = false;
			}
			offset++;
		}
		return results;
	}

}
