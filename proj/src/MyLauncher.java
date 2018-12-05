import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Vector;

import agents.BoardAgent;
import agents.PlayerAgent;
import agents.PlayerMindset;
import agents.messages.Actions;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.wrapper.ControllerException;
import jade.wrapper.StaleProxyException;
import logic.Continent;
import logic.Game;
import logic.Map;
import logic.Territory;
import logic.Unit;
import logic.Utils;
import sajas.core.Runtime;
import sajas.sim.repast3.Repast3Launcher;
import sajas.wrapper.ContainerController;
import uchicago.src.reflector.ListPropertyDescriptor;
import uchicago.src.sim.analysis.*;
import uchicago.src.sim.engine.BasicAction;
import uchicago.src.sim.engine.Schedule;
import uchicago.src.sim.engine.SimInit;
import uchicago.src.sim.gui.DisplaySurface;
import uchicago.src.sim.gui.Object2DDisplay;
import uchicago.src.sim.gui.TextDisplay;
import uchicago.src.sim.space.Object2DTorus;

public class MyLauncher extends Repast3Launcher {

	private static boolean BATCH_MODE = true;

	private int runNumber = 1;

	private ArrayList<String> roundWinners;

	private BoardAgent boardAgent;

	private PlayerMindset playerOneMindset;
	private PlayerMindset playerTwoMindset;
	private PlayerMindset playerThreeMindset;
	private PlayerMindset playerFourMindset;
	private PlayerMindset playerFiveMindset;
	private PlayerMindset playerSixMindset;

	private ArrayList<Unit> agentList;
	private ArrayList<Map> mapAgent;
	private Schedule schedule;
	private DisplaySurface dsurf;
	private Object2DTorus space;
	private Object2DTorus background;
	private ArrayList<TextDisplay> infoDisplays;
	private OpenSequenceGraph plot;

	private DataRecorder fortifyRecorder;
	private DataRecorder attackRecorder;
	private DataRecorder defendRecorder;

	private Random random;

	private ArrayList<PlayerMindset> mindsets;

	private int spaceSize;
	private int numberOfPlayers;

	private ContainerController mainContainer;

	private MyLauncher() {
		random = new Random();
		this.spaceSize = 150;
		this.numberOfPlayers = 3;
		this.playerOneMindset = PlayerMindset.Smart;
		this.playerTwoMindset = PlayerMindset.Defensive;
		this.playerThreeMindset = PlayerMindset.Aggressive;
		this.playerFourMindset = PlayerMindset.Defensive;
		this.playerFiveMindset = PlayerMindset.Random;
		this.playerSixMindset = PlayerMindset.Random;
	}

	@Override
	public String[] getInitParam() {
		return new String[] {"numberOfPlayers", "playerOneMindset", "playerTwoMindset", "playerThreeMindset", "playerFourMindset", "playerFiveMindset", "playerSixMindset", };
	}

	//	public Schedule getSchedule() {
	//		return schedule;
	//	}


	@Override
	public String getName() {
		return "Risk";
	}

	@Override
	protected void launchJADE() {

		Runtime rt = Runtime.instance();
		Profile p1 = new ProfileImpl();
		mainContainer = rt.createMainContainer(p1);
		boardAgent = new BoardAgent();

		launchAgents();
	}

