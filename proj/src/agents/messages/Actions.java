package agents.messages;
/**
 * Possible actions depend on the needed action
 * 
 * Setup - request player to begins his turn by placing a certain amount of pieces
 * Play - request player to make a play 
 * Attack - propose an attack
 * Fortify - propose a fortification
 * Done - propose no action
 * Defend - request player to defend himself
 * TradeCards - propose player card trade
 * End - used in data recording to sign a player ended its game
 * 
 */
public enum Actions {
	Setup, EndSetup, Play, Attack, Fortify, Done, Defend, TradeCards, End
}

