package agents.dataminer;

import agents.messages.Actions;

public class DecisionDefend extends Decision {
	
	private int numDiceAttacker;
	private int numDiceDefender;
	private int numUnitsUnderAttack;

	public DecisionDefend(int playerID, int numDiceAttacker, int numDiceDefender, int numUnitsUnderAttack) {
		super(Actions.Defend, playerID);
		this.numDiceAttacker = numDiceAttacker;
		this.numDiceDefender = numDiceDefender;
		this.numUnitsUnderAttack = numUnitsUnderAttack;
		
	}

	public int getNumDiceAttacker() {
		return numDiceAttacker;
	}

	public int getNumDiceDefender() {
		return numDiceDefender;
	}

	public int getNumUnitsUnderAttack() {
		return numUnitsUnderAttack;
	}

}
