package logic;

import java.io.Serializable;
import java.util.ArrayList;

public class Territory implements Serializable{

	/**
	 * List of all adjacent or connected territories
	 */
	private ArrayList<Territory> neighbours;
	
	/**
	 * Unique id to identify the territory its value is one unit above its position in {@link Game} class' territories array list
	 */
	public final int territoryID;
	
	/**
	 * Id of player that controls the territory, 0 if not taken yet
	 */
	private int playerID;
	
	/**
	 * Number of units that a player has on this territory
	 */
	private int units;
	
	/**
	 * Id of the continent this territory belongs to
	 */
	private int continentID;
	
	
	/**
	 * Creates a territory given its id
	 * @param territoryID id of the territory created
	 */
	public Territory(int territoryID) {
		this.territoryID = territoryID;
		this.neighbours = new ArrayList<Territory>();
		this.playerID = 0;
		this.units = 0;
	}
	
	public int getPlayerID() {
		return playerID;
	}

	public void setPlayerID(int playerID) {
		this.playerID = playerID;
	}
	
	/**
	 * Getter for field neighbours
	 * @return array list with neighbours of the current territory
	 */
	public ArrayList<Territory> getNeighbours() {
		return neighbours;
	}

	/**
	 * Adds a neighbour to the current territory's array list neighbours
	 * @param neighbour territory to be added as neighbour
	 */
	public void addNeighbour(Territory neighbour) {
		this.neighbours.add(neighbour);
	}
	
	/**
	 * Method used to check if a territory is neighbours with the current territory
	 * @param territory territory to be searched in the neighbours array list
	 * @return true if the territory is neighbours with current territory and false otherwise
	 */
	public boolean isNeighbour(Territory territory) {
		for (int i = 0; i < this.neighbours.size(); i++) {
			if(this.neighbours.get(i).territoryID == territory.territoryID)
			{
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Method used to get number of units each player has on this territory
	 * @return ArrayList containing the information
	 */
	public int getUnits(){
		return this.units;
	}
	
	/**
	 * setter for the number of units
	 * @param units
	 */
	public void setUnits(int units) {
		this.units = units;
	}
	
	/**
	 * increases the number of units
	 * @param amount amount to increase
	 */
	public void increaseUnits(int amount) {
		this.units += amount;
	}
	
	/**
	 * decreases the number of units
	 * @param amount amount to decrease
	 */
	public void decreaseUnits(int amount) {
		this.units -= amount;
	}
	
	public int getContinentID() {
		return continentID;
	}

	public void setContinentID(int continentID) {
		this.continentID = continentID;
	}

	/**
	 * Method used to print in the console the data in this territory
	 */
	public void dump()
	{
		System.out.print("I am: " + (this.territoryID));
		System.out.print("\tNeighbour of:");
		
		for (int i = 0; i < this.neighbours.size(); i++) {
			System.out.print(" " + (this.neighbours.get(i).territoryID));
		}
		System.out.print("\n");
	}
	

}
