package agents.behaviours.board;

import java.io.IOException;

import agents.BoardAgent;
import agents.messages.Actions;
import agents.messages.PlayerAction;
import agents.messages.board.RequestPlayerAction;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import logic.Game;
import logic.Game.GameStage;

public class BoardPlayingBehaviour extends Behaviour {

	private Game game;

	private boolean setupMade = false; // TODO after receiving a 'done' set back to false
	public BoardPlayingBehaviour(Agent a) {
		super(a);
		this.game = ((BoardAgent) a).getGame();
	}

	@Override
	public void onStart(){
		System.out.println("STARTED -------------------");
		game.start(((BoardAgent) myAgent).getPlayers());
	}

	@Override
	public void action() {
		/*//Get current AID
		AID currentPlayer = this.game.getCurrentAID();

		PlayerAction request;

		// Send message to aid
		if(this.game.getStage() == GameStage.Setup)
		{
			request = new RequestPlayerAction(Actions.Setup, this.game);
		} else if(this.game.getStage() == GameStage.Playing)
		{
			if(!setupMade)
			{
				request = new RequestPlayerAction(Actions.Setup, this.game);
			} else {
				request = new RequestPlayerAction(Actions.Play, this.game);
			}

		} else {
			return;
		}
		ACLMessage response;
		ACLMessage message = new ACLMessage(ACLMessage.REQUEST);
		try {
			message.setContentObject(request);
			message.addReceiver(currentPlayer);
			this.getAgent().send(message);

			//Get answer from aid
			MessageTemplate mt = MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.PROPOSE), MessageTemplate.MatchSender(currentPlayer));
			response = this.getAgent().blockingReceive(mt);

		} catch (IOException e) {
			e.printStackTrace();
			return;
		}// TODO make more robust


		//Check valid response and make changes
		try {
			PlayerAction action = (PlayerAction) response.getContentObject();
			switch (action.getAction()) {
			case Setup:

				if(this.game.getStage() == GameStage.Setup)
				{
					//apply changes and change turn

				} 

				if (!setupMade) {
					//apply changes and continue
					//Send requestAction
				}
				break;
			case Attack:
				if(this.game.getStage() == GameStage.Playing)
				{
					//Request defender to choose dice amount
					//Apply attack or break if not valid	
				}
				break;
			case Fortify:	
				if(this.game.getStage() == GameStage.Playing)
				{
					//apply changes or break if not valid
				}
				
				break;
			case Done:
				if (setupMade)
				{
					//apply changes and change turn
				} 


				break;
			default:
				break;
			}
		} catch (Exception e) {
			// TODO: handle exception
		}*/

	}

	@Override
	public boolean done() {
		return game.getStage().equals(GameStage.Finished);
	}

}
