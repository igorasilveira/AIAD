package logic;

import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import logic.Card.Army;

public class Game implements Serializable {
	public enum GameStage {
		Waiting, Setup, Playing, Finished
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
	 * Sets of cards turned in
	 */
	private int setsTurnedIn;

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
	 *  variable to store generated initial player index between methods
	 */
	private int startingPlayer;

	/**
	 * Constructs a game class with no territories
	 */
	public Game() {
		this.continents = new ArrayList<>();
		this.cards = new ArrayList<>();
		this.players = new ArrayList<>();
		this.stage = GameStage.Waiting;
		this.turn = 0;
		this.setsTurnedIn = 0;

	}

	public void setup(ArrayList<AID> numberOfPlayers) {

		loadContinents();
		loadTerritories();
		loadCards();

		shuffleCards();

		loadPlayers(numberOfPlayers);

		startingPlayer = new Random().nextInt(this.players.size());
		turn = startingPlayer;

	}

	public static void main(String[] args) {
		Game g = new Game();

	}

	/**
	 * starts the game
	 */
	public void start(ArrayList<AID> numberOfPlayers) {

		System.out.println("Started GAME!");

		//TODO remove
		boolean x = true;

		while(this.stage != GameStage.Finished) {


			switch(this.stage) {
				case Setup:
					this.setupTurn();
					nextTurn();

					if(setupFinished()) {
						this.stage = GameStage.Playing;
						this.turn = startingPlayer;
					}
					break;
				case Playing:
					this.playingTurn();
					nextTurn();

					if(isGameFinished() != 0) {
						this.stage = GameStage.Finished;
					}
					break;
			}
		}
	}


	private void setupTurn() {
		Player currentPlayer = this.players.get(this.turn);
		ArrayList<Territory> unclaimed = getUnclaimedTerrritories();

		if(unclaimed.size() > 0) {
			//TODO agent chooses territory
			Collections.shuffle(unclaimed);

			Territory t = unclaimed.get(0);

			t.setPlayerID(currentPlayer.getID());
			t.setUnits(1);

			currentPlayer.decreaseUnits(1);
		}
		else {
			//TODO agent chooses territory
			ArrayList<Territory> claimed = getClaimedTerritories(currentPlayer.getID());
			Collections.shuffle(claimed);

			Territory t = claimed.get(0);
			t.increaseUnits(1);

			currentPlayer.decreaseUnits(1);
		}

	}

	public Player getCurrentPlayer() {
		return players.get(turn);
	}

	public Player findPlayerByID(int id) {
		for (Player player: players) {
			if (player.getID() == id)
				return player;
		}
		return null;
	}

	//TODO remove this functions
	public int findByAID(AID aid) {
		for (Player player :
			players) {
			if (player.getAid() == aid)
				return player.getID();
		}
		return -1;
	}

