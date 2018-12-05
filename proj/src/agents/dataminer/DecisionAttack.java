package agents.dataminer;

import agents.messages.Actions;

public class DecisionAttack extends Decision{

	private int diceChosen;
	private int attackingTerritoryPieces;
	private int defendingTerritoryPieces;
	
	public DecisionAttack(int playerID, int diceChosen, int attackingPieces, int defendingPieces) {
		super(Actions.Attack, playerID);
		this.diceChosen = diceChosen;
		this.attackingTerritoryPieces = attackingPieces;
		this.defendingTerritoryPieces = defendingPieces;
		
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
	
	
	
	public static String header() {
		return "dinalResut,diceChosen,attackingTerritoryPieces,defendingTerritoryPieces";
	}

	@Override
	public String toString() {
		return diceChosen + "," + attackingTerritoryPieces + "," + defendingTerritoryPieces;
	}

}
