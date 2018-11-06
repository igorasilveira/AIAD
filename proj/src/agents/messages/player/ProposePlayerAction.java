package agents.messages.player;

import agents.messages.Actions;
import agents.messages.PlayerAction;

public class ProposePlayerAction extends PlayerAction {

	private int playerID;

	public ProposePlayerAction(Actions action, int playerID) {
		super(action);
		// player proposing some action
		// an instance of this class will be used for treaties or done
		this.playerID = playerID;
	}

	public int getPlayerID() {
		return playerID;
	}

	public void setPlayerID(int playerID) {
		this.playerID = playerID;
	}

}
