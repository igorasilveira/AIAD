package agents.messages.player;

import agents.messages.Actions;
import agents.messages.PlayerAction;
import logic.Territory;

public class ProposePlayerSetup extends PlayerAction {

    int territory;

    public ProposePlayerSetup(Actions action, int chosenID) {
        super(action);
        this.territory = chosenID;
    }

    public int getTerritory() {
        return territory;
    }

    public void setTerritory(int territory) {
        this.territory = territory;
    }
}
