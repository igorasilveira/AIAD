package agents.messages.player;

import agents.messages.Actions;
import logic.Fortify;

public class ProposePlayerFortify  extends ProposePlayerAction {

	private Fortify fortify;


	public ProposePlayerFortify(Fortify fortify) {
		super(Actions.Fortify);
		this.fortify = fortify;
	}

	public Fortify getFortify() {
		return fortify;
	}


	public void setFortify(Fortify fortify) {
		this.fortify = fortify;
	}

}
