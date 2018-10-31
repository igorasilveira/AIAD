package logic;

public class Player {
	/**
	 * Units that are not on the board
	 */
	private int unitsLeft;
	
	/**
	 * Player ID
	 */
	private final int id;
	
	public Player(int id, int units) {
		this.id = id;
		this.unitsLeft = units;
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
	
}
