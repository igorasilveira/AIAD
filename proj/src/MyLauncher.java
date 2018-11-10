import agents.BoardAgent;
import agents.PlayerAgent;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.wrapper.StaleProxyException;

import logic.Unit;
import sajas.core.Runtime;
import sajas.sim.repast3.Repast3Launcher;
import sajas.wrapper.ContainerController;

import uchicago.src.reflector.ListPropertyDescriptor;
import uchicago.src.sim.analysis.OpenSequenceGraph;
import uchicago.src.sim.engine.BasicAction;
import uchicago.src.sim.engine.Schedule;
import uchicago.src.sim.engine.SimInit;
import uchicago.src.sim.gui.DisplaySurface;
import uchicago.src.sim.gui.Object2DDisplay;
import uchicago.src.sim.space.Object2DTorus;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.Vector;

public class MyLauncher extends Repast3Launcher {

	private ArrayList<Unit> agentList;
	private Schedule schedule;
	private DisplaySurface dsurf;
	private Object2DTorus space;
	private OpenSequenceGraph plot;

	private Random random;


	private int numberOfAgents, spaceSize, numberOfPlayers;

	private ContainerController mainContainer;

	private MyLauncher() {
		random = new Random();
		this.numberOfAgents = 500;
		this.spaceSize = 150;
		this.numberOfPlayers = 3;
	}

	@Override
	public String[] getInitParam() {
		return new String[] {"numberOfAgents", "spaceSize", "numberOfPlayers"};
	}

//	public Schedule getSchedule() {
//		return schedule;
//	}

	public int getNumberOfAgents() {
		return numberOfAgents;
	}

	public void setNumberOfAgents(int numberOfAgents) {
		this.numberOfAgents = numberOfAgents;
	}

	public int getNumberOfPlayers() {
		return numberOfPlayers;
	}

	public void setNumberOfPlayers(int numberOfPlayers) {
		this.numberOfPlayers = numberOfPlayers;
	}

	public int getSpaceSize() {
		return spaceSize;
	}

	public void setSpaceSize(int spaceSize) {
		this.spaceSize = spaceSize;
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

		launchAgents();
	}
	
	private void launchAgents() {
		
		try {

			mainContainer.acceptNewAgent("MyAgent", new BoardAgent()).start();
			mainContainer.acceptNewAgent("MyAgent1", new PlayerAgent()).start();
			mainContainer.acceptNewAgent("MyAgent2", new PlayerAgent()).start();
			mainContainer.acceptNewAgent("MyAgent3", new PlayerAgent()).start();
			
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
		buildDisplay();
		buildSchedule();
	}

	private void buildModel() {
		agentList = new ArrayList<>();
		space = new Object2DTorus(spaceSize, spaceSize);
		Random random = new Random();
		for (int i = 0; i<numberOfAgents; i++) {
			int x, y;
			x = random.nextInt(space.getSizeX() - 1);
			y = random.nextInt(space.getSizeY() - 1);
			Color color =  new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255));
			Unit agent = new Unit(x, y, color, space);
			space.putObjectAt(x, y, agent);
			agentList.add(agent);
		}
	}

	private void buildDisplay() {
		// space and display surface
		Object2DDisplay display = new Object2DDisplay(space);
		display.setObjectList(agentList);
		dsurf.addDisplayableProbeable(display, "Agents Space");
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
		schedule.scheduleActionAtInterval(1, dsurf, "updateDisplay", Schedule.LAST);
//        schedule.scheduleActionAtInterval(1, plot, "step", Schedule.LAST);
	}

	/**
	 * Launching Repast3
	 * @param args program arguments' string
	 */
	public static void main(String[] args) {
		boolean BATCH_MODE = true;
		SimInit init = new SimInit();
		init.setNumRuns(5);   // works only in batch mode
		init.loadModel(new MyLauncher(), null, BATCH_MODE);
	}

}
