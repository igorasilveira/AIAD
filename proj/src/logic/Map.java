package logic;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import jade.core.Agent;
import uchicago.src.sim.gui.Drawable;
import uchicago.src.sim.gui.SimGraphics;
import uchicago.src.sim.space.Object2DTorus;

public class Map extends Agent implements Drawable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private BufferedImage image;
	private int x, y;
	private final String filename = "map.jpg";

	public Map(int x, int y, Object2DTorus space) throws IOException {
		image = ImageIO.read(Map.class.getResource("/assets/" + filename));
		this.x = x;
		this.y = y;
	}

	@Override
	public void draw(SimGraphics simGraphics) {
		simGraphics.drawImage(image);
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

	public void setImage(BufferedImage image) {
		this.image = image;
	}
}
