package agents.behaviours.player;

import agents.PlayerAgent;
import agents.PlayerMindset;
import agents.messages.Actions;
import agents.messages.PlayerAction;
import agents.messages.board.RequestPlayerAction;
import agents.messages.player.ProposePlayerSetup;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import logic.Continent;
import logic.Game;
import logic.Territory;
import sajas.core.Agent;
import sajas.core.behaviours.Behaviour;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class PlayerSetupBehaviour extends Behaviour {

	private Game lastGameState;
	private boolean setupFinished = false;

	public PlayerSetupBehaviour(Agent a) {
		super(a);
	}

	@Override
	public void onStart() {
		System.out.println(myAgent.getLocalName() + " - Player SETUP BEHAVIOUR STARTED");
	}

	@Override
	public void action() {
		MessageTemplate messageTemplate = MessageTemplate.and(
				MessageTemplate.MatchSender(((PlayerAgent) myAgent).getBoardAID()),
				MessageTemplate.or(
						MessageTemplate.MatchPerformative(ACLMessage.REQUEST),
						MessageTemplate.MatchPerformative(ACLMessage.INFORM)));

		ACLMessage request = myAgent.receive(messageTemplate);

		if (request != null) {

			try {
				// set class current game to received from Board
				switch (request.getPerformative()) {
					case ACLMessage.REQUEST:
						lastGameState = ((RequestPlayerAction) request.getContentObject()).getGame();
						if (((PlayerAction) request.getContentObject()).getAction() == Actions.Setup) {

							ACLMessage response = request.createReply();
							response.setPerformative(ACLMessage.PROPOSE);

							ArrayList<Integer> territories = new ArrayList<>();

							ArrayList<Territory> unclaimed = lastGameState.getUnclaimedTerrritories();

                            if(unclaimed.size() > 0) {

                                territories.add(chooseUnclaimedTerritories(unclaimed));

                            }
                            else {
                                //TODO agent chooses territory
                                ArrayList<Territory> claimed = lastGameState.getClaimedTerritories(lastGameState.getCurrentPlayer().getID());

                                territories.add(chooseClaimedTerritories(claimed));

							}

							PlayerAction action = new ProposePlayerSetup(territories);
							response.setContentObject(action);
							myAgent.send(response);
						}
						break;
						case ACLMessage.INFORM:
							if (((PlayerAction) request.getContentObject()).getAction() == Actions.EndSetup) {
								setupFinished = true;
							}
							break;
						default: break;
				}
			} catch (UnreadableException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private int chooseClaimedTerritories(ArrayList<Territory> claimed) {
		Collections.shuffle(claimed);

		if(((PlayerAgent)myAgent).getMindset() == PlayerMindset.Random) {
			return claimed.get(0).territoryID;
		}


		int id = lastGameState.getCurrentPlayer().getID();

		for(Territory t : claimed) {
			ArrayList<Territory> neighbours = t.getNeighbours();

			for(Territory n : neighbours) {
				if(n.getPlayerID() != id) {
					return t.territoryID;
				}
			}
		}

		return claimed.get(0).territoryID;
	}

	private int chooseUnclaimedTerritories(ArrayList<Territory> unclaimed) {
		Collections.shuffle(unclaimed);

		switch(((PlayerAgent)myAgent).getMindset()) {
		case Smart:
			Collections.sort(unclaimed, (t1, t2) -> {
				ArrayList<Territory> t1Neighbours = t1.getNeighbours();
				ArrayList<Territory> t2Neighbours = t2.getNeighbours();

				int id = lastGameState.getCurrentPlayer().getID();

				int t1C = 0;
				int t2C = 0;

				for(Territory t : t1Neighbours) {
					if(t.getPlayerID() == id) {
						t1C++;
					}
				}

				for(Territory t : t2Neighbours) {
					if(t.getPlayerID() == id) {
						t2C++;
					}
				}

				if(t1C < t2C) {
					return 1;
				}

				if(t1C > t2C) {
					return -1;
				}

				if(t1.getContinentID() == t2.getContinentID()) {
					return 0;
				}

				Continent t1Continent = lastGameState.getContinent(t1.getContinentID());
				Continent t2Continent = lastGameState.getContinent(t2.getContinentID());

				t1C = 0;
				t2C = 0;

				for(Territory t : t1Continent.getTerritories()) {
					if(t.getPlayerID() == id) {
						t1C++;
					}
				}

				for(Territory t : t2Continent.getTerritories()) {
					if(t.getPlayerID() == id) {
						t2C++;
					}
				}

				if(t1C < t2C) {
					return 1;
				}

				if(t1C > t2C) {
					return -1;
				}

				return 0;
			});
			return unclaimed.get(0).territoryID;
		case Aggressive:
			Collections.sort(unclaimed, (t1, t2) -> {
				ArrayList<Territory> t1Neighbours = t1.getNeighbours();
				ArrayList<Territory> t2Neighbours = t2.getNeighbours();

				int id = lastGameState.getCurrentPlayer().getID();

				int t1C = 0;
				int t2C = 0;

				for(Territory t : t1Neighbours) {
					if(t.getPlayerID() != id) {
						t1C++;
					}
				}

				for(Territory t : t2Neighbours) {
					if(t.getPlayerID() != id) {
						t2C++;
					}
				}

				if(t1C < t2C) {
					return 1;
				}

				if(t1C > t2C) {
					return -1;
				}

				return 0;
			});

			return unclaimed.get(0).territoryID;
		case Defensive:
			Collections.sort(unclaimed, (t1, t2) -> {
				ArrayList<Territory> t1Neighbours = t1.getNeighbours();
				ArrayList<Territory> t2Neighbours = t2.getNeighbours();

				int id = lastGameState.getCurrentPlayer().getID();

				int t1C = 0;
				int t2C = 0;

				for(Territory t : t1Neighbours) {
					if(t.getPlayerID() == id) {
						t1C++;
					}
				}

				for(Territory t : t2Neighbours) {
					if(t.getPlayerID() == id) {
						t2C++;
					}
				}

				if(t1C < t2C) {
					return 1;
				}

				if(t1C > t2C) {
					return -1;
				}

				return 0;
			});

			return unclaimed.get(0).territoryID;
		case Random:
			return unclaimed.get(0).territoryID;
		default:
			break;
		}

		return -1;
	}

	@Override
	public boolean done() {
		if (setupFinished)
			System.out.println(myAgent.getLocalName() + " - Player SETUP BEHAVIOUR ENDED");
		return setupFinished;
	}

}
