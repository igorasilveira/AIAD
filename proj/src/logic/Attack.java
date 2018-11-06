package logic;

import java.io.Serializable;

public class Attack implements Serializable {
	public final Territory attacker;
	public final Territory defender;
	
	public final int diceAmount;
	
	public Attack(Territory attacker, Territory defender, int diceAmount) {
		this.attacker = attacker;
		this.defender= defender;
		this.diceAmount = diceAmount;
	}
}
