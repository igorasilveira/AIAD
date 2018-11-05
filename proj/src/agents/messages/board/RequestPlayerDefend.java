package agents.messages.board;

import logic.Attack;

public class RequestPlayerDefend extends RequestPlayerAction {

	private Attack attack;
	
	public RequestPlayerDefend() {
		// board requesting player to choose dice for a defense
	}
	
	public Attack getAttack() {
		return attack;
	}
	
	public void setAttack(Attack attack) {
		this.attack = attack;
	}

}
