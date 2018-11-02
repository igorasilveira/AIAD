package agents.behaviours;

import agents.BoardAgent;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import logic.Game;
import logic.Game.GameStage;

public class BoardPlayingBehaviour extends Behaviour {

	private Game game;
	public BoardPlayingBehaviour(Agent a) {
		super(a);
		this.game = ((BoardAgent) a).getGame();
		
	}
	
	@Override
	public void onStart(){
		System.out.println("start");
		game.start(((BoardAgent) myAgent).getPlayers());
	}

	@Override
	public void action() {
		//System.out.println("playing");
		//Get current AID
		// Send message to aid
		//Get answer from aid
		//Check valid response
		//Send response to game
		//Send response do AID
		
	}

	@Override
	public boolean done() {
		return game.getStage().equals(GameStage.Finished);
	}

}
