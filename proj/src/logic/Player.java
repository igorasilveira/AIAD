package logic;

import java.util.ArrayList;

public class Player {
	/**
	 * Units that are not on the board
	 */
	private int unitsLeft;
	
	/**
	 * Player ID
	 */
	private final int id;
	
	/**
	 * Cards that the player has
	 */
	private ArrayList<Card> cards;
	
	public Player(int id, int units) {
		this.id = id;
		this.unitsLeft = units;
		this.cards = new ArrayList<Card>();
	}
	
	public int getID() {
		return this.id;
	}
	
	/**
	 * @return number of units that the player has not placed on the board
	 */
	public int getUnitsLeft() {
		return this.unitsLeft;
	}
	
	/**
	 * decreases the number of units
	 * @param amount amount to decrease
	 */
	public void decreaseUnits(int amount) {
		this.unitsLeft -= amount;
	}
	
	/**
	 * increases the number of units
	 * @param amount amount to increase
	 */
	public void increaseUnits(int amount) {
		this.unitsLeft += amount;
	}
	
	/**
	 * set the units to the given amount
	 * @param units 
	 */
	public void setUnits(int units) {
		this.unitsLeft = units;
	}
	
	/**
	 * adds card to the list of cards
	 * @param card card to add
	 */
	public void addCard(Card card) {
		this.cards.add(card);
	}
	
	/**
	 * @return Player card list
	 */
	public ArrayList<Card> getCards() {
		return this.cards;
	}
	
}
