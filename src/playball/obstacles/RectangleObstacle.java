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
	public void draw(Graphics g) {
		g.setColor(color);
		g.fillRect(x, y, width, height);
	}

	@Override
	public void onCollision(ArrayList<Obstacle> obstaclesHit) {}

	@Override
	public void onCollision(Direction d, Hitbox h) {}
}
