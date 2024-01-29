package playball.obstacles;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.ArrayList;

import playball.Direction;
import playball.hitboxes.CircleHitbox;
import playball.hitboxes.Hitbox;

public class CircleObstacle extends Obstacle {

	/**
	 * Defines a circle obstacle
	 * 
	 * @param diameter - diameter of the circle
	 * @param x - x position
	 * @param y - y position
	 * @param speed - speed of the circle
	 * @param dir - direction of the circle
	 */
	public CircleObstacle(int diameter, int x, int y, int speed, Direction dir) {
		super(diameter, diameter, x, y, speed, dir);
		
		setHitbox(new CircleHitbox(x, y, diameter, diameter/2));
	}
	
	/**
	 * Defines a circle obstacle with a custom color
	 * 
	 * @param diameter - diameter of the circle
	 * @param x - x position
	 * @param y - y position
	 * @param speed - speed of the circle
	 * @param dir - direction of the circle
	 * @param color - custom color
	 */
	public CircleObstacle(int diameter, int x, int y, int speed, Direction dir, Color color) {
		super(diameter, diameter, x, y, speed, dir, color);
		
		setHitbox(new CircleHitbox(x, y, diameter, diameter/2));
	}

	@Override
	public void move(Dimension screenDimension) {
		checkBorderCollision(screenDimension);
        
		runMoveSteps();
        
        
        hitbox.updatePosition(x, y);
	}

	@Override
	public void draw(Graphics g) {
		g.setColor(color);
		g.fillOval(x, y, width, height);
	}

	@Override
	public void onCollision(ArrayList<Obstacle> obstaclesHit) {}

	@Override
	public void onCollision(Direction d, Hitbox h) {}

	@Override
	public void cleanup() {}
	
}
