package playball.obstacles;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;

import javax.swing.ImageIcon;

import playball.Direction;
import playball.GameController;
import playball.hitboxes.CircleHitbox;
import playball.hitboxes.Hitbox;

public class SlowAbilityObstacle extends Obstacle {
	private int speedThrottle;
	private ImageIcon icon;
	private final int SLOW_AMMOUNT = 1;
	private CircleHitbox auraHitbox;
	private int auraRange;
	private int radius = 17;
	
	private boolean playerHasEntered = false;
	
	/**
	 * Defines a slow ability enemy
	 * 
	 * @param x - x spawn
	 * @param y - y spawn
	 * @param speedThrottle - dampens the speed by this many frames
	 * @param dir - starting direction of travel
	 * @param controller - game controller
	 * @param auraRange - range of the slow effect
	 */
	public SlowAbilityObstacle(int x, int y, int speedThrottle, Direction dir, GameController controller, int auraRange) {
		super(35, 35, x, y, 1, dir);
		setHitbox(new CircleHitbox(x, y, 35, 17));
		
		icon = new ImageIcon(getClass().getResource("/images/slow-ability-enemy.png"));
		this.speedThrottle = speedThrottle;
		this.auraRange = auraRange;
		auraHitbox = new CircleHitbox(x-(auraRange/2)+radius, y-(auraRange/2)+radius, auraRange, auraRange/2);
		
		setController(controller);
	}

	@Override
	public void onCollision(ArrayList<Obstacle> obstaclesHit) {}

	@Override
	public void onCollision(Direction d, Hitbox h) {}

	@Override
	public void move(Dimension screenDimension) {
		if (queueRemove) { // dont slow the player if removed
			return;
		}
		
		if (controller.score % speedThrottle == 0) {
			checkBorderCollision(screenDimension);
			
			runMoveSteps();
			
			hitbox.updatePosition(x, y);
			
			auraHitbox.updatePosition(x-(auraRange/2)+radius, y-(auraRange/2)+radius);
		}
		
		// check if player needs to be slowed:
		if (auraHitbox.checkCollision(controller.player.getHitbox()) != null) {
			playerHasEntered = true;
			controller.player.setSlowed(SLOW_AMMOUNT);
		} else {
			if (playerHasEntered) {
				controller.player.clearSlow();
				playerHasEntered = false;
			}
		}
	}

	@Override
	public void draw(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		
		g2d.drawImage(icon.getImage(), x, y, null);
		
		g2d.setColor(new Color(173, 216, 230));
		g2d.setStroke(new BasicStroke(4));
		g2d.drawOval(auraHitbox.x, auraHitbox.y, auraHitbox.getWidth(), auraHitbox.getHeight());
	}

	@Override
	public void cleanup() {
		controller.player.clearSlow();
	}

}
