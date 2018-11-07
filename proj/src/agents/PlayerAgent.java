package agents;

import agents.behaviours.player.PlayerPlayingBehaviour;
import agents.behaviours.player.PlayerSetupBehaviour;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.SequentialBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class PlayerAgent extends Agent {

	public AID getBoardAID() {
		return boardAID;
	}

	private AID boardAID;
	
	public void setup() {
		
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(getAID());
		ServiceDescription sd = new ServiceDescription();
		sd.setType("player");
		sd.setName(getLocalName());
		dfd.addServices(sd);
		try {
			DFService.register(this, dfd);
		} catch(FIPAException fe) {
			fe.printStackTrace();
		}
		MessageTemplate template = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
		ACLMessage msg = blockingReceive();
		System.out.println("Player: Received Board initial message with AID");
		this.boardAID = msg.getSender();
		
		System.out.println(this.boardAID.toString());

		SequentialBehaviour sequentialBehaviour = new SequentialBehaviour();

		sequentialBehaviour.addSubBehaviour(new PlayerSetupBehaviour(this));
		sequentialBehaviour.addSubBehaviour(new PlayerPlayingBehaviour(this));

		addBehaviour(sequentialBehaviour);

		System.out.println(getLocalName() + ": starting to work!");
	}
	public void takeDown() {
		try {
			DFService.deregister(this);  
		} catch(FIPAException e) {
			e.printStackTrace();
		}
		System.out.println(getLocalName() + ": done working.");
	}
}
