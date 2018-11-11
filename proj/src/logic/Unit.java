package logic;

import java.awt.Color;

import jade.core.Agent;
import uchicago.src.sim.gui.Drawable;
import uchicago.src.sim.gui.SimGraphics;

public class Unit extends Agent implements Drawable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private Color color;
	private int x, y;

	public Unit(int x, int y, Color color) {
		this.color = color;
		this.x = x;
		this.y = y;
	}

	@Override
	public void draw(SimGraphics simGraphics) {
		simGraphics.drawFastCircle(color);
	}

	@Override
	public int getX() {
		return x;
	}

	@Override
	public int getY() {
		return y;
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}

	public void setColor(Color color) {
		this.color = color;
	}
}
