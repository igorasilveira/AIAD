package agents.dataminer;

import agents.messages.Actions;

public class DesisionFortify extends Decision {

	private int originDisadvantage;
	private int destinyDisadvantage;
	
	public DesisionFortify(Actions playerAction, int playerID, int originDisadvantage, int destinyDisadvantage) {
		super(playerAction, playerID);
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
