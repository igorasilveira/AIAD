package agents;

import agents.behaviours.PlayerPlayingBehaviour;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

public class PlayerAgent extends Agent {
	
	
	
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
