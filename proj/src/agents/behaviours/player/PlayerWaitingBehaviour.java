package agents.behaviours.player;

import agents.PlayerAgent;
import agents.messages.Actions;
import agents.messages.PlayerAction;
import agents.messages.board.RequestPlayerAction;
import agents.messages.board.RequestPlayerDefend;
import agents.messages.player.*;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import logic.*;
import sajas.core.AID;
import sajas.core.Agent;
import sajas.core.behaviours.Behaviour;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class PlayerWaitingBehaviour extends Behaviour {

	Game lastGameState;

	public PlayerWaitingBehaviour(Agent a) {
		super(a);
		// TODO Auto-generated constructor stub
	}


	@Override
	public void onStart() {
		System.out.println(myAgent.getLocalName() + " - Player WAITING BEHAVIOUR STARTED");
	}

	@Override
	public void action() {
		MessageTemplate template = MessageTemplate.MatchPerformative(ACLMessage.INFORM);

		ACLMessage msg = myAgent.receive(template);
		if (msg != null) {
			((PlayerAgent) myAgent).setBoardAID(msg.getSender());
			System.out.println("Player: Received Board initial message with AID = " + ((PlayerAgent) myAgent).getBoardAID());
		}
	}

	@Override
	public boolean done() {
		return ((PlayerAgent) myAgent).getBoardAID() != null;
	}
}
