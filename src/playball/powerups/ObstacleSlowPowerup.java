package playball.powerups;

import javax.swing.ImageIcon;

import playball.GameController;
import playball.effects.SlowEffect;
import playball.hitboxes.RectangleHitbox;

public class ObstacleSlowPowerup extends Powerup {

	public ObstacleSlowPowerup(int x, int y) {
		super(x, y, 24, 24, new RectangleHitbox(x, y, 24, 24), null);
		
		setIcon(new ImageIcon(getClass().getResource("/images/slow.png")));
	}

	@Override
	public void activate(GameController g) {
		g.obstacles.forEach(o -> {
			if (o.speed > 1) {
				o.setSpeed(1);
				g.addEffect(new SlowEffect(o));
			}
		});
	}

}
