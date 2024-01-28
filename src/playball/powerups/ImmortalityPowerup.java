package playball.powerups;

import javax.swing.ImageIcon;

import playball.GameController;
import playball.hitboxes.RectangleHitbox;

public class ImmortalityPowerup extends Powerup {

	public ImmortalityPowerup(int x, int y) {
		super(x, y, 24, 24, new RectangleHitbox(x, y, 24, 24), null);
		
		setIcon(new ImageIcon(getClass().getResource("/images/immortality.png")));
	}

	@Override
	public void activate(GameController g) {
		g.player.setImmortal(300);
	}

}
