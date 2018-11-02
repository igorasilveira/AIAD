package agents.behaviours;

import jade.core.AID;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.lang.acl.ACLMessage;
import jade.proto.SubscriptionInitiator;

import agents.BoardAgent;

public class BoardSetupBehaviour extends SubscriptionInitiator {

	private boolean connectingAgents;

	public BoardSetupBehaviour(Agent agent, DFAgentDescription dfad) {
		super(agent, DFService.createSubscriptionMessage(agent, agent.getDefaultDF(), dfad, null));
	}

	protected void handleInform(ACLMessage inform) {
		try {
			DFAgentDescription[] dfds = DFService.decodeNotification(inform.getContent());
			for(int i=0; i<dfds.length; i++) {
				AID agent = dfds[i].getName();
				System.out.println("New agent in town: " + agent.getLocalName());
				((BoardAgent) this.getAgent()).togglePlayer(agent);
				System.out.println(((BoardAgent) this.getAgent()).getPlayerAmount());
			}
		} catch (FIPAException fe) {
			fe.printStackTrace();
		}
	}


}
