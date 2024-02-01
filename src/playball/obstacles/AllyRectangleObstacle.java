package playball.obstacles;

import java.awt.Color;
import java.util.ArrayList;

import playball.Direction;
import playball.hitboxes.Hitbox;

public class AllyRectangleObstacle extends RectangleObstacle {
	private int maxCollisions;
	private static int MAX_SPEED = 5;

	public AllyRectangleObstacle(int width, int height, int x, int y, int speed, Direction dir, int maxCollisions) {
		super(width, height, x, y, speed, dir, Color.GREEN);
		
		this.maxCollisions = maxCollisions;
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