	public void playingTurn()
	{
		Player currentPlayer = this.players.get(this.turn);
		currentPlayer.setUnits(0);
		currentPlayer.increaseUnits(getNewUnits(currentPlayer.getID()));

		//check cards
		//if player has 5 or more cards he has to turn in a set, until he has 4 cards or fewer

		ArrayList<Card> playerCards = currentPlayer.getCards();
		ArrayList<CardSet> sets;

		while(playerCards.size() >= 5) {
			sets = getCardSets(playerCards);
			//TODO choose set
			Collections.shuffle(sets);
			CardSet set = sets.get(0);

			currentPlayer.increaseUnits(turnInCardSet(set, playerCards));
		}

		//TODO turning in a set is optional if you have 4 cards or fewer
		sets = getCardSets(playerCards);

		if(sets.size() > 0) {
			//TODO choose if you want to turn in set
			Collections.shuffle(sets);
			CardSet set = sets.get(0);

			currentPlayer.increaseUnits(turnInCardSet(set, playerCards));
		}

		ArrayList<Territory> claimed = getClaimedTerritories(currentPlayer.getID());

		while(currentPlayer.getUnitsLeft() > 0) {
			//TODO agent chooses territory
			Collections.shuffle(claimed);

			Territory t = claimed.get(0);
			t.increaseUnits(1);

			currentPlayer.decreaseUnits(1);
		}


		ArrayList<Attack> attacks = getAttackOptions(currentPlayer.getID());

		//TODO decide battle
		Random r = new Random();

		while(attacks.size() > 0) {
			Collections.shuffle(attacks);

			Attack a = attacks.get(0);

			//TODO decide number of dice
			int defDice = r.nextInt(2)+1;

			boolean[] result = diceRollWinner(a.getDiceAmount(), defDice);

			int i = 0;
			while(i < result.length && a.getAttacker().getUnits() >= 2 && a.getDefender().getUnits() >= 1) {
				if(result[i]) {
					a.getDefender().decreaseUnits(1);
				}
				else {
					a.getAttacker().decreaseUnits(1);
				}

				i++;
			}

			if(a.getDefender().getUnits() == 0) {
				int defenderID = a.getDefender().getPlayerID();

				a.getDefender().setPlayerID(currentPlayer.getID());
				a.getDefender().increaseUnits(1);

				a.getAttacker().decreaseUnits(1);

				//TODO move units from the attacking territory if you want
				int amount = r.nextInt(a.getAttacker().getUnits());
				a.getAttacker().decreaseUnits(amount);
				a.getDefender().increaseUnits(amount);

				//check if player was eliminated
				//remove player from list
				//TODO need to get cards from the player and if the total is 5 or more then you have to turn in 
				//card sets and place the new units

				System.out.println("ATTACKING PLAYER " + defenderID);

				if(playerLost(defenderID)) {
					removePlayer(defenderID);
				}
			}

			attacks = getAttackOptions(currentPlayer.getID());
		}


		//TODO fortify position (function getFortifyOptions), only once and you can move as many units as you want,
		//but you cant leave a territory with 0 units

		//check if you have to receive cards
		if(getClaimedTerritories(currentPlayer.getID()).size() > claimed.size()) {
			if(this.cards.size() > 0) {
				currentPlayer.addCard(this.cards.remove(0));
			}
		}

		/*
		System.out.println("Turn: Player " + currentPlayer.getID());

		for(Player pl : this.players) {
			ArrayList<Territory> c = getClaimedTerritories(pl.getID());

			System.out.println("Player " + pl.getID() + " has " + c.size() + " territories!");
		}
		System.out.println("");
		 */

	}

	/**
	 * removes a player when he loses
	 * @param id id of the player to remove
	 */
	public void removePlayer(int id) {
		int turnID = this.players.get(this.turn).getID();

		for(int i = 0; i < this.players.size(); i++) {
			if(this.players.get(i).getID() == id) {
				this.players.remove(i);
				break;
			}
		}

		for(int i = 0; i < this.players.size(); i++) {
			if(this.players.get(i).getID() == turnID) {
				this.turn = i;
				break;
			}
		}
	}

	/**
	 * turns in card set
	 * @param playerCards array list with the player's cards
	 */
	public int turnInCardSet(CardSet set, ArrayList<Card> playerCards) {
		this.setsTurnedIn++;
		ArrayList<Card> cards = set.getCards();
		while(cards.size() > 0) {

			Card card = cards.get(0);

			for(int i = 0; i < playerCards.size(); i++) {
				if(card.isEqual(playerCards.get(i))) {
					cards.remove(0);
					playerCards.remove(i);
					break;
				}
			}
		}

		return (2*this.setsTurnedIn)+2;
	}

