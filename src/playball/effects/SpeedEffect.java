package playball.effects;

import java.awt.AlphaComposite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;

import javax.swing.ImageIcon;

import playball.obstacles.Obstacle;

public class SpeedEffect extends Effect {
	ArrayList<Obstacle> obstacles;
	private int frameCount = 30;
	private ImageIcon speedImage = new ImageIcon(getClass().getResource("/images/speed-effect.png"));
	
	public SpeedEffect(ArrayList<Obstacle> obstacles) {
		super(0, 0);
		this.obstacles = obstacles;
	}
	
	public SpeedEffect(Obstacle o) {
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
		float alpha = frameCount/30.0f; 
		AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);
		g2d.setComposite(ac);
		
		for (Obstacle o : obstacles) {
			g2d.drawImage(speedImage.getImage(), o.getHitbox().getCenterX()-12, o.getHitbox().getCenterY()-12, null);
		}
		
	}

}
