package logic;

import java.io.Serializable;

public class Fortify implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
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
