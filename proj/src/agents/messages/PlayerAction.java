package agents.messages;

import java.io.Serializable;

public class PlayerAction implements Serializable {

	protected Actions action;
	
	public PlayerAction(Actions action) {
		// represents any possible action
		this.action = action;
	}

	public Actions getAction() {
		return action;
	}

	public void setAction(Actions action) {
		this.action = action;
	}

}
