package logic;

public class Attack {
	public final Territory attacker;
	public final Territory defender;
	
	public Attack(Territory attacker, Territory defender) {
		this.attacker = attacker;
		this.defender= defender;
	}
}
