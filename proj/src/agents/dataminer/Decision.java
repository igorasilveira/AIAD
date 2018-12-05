package agents.dataminer;

import agents.messages.Actions;

public class Decision {

	private Actions playerAction;
	private int playerID;
	
	public Decision(Actions playerAction, int playerID) {
		this.playerAction = playerAction;
		this.playerID = playerID;
	}

	public Actions getPlayerAction() {
		return playerAction;
	}

	public int getPlayerID() {
		return playerID;
	}

}
