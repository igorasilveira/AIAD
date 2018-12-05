package agents;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import agents.behaviours.board.BoardPlayingBehaviour;
import agents.behaviours.board.BoardSetupBehaviour;
import agents.behaviours.board.BoardWaitingBehaviour;
import agents.dataminer.Decision;
import jade.core.AID;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import logic.Game;
import sajas.core.Agent;
import sajas.core.behaviours.SequentialBehaviour;

public class BoardAgent extends Agent {

	private Game game;
	private SequentialBehaviour sequentialBehaviour;

	private ConcurrentLinkedQueue<Decision> decisions;
	
	private ArrayList<AID> players = new ArrayList<>();

	public void setup() {

		this.game = new Game();
		
		this.decisions = new ConcurrentLinkedQueue<>();


		DFAgentDescription template = new DFAgentDescription();
		ServiceDescription sd = new ServiceDescription();
		sd.setType("player");
		template.addServices(sd);

		sequentialBehaviour = new SequentialBehaviour();
		sequentialBehaviour.addSubBehaviour(new BoardWaitingBehaviour(this, template));
		sequentialBehaviour.addSubBehaviour(new BoardSetupBehaviour(this));
		sequentialBehaviour.addSubBehaviour(new BoardPlayingBehaviour(this));
		addBehaviour(sequentialBehaviour);

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


	public SequentialBehaviour getSequentialBehaviour() {
		return sequentialBehaviour;
	}
	
	public void pushDecision(Decision decision){
		decisions.add(decision);
	}
	
	public Decision popDecision() {		
		return decisions.poll();
	}
}
