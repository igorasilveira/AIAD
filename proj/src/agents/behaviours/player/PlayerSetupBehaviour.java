package agents.behaviours.player;

import agents.BoardAgent;
import agents.PlayerAgent;
import agents.messages.Actions;
import agents.messages.PlayerAction;
import agents.messages.board.RequestPlayerAction;
import agents.messages.player.ProposePlayerAction;
import agents.messages.player.ProposePlayerSetup;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.domain.AMSService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.AMSAgentDescription;
import jade.domain.FIPAAgentManagement.SearchConstraints;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import logic.Game;
import logic.Territory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class PlayerSetupBehaviour extends Behaviour {

	Game lastGameState;
	private boolean setupFinished = false;

	public PlayerSetupBehaviour(Agent a) {
		super(a);
	}

	@Override
	public void onStart() {
		System.out.println(myAgent.getLocalName() + " - Player SETUP BEHAVIOUR STARTED");
	}

	@Override
	public void action() {
		MessageTemplate messageTemplate = MessageTemplate.and(
				MessageTemplate.MatchSender(((PlayerAgent) myAgent).getBoardAID()),
				MessageTemplate.or(
						MessageTemplate.MatchPerformative(ACLMessage.REQUEST),
						MessageTemplate.MatchPerformative(ACLMessage.INFORM)));

		ACLMessage request = myAgent.blockingReceive(messageTemplate);

		try {
			// set class current game to received from Board
			switch (request.getPerformative()) {
				case ACLMessage.REQUEST:
					lastGameState = ((RequestPlayerAction) request.getContentObject()).getGame();
					if (((PlayerAction) request.getContentObject()).getAction() == Actions.Setup) {

						ACLMessage response = request.createReply();
						response.setPerformative(ACLMessage.PROPOSE);

						ArrayList<Integer> territories = new ArrayList<Integer>();

						ArrayList<Territory> unclaimed = lastGameState.getUnclaimedTerrritories();

						if(unclaimed.size() > 0) {
							//TODO agent chooses territory
							Collections.shuffle(unclaimed);
							
							territories.add(unclaimed.get(0).territoryID);

						} else {
							//TODO agent chooses territory
							ArrayList<Territory> claimed = lastGameState.getClaimedTerritories(lastGameState.getCurrentPlayer().getID());
							Collections.shuffle(claimed);

							territories.add(claimed.get(0).territoryID);

						}

						PlayerAction action = new ProposePlayerSetup(territories);
						response.setContentObject(action);
						myAgent.send(response);
					}
					break;
					case ACLMessage.INFORM:
						if (((PlayerAction) request.getContentObject()).getAction() == Actions.EndSetup) {
							setupFinished = true;
						}
						break;
					default: break;
			}
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
		if (setupFinished)
			System.out.println(myAgent.getLocalName() + " - Player SETUP BEHAVIOUR ENDED");
		return setupFinished;
	}

}
