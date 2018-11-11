package logic;

import jade.core.Agent;
import uchicago.src.sim.gui.Drawable;
import uchicago.src.sim.gui.SimGraphics;
import uchicago.src.sim.space.Object2DTorus;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Map extends Agent implements Drawable {

    private BufferedImage image;
    private int x, y;
    private Territory territory;
    private Object2DTorus space;

    private final String filename = "map.jpg";

    public Map(int x, int y, Object2DTorus space) throws IOException {
        this.image = ImageIO.read(new File("src/assets/" + filename));
        this.x = x;
        this.y = y;
        this.space = space;
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