	private void launchAgents() {

		try {

			mainContainer.acceptNewAgent("MyAgent", boardAgent).start();

			mindsets = new ArrayList<>();
			mindsets.add(playerOneMindset);
			mindsets.add(playerTwoMindset);
			mindsets.add(playerThreeMindset);
			mindsets.add(playerFourMindset);
			mindsets.add(playerFiveMindset);
			mindsets.add(playerSixMindset);

			for (int i = 0; i < numberOfPlayers; i++) {
				PlayerAgent playerAgent = new PlayerAgent();
				playerAgent.setMindset(mindsets.get(i));
				mainContainer.acceptNewAgent("Player" + (i + 1), playerAgent).start();
			}

		} catch (StaleProxyException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void setup() {
		super.setup();

		roundWinners = new ArrayList<>();
		runNumber = 1;

		infoDisplays = new ArrayList<>();
		schedule = new Schedule();
		if (dsurf != null) dsurf.dispose();
		dsurf = new DisplaySurface(this, "Risk Display");
		registerDisplaySurface("Risk Display", dsurf);

		// property descriptors
		Vector<Integer> vMM = new Vector<>();
		for(int i = 3; i < 7; i++) {
			vMM.add(i);
		}
		descriptors.put("NumberOfPlayers", new ListPropertyDescriptor("NumberOfPlayers", vMM));

		Vector<PlayerMindset> pm1 = new Vector<>();
		for(int i = 0; i < PlayerMindset.values().length; i++) {
			pm1.add(PlayerMindset.values()[i]);
		}
		descriptors.put("PlayerOneMindset", new ListPropertyDescriptor("PlayerOneMindset", pm1));

		Vector<PlayerMindset> pm2 = new Vector<>();
		for(int i = 0; i < PlayerMindset.values().length; i++) {
			pm2.add(PlayerMindset.values()[i]);
		}
		descriptors.put("PlayerTwoMindset", new ListPropertyDescriptor("PlayerTwoMindset", pm2));

		Vector<PlayerMindset> pm3 = new Vector<>();
		for(int i = 0; i < PlayerMindset.values().length; i++) {
			pm3.add(PlayerMindset.values()[i]);
		}
		descriptors.put("PlayerThreeMindset", new ListPropertyDescriptor("PlayerThreeMindset", pm3));

		Vector<PlayerMindset> pm4 = new Vector<>();
		for(int i = 0; i < PlayerMindset.values().length; i++) {
			pm4.add(PlayerMindset.values()[i]);
		}
		descriptors.put("PlayerFourMindset", new ListPropertyDescriptor("PlayerFourMindset", pm4));

		Vector<PlayerMindset> pm5 = new Vector<>();
		for(int i = 0; i < PlayerMindset.values().length; i++) {
			pm5.add(PlayerMindset.values()[i]);
		}
		descriptors.put("PlayerFiveMindset", new ListPropertyDescriptor("PlayerFiveMindset", pm5));

		Vector<PlayerMindset> pm6 = new Vector<>();
		for(int i = 0; i < PlayerMindset.values().length; i++) {
			pm6.add(PlayerMindset.values()[i]);
		}
		descriptors.put("PlayerSixMindset", new ListPropertyDescriptor("PlayerSixMindset", pm6));
	}

	@Override
	public void begin() {
		super.begin();
		buildModel();
		if (!BATCH_MODE)
			buildDisplay();
		buildSchedule();
	}

	class Decision implements DataSource {
		public Object execute() {
			return boardAgent.popDecision().toString();
		}
	}

	private void buildModel() {
		agentList = new ArrayList<>();
		mapAgent = new ArrayList<>();
		space = new Object2DTorus(spaceSize, spaceSize);

		background = new Object2DTorus(spaceSize, spaceSize);
		try {
			Map map = new Map(0, 0, background);
			background.putObjectAt(0, 0, map);
			mapAgent.add(map);
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Fortify
		fortifyRecorder = new DataRecorder("sims/fortify.txt", this);
		fortifyRecorder.addObjectDataSource("play", new Decision());

		// Attack
		attackRecorder = new DataRecorder("sims/attack.txt", this);
		attackRecorder.addObjectDataSource("play", new Decision());

		// Defend
		defendRecorder = new DataRecorder("sims/defend.txt", this);
		defendRecorder.addObjectDataSource("play", new Decision());
	}

	private void buildDisplay() {
		// Background 2DDisplay
		Object2DDisplay backgroundDisplay = new Object2DDisplay(background);
		backgroundDisplay.setObjectList(mapAgent);
		dsurf.addDisplayableProbeable(backgroundDisplay, "Background Space");

		// Object 2DDisplay
		Object2DDisplay display = new Object2DDisplay(space);
		display.setObjectList(agentList);
		dsurf.addDisplayableProbeable(display, "Agents Space");

		//Text displays
		int fontSize = 16;
		int numLines = 3;
		for (int i = 1; i < numberOfPlayers + 1; i++) {
			TextDisplay textDisplay = new TextDisplay(5, 20 + ((numLines  + 2) * fontSize * i), Utils.COLORS[i - 1]);
			textDisplay.setHeader("Player " + i + " - " + mindsets.get(i - 1));
			textDisplay.setFontSize(fontSize);
			textDisplay.addLine("#Territories ", 0);
			textDisplay.addLine("#Troups ", 1);
			dsurf.addDisplayableProbeable(textDisplay, "Text Display P" + i);

			infoDisplays.add(textDisplay);
		}
		dsurf.display();

		//        // graph
		//        if (plot != null) plot.dispose();
		//        plot = new OpenSequenceGraph("Colors and Agents", this);
		//        plot.setAxisTitles("time", "n");
		//        // plot number of different existing colors
		//        plot.addSequence("Number of colors", new Sequence() {
		//            public double getSValue() {
		//                return agentColors.size();
		//            }
		//        });
		//        // plot number of agents with the most abundant color
		//        plot.addSequence("Top color", new Sequence() {
		//            public double getSValue() {
		//                int n = 0;
		//                Enumeration<Integer> agentsPerColor = agentColors.elements();
		//                while(agentsPerColor.hasMoreElements()) {
		//                    int c = agentsPerColor.nextElement();
		//                    if(c>n) n=c;
		//                }
		//                return n;
		//            }
		//        });
		//        plot.display();
	}

	private void buildSchedule() {
		getSchedule().scheduleActionBeginning(0, new MainAction());
		if (!BATCH_MODE)
			getSchedule().scheduleActionAtInterval(1, dsurf, "updateDisplay", Schedule.LAST);
		//        schedule.scheduleActionAtInterval(1, plot, "step", Schedule.LAST);
		getSchedule().scheduleActionAtEnd(fortifyRecorder, "writeToFile");
		getSchedule().scheduleActionAtEnd(attackRecorder, "writeToFile");
		getSchedule().scheduleActionAtEnd(defendRecorder, "writeToFile");
	}

	/**
	 * Launching Repast3
	 * @param args program arguments' string
	 */
	public static void main(String[] args) {
		SimInit init = new SimInit();
		init.setNumRuns(1);   // works only in batch mode
		init.loadModel(new MyLauncher(), null, BATCH_MODE);
	}

	private class MainAction extends BasicAction {

		@Override
		public void execute() {

			int[] counts = {0, 0, 0, 0, 0, 0, 0};
			int[] troups = {0, 0, 0, 0, 0, 0, 0};
			Game currentGame = boardAgent.getGame();

			for (Unit unit : agentList) {
				space.putObjectAt(unit.getX(), unit.getY(), null);
			}

			agentList.clear();

			if (currentGame != null) {
				for (Continent continent : currentGame.getContinents()) {
					for (Territory territory : continent.getTerritories()) {
						counts[territory.getPlayerID()] = counts[territory.getPlayerID()] + 1;
						troups[territory.getPlayerID()] = troups[territory.getPlayerID()] + territory.getUnits();
						for (Unit unit : territory.getUnitsList()) {
							space.putObjectAt(unit.getX(), unit.getY(), unit);
							agentList.add(unit);
						}
					}
				}

				if (!BATCH_MODE) {

					for (int i = 0; i < numberOfPlayers; i++) {
						infoDisplays.get(i).clearLine(0);
						infoDisplays.get(i).addLine("#Territories: " + counts[i + 1], 0);
						infoDisplays.get(i).clearLine(1);
						infoDisplays.get(i).addLine("#Troups: " + troups[i + 1], 1);
					}
				}

				int gameFinished = currentGame.isGameFinished();

				if (gameFinished != 0) {
					// shutdown

					try {
						if (roundWinners.size() < runNumber)
							roundWinners.add(currentGame.getPlayerByID(gameFinished).getAid().getLocalName());
						boardAgent.getContainerController().getPlatformController().kill();

					} catch (ControllerException e) {

						e.printStackTrace();
					}
				}

				if (agentList.size() > 0) {
					if (boardAgent.peekDecision() != null) {
						switch (boardAgent.peekDecision().getPlayerAction()) {
							case Fortify:
								fortifyRecorder.record();
								break;
							case Attack:
								attackRecorder.record();
								break;
							case Defend:
								defendRecorder.record();
								break;
							default:
								break;
						}
					}
				}
			}
		}
	}

	public ArrayList<Unit> getAgentList() {
		return agentList;
	}



	public int getNumberOfPlayers() {
		return numberOfPlayers;
	}

	public void setNumberOfPlayers(int numberOfPlayers) {
		this.numberOfPlayers = numberOfPlayers;
	}

	public PlayerMindset getPlayerOneMindset() {
		return playerOneMindset;
	}

	public void setPlayerOneMindset(PlayerMindset playerOneMindset) {
		this.playerOneMindset = playerOneMindset;
	}

	public PlayerMindset getPlayerTwoMindset() {
		return playerTwoMindset;
	}

	public void setPlayerTwoMindset(PlayerMindset playerTwoMindset) {
		this.playerTwoMindset = playerTwoMindset;
	}

	public PlayerMindset getPlayerThreeMindset() {
		return playerThreeMindset;
	}

	public void setPlayerThreeMindset(PlayerMindset playerThreeMindset) {
		this.playerThreeMindset = playerThreeMindset;
	}

	public PlayerMindset getPlayerFourMindset() {
		return playerFourMindset;
	}

	public void setPlayerFourMindset(PlayerMindset playerFourMindset) {
		this.playerFourMindset = playerFourMindset;
	}

	public PlayerMindset getPlayerFiveMindset() {
		return playerFiveMindset;
	}

	public void setPlayerFiveMindset(PlayerMindset playerFiveMindset) {
		this.playerFiveMindset = playerFiveMindset;
	}

	public PlayerMindset getPlayerSixMindset() {
		return playerSixMindset;
	}

	public void setPlayerSixMindset(PlayerMindset playerSixMindset) {
		this.playerSixMindset = playerSixMindset;
	}
}
