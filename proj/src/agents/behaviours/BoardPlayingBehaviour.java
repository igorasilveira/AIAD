package agents.behaviours;

import java.util.ArrayList;

import agents.BoardAgent;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import logic.Game;
import logic.Game.GameStage;

public class BoardPlayingBehaviour extends Behaviour {

	private Game game;
	public BoardPlayingBehaviour(Agent a, ArrayList<AID> players) {
		super(a);
		this.game = ((BoardAgent) a).getGame();
		game.start(players);
	}

	@Override
	public void action() {
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
