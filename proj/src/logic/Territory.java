package logic;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;

public class Territory implements Serializable{

	private Polygon area;

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

	private ArrayList<Unit> unitsList;
	
	/**
	 * Creates a territory given its id
	 * @param territoryID id of the territory created
	 */
	public Territory(int territoryID) {
		this.territoryID = territoryID;
		this.neighbours = new ArrayList<>();
		this.unitsList = new ArrayList<>();
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
		unitsList.clear();
		createUnit(units);
		this.units = units;
	}
	
	/**
	 * increases the number of units
	 * @param amount amount to increase
	 */
	public void increaseUnits(int amount) {
		createUnit(amount);
		this.units += amount;
	}

	private void createUnit(int amount) {

		Rectangle r = area.getBounds();
		for (int i = 0; i < amount; i++) {
			int x, y;
			do {
				x = (int) Math.floor(r.getX() + (int) r.getWidth() *Math.random());
				y = (int) Math.floor(r.getY() + r.getHeight() * Math.random());
			} while (!area.contains(x, y));
			Unit unit = new Unit(x, y, Utils.COLORS[playerID - 1]);

			unitsList.add(unit);
		}
	}

	/**
	 * decreases the number of units
	 * @param amount amount to decrease
	 */
	public void decreaseUnits(int amount) {
		for (int i = 0; i < amount; i++) {
			unitsList.remove(unitsList.size() - 1);
		}
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


	public Polygon getArea() {
		return area;
	}

	public void setArea(Polygon area) {
		this.area = area;
	}

	public ArrayList<Unit> getUnitsList() {
		return unitsList;
	}

}
