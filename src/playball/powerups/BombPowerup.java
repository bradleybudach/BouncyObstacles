package playball.powerups;

import javax.swing.ImageIcon;

import playball.GameController;
import playball.effects.BombEffect;
import playball.hitboxes.CircleHitbox;
import playball.hitboxes.RectangleHitbox;

public class BombPowerup extends Powerup {
	private int diameter = 300;
	private int radius = 150;
	
	/**
	 * Create bomb with x and y position
	 * 
	 * @param x - x spawn position
	 * @param y - y spawn position
	 */
	public BombPowerup(int x, int y) {
		super(x, y, 24, 24, new RectangleHitbox(x, y, 24, 24), null);
		
		setIcon(new ImageIcon(getClass().getResource("/images/bomb.png")));
	}

	/**
	 * Create a bomb with a defined range
	 * 
	 * @param x - x spawn position
	 * @param y - y spawn position
	 * @param range - range
	 */
	public BombPowerup(int x, int y, int range) {
		super(x, y, 24, 24, new RectangleHitbox(x, y, 24, 24), null);
		
		setIcon(new ImageIcon(getClass().getResource("/images/bomb.png")));
		
		diameter = range;
		radius = range/2;
	}
	
	@Override
	public void activate(GameController controller) {
		controller.clearObstaclesFromArea(new CircleHitbox(x-radius, y-radius, diameter, radius));
		controller.addEffect(new BombEffect(x-radius, y-radius, diameter, radius));
		
	}

}
