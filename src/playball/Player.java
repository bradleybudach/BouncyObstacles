package playball;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;

import playball.effects.SlowEffect;
import playball.hitboxes.CircleHitbox;
import playball.hitboxes.Hitbox;
import playball.obstacles.Obstacle;

public class Player {
	private GameController controller;
	
	public boolean isAlive = true;
	public int x, y;
	private int size = 16;
	private CircleHitbox playerHitbox;
	public Direction orientation = new Direction(false, true, false, false);
	private int speed = 2;
	
	// Upgrades:
	
	// Shields:
	private int shields = 0;
	private int MAX_SHIELDS = 3;
	
	// Immortality:
	private boolean isImmortal = false;
	private double maxImmortality;
	private int immortalityDuration = 0;
	
	// Cold Aura:
	private boolean hasColdAura = false;
	private int coldAuraRange = 50;
	
	// Player slowed:
	private boolean slowed = false;
	private int slowedSpeed = 1;
	
	// Parry:
	private boolean parry = false;
	private int parryThreshold = 5;
	private int parryTimer = 5;
	private boolean isParrying = false;
	private boolean parryConvertsEnemy = false;
	
	// No damage timer:
	public int timeSinceLastHit = 0;
	
	public Player(int x, int y) {
		this.x = x;
		this.y = y;
		
		playerHitbox = new CircleHitbox(x, y, size, 7);
	}
	
	public void setController(GameController g) {
		controller = g;
	}
	
