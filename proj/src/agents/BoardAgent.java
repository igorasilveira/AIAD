package agents;

import java.util.ArrayList;

import agents.behaviours.BoardSetupBehaviour;
import jade.core.AID;
import jade.core.Agent;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import logic.Game;

public class BoardAgent extends Agent {

	private Game game;

	private ArrayList<AID> players = new ArrayList<AID>();

	public void setup() {
		
		this.game = new Game();
		
		DFAgentDescription template = new DFAgentDescription();
		ServiceDescription sd = new ServiceDescription();
		sd.setType("player");
		template.addServices(sd);
		
		addBehaviour(new BoardSetupBehaviour(this, template));
		

		System.out.println(getLocalName() + ": starting to work!");
	}
	public void takeDown() {
		System.out.println(getLocalName() + ": done working.");
	}

	public Game getGame() {
		return game;
	}
	
	public ArrayList<AID> getPlayers() {
		return players;
	}


	public int getPlayerAmount() {
		return players.size();
	}
	
	public void togglePlayer(AID player){
		
		for (AID aid : players) {
			if(aid.equals(player))
			{
				this.players.remove(aid);
				return;
			}
		}
		this.players.add(player);
	}
}
