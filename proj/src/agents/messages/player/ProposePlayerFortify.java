package agents.messages.player;

public class ProposePlayerFortify  extends ProposePlayerAction {

	private int originContinentID;
	private int originTerritoryID;
	private int destinationContinentID;
	private int destinationTerritoryID;
	
	private int maxAmount;
	
	public ProposePlayerFortify() {
		super();
		// player proposes a fortification to the board
	}

	public int getOriginContinentID() {
		return originContinentID;
	}

	public void setOriginContinentID(int originContinentID) {
		this.originContinentID = originContinentID;
	}

	public int getOriginTerritoryID() {
		return originTerritoryID;
	}

	public void setOriginTerritoryID(int originTerritoryID) {
		this.originTerritoryID = originTerritoryID;
	}

	public int getDestinationContinentID() {
		return destinationContinentID;
	}

	public void setDestinationContinentID(int destinationContinentID) {
		this.destinationContinentID = destinationContinentID;
	}

	public int getDestinationTerritoryID() {
		return destinationTerritoryID;
	}

	public void setDestinationTerritoryID(int destinationTerritoryID) {
		this.destinationTerritoryID = destinationTerritoryID;
	}

	public int getMaxAmount() {
		return maxAmount;
	}

	public void setMaxAmount(int maxAmount) {
		this.maxAmount = maxAmount;
	}

}
