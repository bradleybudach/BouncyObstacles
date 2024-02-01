package playball.powerups;

import java.util.ArrayList;

import javax.swing.ImageIcon;

import playball.GameController;
import playball.hitboxes.RectangleHitbox;
import playball.obstacles.Obstacle;

public class DowngradePowerup extends Powerup {

	/**
	 * Creates a downgrade powerup that downgrades all enemies on contact with the player
	 * @param x
	 * @param y
	 */
	public DowngradePowerup(int x, int y) {
		super(x, y, 24, 24, new RectangleHitbox(x, y, 24, 24), null);

		setIcon(new ImageIcon(getClass().getResource("/images/downgrade-powerup.png")));
	}

	@Override
	public void activate(GameController g) {
		for (Obstacle o : g.obstacles) {
			g.downgradeObstacle(o); // downgrade all obstacles
		};
	}

}
