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
		if (controller.score % 25 == 0) {
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
        
        for (int i = 0; i < speed; i++) {
        	currentMoveStep = (currentMoveStep + 1) % dir.moveSteps.size();
        	switch (dir.moveSteps.get(currentMoveStep)) {
		    	case LEFT:
		    		x -= 1;
		    		break;
		    	case RIGHT:
		    		x += 1;
		    		break;
		    	case UP:
		    		y -= 1;
		    		break;
		    	case DOWN:
		    		y += 1;
		    		break;
		    	default:
		    		break;
		    }
        }
        
        hitbox.updatePosition(x, y);
	}
	
	@Override
	public void onCollision(ArrayList<Obstacle> obstaclesHit) {
		maxCollisions -= obstaclesHit.size();
		obstaclesHit.forEach(o -> o.remove());
		
		if (maxCollisions <= 0) {
			this.remove();
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
