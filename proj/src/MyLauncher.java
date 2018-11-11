import agents.BoardAgent;
import agents.PlayerAgent;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.wrapper.StaleProxyException;

import logic.*;
import sajas.core.Agent;
import sajas.core.Runtime;
import sajas.sim.repast3.Repast3Launcher;
import sajas.wrapper.ContainerController;

import uchicago.src.reflector.ListPropertyDescriptor;
import uchicago.src.sim.analysis.OpenSequenceGraph;
import uchicago.src.sim.engine.BasicAction;
import uchicago.src.sim.engine.Schedule;
import uchicago.src.sim.engine.SimInit;
import uchicago.src.sim.gui.*;
import uchicago.src.sim.space.Object2DTorus;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Vector;

public class MyLauncher extends Repast3Launcher {

	private static int[] PIECES = {35, 30, 25, 20};

	private static boolean BATCH_MODE = false;

	private BoardAgent boardAgent;

	private ArrayList<Unit> agentList;
	private ArrayList<Map> mapAgent;
	private Schedule schedule;
	private DisplaySurface dsurf;
	private Object2DTorus space;
	private Object2DTorus background;
	private TextDisplay textDisplay;
	private OpenSequenceGraph plot;

	private Random random;


	private int numberOfAgents, spaceSize, numberOfPlayers;

	private ContainerController mainContainer;

	private MyLauncher() {
		random = new Random();
		this.spaceSize = 150;
		this.numberOfPlayers = 3;
	}

	@Override
	public String[] getInitParam() {
		return new String[] {"numberOfPlayers"};
	}

//	public Schedule getSchedule() {
//		return schedule;
//	}


	public int getNumberOfPlayers() {
		return numberOfPlayers;
	}

	public void setNumberOfPlayers(int numberOfPlayers) {
		this.numberOfPlayers = numberOfPlayers;
	}

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

			for (int i = 0; i < numberOfPlayers; i++) {
				mainContainer.acceptNewAgent("Player" + i, new PlayerAgent()).start();
			}

		} catch (StaleProxyException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void setup() {
		super.setup();
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
	}

	@Override
	public void begin() {
		super.begin();
		buildModel();
		if (!BATCH_MODE)
			buildDisplay();
		buildSchedule();
	}

	private void buildModel() {
		agentList = new ArrayList<>();
		mapAgent = new ArrayList<>();
		space = new Object2DTorus(spaceSize, spaceSize);

		Unit a = new Unit(100, 25, Color.BLUE);
		space.putObjectAt(a.getX(), a.getY(), a);
		agentList.add(a);
//		for (int i = 0; i<1; i++) {
//			int x, y;
//			x = 10;
//			y = 10;
//			Color color =  new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255));
//			Unit agent = new Unit(x, y, color, space);
//			space.putObjectAt(x, y, agent);
//			agentList.add(agent);
//		}

		background = new Object2DTorus(spaceSize, spaceSize);
		try {
			Map map = new Map(0, 0, background);
			background.putObjectAt(0, 0, map);
			mapAgent.add(map);
		} catch (IOException e) {
			e.printStackTrace();
		}

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

		//Text display
		textDisplay = new TextDisplay(20, 20, Color.BLUE);
		textDisplay.setFontSize(30);
		dsurf.addDisplayableProbeable(textDisplay, "Text Display");

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
//		getSchedule().scheduleActionBeginning(0, new MainAction());
		if (!BATCH_MODE)
			getSchedule().scheduleActionAtInterval(1, dsurf, "updateDisplay", Schedule.LAST);
//        schedule.scheduleActionAtInterval(1, plot, "step", Schedule.LAST);
	}

	/**
	 * Launching Repast3
	 * @param args program arguments' string
	 */
	public static void main(String[] args) {
		SimInit init = new SimInit();
		init.setNumRuns(5);   // works only in batch mode
		init.loadModel(new MyLauncher(), null, BATCH_MODE);
	}

	private class MainAction extends BasicAction {

		@Override
		public void execute() {
			if (!BATCH_MODE) {

				textDisplay.clearLines();
				textDisplay.addLine("#Agents: " + getAgentList().size());

				Game currentGame = boardAgent.getGame();

				for (Unit unit : agentList) {
					space.putObjectAt(unit.getX(), unit.getY(), null);
				}

				agentList.clear();

				if (currentGame != null) {
					for (Continent continent : currentGame.getContinents()) {
						for (Territory territory : continent.getTerritories()) {
							for (Unit unit : territory.getUnitsList()) {
								space.putObjectAt(unit.getX(), unit.getY(), unit);
								agentList.add(unit);
							}
						}
					}
				}
			}
		}
	}

	public ArrayList<Unit> getAgentList() {
		return agentList;
	}
}
