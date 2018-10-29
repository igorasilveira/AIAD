package logic;

public class Player {
	/**
	 * Units that are not on the board
	 */
	private int unitsLeft;
	
	/**
	 * Player ID
	 */
	private int id;
	
	public Player(int id, int units) {
		this.id = id;
		this.unitsLeft = units;
	}
	
}
