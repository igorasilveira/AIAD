package logic;

import java.util.ArrayList;

public class Continent {
	private ArrayList<Territory> territories;
	public int continentID;
	public final int value;
	
	public Continent(int id, int value) {
		this.continentID = id;
		this.value = value;
		this.territories =  new ArrayList<Territory>();
	}
	
	public void addTerritory(Territory territory)
	{
		this.territories.add(territory);
	}
	
	/**
	 * Method used to print in the console the data in this continent
	 */
	public void dump()
	{
		System.out.print("I am: " + (this.continentID));
		System.out.println("\n" + this.territories.size());
		System.out.println("\tContainer of:");
		
		for (int i = 0; i < this.territories.size(); i++) {
			System.out.print("\t");
			this.territories.get(i).dump();
		}
		System.out.print("\n");
	}
}
