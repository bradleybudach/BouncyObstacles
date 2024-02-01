package playball.obstacles;

import java.awt.Dimension;
import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.ImageIcon;

import playball.Direction;
import playball.GameController;
import playball.effects.BombEffect;
import playball.hitboxes.CircleHitbox;
import playball.hitboxes.Hitbox;

public class BombObstacle extends Obstacle {
	private int slowSpeed;
	private int explosionDiameter = 250;
	private ImageIcon icon;
	private int radius;
	
	public BombObstacle(int x, int y, int slowSpeed, GameController controller) {
		super(50, 50, x, y, 1, new Direction());
		
		setHitbox(new CircleHitbox(x, y, 50, 50/2));
		icon = new ImageIcon(getClass().getResource("/images/bomb-obstacle.png"));
		
		this.radius = 50/2;
		this.slowSpeed = slowSpeed;
		setController(controller);
	}

	@Override
	public void onCollision(ArrayList<Obstacle> obstaclesHit) {
		// create explosion when colliding with another obstacle
		CircleHitbox explosionHitbox = new CircleHitbox(x-(explosionDiameter/2)+radius, y-(explosionDiameter/2)+radius, explosionDiameter, explosionDiameter/2);
		controller.player.hitPlayer(explosionHitbox); // checks if bomb hit player
		
		controller.addEffect(new BombEffect(x-(explosionDiameter/2)+radius, y-(explosionDiameter/2)+radius, explosionDiameter, explosionDiameter/2));
		this.queueRemove();
	}

	@Override
	public void onCollision(Direction d, Hitbox h) {}

	@Override
	public void move(Dimension screenDimension) {
		if (controller.score % slowSpeed == 0) { // throttle move speed
			checkBorderCollision(screenDimension);
			
			runMoveSteps();
			
			hitbox.updatePosition(x, y);
		}
	}

	@Override
	public void draw(Graphics g) {
		g.drawImage(icon.getImage(), x, y, null);
	}

	@Override
	public void cleanup() {}

}
