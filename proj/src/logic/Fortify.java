package logic;

public class Fortify {
	public final Territory from;
	public final Territory to;
	
	public final int maxAmount;
	
	public Fortify(Territory from, Territory to, int maxAmount) {
		this.from = from;
		this.to = to; 
		this.maxAmount = maxAmount;
	}
	
}
