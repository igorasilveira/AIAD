package agents.behaviours.player;

import agents.PlayerAgent;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import sajas.core.Agent;
import sajas.core.behaviours.Behaviour;

public class PlayerWaitingBehaviour extends Behaviour {

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
