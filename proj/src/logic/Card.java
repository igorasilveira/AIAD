package logic;

import logic.Utils.Army;

public class Card {
	/**
	 * Unique id to identify the territory. -1 if it is a wildcard.
	 */
	private int territoryID;
	
	/**
	 * Army type of the card. null if it is a wildcard.
	 */
	private Army army;
	
	public Card(int territoryID, Army army) {
		this.territoryID = territoryID;
		this.army = army;
	}
	
	public void dump() {
		if(territoryID != -1) {
			System.out.println("Country: " + territoryID + " Army: " + army);
		}
		else {
			System.out.println("WildCard");
		}
	}
}
