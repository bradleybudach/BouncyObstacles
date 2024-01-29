package playball.powerups;

import javax.swing.ImageIcon;

import playball.GameController;
import playball.hitboxes.RectangleHitbox;

public class ImmortalityPowerup extends Powerup {
	private int immortalityDuration = 300;
	
	public ImmortalityPowerup(int x, int y) {
		super(x, y, 24, 24, new RectangleHitbox(x, y, 24, 24), null);
		
		setIcon(new ImageIcon(getClass().getResource("/images/immortality.png")));
	}
	
	public ImmortalityPowerup(int x, int y, int immortalityDuration) {
		super(x, y, 24, 24, new RectangleHitbox(x, y, 24, 24), null);
		
		this.immortalityDuration = immortalityDuration;
		setIcon(new ImageIcon(getClass().getResource("/images/immortality.png")));
	}

	@Override
	public void activate(GameController g) {
		g.player.setImmortal(immortalityDuration);
	}

}
