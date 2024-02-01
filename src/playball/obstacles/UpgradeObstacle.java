package playball.obstacles;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;

import javax.swing.ImageIcon;

import playball.Direction;
import playball.GameController;
import playball.hitboxes.Hitbox;
import playball.hitboxes.RectangleHitbox;

public class UpgradeObstacle extends Obstacle {
	private int speedThrottle;
	ImageIcon icon;
	ArrayList<Obstacle> obstaclesUpgraded = new ArrayList<Obstacle>();
	
	/**
	 * Creates an UpgradeObstacle that upgrades all ordinary obstacles it touches
	 * 
	 * @param x
	 * @param y
	 * @param speedThrottle - only move every speedThrottle frames
	 * @param controller - GameController
	 */
	public UpgradeObstacle(int x, int y, int speedThrottle, GameController controller) {
		super(35, 35, x, y, 1, new Direction());
		setHitbox(new RectangleHitbox(x, y, 35, 35));
		
		this.speedThrottle = speedThrottle;
		icon = new ImageIcon(getClass().getResource("/images/upgrade-obstacle.png"));
		setController(controller);
	}

	@Override
	public void cleanup() {
		// sysout cleanup:
		for (Obstacle o : obstaclesUpgraded) { // remove all upgrades on death
			controller.downgradeObstacle(o);
		}
	}

	@Override
	public void onCollision(ArrayList<Obstacle> obstaclesHit) {
		for (Obstacle o : obstaclesHit) {
			if (o instanceof RectangleObstacle || o instanceof CircleObstacle) { // if o can be upgraded
				Obstacle newOb;
				
				double rand = Math.random();
				if (rand < 0.16) {
					newOb = new BombObstacle(o.x, o.y, o.speed, controller);
				} else if (rand < 0.32) {
					newOb = new TrackingCircleObstacle(Math.max(o.width, o.height), o.x, o.y, o.speed, o.dir, controller);
				} else if (rand < 0.48) {
					newOb = new SlowAbilityObstacle(o.x, o.y, 2, o.dir, controller, 250);
				} else if (rand < 0.64) {
					newOb = new SpeedAbilityObstacle(o.x, o.y, 2, o.dir, controller);
				} else if (rand < 0.8) {
					newOb = new PowerupEaterObstacle(o.x, o.y, controller);
				} else {
					newOb = new SpawnerObstacle(o.x, o.y, 5, controller);
				}
				
				o.queueRemove();
				obstaclesUpgraded.add(newOb); // add to upgradeList
				controller.queueAddObstacle(newOb);
			}
		}
	}

	@Override
	public void onCollision(Direction d, Hitbox h) {}

	@Override
	public void move(Dimension screenDimension) {
		if (controller.score % speedThrottle == 0) {
			checkBorderCollision(screenDimension);
			
			runMoveSteps();
			
			hitbox.updatePosition(x, y);
		}
	}

	@Override
	public void draw(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		
		g2d.drawImage(icon.getImage(), x, y, null);
		
		g2d.setColor(new Color(156, 0, 164));
		
		// Draw effect on converted obstacles:
		ArrayList<Obstacle> removeList = new ArrayList<Obstacle>();
		for (Obstacle o : obstaclesUpgraded) { // remove obstacles that no longer exist
			if (o.queueRemove) {
				removeList.add(o);
			} else { // draw obstacle effect
				g2d.setStroke(new BasicStroke(4));
				g2d.drawOval(o.x-2, o.y-2, o.width+4, o.height+4);
			}
		}
		obstaclesUpgraded.removeAll(removeList);
	}

}
