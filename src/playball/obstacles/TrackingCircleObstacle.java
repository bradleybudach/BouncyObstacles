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
		
		
		if (x+width >= screenDimension.width) { // bounce of left or right walls
            dir.setLeft();
        } else if (x <= 6) {
            dir.setRight();
        }
        
        if(y+height >= screenDimension.height) { // bounce off top or bottom walls
            dir.setUp();
        } else if (y <= 6) {
            dir.setDown();
        }
        
        for (int i = 0; i < speed; i++) {
        	currentMoveStep = (currentMoveStep + 1) % dir.moveSteps.size();
        	switch (dir.moveSteps.get(currentMoveStep)) {
		    	case LEFT:
		    		x -= 1;
		    		break;
		    	case RIGHT:
		    		x += 1;
		    		break;
		    	case UP:
		    		y -= 1;
		    		break;
		    	case DOWN:
		    		y += 1;
		    		break;
		    	default:
		    		break;
		    }
        }
        
        hitbox.updatePosition(x, y);
	}

}
