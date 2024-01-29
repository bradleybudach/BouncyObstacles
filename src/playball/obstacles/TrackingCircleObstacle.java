package playball.obstacles;

import java.awt.Color;
import java.awt.Dimension;

import playball.Direction;
import playball.GameController;
import playball.Player;

public class TrackingCircleObstacle extends CircleObstacle {

	public TrackingCircleObstacle(int diameter, int x, int y, int speed, Direction dir, GameController controller) {
		super(diameter, x, y, speed, dir, new Color(128, 0, 128));
		setController(controller);
	}
	
	@Override
	public void move(Dimension screenDimension) {
		
		// Track player:
		Player p = controller.player;
		
		if (controller.score % 25 == 0) { // tracking rate
			if (p.x < this.x) {
				dir.setLeft();
			} else {
				dir.setRight();
			}
			
			if (p.y < this.y) {
				dir.setUp();
			} else {
				dir.setDown();
			}
		}
		
		
		checkBorderCollision(screenDimension);
        
		runMoveSteps();
        
        hitbox.updatePosition(x, y);
	}

}
