package logic;

import jade.core.AID;

public class Player {
	/**
	 * Units that are not on the board
	 */
	private int unitsLeft;
	
	/**
	 * Player ID
	 */
	private final int id;
	
	private final AID aid;
	
	public Player(int id, int units, AID aid) {
		this.id = id;
		this.unitsLeft = units;
		this.aid = aid;
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
	
}
