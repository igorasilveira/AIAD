package agents;

import agents.behaviours.PlayerPlayingBehaviour;
import jade.core.AID;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class PlayerAgent extends Agent {
	
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
		this.boardAID = msg.getSender();
		
		System.out.println(this.boardAID.toString());

		addBehaviour(new PlayerPlayingBehaviour(this));
		
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
