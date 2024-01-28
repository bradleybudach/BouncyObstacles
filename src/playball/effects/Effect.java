package playball.effects;

import java.awt.Graphics;

public abstract class Effect {
	public int x, y;
	public boolean isAlive = true;
	
	public Effect(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public abstract void drawFrame(Graphics g);
}