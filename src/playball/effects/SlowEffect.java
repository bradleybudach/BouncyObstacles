package playball.effects;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;

import javax.swing.ImageIcon;

import playball.obstacles.Obstacle;

public class SlowEffect extends Effect {
	private ArrayList<Obstacle> obstacles;
	private int frameCount = 50;
	private ImageIcon slowImage = new ImageIcon(getClass().getResource("/images/slow.png"));
	
	public SlowEffect(ArrayList<Obstacle> obstacles) {
		super(0, 0);
		this.obstacles = obstacles;
	}
	
	public SlowEffect(Obstacle o) {
		super(0, 0);
		obstacles = new ArrayList<Obstacle>();
		obstacles.add(o);
	}

	@Override
	public void drawFrame(Graphics g) {
		Graphics2D g2d = (Graphics2D) g; // convert to graphics2D
		frameCount--;
		if (frameCount == 0) {
			isAlive = false;
		}
		
		if (!isAlive) {
			return;
		}
		
		//change transparency over time:
		float alpha = frameCount/50.0f; 
		AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);
		g2d.setComposite(ac);
		
		for (Obstacle o : obstacles) {
			g2d.drawImage(slowImage.getImage(), o.getHitbox().getCenterX()-12, o.getHitbox().getCenterY()-12, null);
		}
	}

}
