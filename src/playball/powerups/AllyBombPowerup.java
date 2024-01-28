package playball.powerups;

import java.awt.Color;

import javax.swing.ImageIcon;

import playball.GameController;
import playball.effects.BombEffect;
import playball.hitboxes.CircleHitbox;
import playball.hitboxes.RectangleHitbox;

public class AllyBombPowerup extends Powerup {
	private int diameter = 300;
	private int radius = 150;
	
	public AllyBombPowerup(int x, int y) {
		super(x, y, 24, 24, new RectangleHitbox(x, y, 24, 24), null);
		
		setIcon(new ImageIcon(getClass().getResource("/images/ally-bomb.png")));
	}

	@Override
	public void activate(GameController controller) {
		controller.convertObstaclesInArea(new CircleHitbox(x-radius, y-radius, diameter, radius));
		
		BombEffect effect = new BombEffect(x-radius, y-radius, diameter, radius);
		effect.setColor(Color.GREEN); // green explosion effect
		controller.addEffect(effect);
	}

}
