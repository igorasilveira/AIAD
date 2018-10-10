package logic;

import java.util.ArrayList;

public class Territory {

	/**
	 * List of all adjacent or connected territories
	 */
	private ArrayList<Territory> neighbours;
	
	private int territoryID;
	
	
	public Territory(int territoryID) {
		this.territoryID= territoryID;
	}
	
	public int getTerritoryID() {
		return territoryID;
	}

	public ArrayList<Territory> getNeighbours() {
		return neighbours;
	}


	public void addNeighbour(Territory neighbour) {
		this.neighbours.add(neighbour);
	}
	
	public boolean isNeighbour(Territory territory) {
		for (int i = 0; i < this.neighbours.size(); i++) {
			if(this.territoryID == territory.territoryID)
			{
				return true;
			}
		}
		return false;
	}
	
	public void dump()
	{
		System.out.print("I am:" + this.territoryID);
		System.out.print("\tNeighbour of:");
		
		for (int i = 0; i < this.neighbours.size(); i++) {
			System.out.print(" " + this.neighbours.get(i).getTerritoryID());
		}
	}
	

}
