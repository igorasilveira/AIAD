package logic;

import java.util.ArrayList;

public class Continent {
	private ArrayList<Territory> territories;
	public int id;
	public final int value;
	
	public Continent(int id, int value) {
		this.id = id;
		this.value = value;
		this.territories =  new ArrayList();
	}
}
