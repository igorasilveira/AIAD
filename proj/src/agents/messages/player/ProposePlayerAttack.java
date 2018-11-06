package agents.messages.player;

import logic.Territory;

public class ProposePlayerAttack extends ProposePlayerAction {

	private Territory attackerTerritory;
	private Territory defenderTerritory;

	public ProposePlayerAttack() {
		super();
		// player proposes an attack to the board
	}


	public Territory getAttackerTerritoryID() {
		return attackerTerritory;
	}

	public void setAttackerTerritoryID(Territory attackerTerritoryID) {
		this.attackerTerritory = attackerTerritoryID;
	}

	public Territory getDefenderTerritoryID() {
		return defenderTerritory;
	}

	public void setDefenderTerritoryID(Territory defenderTerritoryID) {
		this.defenderTerritory = defenderTerritoryID;
	}
}
