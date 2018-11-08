package agents.behaviours.board;

import java.awt.*;
import java.io.IOException;
import java.security.acl.LastOwnerException;
import java.util.ArrayList;
import java.util.Random;

import agents.BoardAgent;
import agents.messages.Actions;
import agents.messages.PlayerAction;
import agents.messages.board.RequestPlayerAction;
import agents.messages.board.RequestPlayerDefend;
import agents.messages.player.ProposePlayerAttack;
import agents.messages.player.ProposePlayerDefend;
import agents.messages.player.ProposePlayerSetup;
import agents.messages.player.ProposePlayerTradeCards;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import logic.*;
import logic.Game.GameStage;

public class BoardPlayingBehaviour extends Behaviour {

	private Game game;

	private boolean setupMade = false;

	private boolean cardsTraded = false;

	private boolean attackerWonTerritory = false;

	public BoardPlayingBehaviour(Agent a) {
		super(a);
		this.game = ((BoardAgent) a).getGame();
	}

	@Override
	public void onStart(){
		System.out.println("Board PLAYING BEHAVIOUR STARTED");
//		game.start(((BoardAgent) myAgent).getPlayers());
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
			System.out.println(myAgent.getLocalName() + " Sent REQUEST to " + currentPlayer.getLocalName() + " with request for " + request.getAction().toString());

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

					System.out.println(myAgent.getLocalName() + " Received ATTACK from " + currentPlayer.getLocalName());
					Attack attack = ((ProposePlayerAttack) response.getContentObject()).getAttack();

					//Request defender to choose dice amount
					ACLMessage defendMessage = new ACLMessage(ACLMessage.REQUEST);
					RequestPlayerDefend requestPlayerDefend = new RequestPlayerDefend(game, attack);

					int defenderID = attack.getDefender().getPlayerID();
					AID defenderAID = game.findPlayerByID(defenderID).getAid();

					defendMessage.addReceiver(defenderAID);
					defendMessage.setContentObject(requestPlayerDefend);
					myAgent.send(defendMessage);
					System.out.println(myAgent.getLocalName() + " Sent DEFEND to Player " + defenderAID.getLocalName());

					MessageTemplate messageTemplate = MessageTemplate.and(
							MessageTemplate.MatchPerformative(ACLMessage.PROPOSE),
							MessageTemplate.MatchSender(defenderAID));

					ACLMessage defenderResponse = myAgent.blockingReceive(messageTemplate);

					System.out.println(myAgent.getLocalName() + " Received DEFEND from Player " + defenderAID.getLocalName());
					// resolve attack
					int defenderDiceAmount = ((ProposePlayerDefend) defenderResponse.getContentObject()).getDiceAmount();
					boolean[] result = game.diceRollWinner(attack.getDiceAmount(), defenderDiceAmount);

					int i = 0;

					Territory attackerTerritory = game.getTerritory(attack.getAttacker().territoryID);
					Territory defenderTerritory = game.getTerritory(attack.getDefender().territoryID);

					while (i < result.length && game.getTerritory(attackerTerritory.territoryID).getUnits() >= 2 && game.getTerritory(defenderTerritory.territoryID).getUnits() >= 1) {
						if (result[i]) {
							defenderTerritory.decreaseUnits(1);
						} else {
							attackerTerritory.decreaseUnits(1);
						}

						i++;
					}

					System.out.println(myAgent.getLocalName() + " Player " + currentPlayer.getLocalName() + " attacked " + defenderAID.getLocalName());

					// Attacker won
					if (defenderTerritory.getUnits() == 0) {

						attackerWonTerritory = true;

						defenderTerritory.setPlayerID(game.getCurrentPlayer().getID());
						defenderTerritory.increaseUnits(1);

						attackerTerritory.decreaseUnits(1);

						//TODO move units from the attacking territory if you want
						int amount = new Random().nextInt(attackerTerritory.getUnits());
						attackerTerritory.decreaseUnits(amount);
						defenderTerritory.increaseUnits(amount);

						//check if player was eliminated
						//remove player from list
						//TODO need to get cards from the player and if the total is 5 or more then you have to turn in
						//card sets and place the new units

						if (game.playerLost(defenderID)) {
							// TODO send defender LOST message?
							game.removePlayer(defenderID);
						}
						System.out.println("Turn: " + currentPlayer.getLocalName() + "  changed territory " + defenderTerritory.territoryID);

						for(Player pl : game.getPlayers()) {
							ArrayList<Territory> c = game.getClaimedTerritories(pl.getID());

							System.out.println(pl.getAid().getLocalName() + " has " + c.size() + " territories!");
						}

						System.out.println();
					}

					break;
				case Fortify:

					//apply changes or break if not valid

					break;
				case Done:
					if (setupMade)
					{
						//apply changes and change turn

						if (attackerWonTerritory) {

							if (game.getCards().size() > 0) {
								System.out.println(myAgent.getLocalName() + " Attacker won territory and is awarded a card");
								game.getCurrentPlayer().addCard(game.getCards().remove(0));
							}

						}

						game.nextTurn();

						setupMade = false;
						cardsTraded = false;
						attackerWonTerritory = false;
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
		if (game.isGameFinished() != 0) {
			System.out.println(game.getPlayers().get(0).getAid().getLocalName() + " HAS OWN THE GAME!");

			Toolkit.getDefaultToolkit().beep();
			System.out.println("Board PLAYING BEHAVIOUR ENDED");
		}
		return game.isGameFinished() != 0;
	}

}
