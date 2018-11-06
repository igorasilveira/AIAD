package logic;

import java.io.Serializable;

public class Attack implements Serializable {
	public final Territory attacker;
	public final Territory defender;
	
	public Attack(Territory attacker, Territory defender) {
		this.attacker = attacker;
		this.defender= defender;
	}
}
