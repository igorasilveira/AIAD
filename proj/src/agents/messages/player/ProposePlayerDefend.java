package agents.messages.player;

import agents.messages.Actions;
import logic.Attack;
import logic.Territory;

public class ProposePlayerDefend extends ProposePlayerAction {

    private int diceAmount;

    public ProposePlayerDefend(int diceAmount) {
        super(Actions.Defend);
        this.diceAmount = diceAmount;
        // player proposes an attack to the board
    }

    public int getDiceAmount() {
        return diceAmount;
    }
}
