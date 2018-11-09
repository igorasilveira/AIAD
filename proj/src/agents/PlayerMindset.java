package agents;


/**
 * Aggressive - attacks whenever possible and places pieces in the best position for future attacks
 * Defensive - only attacks if it finds a very good reason
 * Smart - considers possible attacks and fortifications and chooses the best option
 * Random - makes random moves and choices
 */
public enum PlayerMindset {
	Aggressive, Defensive, Random, Smart
}