	public ArrayList<CardSet> getCardSets(ArrayList<Card> cards){
		ArrayList<CardSet> sets = new ArrayList<>();
		ArrayList<Card> buffer = new ArrayList<>();

		//check 3 infantry
		buffer.clear();

		for(Card card : cards) {
			if(card.army == Army.Infantry) {
				buffer.add(card);
				if(buffer.size() == 3) {
					break;
				}
			}
		}

		if(buffer.size() == 3) {
			sets.add(new CardSet(buffer));
		}

		//check 3 cavalry
		buffer.clear();

		for(Card card : cards) {
			if(card.army == Army.Cavalry) {
				buffer.add(card);
				if(buffer.size() == 3) {
					break;
				}
			}
		}

		if(buffer.size() == 3) {
			sets.add(new CardSet(buffer));
		}


		//check 3 artillery
		buffer.clear();

		for(Card card : cards) {
			if(card.army == Army.Artillery) {
				buffer.add(card);
				if(buffer.size() == 3) {
					break;
				}
			}
		}

		if(buffer.size() == 3) {
			sets.add(new CardSet(buffer));
		}

		//check 1 infantry, 1 cavalry, 1 artillery
		buffer.clear();

		for(Card card : cards) {
			if(card.army == Army.Infantry) {
				buffer.add(card);
				break;
			}
		}

		for(Card card : cards) {
			if(card.army == Army.Cavalry) {
				buffer.add(card);
				break;
			}
		}

		for(Card card : cards) {
			if(card.army == Army.Artillery) {
				buffer.add(card);
				break;
			}
		}

		if(buffer.size() == 3) {
			sets.add(new CardSet(buffer));
		}

		//check two cards with one wildcard
		buffer.clear();

		for(Card card : cards) {
			if(card.army == null) {
				buffer.add(card);
				break;
			}
		}

		if(buffer.size() > 0) {
			for(Card card : cards) {
				if(card.army != null) {
					buffer.add(card);
					if(buffer.size() == 3) {
						break;
					}
				}
			}
		}

		if(buffer.size() == 3) {
			sets.add(new CardSet(buffer));
		}

		return sets;
	}

	/**
	 *
	 * @param id player id
	 * @return returns fortify options
	 */
	public ArrayList<Fortify> getFortifyOptions(int id){
		ArrayList<Fortify> fortify = new ArrayList<>();

		for(Continent continent : this.continents) {
			for(Territory from : continent.getTerritories()) {

				if(from.getPlayerID() == id && from.getUnits() >= 2) {

					for(Territory to : from.getNeighbours()) {
						if(to.getPlayerID() == id) {
							fortify.add(new Fortify(from, to, from.getUnits() - 1));
						}
					}
				}
			}
		}

		return fortify;
	}

	/**
	 *
	 * @param id attacking player id
	 * @return attack options for that player
	 */
	public ArrayList<Attack> getAttackOptions(int id) {
		ArrayList<Attack> attacks = new ArrayList<>();
		Random r  = new Random();
		for(Continent continent : this.continents) {
			for(Territory territory : continent.getTerritories()) {

				if(territory.getPlayerID() == id && territory.getUnits() >= 2) {

					for(Territory neighbour : territory.getNeighbours()) {
						if(neighbour.getPlayerID() != id) {
							attacks.add(new Attack(territory, neighbour, 1));
						}
					}
				}
			}
		}

		return attacks;
	}

	public ArrayList<Attack> getDefenseOptions(Territory defender) {
		ArrayList<Attack> attacks = new ArrayList<Attack>();

		for (Territory attacker : defender.getNeighbours()) {
			if(attacker.getPlayerID() != defender.getPlayerID())
			{
				attacks.add(new Attack(attacker, defender, 1));
			}
		}

		return attacks;
	}

	/**
	 * @param id player id
	 * @return number of units player receives on the beggining of the turn
	 */
	public int getNewUnits(int id) {
		int result;

		ArrayList<Territory> claimed = getClaimedTerritories(id);
		result = claimed.size()/3;

		if(result < 3) {
			result = 3;
		}

		for(Continent continent : this.continents) {
			boolean controls = true;

			for(Territory territory : continent.getTerritories()) {
				if(territory.getPlayerID() != id) {
					controls = false;
					break;
				}
			}

			if(controls) {
				result+=continent.value;
			}
		}

		return result;
	}

