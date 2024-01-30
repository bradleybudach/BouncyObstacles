package playball.obstacles;

import java.awt.Dimension;
import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.ImageIcon;

import playball.Direction;
import playball.GameController;
import playball.effects.SpeedEffect;
import playball.hitboxes.CircleHitbox;
import playball.hitboxes.Hitbox;

public class SpeedAbilityObstacle extends Obstacle {
	private int speedThrottle;
	private ImageIcon icon;

	public SpeedAbilityObstacle(int x, int y, int speedThrottle, Direction dir, GameController controller) {
		super(35, 35, x, y, 1, dir);
		setHitbox(new CircleHitbox(x, y, 35, 17));
		
		icon = new ImageIcon(getClass().getResource("/images/speed-ability-enemy.png"));
		this.speedThrottle = speedThrottle;
		
		setController(controller);
	}

	@Override
	public void cleanup() {}

	@Override
	public void onCollision(ArrayList<Obstacle> obstaclesHit) {
		obstaclesHit.forEach(o -> {
			int updatedSpeed = Math.min(controller.enemyMaxSpeed, o.speed+1); // updates speed
			o.setSpeed(updatedSpeed);
		});
		
		controller.addEffect(new SpeedEffect(obstaclesHit)); // add effect to all hit obstacles
		
	}

	@Override
	public void onCollision(Direction d, Hitbox h) {}

	@Override
	public void move(Dimension screenDimension) {
		if (controller.score % speedThrottle == 0) {
			checkBorderCollision(screenDimension);
			
			runMoveSteps();
			
			hitbox.updatePosition(x, y);
		}
	}

	@Override
	public void draw(Graphics g) {
		g.drawImage(icon.getImage(), x, y, null);
	}
}
