package logic;

import java.io.Serializable;

public class Fortify implements Serializable {
	public final Territory from;
	public final Territory to;
	
	private int amount;
	
	public Fortify(Territory from, Territory to, int amount) {
		this.from = from;
		this.to = to; 
		this.amount = amount;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}
	
}
