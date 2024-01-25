package playball;

import javax.swing.ImageIcon;

public class BombPowerup extends Powerup {
	private int diameter = 300;
	private int radius = 150;
	
	public BombPowerup(int x, int y) {
		super(x, y, 24, 24, new RectangleHitbox(x, y, 24, 24), null);
		
		setIcon(new ImageIcon(getClass().getResource("images/bomb.png")));
	}

	@Override
	public void activate(GameController controller) {
		controller.clearObstaclesFromArea(new CircleHitbox(x-radius, y-radius, diameter, radius));
		controller.addEffect(new BombEffect(x-radius, y-radius, diameter, radius));
		
	}

}
