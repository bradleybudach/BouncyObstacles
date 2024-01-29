package playball.obstacles;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.ArrayList;

import playball.Direction;
import playball.hitboxes.Hitbox;
import playball.hitboxes.RectangleHitbox;

public class RectangleObstacle extends Obstacle {
	public RectangleObstacle(int width, int height, int x, int y, int speed, Direction dir) {
		super(width, height, x, y, speed, dir);
		
		setHitbox(new RectangleHitbox(x, y, width, height));
	}
	
	public RectangleObstacle(int width, int height, int x, int y, int speed, Direction dir, Color color) {
		super(width, height, x, y, speed, dir, color);
		
		setHitbox(new RectangleHitbox(x, y, width, height));
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
		g.fillRect(x, y, width, height);
	}

	@Override
	public void onCollision(ArrayList<Obstacle> obstaclesHit) {}

	@Override
	public void onCollision(Direction d, Hitbox h) {}

	@Override
	public void cleanup() {}
}
