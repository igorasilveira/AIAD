package agents.dataminer;

import agents.messages.Actions;

public class DecisionAttack extends Decision{

	private int AttackDefenseDiff;
	private int diceChosen;
	private int attackingTerritoryPieces;
	private int defendingTerritoryPieces;
	
	public DecisionAttack(int playerID, int AttackDefenseDiff, int diceChosen, int attackingPieces, int defendingPieces) {
		super(Actions.Attack, playerID);
		this.AttackDefenseDiff = AttackDefenseDiff;
		this.diceChosen = diceChosen;
		this.attackingTerritoryPieces = attackingPieces;
		this.defendingTerritoryPieces = defendingPieces;
		
	}

	public int getAttackDefenseDiff() {
		return AttackDefenseDiff;
	}

	public int getDiceChosen() {
		return diceChosen;
	}

	public int getAttackingTerritoryPieces() {
		return attackingTerritoryPieces;
	}

	public int getDefendingTerritoryPieces() {
		return defendingTerritoryPieces;
	}
	
	public int getAttackerAdvantage() {
		return attackingTerritoryPieces - defendingTerritoryPieces;
	}

}
