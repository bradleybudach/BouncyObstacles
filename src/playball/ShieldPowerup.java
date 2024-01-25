package playball;

import javax.swing.ImageIcon;

public class ShieldPowerup extends Powerup {

	public ShieldPowerup(int x, int y) {
		super(x, y, 24, 24, new RectangleHitbox(x, y, 24, 24), null);
		
		setIcon(new ImageIcon(getClass().getResource("images/shield-pickup.png")));
	}

	@Override
	public void activate(GameController controller) {
		controller.player.addShields();
	}
	
}
