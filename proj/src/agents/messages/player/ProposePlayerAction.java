package agents.messages.player;

import agents.messages.PlayerAction;

public class ProposePlayerAction extends PlayerAction {

	private int playerID;

	public ProposePlayerAction() {
		// player proposing some action
		// an instance of this class will be used for treaties
	}

	public int getPlayerID() {
		return playerID;
	}

	public void setPlayerID(int playerID) {
		this.playerID = playerID;
	}

}
