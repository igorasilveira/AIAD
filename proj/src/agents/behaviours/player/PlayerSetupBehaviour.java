package agents.behaviours.player;

import agents.BoardAgent;
import agents.PlayerAgent;
import agents.messages.Actions;
import agents.messages.PlayerAction;
import agents.messages.board.RequestPlayerAction;
import agents.messages.player.ProposePlayerAction;
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

import java.io.IOException;

public class PlayerSetupBehaviour extends Behaviour {

	Game lastGameState;

	public PlayerSetupBehaviour(Agent a) {
		super(a);
	}

	@Override
	public void action() {
		MessageTemplate messageTemplate = MessageTemplate.and(
				MessageTemplate.MatchSender(((PlayerAgent) myAgent).getBoardAID()),
				MessageTemplate.MatchPerformative(ACLMessage.REQUEST));

		ACLMessage request = myAgent.blockingReceive(messageTemplate);

		try {
			// set class current game to received from Board
			lastGameState = ((RequestPlayerAction) request.getContentObject()).getGame();

			if (((PlayerAction) request.getContentObject()).getAction() == Actions.Setup) {
				System.out.println("Received Setup Request from Board");

				ACLMessage response = request.createReply();
				response.setPerformative(ACLMessage.PROPOSE);

				PlayerAction action = new ProposePlayerAction(Actions.Setup);

				response.setContentObject(action);

				myAgent.send(response);
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
		return lastGameState.setupFinished();
	}

}
