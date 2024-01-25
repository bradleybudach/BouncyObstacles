package playball;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

public class CircleObstacle extends Obstacle {

	/**
	 * Defines a circle obstacle
	 * 
	 * @param diameter - diameter of the circle
	 * @param x - x position
	 * @param y - y position
	 * @param speed - speed of the circle
	 * @param dir - direction of the circle
	 * @param radius - radius of the circle
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
	 * @param radius - radius of the circle
	 * @param color - custom color
	 */
	public CircleObstacle(int diameter, int x, int y, int speed, Direction dir, Color color) {
		super(diameter, diameter, x, y, speed, dir, color);
		
		setHitbox(new CircleHitbox(x, y, diameter, diameter/2));
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
        
        currentMoveStep = (currentMoveStep + 1) % dir.moveSteps.size();
        
        switch (dir.moveSteps.get(currentMoveStep)) {
	    	case LEFT:
	    		x -= speed;
	    		break;
	    	case RIGHT:
	    		x += speed;
	    		break;
	    	case UP:
	    		y -= speed;
	    		break;
	    	case DOWN:
	    		y += speed;
	    		break;
	    	default:
	    		System.out.println("ERR");
	    		break;
	    }
        
        hitbox.updatePosition(x, y);
	}

	@Override
	public void draw(Graphics g) {
		g.setColor(color);
		g.fillOval(x, y, width, height);
	}
	
}
