package agents.messages.player;

import agents.messages.Actions;
import logic.Territory;

public class ProposePlayerFortify  extends ProposePlayerAction {

	private Territory originTerritory;
	private Territory destinationTerritory;

	private int maxAmount;

	public ProposePlayerFortify(Territory originTerritory, Territory destinationTerritory, int maxAmount) {
		super(Actions.Fortify);
		this.originTerritory = originTerritory;
		this.destinationTerritory= destinationTerritory;
		this.maxAmount = maxAmount;
	}

	public Territory getOriginTerritoryID() {
		return originTerritory;
	}

	public void setOriginTerritoryID(Territory originTerritoryID) {
		this.originTerritory = originTerritoryID;
	}

	public Territory getDestinationTerritoryID() {
		return destinationTerritory;
	}

	public void setDestinationTerritoryID(Territory destinationTerritoryID) {
		this.destinationTerritory = destinationTerritoryID;
	}

	public int getMaxAmount() {
		return maxAmount;
	}

	public void setMaxAmount(int maxAmount) {
		this.maxAmount = maxAmount;
	}

}
