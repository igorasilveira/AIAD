package agents.dataminer;

import agents.messages.Actions;

public class DecisionFortify extends Decision {

	private int originDisadvantage;
	private int destinyDisadvantage;
	
	public DecisionFortify(int playerID, int originDisadvantage, int destinyDisadvantage) {
		super(Actions.Fortify, playerID);
		this.originDisadvantage = originDisadvantage;
		this.destinyDisadvantage = destinyDisadvantage;
	}

	public int getOriginDisadvantage() {
		return originDisadvantage;
	}

	public int getDestinyDisadvantage() {
		return destinyDisadvantage;
	}

}
