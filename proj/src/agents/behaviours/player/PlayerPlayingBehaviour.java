package agents.behaviours.player;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import agents.PlayerAgent;
import agents.messages.Actions;
import agents.messages.PlayerAction;
import agents.messages.board.RequestPlayerAction;
import agents.messages.player.ProposePlayerAction;
import agents.messages.player.ProposePlayerSetup;
import agents.messages.player.ProposePlayerTradeCards;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import logic.Card;
import logic.CardSet;
import logic.Game;
import logic.Territory;

public class PlayerPlayingBehaviour extends Behaviour {

	Game lastGameState;

	public PlayerPlayingBehaviour(Agent a) {
		super(a);
		// TODO Auto-generated constructor stub
	}


	@Override
	public void action() {
		MessageTemplate messageTemplate = MessageTemplate.and(
				MessageTemplate.MatchSender(((PlayerAgent) myAgent).getBoardAID()),
				MessageTemplate.MatchPerformative(ACLMessage.REQUEST));

		ACLMessage request = myAgent.blockingReceive(messageTemplate);
		PlayerAction action;
		try {
			// set class current game to received from Board
			lastGameState = ((RequestPlayerAction) request.getContentObject()).getGame();
			ACLMessage response = request.createReply();
			response.setPerformative(ACLMessage.PROPOSE);
			ArrayList<Integer> territories = new ArrayList<Integer>();

			if (((PlayerAction) request.getContentObject()).getAction() == Actions.Setup) {

				ArrayList<Territory> claimed = lastGameState.getClaimedTerritories(lastGameState.getCurrentPlayer().getID());
				
				while (lastGameState.getCurrentPlayer().getUnitsLeft() > 0) {
					//TODO agent chooses territory

					Collections.shuffle(claimed);
					territories.add(claimed.get(0).territoryID);
				}
					
				action = new ProposePlayerSetup(territories);

			} else if (((PlayerAction) request.getContentObject()).getAction() == Actions.TradeCards) {
				ArrayList<Card> playerCards = lastGameState.getCurrentPlayer().getCards();
				ArrayList<CardSet> sets;
				
				if(playerCards.size() >= 5)
				{
					sets = lastGameState.getCardSets(playerCards);
					// TODO choose set
					
					Collections.shuffle(sets);
					action = new ProposePlayerTradeCards(sets.get(0));
				
				} else {
					sets = lastGameState.getCardSets(playerCards);
					// TODO choose set and if wants to trade
					
					Collections.shuffle(sets);
					action = new ProposePlayerTradeCards(sets.get(0));
				}
				
				
			} else if (((PlayerAction) request.getContentObject()).getAction() == Actions.Defend){
				action = new ProposePlayerAction(Actions.Defend);
			}else { // Play
				action = new ProposePlayerAction(Actions.Attack);
//				action = new ProposePlayerAction(Actions.Fortify);
//				action = new ProposePlayerAction(Actions.Done);
			}
			
			response.setContentObject(action);
			myAgent.send(response);
		} catch (UnreadableException e) {
			e.printStackTrace();
			return;
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
	}

	@Override
	public boolean done() {
		return lastGameState.isGameFinished() != 0;
	}
}
