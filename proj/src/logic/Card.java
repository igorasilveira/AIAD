package logic;

public class Card {
	public enum Army {
		Cavalry, Infantry, Artillery;
	}
	
	/**
	 * Unique id to identify the territory. -1 if it is a wildcard.
	 */
	public final int territoryID;
	
	/**
	 * Army type of the card. null if it is a wildcard.
	 */
	public final Army army;
	
	public Card(int territoryID, Army army) {
		this.territoryID = territoryID;
		this.army = army;
	}
	
	public boolean isEqual(Card card) {
		if(this.territoryID != card.territoryID) {
			return false;
		}
		
		if(this.army != card.army) {
			return false;
		}
		
		return true;
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
