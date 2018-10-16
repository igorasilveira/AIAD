package logic;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import logic.Utils.Army;

public class Game {

	/**
	 * Name of the file used to store the board territories adjacency data
	 */
	private static final String boardFileName = "board.dat";
	
	/**
	 * Name of the file used to store the card data
	 */
	private static final String cardsFileName = "cards.dat";

	/**
	 * List of all the territories in the board
	 */
	private ArrayList<Territory> territories;
	
	/**
	 * List of all the cards
	 */
	private ArrayList<Card> cards;

	/**
	 * Constructs a game class with no territories
	 */
	public Game() {
		territories = new ArrayList<Territory>();
		cards = new ArrayList<Card>();
		
		loadTerritories();
		loadCards();
	}

	public static void main(String[] args) {
		Game g = new Game();
		
		/*for(int i = 0; i < g.territories.size(); i++) {
			g.territories.get(i).dump();
		}
		
		for(int i = 0; i < g.cards.size(); i++) {
			 g.cards.get(i).dump();
		}*/
	}

	/**
	 * Reads the asset containing the territories data
	 * @return true if successful, false otherwise
	 */
	private boolean loadTerritories(){
		try{

			File file = new File("src/assets/" + Game.boardFileName);

			if(file.exists()){

				FileReader fr = new FileReader(file);
				BufferedReader reader = new BufferedReader(fr);

				String line = reader.readLine();
				int lineCount = 1;
				while(line != null)
				{
					if(!processTerritoryLine(lineCount, line.split(",")))
					{
						System.out.println("Line format for board.dat asset should be integers separated by commas");
						reader.close();
						return false;
					}
					lineCount++;
					line = reader.readLine();
				}

				reader.close();


			}else{
				System.out.println("Couldn't find " + Game.boardFileName + " in \"assets\" folder!");
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
	private boolean processTerritoryLine(int currentTerritoryID, String[] neighbours) {

		if(currentTerritoryID > this.territories.size())
		{
			fillTerritories(currentTerritoryID);
		}

		for (int i = 0; i < neighbours.length; i++) {
			int currentNeighbourID;
			try {
				currentNeighbourID = Integer.parseInt(neighbours[i]);	
			} catch (NumberFormatException e) { //not an integer program should shut down
				return false;
			}

			if(currentNeighbourID > this.territories.size())
			{
				fillTerritories(currentNeighbourID);
			}

			this.getTerritoryByID(currentTerritoryID).addNeighbour(this.getTerritoryByID(currentNeighbourID));
		}
		return true;
	}

	/**
	 * Function that creates the needed amount of territories to match the total of territories given as a parameter
	 * @param totalGoal goal amount of territories
	 */
	private void fillTerritories(int totalGoal)
	{
		while(this.territories.size() < totalGoal)
		{
			this.territories.add(new Territory(this.territories.size()));
		}
	}
	
	
	
	/**
	 * Reads the asset containing the territories data
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

	public Territory getTerritoryByID(int id) {
		if(id > this.territories.size()) {
			return null;
		}
		
		return this.territories.get(id-1);
	}

}
