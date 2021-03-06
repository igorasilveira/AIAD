package logic;

import java.awt.Polygon;
import java.awt.Rectangle;
import java.io.Serializable;
import java.util.ArrayList;

public class Territory implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private Polygon area;

	/**
	 * List of all adjacent or connected territories
	 */
	private ArrayList<Territory> neighbours;

	/**
	 * Unique id to identify the territory its value is one unit above its
	 * position in {@link Game} class' territories array list
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
	 * 
	 * @param territoryID
	 *            id of the territory created
	 */
	public Territory(int territoryID) {
		this.territoryID = territoryID;
		neighbours = new ArrayList<>();
		unitsList = new ArrayList<>();
		playerID = 0;
		units = 0;
	}

	public int getPlayerID() {
		return playerID;
	}

	public void setPlayerID(int playerID) {
		for (Unit unit : unitsList) {
			unit.setColor(Utils.COLORS[playerID - 1]);
		}
		this.playerID = playerID;
	}

	/**
	 * Getter for field neighbours
	 * 
	 * @return array list with neighbours of the current territory
	 */
	public ArrayList<Territory> getNeighbours() {
		return neighbours;
	}

	/**
	 * Adds a neighbour to the current territory's array list neighbours
	 * 
	 * @param neighbour
	 *            territory to be added as neighbour
	 */
	void addNeighbour(Territory neighbour) {
		neighbours.add(neighbour);
	}

	/**
	 * Method used to check if a territory is neighbours with the current
	 * territory
	 * 
	 * @param territory
	 *            territory to be searched in the neighbours array list
	 * @return true if the territory is neighbours with current territory and
	 *         false otherwise
	 */
	public boolean isNeighbour(Territory territory) {
		for (Territory neighbour : neighbours) {
			if (neighbour.territoryID == territory.territoryID) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Method used to get number of units each player has on this territory
	 * 
	 * @return ArrayList containing the information
	 */
	public int getUnits() {
		return units;
	}

	/**
	 * setter for the number of units
	 * 
	 * @param units
	 *            amount to set to
	 */
	void setUnits(int units) {
		unitsList.clear();
		createUnit(units);
		this.units = units;
	}

	/**
	 * increases the number of units
	 * 
	 * @param amount
	 *            amount to increase
	 */
	public void increaseUnits(int amount) {
		createUnit(amount);
		units += amount;
	}

	private void createUnit(int amount) {

		Rectangle r = area.getBounds();
		for (int i = 0; i < amount; i++) {
			int x, y;
			do {
				x = (int) Math.floor(r.getX() + ((int) r.getWidth() * Math.random()));
				y = (int) Math.floor(r.getY() + (r.getHeight() * Math.random()));
			} while (!area.contains(x, y));
			Unit unit = new Unit(x, y, Utils.COLORS[playerID - 1]);

			unitsList.add(unit);
		}
	}

	/**
	 * decreases the number of units
	 * 
	 * @param amount
	 *            amount to decrease
	 */
	public void decreaseUnits(int amount) {
		for (int i = 0; i < amount; i++) {
			unitsList.remove(unitsList.size() - 1);
		}
		units -= amount;
	}

	public int getContinentID() {
		return continentID;
	}

	void setContinentID(int continentID) {
		this.continentID = continentID;
	}

	/**
	 * Method used to print in the console the data in this territory
	 */
	void dump() {
		System.out.print("I am: " + (territoryID));
		System.out.print("\tNeighbour of:");

	}

	public Polygon getArea() {
		return area;
	}

	void setArea(Polygon area) {
		this.area = area;
	}

	public ArrayList<Unit> getUnitsList() {
		return unitsList;
	}

}
