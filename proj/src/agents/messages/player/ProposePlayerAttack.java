package agents.messages.player;

import agents.messages.Actions;
import logic.Territory;

public class ProposePlayerAttack extends ProposePlayerAction {

	private Territory attackerTerritory;
	private Territory defenderTerritory;
	private int amountOfDice;

	public ProposePlayerAttack(Territory attackerTerritory, Territory defenderTerritory, int amountOfDice) {
		super(Actions.Attack);
		this.attackerTerritory = attackerTerritory;
		this.defenderTerritory= defenderTerritory;
		this.amountOfDice = amountOfDice;
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


	public int getAmountOfDice() {
		return amountOfDice;
	}


	public void setAmountOfDice(int amountOfDice) {
		this.amountOfDice = amountOfDice;
	}
}
