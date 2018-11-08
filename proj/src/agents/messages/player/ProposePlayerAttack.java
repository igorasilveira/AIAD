package agents.messages.player;

import agents.messages.Actions;
import logic.Attack;
import logic.Territory;

public class ProposePlayerAttack extends ProposePlayerAction {

	private Attack attack;

	public ProposePlayerAttack(Attack attack) {
		super(Actions.Attack);
		this.attack = attack;
		// player proposes an attack to the board
	}

	public Attack getAttack() {
		return attack;
	}
}
