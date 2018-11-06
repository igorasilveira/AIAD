package agents.behaviours;

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

public class BoardSetupBehaviour extends SubscriptionInitiator{

	private TimerTask task = new TimerSchedule(this);
	private Timer timer = new Timer(true);
	
	public BoardSetupBehaviour(Agent agent, DFAgentDescription dfad) {
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
				this.myAgent.send(msg);

				((BoardAgent) this.getAgent()).togglePlayer(agent);
				if(((BoardAgent) this.getAgent()).getPlayerAmount()  > 6)
				{
					((BoardAgent) this.getAgent()).togglePlayer(agent);
				}

				if(((BoardAgent) this.getAgent()).getPlayerAmount()  > 2)
				{
					timer.schedule(task, 10000);
					System.out.println("Scheduled timer");
				} else
				{
					System.out.println("Unscheduled timer");
					timer.cancel();
					timer.purge();
					timer = new Timer(true);
					task = new TimerSchedule(this);
				}


			}
		} catch (FIPAException fe) {
			fe.printStackTrace();
		}

	}

	
	public class TimerSchedule extends TimerTask {
		BoardSetupBehaviour behaviour;
		public TimerSchedule(BoardSetupBehaviour behaviour) {
			super();
			this.behaviour = behaviour;
		}
		@Override
		public void run() {
			((BoardAgent) behaviour.getAgent()).getGame().setStage(GameStage.Setup);
			System.out.println(((BoardAgent) behaviour.getAgent()).getGame().getStage());
			behaviour.scheduleNext(true, 0);
		}
		
	}

}