	public void move(Dimension screenDimension) {
		// Collision with left or right side of screen
        if (x+16 >= screenDimension.width) {
        	orientation.setNone();
            orientation.setLeft();
        } else if (x <= 6) {
        	orientation.setNone();
        	orientation.setRight();
        }
        
        // Collision with top or bottom of screen
        if(y+16 >= screenDimension.height) {
        	orientation.setNone();
        	orientation.setUp();
        } else if (y <= 6) {
        	orientation.setNone();
        	orientation.setDown();
        }
        
        int tempSpeed = speed; // save speed to return to it after
        if (slowed) {
        	speed = slowedSpeed;
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
        
        // reduce immortality over time
        if (immortalityDuration > 0) {
        	immortalityDuration--;
        } else {
        	isImmortal = false;
        }
        
        if (slowed) {
        	speed = tempSpeed;
        }
	}
	
	public void draw(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		
		if (shields > 3) {
			g2d.setColor(new Color(0, 0, 0, (int)(255*(shields/6.0))));
			g2d.fillOval(x-8, y-8, size+16, size+16);
		}
		
		if (shields > 2) {
			g2d.setColor(new Color(25, 25, 112));
			g2d.fillOval(x-6, y-6, size+12, size+12);
		}
		
		if (shields > 1) {
			g2d.setColor(Color.BLUE);
			g2d.fillOval(x-4, y-4, size+8, size+8);
		}
		
		if (shields > 0) {
			g2d.setColor(new Color(0,191,255));
			g2d.fillOval(x-2, y-2, size+4, size+4);
		}
		
		
		if (hasColdAura) {
			g2d.setColor(Color.CYAN);
			g2d.setStroke(new BasicStroke(4));
			g2d.drawOval(x-coldAuraRange/2, y-coldAuraRange/2, size+coldAuraRange, size+coldAuraRange);
			
		}
		
		// Slowed:
		if (slowed) {
			g2d.setColor(Color.LIGHT_GRAY);
			g2d.setStroke(new BasicStroke(4));
			g2d.drawOval(x-2, y-2, size+4, size+4);
			g2d.setColor(Color.GREEN);
		}
		
		// Parry:
		if (parry && parryTimer < parryThreshold) { // parry window is active
			g2d.setColor(Color.YELLOW);
		}
		
		// Immortality:
		int transparency = (int)((immortalityDuration/maxImmortality)*255);
		if (isImmortal) {
			g2d.setColor(new Color(255, 0, 0, transparency));
			g2d.setStroke(new BasicStroke(4));
			g2d.drawOval(x-7, y-7, size+14, size+14);
			g2d.setColor(Color.RED);
		} else {
			g2d.setColor(Color.GREEN);
		}
		
		
		g2d.fillOval(x, y, size, size);
	}
	
	
	/**
	 * Reset all player values to defaults
	 */
	public void reset() {
		x = 10;
		y = 10;
		playerHitbox.updatePosition(x, y);
		isAlive = true;
		shields = 0;
		MAX_SHIELDS = 3;
		isImmortal = false;
		immortalityDuration = 0;
		hasColdAura = false;
		coldAuraRange = 50;
		parry = false;
		parryThreshold = 5;
		parryTimer = 5;
		isParrying = false;
		parryConvertsEnemy = false;
		timeSinceLastHit = 0;
		slowed = false;
		speed = 2;
	}
	
	// Powers:
	public void addShields() {
		if (shields < MAX_SHIELDS) {
			shields++;
		}
	}
	
	public void increaseMaxShields() {
		if (MAX_SHIELDS < 6) {
			MAX_SHIELDS++;
		}
	}
	
	public void setImmortal(int immortalityDuration) {
		isImmortal = true;
		this.immortalityDuration = immortalityDuration;
		this.maxImmortality = immortalityDuration;
	}
	
	public void addColdAura() {
		hasColdAura = true;
	}
	
	public void increaseColdAuraRange(int increase) {
		this.coldAuraRange += increase;
	}
	
	public void enableParry() {
		parry = true;
	}
	
	public void parry() {
		isParrying = true;
	}
	
	public void increaseParryWindow(int increase) {
		parryThreshold += increase;
		parryTimer = parryThreshold;
	}
	
	public void upgradeParryConvertsEnemy() {
		parryConvertsEnemy = true;
	}
	
	public void setMoveSpeed(int speed) {
		this.speed = speed;
	}
	
	/**
	 * Add slow effect
	 * @param slowAmmount - slow player by this ammount
	 */
	public void setSlowed(int slowAmmount) {
		slowed = true;
		slowedSpeed = Math.max(speed-slowAmmount, 1); // set slow speed, min 1
	}
	
	/**
	 * clear slow effect
	 */
	public void clearSlow() {
		slowed = false;
	}
	
	/**
     * @return returns true if ball has collided with an obstacle
     */
    public void checkCollisions(ArrayList<Obstacle> obstacles) {
    	boolean isHit = false;
    	
    	for (Obstacle o : obstacles) { // checks ball collisions with all obstacles
    		if (o.getHitbox().checkCollision(playerHitbox) != null) {    			
    			if (isImmortal) {
    				o.remove(); // delete hit obstacle
    				return;
    			}
    			
    			isHit = true;
    			if (parry) {
    				if (parryTimer > 0) {
    					if (isParrying) {
    						
    						// distances for bounce:
    						int leftDist = Math.abs((x+size) - o.x);
    						int rightDist = Math.abs(x - (o.x + o.width));
    						int topDist = Math.abs((y+size) - o.y);
    						int bottomDist = Math.abs(y - (o.y + o.height));
    						
    						if (parryConvertsEnemy) { // conversion upgrade
    							controller.convertObstacleToAlly(o);
    						}
    						
    						o.remove(); // remove parried obstacle
    						
    						// Bounce player off obstacle:
    						int leastDist = Integer.MAX_VALUE;
    						
    						if (leftDist < leastDist) {
    							leastDist = leftDist;
    							orientation.setNone();
								orientation.setLeft();
    						}
    						
    						if (rightDist < leastDist) {
    							leastDist = rightDist;
    							orientation.setNone();
								orientation.setRight();
    						}
    						
    						if (topDist < leastDist) {
    							leastDist = topDist;
    							orientation.setNone();
								orientation.setUp();
    						}
    						
    						if (bottomDist < leastDist) {
    							leastDist = bottomDist;
    							orientation.setNone();
								orientation.setDown();
    						}
    						// TODO: other parry effects here
    					}
    					
    					continue; // prevent damage until parry timer runs out
    				}
    			}
    			
    			if (shields > 0) {
    				shields--;
    				o.remove(); // delete hit obstacle
    			} else {
    				isAlive = false;
    			}
    		}
    		
    		// Cold aura:
    		if (hasColdAura) {
    			int coldAuraRad = coldAuraRange/2;
    			CircleHitbox coldAuraHitbox = new CircleHitbox(x-coldAuraRad, y-coldAuraRad, coldAuraRange, coldAuraRad);
    			if (coldAuraHitbox.checkCollision(o.getHitbox()) != null) {
    				o.setSpeed(1); // reduce speed by 1
    				controller.addEffect(new SlowEffect(o));
    			}
    		}
    	}
    	
    	// Parry:
		if (parry) {
			if (isHit) {
				parryTimer--; // reduce parry timer
    		} else {
    			parryTimer = parryThreshold; // reset parry timer if not touching an object
    		}
    		
    		if (isParrying) { // reset isParrying
    			isParrying = false;
    		}
		}
		
		if (!isHit) {
			timeSinceLastHit++;
		} else {
			timeSinceLastHit = 0;
		}
    }
    
    
    public void hitPlayer(Hitbox h) {
    	if (playerHitbox.checkCollision(h) != null) {
    		if (isImmortal) { // take no damage;
    			return;
    		}
    		
    		if (shields > 0) { // take damage
				shields--;
			} else {
				isAlive = false;
			}
    	}
    }
    
    /**
     * @return the current ball direction as a string
     */
    public String getDirection() {
    	return (orientation.up) ? "UP" : (orientation.down) ? "DOWN" : (orientation.right) ? "RIGHT" : (orientation.left) ? "LEFT" : "NONE"; 
    }
    
    /**
     * @param direction - sets the direction of the ball
     */
    public void setDirection(String direction) {
    	switch (direction) {
    		case "UP":
    			orientation.setNone();
    			orientation.setUp();
    			break;
    		case "DOWN":
    			orientation.setNone();
    			orientation.setDown();
    			break;
    		case "LEFT":
    			orientation.setNone();
    			orientation.setLeft();
    			break;
    		case "RIGHT":
    			orientation.setNone();
    			orientation.setRight();
    			break;
    		default:
    			orientation.setNone();
    			orientation.setRight();
    			break;
    	}
    }
    
    /**
     * @return the players hitbox
     */
    public CircleHitbox getHitbox() {
    	return playerHitbox;
    }
}
