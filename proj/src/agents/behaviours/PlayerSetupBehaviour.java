package agents.behaviours;

import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.domain.AMSService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.AMSAgentDescription;
import jade.domain.FIPAAgentManagement.SearchConstraints;

public class PlayerSetupBehaviour extends Behaviour {

	public PlayerSetupBehaviour(Agent a) {
		super(a);
	}

	@Override
	public void action() {
		connectToBoard();

	}

	@Override
	public boolean done() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean connectToBoard(){
		AMSAgentDescription [] agents = null;

		try {
			SearchConstraints c = new SearchConstraints();
			c.setMaxResults ( new Long(-1) );

			agents = AMSService.search( this.myAgent, new AMSAgentDescription (), c );
		} catch (FIPAException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
//		AID myID = getAID();
//        for (int i=0; i<agents.length;i++)
//        {
//            AID agentID = agents[i].getName();
//            System.out.println(
//                ( agentID.equals( myID ) ? "*** " : "    ")
//                + i + ": " + agentID.getName() 
//            );
//        }


		return false;
	}

}
