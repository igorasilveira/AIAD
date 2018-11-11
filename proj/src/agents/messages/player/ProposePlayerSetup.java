package agents.messages.player;

import java.util.ArrayList;

import agents.messages.Actions;
import agents.messages.PlayerAction;

public class ProposePlayerSetup extends ProposePlayerAction {

	ArrayList<Integer> territory;



	public ProposePlayerSetup(ArrayList<Integer> chosenID) {
		super(Actions.Setup);
		this.territory = chosenID;

	}

	public ArrayList<Integer> getTerritories() {
		return territory;
	}

	public void setTerritories(ArrayList<Integer> territory) {
		this.territory = territory;
	}


}
