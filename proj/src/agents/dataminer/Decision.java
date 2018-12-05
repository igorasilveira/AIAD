package agents.dataminer;

import agents.messages.Actions;

public class Decision {

	Actions playerAction;
	int playerID;
	
	public Decision(Actions playerAction, int playerID) {
		this.playerAction = playerAction;
		this.playerID = playerID;
	}

}
