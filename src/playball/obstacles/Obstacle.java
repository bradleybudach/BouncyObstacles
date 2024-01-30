package playball.obstacles;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.ArrayList;

import playball.Direction;
import playball.GameController;
import playball.hitboxes.Hitbox;

public abstract class Obstacle {
	public int x;
	public int y;
	public int width;
	public int height;
	
	public int speed;
	public Direction dir;
	public Hitbox hitbox;
	public Color color;
	public int currentMoveStep = 0;
	
	public GameController controller;
	
	public boolean queueRemove = false;
	
	/**
	 * Creates Obstacle
	 * 
	 * @param width - the width of the obstacle
	 * @param height - the height of the obstacle
	 * @param x - the x position of the obstacle
	 * @param y - the y position of the obstacle
	 * @param speed - the speed of the obstacle
	 * @param dir - the initial Direction of the obstacle
	 */
	public Obstacle(int width, int height, int x, int y, int speed, Direction dir) {
		this.width = width;
		this.height = height;
		this.x = x;
		this.y = y;
		this.speed = speed;
		this.dir = dir;
		color = Color.RED; // default red color
	}
	
	/**
	 * Creates Obstacle with custom color
	 * 
	 * @param width - the width of the obstacle
	 * @param height - the height of the obstacle
	 * @param x - the x position of the obstacle
	 * @param y - the y position of the obstacle
	 * @param speed - the speed of the obstacle
	 * @param dir - the initial Direction of the obstacle
	 * @param color - the color of the obstacle
	 */
	public Obstacle(int width, int height, int x, int y, int speed, Direction dir, Color color) {
		this.width = width;
		this.height = height;
		this.x = x;
		this.y = y;
		this.speed = speed;
		this.dir = dir;
		this.color = color; // custom color
	}
	
	/**
	 * Set the controller to allow obstacles to interact with other game items
	 * @param g - game controller
	 */
	public void setController(GameController g) {
		controller = g;
	}
	
	/**
	 * @return obstacle hitbox
	 */
	public Hitbox getHitbox() {
		return hitbox;
	}
	
	/**
	 * Update this obstacles hitbox
	 * @param hitbox
	 */
	public void setHitbox(Hitbox hitbox) {
		this.hitbox = hitbox;
	}
	
	/**
	 * Update this obstacles speed
	 * @param speed
	 */
	public void setSpeed(int speed) {
		this.speed = speed;
	}
	
	public void setColor(Color c) {
		this.color = c;
	}
	
	/**
	 * Checks if this obstacle is colliding with any other obstacles and changes it's direction accordingly.
	 */
	public void checkObstacleCollisions(ArrayList<Obstacle> obstacles) {
		ArrayList<Obstacle> obstaclesHit = new ArrayList<Obstacle>();
		for (Obstacle o : obstacles) {
    		if (this != o) {
    			Direction d = hitbox.checkCollision(o.getHitbox());
    			if (d != null) {
    				// combine collision directions
    				obstaclesHit.add(o);
    				dir.combineDirection(d);
    			}
    		}
    	}
		
		if (obstaclesHit.size() > 0) {
			onCollision(obstaclesHit);
		}
	}
	
	/**
	 * CHeck for a collision with another hitbox and this object
	 * @param h
	 */
	public void checkHitboxCollision(Hitbox h) {
		Direction d = hitbox.checkCollision(h);
		if (d != null) {
			onCollision(d, h);
		}
	}
	
	/**
	 * Queue this obstacle to be removed in the next game update
	 */
	public void remove() {
		cleanup();
		queueRemove = true;
	}
	
	/**
	 * Cleanup anything that needs to be before obstacle is removed
	 */
	public abstract void cleanup();
	
	/**
	 * Implement functionality for collision with a list of obstacles
	 * @param obstaclesHit
	 */
	public abstract void onCollision(ArrayList<Obstacle> obstaclesHit);
	
	/**
	 * Implement functionality for collision with a single obstacle
	 * @param d - direction of the collision
	 * @param h - hitbox of the hit item
	 */
	public abstract void onCollision(Direction d, Hitbox h);
	
	/**
	 * Updates this obstacles position based on it's speed and direction
	 * 
	 * @param screenDimension - the dimensions of the container, allows obstacles to bounce off the edge
	 * @return true if there is a collision with the edge
	 */
	public abstract void move(Dimension screenDimension);
	
	public boolean checkBorderCollision(Dimension screenDimension) {
		if (x+width >= screenDimension.width) { // bounce of left or right walls
            dir.setLeft();
            return true;
        } else if (x <= 6) {
            dir.setRight();
            return true;
        }
        
        if(y+height >= screenDimension.height) { // bounce off top or bottom walls
            dir.setUp();
            return true;
        } else if (y <= 6) {
            dir.setDown();
            return true;
        }
        
        return false;
	}
	
	public void runMoveSteps() {
		if (dir.moveSteps.size() == 0) { // no movement
			return;
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
	}
	
	
	/**
	 * Draw call to make obstacle appear
	 * 
	 * @param g - graphics component to draw the obstacle
	 */
	public abstract void draw(Graphics g);
}