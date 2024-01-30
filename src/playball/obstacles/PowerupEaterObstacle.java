package playball.obstacles;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.util.ArrayList;

import javax.swing.ImageIcon;

import playball.Direction;
import playball.GameController;
import playball.hitboxes.CircleHitbox;
import playball.hitboxes.Hitbox;
import playball.powerups.Powerup;

public class PowerupEaterObstacle extends Obstacle {
	private int speedThrottle = 3;
	private Powerup closestPowerup = null;
	private ImageIcon icon;
	private boolean eatingPowerup = false;
	private int eatingTime = 100;
	private int eatingProcess = 0;
	
	/**
	 * Defines a powerup eater obstacle that travels to nearby powerups and consumes them, growing stronger and faster.
	 * @param width - int
	 * @param height - int
	 * @param x - int
	 * @param y - int
	 * @param controller - GameController
	 */
	public PowerupEaterObstacle(int x, int y, GameController controller) {
		super(35, 35, x, y, 1, new Direction(false, false, false, false));
		
		icon = new ImageIcon(getClass().getResource("/images/powerup-eater.png"));
		
		setHitbox(new CircleHitbox(x, y, 35, 17));
		
		setController(controller);
	}

	@Override
	public void cleanup() {}

	@Override
	public void onCollision(ArrayList<Obstacle> obstaclesHit) {}

	@Override
	public void onCollision(Direction d, Hitbox h) {}

	@Override
	public void move(Dimension screenDimension) {
		if (!eatingPowerup && controller.score % 10 == 0) {
			double minDist = Integer.MAX_VALUE;
			for (Powerup p : controller.powerups) {
				double distance = Math.sqrt((this.x-p.x)*(this.x-p.x) + (this.y-p.y)*(this.y-p.y));
				
				if (distance < minDist) {
					minDist = distance;
					closestPowerup = p;
				}
			}
			
			if (closestPowerup != null) {
				if (closestPowerup.x < this.x) {
					dir.setLeft();
				} else {
					dir.setRight();
				}
				
				if (closestPowerup.y < this.y) {
					dir.setUp();
				} else {
					dir.setDown();
				}
			} else {
				dir.setNone();
			}
		}
		
		if (eatingPowerup) { // dont move while eating
			dir.setNone();
		}
		
		if (controller.score % speedThrottle == 0) { // throttle speed
			checkBorderCollision(screenDimension);
			
			runMoveSteps();
			
			hitbox.updatePosition(x, y);
			
			if (closestPowerup != null) {
				if (!controller.powerups.contains(closestPowerup)) { // removes if it is no longer in the powerup list
					closestPowerup = null;
					return;
				}
				
				if (hitbox.checkCollision(closestPowerup.hitbox) != null) {
					eatingPowerup = true;
					eatingProcess++;
					
					if (eatingProcess >= eatingTime) {
						closestPowerup.remove(); // eat powerup
						eatingProcess = 0;
						eatingPowerup = false;
						closestPowerup = null;
						
						// increase speed:
						if (speedThrottle > 1) {
							speedThrottle--;
							scaleIcon();
						} else if (speed < 2) {
							speed++;
							scaleIcon();
						}
					}
				} else {
					eatingProcess = 0;
					eatingPowerup = false;
				}
			}
		}
	}
	
	private void scaleIcon() {
		width += 5;
		height += 5;
		Image newicon = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
		icon = new ImageIcon(newicon);
		
		hitbox.updateSize(width, height);
	}

	@Override
	public void draw(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.drawImage(icon.getImage(), x, y, null);
		
		if (eatingPowerup) {
			g2d.setColor(Color.BLACK);
			int strokeSize = (int)(10*((double)eatingProcess/eatingTime)); 
			g2d.setStroke(new BasicStroke(strokeSize));
			g2d.drawOval(closestPowerup.x-5, closestPowerup.y-5, closestPowerup.width+10, closestPowerup.height+10);
		}
	}

}
