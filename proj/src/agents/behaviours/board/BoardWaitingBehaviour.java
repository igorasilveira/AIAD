package agents.behaviours.board;

import java.util.Timer;
import java.util.TimerTask;

import agents.BoardAgent;
import jade.core.AID;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.lang.acl.ACLMessage;
import jade.proto.SubscriptionInitiator;
import logic.Game.GameStage;

public class BoardWaitingBehaviour extends SubscriptionInitiator {

	private TimerTask task = new TimerSchedule(this);
	private Timer timer = new Timer(true);

	private final int WAITING_TIME = 2 * 1000; // 15 seconds
	
	public BoardWaitingBehaviour(Agent agent, DFAgentDescription dfad) {
		super(agent, DFService.createSubscriptionMessage(agent, agent.getDefaultDF(), dfad, null));
	}

	protected void handleInform(ACLMessage inform) {
		try {
			DFAgentDescription[] dfds = DFService.decodeNotification(inform.getContent());
			for(int i=0; i<dfds.length; i++) {
				AID agent = dfds[i].getName();
				System.out.println("New agent in town: " + agent.getLocalName());

				ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
				msg.addReceiver(agent);
				msg.setContent("Welcome!!");
				myAgent.send(msg);

				((BoardAgent) myAgent).togglePlayer(agent);
				if(((BoardAgent) myAgent).getPlayerAmount()  > 6)
				{
					((BoardAgent) myAgent).togglePlayer(agent);
				}

				// reset timer always
				timer.cancel();
				timer.purge();
				timer = new Timer(true);
				task = new TimerSchedule(this);

				if(((BoardAgent) myAgent).getPlayerAmount()  > 2)
				{
					timer.schedule(task, WAITING_TIME);
					System.out.println("Scheduled timer");
				} else
				{
					System.out.println("Unscheduled timer");
				}


			}
		} catch (FIPAException fe) {
			fe.printStackTrace();
		}

	}

	
	public class TimerSchedule extends TimerTask {
		BoardWaitingBehaviour behaviour;
		public TimerSchedule(BoardWaitingBehaviour behaviour) {
			super();
			this.behaviour = behaviour;
		}
		@Override
		public void run() {
			((BoardAgent) behaviour.getAgent()).getGame().setStage(GameStage.Setup);
			System.out.println("Timer Complete");
			((BoardAgent) behaviour.getAgent()).getSequentialBehaviour().removeSubBehaviour(behaviour);
			System.out.println("Board WAITING BEHAVIOUR ENDED");
			((BoardAgent) behaviour.getAgent()).getSequentialBehaviour().reset();
		}
		
	}

}
