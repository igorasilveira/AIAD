package logic;

import java.io.Serializable;

public class Attack implements Serializable {
	private final Territory attacker;
	private final Territory defender;

	private int diceAmount;
	
	Attack(Territory attacker, Territory defender, int diceAmount) {
		this.attacker = attacker;
		this.defender= defender;
		this.diceAmount = diceAmount;
	}

	public Territory getAttacker() {
		return attacker;
	}

	public Territory getDefender() {
		return defender;
	}

	public int getDiceAmount() {
		return diceAmount;
	}
	
	public void setDiceAmount(int diceAmount) {
		this.diceAmount = diceAmount;
	}
}
