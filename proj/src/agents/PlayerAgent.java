package agents;

import agents.behaviours.player.PlayerPlayingBehaviour;
import agents.behaviours.player.PlayerSetupBehaviour;
import agents.behaviours.player.PlayerWaitingBehaviour;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.core.AID;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import sajas.core.Agent;
import sajas.core.behaviours.SequentialBehaviour;
import sajas.domain.DFService;

public class PlayerAgent extends Agent {


	private AID boardAID;

	private PlayerMindset mindset = PlayerMindset.Random;

	public void setup() {

		Object[] args = getArguments();
		if(args != null)
		{
			String arg1 = args[0].toString();
			switch (arg1) {
			case "aggressive":
				this.mindset = PlayerMindset.Aggressive;
				break;

			case "defensive":
				this.mindset = PlayerMindset.Defensive;
				break;
			case "smart":
				this.mindset = PlayerMindset.Smart;
				break;
			default:
				break;
			}
		}

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

		SequentialBehaviour sequentialBehaviour = new SequentialBehaviour();

		sequentialBehaviour.addSubBehaviour(new PlayerWaitingBehaviour(this));
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

	public PlayerMindset getMindset() {
		return mindset;
	}

	public void setBoardAID(AID boardAID) {
		this.boardAID = boardAID;
	}

	public AID getBoardAID() {
		return boardAID;
	}
}
