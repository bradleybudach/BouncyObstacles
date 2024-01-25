package playball;

import javax.swing.ImageIcon;

public class ObstacleSlowPowerup extends Powerup {

	public ObstacleSlowPowerup(int x, int y) {
		super(x, y, 24, 24, new RectangleHitbox(x, y, 24, 24), null);
		
		setIcon(new ImageIcon(getClass().getResource("images/bomb.png")));
	}

	@Override
	public void activate(GameController g) {
		// TODO Auto-generated method stub
		
	}

}
