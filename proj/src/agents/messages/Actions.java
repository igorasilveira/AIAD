package agents.messages;
/**
 * Possible actions depend on the needed action
 * 
 * Setup - request player to begins his turn by placing a certain amount of pieces
 * Play - request player to make a play 
 * Attack - propose an attack
 * Fortify - propose a fortification
 * Done - propose no action
 * Defend - request player to defnd himself
 * Treaty - propose a treaty to another player
 * 
 */
public enum Actions {
	Setup, EndSetup, Play, Attack, Fortify, Done, Defend, Treaty, TradeCards
}

