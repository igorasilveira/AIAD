package agents.behaviours.board;

import java.io.IOException;
import java.security.acl.LastOwnerException;

import agents.BoardAgent;
import agents.messages.Actions;
import agents.messages.PlayerAction;
import agents.messages.board.RequestPlayerAction;
import agents.messages.player.ProposePlayerSetup;
import agents.messages.player.ProposePlayerTradeCards;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import logic.Game;
import logic.Territory;
import logic.Game.GameStage;

public class BoardPlayingBehaviour extends Behaviour {

	private Game game;

	private boolean setupMade = false; 

	private boolean cardsTraded = false;
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
		//Get current AID
		AID currentPlayer = this.game.getCurrentAID();

		PlayerAction request;

		// Send message to aid

		if(!cardsTraded)
		{
			//Change player units in game
			game.getCurrentPlayer().setUnits(game.getNewUnits(game.getCurrentPlayer().getID()));
			
			request = new RequestPlayerAction(Actions.TradeCards, this.game);
		} else{
			if(!setupMade)

			{
				request = new RequestPlayerAction(Actions.Setup, this.game);
			} else {
				request = new RequestPlayerAction(Actions.Play, this.game);
			}
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

				if (!setupMade) {
					//apply changes and continue
					//Send requestAction

					for (int territory : ((ProposePlayerSetup)action).getTerritories()) {
						game.getTerritory(territory).increaseUnits(1);
						game.getCurrentPlayer().decreaseUnits(1);
					}
					setupMade = true;
					cardsTraded = true;
				}
				break;
			case TradeCards:
				if(!cardsTraded)
				{
					game.getCurrentPlayer().increaseUnits(game.turnInCardSet(((ProposePlayerTradeCards) action).getCardSet(), game.getCurrentPlayer().getCards()));
					cardsTraded= true;
				}

				break;
			case Attack:

				//Request defender to choose dice amount
				//Apply attack or break if not valid	

				break;
			case Fortify:	

				//apply changes or break if not valid

				break;
			case Done:
				if (setupMade)
				{
					//apply changes and change turn
					setupMade = false;
				} 


				break;
			default:
				break;
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	@Override
	public boolean done() {
		return game.getStage().equals(GameStage.Finished);
	}

}
