package playball;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.ArrayList;

public class Player {
	public boolean isAlive = true;
	public int x, y;
	private int size = 16;
	private CircleHitbox playerHitbox;
	public Direction orientation = new Direction(false, true, false, false);
	private int speed = 2;
	
	// Powers:
	private int shields = 0;
	private final int MAX_SHIELDS = 3;
	
	public Player(int x, int y) {
		this.x = x;
		this.y = y;
		
		playerHitbox = new CircleHitbox(x, y, size, 7);
	}
	
	public void move(Dimension screenDimension) {
		// Collision with left or right side of screen
        if (x+16 >= screenDimension.width) {
            orientation.setLeft();
        } else if (x <= 6) {
        	orientation.setRight();
        }
        
        // Collision with top or bottom of screen
        if(y+16 >= screenDimension.height) {
        	orientation.setUp();
        } else if (y <= 6) {
        	orientation.setDown();
        }
        
        // move up or down
        if (orientation.up) {
            y -= speed;
        } else if (orientation.down) {
            y += speed;
        }
        
        // move left or right
        if (orientation.left) {
            x -= speed;
        } else if (orientation.right) {
            x += speed;
        }
        
        playerHitbox.updatePosition(x, y);
	}
	
	public void draw(Graphics g) {
		if (shields > 2) {
			g.setColor(new Color(25, 25, 112));
			g.fillOval(x-6, y-6, size+12, size+12);
		}
		
		if (shields > 1) {
			g.setColor(Color.BLUE);
			g.fillOval(x-4, y-4, size+8, size+8);
		}
		
		if (shields > 0) {
			g.setColor(new Color(0,191,255));
			g.fillOval(x-2, y-2, size+4, size+4);
		}
		
		
		
		
		g.setColor(Color.GREEN);
		g.fillOval(x, y, size, size);
	}
	
	public void reset() {
		x = 10;
		y = 10;
		playerHitbox.updatePosition(x, y);
		isAlive = true;
		shields = 0;
	}
	
	// Powers:
	public void addShields() {
		if (shields < MAX_SHIELDS) {
			shields++;
		}
	}
	
	
	/**
     * @return returns true if ball has collided with an obstacle
     */
    public boolean checkCollisions(ArrayList<Obstacle> obstacles) {
    	for (Obstacle o : obstacles) { // checks ball collisions with all obstacles
    		if (o.getHitbox().checkCollision(playerHitbox) != null) {
    			if (shields > 0) {
    				shields--;
    				obstacles.remove(o); // delete hit obstacle
    			} else {
    				isAlive = false;
    			}
    			
    			return true;
    		}
    	}
    	
    	return false;
    }
    
    /**
     * @return the current ball direction as a string
     */
    public String getDirection() {
    	return (orientation.up) ? "UP" : (orientation.down) ? "DOWN" : (orientation.right) ? "RIGHT" : (orientation.left) ? "LEFT" : "NONE"; 
    }
    
    /**
     * @return the players hitbox
     */
    public CircleHitbox getHitbox() {
    	return playerHitbox;
    }
}
