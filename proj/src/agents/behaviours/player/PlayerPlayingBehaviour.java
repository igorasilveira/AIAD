package agents.behaviours.player;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import agents.PlayerAgent;
import agents.messages.Actions;
import agents.messages.PlayerAction;
import agents.messages.board.RequestPlayerAction;
import agents.messages.board.RequestPlayerDefend;
import agents.messages.player.*;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import logic.*;

public class PlayerPlayingBehaviour extends Behaviour {

	Game lastGameState;

	public PlayerPlayingBehaviour(Agent a) {
		super(a);
		// TODO Auto-generated constructor stub
	}


	@Override
	public void onStart() {
		System.out.println(myAgent.getLocalName() + " - Player PLAYING BEHAVIOUR STARTED");
	}

	@Override
	public void action() {
		MessageTemplate messageTemplate = MessageTemplate.and(
				MessageTemplate.MatchSender(((PlayerAgent) myAgent).getBoardAID()),
				MessageTemplate.or(
						MessageTemplate.MatchPerformative(ACLMessage.REQUEST),
						MessageTemplate.MatchPerformative(ACLMessage.INFORM)));

		ACLMessage request = myAgent.blockingReceive(messageTemplate);
		PlayerAction action;
		try {
			// set class current game to received from Board
			lastGameState = ((RequestPlayerAction) request.getContentObject()).getGame();
			ACLMessage response = request.createReply();
			response.setPerformative(ACLMessage.PROPOSE);

			if (((PlayerAction) request.getContentObject()).getAction() == Actions.Setup) {
				System.out.println(myAgent.getLocalName() + " Received SETUP");

				action = setup();

			} else if (((PlayerAction) request.getContentObject()).getAction() == Actions.TradeCards) {
				System.out.println(myAgent.getLocalName() + " Received TRADECARDS");

				action = setup();

				ArrayList<Card> playerCards = lastGameState.getCurrentPlayer().getCards();
				ArrayList<CardSet> sets;

				if (playerCards.size() >= 5)
				{
					sets = lastGameState.getCardSets(playerCards);
					CardSet set = chooseCardSet(sets);

					action = new ProposePlayerTradeCards(set);
				}
				else {
					//TODO turning in a set is optional if you have 4 cards or fewer
					sets = lastGameState.getCardSets(playerCards);

					if(sets.size() > 0) {
						sets = lastGameState.getCardSets(playerCards);
						boolean trade = chooseToTrade();

						if (trade) { // trade cards
							CardSet set = chooseCardSet(sets);
							action = new ProposePlayerTradeCards(set);
						}
					}
				}

			} else if (((PlayerAction) request.getContentObject()).getAction() == Actions.Defend){
				System.out.println(myAgent.getLocalName() + " Received DEFEND");

				Attack attack = ((RequestPlayerDefend)request.getContentObject()).getAttack();

				int defDice = chooseDefenseDiceAmount(attack);
				
				action = new ProposePlayerDefend(defDice);


			} else { // Play

				System.out.println(myAgent.getLocalName() + " Received PLAY");
				ArrayList<Attack> attacks = lastGameState.getAttackOptions(lastGameState.getCurrentPlayer().getID());

				action = new ProposePlayerAction(Actions.Done);

				if (attacks.size() > 0) {

					// TODO decide to battle or not
					Random ran = new Random();
					int n = ran.nextInt(2);

					if (n == 0) {  // attack

						// TODO choose good attack
						Collections.shuffle(attacks);

						Attack attack = attacks.get(0);

						action = new ProposePlayerAttack(attack);
					} else { //dont attack
						action = fortify();
					}

				} else {
					action = fortify();
				}

			}

			response.setContentObject(action);
			myAgent.send(response);
			System.out.println(myAgent.getLocalName() + " Send action with ACTION:" + action.getAction().toString());
		} catch (UnreadableException e) {
			e.printStackTrace();
			return;
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
	}

	private boolean chooseToTrade() {
		switch(((PlayerAgent)myAgent).getMindset()) {
		case Aggressive:
		case Defensive:
			return true;
		case Random:
			Random r = new Random();
			return r.nextBoolean();
		case Smart:
			return false;
		default:
			break;
		}
		
		return false;
	}


	private CardSet chooseCardSet(ArrayList<CardSet> sets) {
		if(sets.size() > 1) {
			for(CardSet set : sets) {
				ArrayList<Card> cards = set.getCards();
				boolean foundWildCard = false;
				
				for(Card card : cards) {
					if(card.army == null) {
						foundWildCard = true;
					}
				}
				
				if(!foundWildCard) {
					return set;
				}
			}
		}
		
		return sets.get(0);
	}


	private int chooseDefenseDiceAmount(Attack attack) {
		int defDice;
		Territory defender = attack.getDefender();
		int attackerDice = attack.getDiceAmount();
		
		switch(((PlayerAgent)myAgent).getMindset()) {
		case Aggressive: 
			// Chooses highest probability of winning (without caring for the amount of pieces at stake)
			defDice = 2;
			break;
		case Defensive:
			// Chooses smallest amount of pieces at stake
			if(attackerDice == 1){
				defDice = 2;
			} else {
				defDice = 1;
			}
			break;
		case Smart:
			//Chooses based on the amount of units at stake and the probability of winning
			if(attackerDice == 1){
				defDice = 2;
			} else {
				if(defender.getUnits() == 2)
				{
					defDice = 1;
				} else {
					defDice = 2;					
				}

			}
			break;
		default:
			defDice = new Random().nextInt(2) + 1;
		}
		return defDice;
	}


	public PlayerAction fortify() {
		PlayerAction action = new ProposePlayerAction(Actions.Done);
		ArrayList<Fortify> fortifications = lastGameState.getFortifyOptions(lastGameState.getCurrentPlayer().getID());

		Random ran = new Random();
		int n = ran.nextInt(2);

		if(fortifications.size() > 0 && (n == 0))
		{
			action = new ProposePlayerFortify(fortifications.get(0));
		}

		return action;
	}

	public PlayerAction setup() {

		ArrayList<Integer> territories = new ArrayList<Integer>();
		ArrayList<Territory> claimed = lastGameState.getClaimedTerritories(lastGameState.getCurrentPlayer().getID());

		int units = lastGameState.getCurrentPlayer().getUnitsLeft();

		while (units > 0) {
			//TODO agent chooses territory

			Collections.shuffle(claimed);
			territories.add(claimed.get(0).territoryID);
			units--;
		}

		return new ProposePlayerSetup(territories);
	}

	@Override
	public boolean done() {
		if (lastGameState.isGameFinished() != 0)
			System.out.println(myAgent.getLocalName() + " - Player PLAYING BEHAVIOUR ENDED");
		return lastGameState.isGameFinished() != 0;
	}
}
