package agents.behaviours.player;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.Random;

import agents.PlayerAgent;
import agents.PlayerMindset;
import agents.messages.Actions;
import agents.messages.PlayerAction;
import agents.messages.board.RequestPlayerAction;
import agents.messages.board.RequestPlayerDefend;
import agents.messages.player.ProposePlayerAction;
import agents.messages.player.ProposePlayerAttack;
import agents.messages.player.ProposePlayerDefend;
import agents.messages.player.ProposePlayerFortify;
import agents.messages.player.ProposePlayerSetup;
import agents.messages.player.ProposePlayerTradeCards;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import logic.Attack;
import logic.Card;
import logic.CardSet;
import logic.Fortify;
import logic.Game;
import logic.Territory;

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
				ArrayList<Fortify> fortifications = lastGameState.getFortifyOptions(lastGameState.getCurrentPlayer().getID());

				action = new ProposePlayerAction(Actions.Done);

				if (attacks.size() > 0) {

					// TODO decide to battle or not
					Random ran = new Random();
					int n = ran.nextInt(2);

					if (n == 0) {  // attack

						Attack attack = chooseAttack();

						action = new ProposePlayerAttack(attack);
					} else if(fortifications.size() > 0) { //dont attack
						action = fortify();
					}

				} else if(fortifications.size() > 0) {
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

	private Attack chooseAttack() {
		ArrayList<Attack> attacks = lastGameState.getAttackOptions(lastGameState.getCurrentPlayer().getID());
		Attack attack;

		if(((PlayerAgent)myAgent).getMindset() == PlayerMindset.Random)
		{
			Collections.shuffle(attacks);
			attack = attacks.get(0);
		} else
		{
			attack = chooseStrategicAttack();
		}

		//Choose dice amount
		chooseAttackerDiceAmount(attack);

		return attack;
	}

	private Attack chooseStrategicAttack() {
		ArrayList<Attack> attacks = lastGameState.getAttackOptions(lastGameState.getCurrentPlayer().getID());

		//Choose best target
		//Group attacks by defender

		Hashtable<Integer, ArrayList<Attack>> viableOptions = new Hashtable<>();

		for (Attack attack : attacks) {
			if(viableOptions.containsKey(attack.getDefender().territoryID))
			{
				viableOptions.get(attack.getDefender().territoryID).add(attack);
			} else {
				ArrayList<Attack> list = new ArrayList<>();
				list.add(attack);
				viableOptions.put(attack.getDefender().territoryID, list);
			}
		}

		ArrayList<ArrayList<Attack>> orderedOptions = new ArrayList<>(viableOptions.values());

		// Sort defenders by calculating the advantage of the attacker in each attack
		// Best atacks will be at front
		Collections.sort(orderedOptions, new Comparator<ArrayList<Attack>>() {
			public int compare(ArrayList<Attack> list1, ArrayList<Attack> list2) {
				return calculateAttackerAdvantage(list2)-calculateAttackerAdvantage(list1);
			}
		});

		//Choose best attacking territory
		Comparator<Attack> compareAttackerUnits = new Comparator<Attack>() {
			public int compare(Attack attack1, Attack attack2) {
				return attack2.getAttacker().getUnits() - attack1.getAttacker().getUnits();
			}
		};

		// Order the top options' attacks to choose the best attacker
		int maxIndex = 0;
		int maxAdvantage = calculateAttackerAdvantage(orderedOptions.get(0));
		System.out.println("MAX ADVANTAGE: " + maxAdvantage);
		for (int i = 1; i < orderedOptions.size(); i++) {

			if(calculateAttackerAdvantage(orderedOptions.get(i)) < maxAdvantage)
			{
				Collections.sort(orderedOptions.get(i), compareAttackerUnits);
				break;
			} else {
				maxIndex++;
			}
		}
		System.out.println("MAX INDEX: " + maxIndex);
		Attack chosenAttack = orderedOptions.get(0).get(0);

		// if more than one territory has the maximum advantage, the attacker chosen will be the one with more units
		for (int i = 1; i <= maxIndex; i++) {
			System.out.println("CHOSEN ATTACKER UNITS: " + chosenAttack.getAttacker().getUnits());
			if(orderedOptions.get(i).get(0).getAttacker().getUnits() > chosenAttack.getAttacker().getUnits())
			{
				chosenAttack = orderedOptions.get(i).get(0);
			}
		}
		System.out.println("CHOSEN ATTACKER UNITS: " + chosenAttack.getAttacker().getUnits());
		return chosenAttack;
	}


	private int calculateAttackerAdvantage(ArrayList<Attack> list) {
		// Attacker advantage is 
		// (its total amount of units surrounding the target) - (target territory's units)
		int valueList = - list.get(0).getDefender().getUnits();

		for (Attack attack : list) {
			valueList += attack.getAttacker().getUnits();
		}
		return valueList;
	}

	private int calculateDefenderDisadvantage(Territory territory) {

		ArrayList<Attack> potencialRisksList = lastGameState.getDefenseOptions(territory);

		// Defender disadvantage is 
		// (max(total units of same player surrounding territory)) - (its units in a territory)
		int defenderUnits = potencialRisksList.get(0).getDefender().getUnits();
		Hashtable<Integer, Integer> surroundingUnits = new Hashtable<>();

		for (Attack attack : potencialRisksList) {
			if(surroundingUnits.containsKey(attack.getAttacker().getPlayerID()))
			{
				int units =  surroundingUnits.get(attack.getAttacker().getPlayerID());
				units += attack.getAttacker().getUnits();
				surroundingUnits.remove(attack.getAttacker().getPlayerID());
				surroundingUnits.put(attack.getAttacker().getPlayerID(), units);
			} else {
				surroundingUnits.put(attack.getAttacker().getPlayerID(), attack.getAttacker().getUnits());
			}
		}


		ArrayList<Integer> values = (ArrayList<Integer>) surroundingUnits.values();
		Collections.sort(values);
		Collections.reverse(values);
		return  values.get(0) - defenderUnits;
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

	private void chooseAttackerDiceAmount(Attack attack) {
		int defDice;

		switch(((PlayerAgent)myAgent).getMindset()) {
		case Aggressive: 
			// Chooses highest probability of winning (without caring for the amount of pieces at stake)
			defDice = 3;
			break;
		case Defensive:
			// Chooses smallest amount of pieces at stake
			defDice = 1;
			break;
		case Smart:
			//Chooses based on the amount of units at stake and the probability of winning
			if(attack.getAttacker().getUnits() == 2){
				defDice = 1;
			} else if(attack.getDefender().getUnits() == 2) {
				defDice = 2;

			} else{
				defDice = 3;
			}
			break;
		default:
			defDice = new Random().nextInt(2) + 1;
		}

		attack.setDiceAmount(defDice);
	}


	public PlayerAction fortify() {
		PlayerAction action = new ProposePlayerAction(Actions.Done);

		Random ran = new Random();
		int n = ran.nextInt(2);

		action = new ProposePlayerFortify(chooseStrategicFortification());


		return action;
	}

	private Fortify chooseStrategicFortification() {
		Fortify chosenFortification;
		ArrayList<Fortify> fortifications = lastGameState.getFortifyOptions(lastGameState.getCurrentPlayer().getID());

		Hashtable<Integer, Integer> territoriesDisadvantage =  new Hashtable<>();

		for (Fortify fortify : fortifications) {
			if(! territoriesDisadvantage.containsKey(fortify.from.territoryID))
			{
				territoriesDisadvantage.put(fortify.from.territoryID, calculateDefenderDisadvantage(fortify.from));
			}
			if(! territoriesDisadvantage.containsKey(fortify.to.territoryID))
			{
				territoriesDisadvantage.put(fortify.to.territoryID, calculateDefenderDisadvantage(fortify.to));
			}
		}
		chosenFortification = fortifications.get(0);
		for (int i = 1; i < fortifications.size(); i++) {

			int chosenOriginDisadvantage = territoriesDisadvantage.get(chosenFortification.from.territoryID);
			int chosenDestinationDisadvantage  = territoriesDisadvantage.get(chosenFortification.to.territoryID);
			int newOriginDisadvantage  = territoriesDisadvantage.get(fortifications.get(i).from.territoryID);
			int newDestinationDisadvantage  = territoriesDisadvantage.get(fortifications.get(i).to.territoryID);

			if(chosenDestinationDisadvantage == 0 || newDestinationDisadvantage == 0)
			{
				if(newOriginDisadvantage < chosenOriginDisadvantage)
				{
					chosenFortification = fortifications.get(i);
					continue;
				}
			}

			float chosenRatio = chosenOriginDisadvantage / ((float) chosenDestinationDisadvantage);

			float newRatio = newOriginDisadvantage / ((float) newDestinationDisadvantage);

			if(chosenRatio * newRatio > 0) {
				if(Math.abs(chosenRatio) < Math.abs(newRatio))
				{
					chosenFortification = fortifications.get(i);
				}
			} else {
				if(chosenRatio > newRatio)
				{
					chosenFortification = fortifications.get(i);
				}
			}

		}

		return chosenFortification;

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
