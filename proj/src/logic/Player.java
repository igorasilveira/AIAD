package logic;

import java.io.Serializable;
import java.util.ArrayList;

import jade.core.AID;

public class Player implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

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
	private final AID aid;

	Player(int id, int units, AID aid) {
		this.id = id;
		unitsLeft = units;
		this.aid = aid;
		cards = new ArrayList<>();
	}

	public int getID() {
		return id;
	}

	/**
	 * @return number of units that the player has not placed on the board
	 */
	public int getUnitsLeft() {
		return unitsLeft;
	}

	/**
	 * decreases the number of units
	 * 
	 * @param amount
	 *            amount to decrease
	 */
	public void decreaseUnits(int amount) {
		unitsLeft -= amount;
	}

	/**
	 * increases the number of units
	 * 
	 * @param amount
	 *            amount to increase
	 */
	public void increaseUnits(int amount) {
		unitsLeft += amount;
	}

	/**
	 * set the units to the given amount
	 * 
	 * @param units
	 *            amount of units to set
	 */
	public void setUnits(int units) {
		unitsLeft = units;
	}

	/**
	 * adds card to the list of cards
	 * 
	 * @param card
	 *            card to add
	 */
	public void addCard(Card card) {
		cards.add(card);
	}

	/**
	 * @return Player card list
	 */
	public ArrayList<Card> getCards() {
		return cards;
	}

	public AID getAid() {
		return aid;
	}

}
