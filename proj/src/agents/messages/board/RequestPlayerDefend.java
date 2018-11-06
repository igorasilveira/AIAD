package agents.messages.board;

import agents.messages.Actions;
import logic.Attack;
import logic.Game;

public class RequestPlayerDefend extends RequestPlayerAction {

	private Attack attack;
	
	public RequestPlayerDefend(Game game, Attack attack) {
		super(Actions.Defend, game);
		this.attack = attack;
		// board requesting player to choose dice for a defense
	}
	
	public Attack getAttack() {
		return attack;
	}
	
	public void setAttack(Attack attack) {
		this.attack = attack;
	}

}
