package playball.obstacles;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.ArrayList;

import playball.Direction;
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
	
	public Hitbox getHitbox() {
		return hitbox;
	}
	
	public void setHitbox(Hitbox hitbox) {
		this.hitbox = hitbox;
	}
	
	public void setSpeed(int speed) {
		this.speed = speed;
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
	
	public void checkHitboxCollision(Hitbox h) {
		Direction d = hitbox.checkCollision(h);
		if (d != null) {
			onCollision(d, h);
		}
	}
	
	public void remove() {
		queueRemove = true;
	}
	
	public abstract void onCollision(ArrayList<Obstacle> obstaclesHit);
	
	public abstract void onCollision(Direction d, Hitbox h);
	
	/**
	 * Updates this obstacles position based on it's speed and direction
	 * 
	 * @param screenDimension - the dimensions of the container, allows obstacles to bounce off the edge
	 */
	public abstract void move(Dimension screenDimension);
	
	
	/**
	 * Draw call to make obstacle appear
	 * 
	 * @param g - graphics component to draw the obstacle
	 */
	public abstract void draw(Graphics g);
}