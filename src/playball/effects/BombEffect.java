package playball.effects;

import java.awt.Color;
import java.awt.Graphics;

public class BombEffect extends Effect {
	private int diameter, radius;
	private int frameCount = 20;
	private Color color = null;
	
	public BombEffect(int x, int y, int diameter, int radius) {
		super(x, y);
		this.diameter = diameter;
		this.radius = radius;
	}
	
	public void setColor(Color color) {
		this.color = color;
	}
	

	@Override
	public void drawFrame(Graphics g) {
		frameCount--;
		if (frameCount == 0) {
			isAlive = false;
		}
		
		if (!isAlive) {
			return;
		}
		
		double factor = (20-frameCount)/20.0;
		
		int minSize = diameter/2;
		int difference = diameter-minSize;
		int val = (int)(difference*factor);
		
		// draw the frame of the bomb:
		
		if (color != null) {
			g.setColor(color);
		} else {
			g.setColor(new Color(255, 0, 0, 150));
		}
		
		g.fillOval(x+(radius - (minSize+val)/2), y+(radius - (minSize+val)/2), minSize+val, minSize+val);
	}

}
