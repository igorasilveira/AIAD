package logic;

import java.io.Serializable;
import java.util.ArrayList;

public class Continent implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private ArrayList<Territory> territories;
	public final int continentID;
	public final int value;

	public Continent(int id, int value) {
		continentID = id;
		this.value = value;
		territories = new ArrayList<>();
	}

	public void addTerritory(Territory territory) {
		territory.setContinentID(continentID);
		territories.add(territory);
	}

	public ArrayList<Territory> getTerritories() {
		return territories;
	}

	/**
	 * Method used to print in the console the data in this continent
	 */
	public void dump() {
		System.out.print("I am: " + (continentID));
		System.out.println("\n" + territories.size());
		System.out.println("\tContainer of:");

		for (int i = 0; i < territories.size(); i++) {
			System.out.print("\t");
			territories.get(i).dump();
		}
		System.out.print("\n");
	}
}
