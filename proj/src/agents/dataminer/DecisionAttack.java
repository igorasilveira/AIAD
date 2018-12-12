package agents.dataminer;

import agents.messages.Actions;

public class DecisionAttack extends Decision{

	private int diceChosen;
	private int attackingTerritoryPieces;
	private int defendingTerritoryPieces;
	private int territoryAmount;
	
	public DecisionAttack(int playerID, int diceChosen, int attackingPieces, int defendingPieces, int territoryAmount) {
		super(Actions.Attack, playerID);
		this.diceChosen = diceChosen;
		this.attackingTerritoryPieces = attackingPieces;
		this.defendingTerritoryPieces = defendingPieces;
		this.territoryAmount = territoryAmount;
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
	
	
	
	public static String header() {
		return "\"run\",\"a\",\"b\",\"finalResut\",\"diceChosen\",\"attackingTerritoryPieces\",\"defendingTerritoryPieces\",\"territoryAmount\"";
	}

	@Override
	public String toString() {
		return super.getPlayerID() + "," + diceChosen + "," + attackingTerritoryPieces + "," + defendingTerritoryPieces + "," +  territoryAmount;
	}

	public int getTerritoryAmount() {
		return territoryAmount;
	}
}