	/**
	 * @return list of unclaimed territories
	 */
	public ArrayList<Territory> getUnclaimedTerrritories() {
		ArrayList<Territory> unclaimed = new ArrayList<>();

		for (Continent continent : this.continents) {
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
	public ArrayList<Territory> getClaimedTerritories(int id) {
		ArrayList<Territory> claimed = new ArrayList<>();

		for (Continent continent : this.continents) {
			for (Territory territory : continent.getTerritories()) {
				if(territory.getPlayerID() == id) {
					claimed.add(territory);
				}
			}
		}
		return claimed;
	}

	/**
	 *
	 * @param id territory id
	 * @return terrytory corresponding to the searched id
	 */
	public Territory getTerritory(int id) {
		for (Continent continent : this.continents) {
			for (Territory territory : continent.getTerritories()) {
				if(territory.territoryID == id) {
					return territory;
				}
			}
		}

		return null;
	}

	/**
	 *
	 * @param id continent id
	 * @return continent corresponding to the searched id
	 */
	public Continent getContinent(int id) {
		for (Continent continent : this.continents) {
			if(continent.continentID == id) {
				return continent;
			}
		}

		return null;
	}

	/**
	 * changes the turn to the next player
	 */
	public void nextTurn() {
		turn = (turn + 1) % players.size();
	}

	/**
	 *
	 * @param id player id
	 * @return true if the player lost, false otherwise
	 */
	public boolean playerLost(int id) {
		boolean lost = true;

		loop:{
			for (Continent continent : this.continents) {
				for (Territory territory : continent.getTerritories()) {
					if(territory.getPlayerID() == id) {
						lost = false;
						break loop;
					}
				}
			}
		}

		return lost;
	}

	/**
	 * Method that checks if the game setup is finished
	 * @return true if it is finished and false otherwise
	 */
	public boolean setupFinished() {
		for (Player player :
				players) {
			if (player.getUnitsLeft() > 0)
				return false;
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
	private void loadPlayers(ArrayList<AID> players) {
		int units = Utils.startingUnits.get(players.size());

		for(int i = 0; i < players.size(); i++) {
			this.players.add(new Player(i+1, units, players.get(i)));
		}
	}

	/**
	 * Reads the asset containing the territories data
	 * @return true if successful, false otherwise
	 */
	private boolean loadTerritories(){
		ArrayList<Territory> territories = new ArrayList<>();
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
					if(!processTerritoryLine(territories, lineCount, territoryInfo[0],  territoryInfo[1].split(","), territoryInfo[2].split("-")))
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
	 * @param area line content (array with boundaries coordinates)
	 * @return true if successfull, false otherwise
	 */
	private boolean processTerritoryLine(ArrayList<Territory> territories, int currentTerritoryID, String continent, String[] neighbours, String[] area) {
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
		Polygon polygon = new Polygon();
		for (String pair :
				area) {
			String[] coordinates = pair.split(",");
			polygon.addPoint(
					Integer.parseInt(coordinates[0]),
					Integer.parseInt(coordinates[1]));
		}
		current.setArea(polygon);
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
	 * @param attacker number of dice to use
	 * @param defender number of dice to use
	 * @return array with results from battle each true means attacker wins and each false means defender wins
	 */
	public boolean[] diceRollWinner(int attacker, int defender)
	{
		if(attacker < 1 || attacker > 3) {
			return null;
		}

		if(defender < 1 || defender > 2) {
			return null;
		}

		boolean[] results = new boolean[Math.min(attacker, defender)];

		int[] attackerDice = Utils.rollDice(attacker);
		int[] defenderDice = Utils.rollDice(defender);

		int index = 0;
		while(index < attackerDice.length && index < defenderDice.length) {
			if(attackerDice[index] > defenderDice[index]){
				//attacker wins
				results[index] = true;
			}
			else{
				//defender wins (including in case of a tie)
				results[index] = false;
			}
			index++;
		}

		return results;
	}

	/**
	 * Checks if all territories are controlled by the same player (condition to win)
	 * @return the id of the winning player or zero if game isn't finished
	 */
	public int isGameFinished(){
		int firstPlayerID = 0;
		for (Continent continent : this.continents) {
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

	public ArrayList<Player> getPlayers() {
		return players;
	}

	public void setStage(GameStage stage)
	{
		this.stage = stage;
	}

	public GameStage getStage()
	{
		return this.stage;
	}

	public AID getCurrentAID() {
		return players.get(turn).getAid();
	}

	public ArrayList<Card> getCards() {
		return cards;
	}

	public ArrayList<Continent> getContinents() {
		return continents;
	}

	public int getTurn() {
		return turn;
	}

}
