package agents;


/**
 * Aggressive - attacks whenever possible and places pieces in the best position for future attacks
 * Defensive - only attacks if it finds a very good reason but risks as little as possible
 * Random - makes random moves and choices
 * Smart - considers possible attacks and fortifications and chooses the best option
 */
public enum PlayerMindset {
	Aggressive, Defensive, Random, Smart
}

