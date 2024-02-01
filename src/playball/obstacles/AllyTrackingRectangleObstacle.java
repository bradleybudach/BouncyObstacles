package playball.obstacles;

import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;

import playball.Direction;
import playball.GameController;
import playball.hitboxes.Hitbox;

public class AllyTrackingRectangleObstacle extends TrackingRectangleObstacle {
	private int maxCollisions = 1;
	private int MAX_SPEED = 5;
	
	public AllyTrackingRectangleObstacle(int width, int height, int x, int y, int speed, Direction dir, GameController controller, int bounceCount) {
		super(width, height, x, y, speed, dir, controller);
		
		maxCollisions = bounceCount;
		setColor(Color.GREEN);
	}
	
	@Override 
	public void move(Dimension screenDimension) {	
		// Track obstacles:
		if (controller.score % 10 == 0) {
			double minDist = Integer.MAX_VALUE;
			Obstacle closestObstacle = null;
			for (Obstacle o : controller.obstacles) {
				double distance = Math.sqrt((this.x-o.x)*(this.x-o.x) + (this.y-o.y)*(this.y-o.y));
				
				if (distance < minDist) {
					minDist = distance;
					closestObstacle = o;
				}
			}
			
			if (closestObstacle != null) {
				if (closestObstacle.x < this.x) {
					dir.setLeft();
				} else {
					dir.setRight();
				}
				
				if (closestObstacle.y < this.y) {
					dir.setUp();
				} else {
					dir.setDown();
				}
			}
		}
		
		
		if (x+width >= screenDimension.width) { // bounce of left or right walls
            dir.setLeft();
        } else if (x <= 6) {
            dir.setRight();
        }
        
        if(y+height >= screenDimension.height) { // bounce off top or bottom walls
            dir.setUp();
        } else if (y <= 6) {
            dir.setDown();
        }
        
        runMoveSteps();
        
        hitbox.updatePosition(x, y);
	}
	
	@Override
	public void onCollision(ArrayList<Obstacle> obstaclesHit) {
		int hitCount = 0;
		for(Obstacle o : obstaclesHit) {
			if (!o.queueRemove) { // if o is queued to be removed
				o.queueRemove();
				hitCount++;
			}
		};
		
		maxCollisions -= hitCount;
		
		
		if (maxCollisions <= 0) {
			this.queueRemove();
		}
	}
	
	/**
	 * Detect collision with player hitbox
	 */
	@Override
	public void onCollision(Direction d, Hitbox h) {
		dir.combineDirection(d); // move in direction of player
		
		if (speed < MAX_SPEED) {
			speed += 1; // add impulse when hitting the player
		}
	}
}
