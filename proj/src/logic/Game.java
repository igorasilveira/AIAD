package logic;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

import logic.Card.Army;

public class Game {
	public enum GameStage {
		Setup, Middle, Finished
	}

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
	 * List of all the Players
	 */
	private ArrayList<Player> players;
	
	/**
	 * Game Stage
	 */
	private GameStage stage;
	
	/**
	 * index on the list of players the has the turn
	 */
	private int turn;

	/**
	 * Constructs a game class with no territories
	 */
	public Game(int numberOfPlayers) {
		this.continents = new ArrayList<Continent>();
		this.cards = new ArrayList<Card>();
		this.players = new ArrayList<Player>();
		this.stage = GameStage.Setup;
		this.turn = 0;
		
		loadPlayers(numberOfPlayers);
		loadContinents();
		loadTerritories();
		loadCards();

		shuffleCards();
	}

	public static void main(String[] args) {
		Game g = new Game(4);
		g.start();
	}

	/**
	 * starts the game
	 */
	public void start() {
		System.out.println("Started Setup!");
		
		int startingPlayer = new Random().nextInt(this.players.size());
		this.turn = startingPlayer;
		
		while(this.stage != GameStage.Finished) {
			switch(this.stage) {
			case Setup:
				Player p = this.players.get(this.turn);
				
				ArrayList<Territory> unclaimed = getUnclaimedTerrritories();
				
				if(unclaimed.size() > 0) {
					Collections.shuffle(unclaimed);
					
					Territory t = unclaimed.get(0);
					
					t.setPlayerID(p.getID());
					t.setUnits(1);
					
					p.decreaseUnits(1);
				}
				else {
					ArrayList<Territory> claimed = getClaimedTerrritories(p.getID());
					Collections.shuffle(claimed);
					
					Territory t = claimed.get(0);
					t.increaseUnits(1);
					
					p.decreaseUnits(1);
				}
				
				nextTurn();
				
				if(setupFinished()) {
					this.stage = GameStage.Middle;
					this.turn = startingPlayer;
				}
				break;
			case Middle:
				this.stage = GameStage.Finished;
				break;
			}
		}
		
		System.out.println("Done!\n");
		
		for(Player p : this.players) {
			ArrayList<Territory> claimed = getClaimedTerrritories(p.getID());
			
			System.out.println("Player " + p.getID() + " claimed " + claimed.size() + " territories!");
			for(Territory t : claimed) {
				System.out.println("Territory " + t.getTerritoryID() + " has " + t.getUnits() + " units");
			}
			System.out.println("");
		}
		
	}
	
	/**
	 * @return list of unclaimed territories
	 */
	private ArrayList<Territory> getUnclaimedTerrritories() {
		ArrayList<Territory> unclaimed = new ArrayList<Territory>();
		
		for (Continent continent : continents) {
			for (Territory territory : continent.getTerritories()) {
				if(territory.getPlayerID() == 0) {
					unclaimed.add(territory);
				}
			}
		}
		return unclaimed;
	}
	
	/**
	 * 
	 * @param id player id
	 * @return territories claimed by that player
	 */
	private ArrayList<Territory> getClaimedTerrritories(int id) {
		ArrayList<Territory> claimed = new ArrayList<Territory>();
		
		for (Continent continent : continents) {
			for (Territory territory : continent.getTerritories()) {
				if(territory.getPlayerID() == id) {
					claimed.add(territory);
				}
			}
		}
		return claimed;
	}
	
	/**
	 * changes the turn to the next player
	 */
	private void nextTurn() {
		if(this.turn == this.players.size() - 1) {
			this.turn = 0;
		}
		else {
			this.turn++;
		}
	}
	
	/**
	 * Method that checks if the game setup is finished
	 * @return true if it is finished and false otherwise
	 */
	private boolean setupFinished() {
		for(int i = 0; i < this.players.size(); i++) {
			if(this.players.get(i).getUnitsLeft() > 0) {
				return false;
			}
		}
		
		return true;
	}

	/**
	 * Shuffles cards
	 */
	private void shuffleCards() {
		Collections.shuffle(this.cards);
	}
	
	/**
	 * Creates players
	 */
	private void loadPlayers(int numberOfPlayers) {
		int units = Utils.startingUnits.get(numberOfPlayers);
		
		for(int i = 0; i < numberOfPlayers; i++) {
			this.players.add(new Player(i+1, units));
		}
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
	public boolean[] decideBattle(int[] attacker, int[] defender)
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
	
	/**
	 * Checks if all territories are controlled by the same player (condition to win)
	 * @return the id of the winning player or zero if game isn't finished
	 */
	public int isGameFinished(){
		int firstPlayerID = 0;
		for (Continent continent : continents) {
			for (Territory territory : continent.getTerritories()) {
				if(firstPlayerID == 0)
				{
					firstPlayerID = territory.getPlayerID();
				} else {
					if(territory.getPlayerID() != firstPlayerID) return 0;
				}
				
			}
		}
		return firstPlayerID;
	}

}
