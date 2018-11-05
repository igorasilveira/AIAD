package agents.messages.player;

public class ProposePlayerAttack extends ProposePlayerAction {

	private int attackerContinentID;
	private int attackerTerritoryID;
	private int defenderContinentID;
	private int defenderTerritoryID;
	
	public ProposePlayerAttack() {
		super();
		// player proposes an attack to the board
	}

	
	public int getAttackerContinentID() {
		return attackerContinentID;
	}

	public void setAttackerContinentID(int attackerContinentID) {
		this.attackerContinentID = attackerContinentID;
	}

	public int getAttackerTerritoryID() {
		return attackerTerritoryID;
	}

	public void setAttackerTerritoryID(int attackerTerritoryID) {
		this.attackerTerritoryID = attackerTerritoryID;
	}

	public int getDefenderContinentID() {
		return defenderContinentID;
	}

	public void setDefenderContinentID(int defenderContinentID) {
		this.defenderContinentID = defenderContinentID;
	}

	public int getDefenderTerritoryID() {
		return defenderTerritoryID;
	}

	public void setDefenderTerritoryID(int defenderTerritoryID) {
		this.defenderTerritoryID = defenderTerritoryID;
	}
}
