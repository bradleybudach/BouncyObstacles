package playball.obstacles;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.ImageIcon;

import playball.Direction;
import playball.GameController;
import playball.hitboxes.CircleHitbox;
import playball.hitboxes.Hitbox;

public class SpawnerObstacle extends Obstacle {
	private int speedThrottle;
	private int spawnRate = 70;
	private int projectileLifespan = 200;
	private ImageIcon icon;
	private ArrayList<Obstacle> projectiles = new ArrayList<Obstacle>();
	
	public SpawnerObstacle(int x, int y, int speedThrottle, GameController controller) {
		super(64, 64, x, y, 1, new Direction());
		
		setHitbox(new CircleHitbox(x, y, 64, 32));
		
		this.speedThrottle = speedThrottle;
		icon = new ImageIcon(getClass().getResource("/images/spawner-enemy.png"));
		
		setController(controller);
	}

	@Override
	public void cleanup() {
		projectiles.clear();
	}

	@Override
	public void onCollision(ArrayList<Obstacle> obstaclesHit) {}

	@Override
	public void onCollision(Direction d, Hitbox h) {}

	@Override
	public void move(Dimension screenDimension) {
		if (controller.score % speedThrottle == 0) {
			checkBorderCollision(screenDimension);
			
			runMoveSteps();
			
			hitbox.updatePosition(x, y);
		}
		
		// Spawn projectiles:
		if (controller.score % spawnRate == 0) {
			projectiles.add(new ProjectileObstacle(15, hitbox.getCenterX(), hitbox.getCenterY(), 1, new Direction(false, false, false, true), projectileLifespan));
			projectiles.add(new ProjectileObstacle(15, hitbox.getCenterX(), hitbox.getCenterY(), 1, new Direction(false, false, true, false), projectileLifespan));
			projectiles.add(new ProjectileObstacle(15, hitbox.getCenterX(), hitbox.getCenterY(), 1, new Direction(false, true, false, false), projectileLifespan));
			projectiles.add(new ProjectileObstacle(15, hitbox.getCenterX(), hitbox.getCenterY(), 1, new Direction(true, false, false, false), projectileLifespan));
		}
		
		// move projectiles
		projectiles.forEach(p -> p.move(screenDimension));
		controller.player.checkCollisions(projectiles);
		
		// remove queued projectiles
		ArrayList<Obstacle> removeList = new ArrayList<Obstacle>();
		for (Obstacle o : projectiles) {
			if (o.queueRemove) {
				removeList.add(o);
			}
		}
		projectiles.removeAll(removeList);
	}

	@Override
	public void draw(Graphics g) {
		g.drawImage(icon.getImage(), x, y, null);
		g.setColor(Color.WHITE);
		
		int size = (int)((controller.score % spawnRate) / (double)spawnRate * 20); // spawn indicator size
		g.fillOval(hitbox.getCenterX()-size/2-1, hitbox.getCenterY()-size/2, size, size);
		
		
		projectiles.forEach(p -> p.draw(g));
	}
}

class ProjectileObstacle extends CircleObstacle {
	private int lifespan;
	
	public ProjectileObstacle(int diameter, int x, int y, int speed, Direction dir, int lifespan) {
		super(diameter, x, y, speed, dir, Color.RED);
		this.lifespan = lifespan;
	}
	
	@Override
	public void move(Dimension screenDimension) {
		if (checkBorderCollision(screenDimension)) { // remove on collision with edge
			remove();
		}
		
		runMoveSteps();
		
		hitbox.updatePosition(x, y);
		
		// die:
		lifespan--;
		if (lifespan <= 0) {
			remove();
		}
	}
}