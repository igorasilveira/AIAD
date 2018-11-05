package agents.messages;

import java.io.Serializable;

public class PlayerAction implements Serializable {

	private Actions action;
	
	public PlayerAction() {
		// represents any possible action
	}

	public Actions getAction() {
		return action;
	}

	public void setAction(Actions action) {
		this.action = action;
	}

}
