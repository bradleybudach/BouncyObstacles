package playball;

import javax.swing.*;

import playball.effects.Effect;
import playball.obstacles.Obstacle;
import playball.powerups.Powerup;

import java.awt.*;
import java.util.ArrayList;

class DrawPanel extends JPanel {
	private static final long serialVersionUID = 1L;
    private Dimension screenDimension = new Dimension(478, 456); // game dimensions
    private GameController controller;
    
    public DrawPanel(GameController controller) {
    	this.controller = controller;
    }
    
    /**
     * Draws component
     */
    public void paintComponent(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, this.getWidth(), this.getHeight());
        g.setColor(Color.BLUE);
        g.fillRect(3, 3, this.getWidth()-6, this.getHeight()-6);
        g.setColor(Color.WHITE);
        g.fillRect(6, 6, this.getWidth()-12, this.getHeight()-12);
        
        controller.player.draw(g);
        
        //paint all controller.obstacles:
        for (Obstacle o : controller.obstacles) {
    		o.draw(g);
    	}
        
        for (Obstacle o : controller.allyObstacles) {
        	o.draw(g);
        }
        
        for (Powerup p : controller.powerups) {
        	p.draw(g);
        }
        
        ArrayList<Effect> removeList = new ArrayList<Effect>();
        for (Effect e : controller.effects) {
        	e.drawFrame(g);
        	if (!e.isAlive) {
        		removeList.add(e);
        	}
        }
        controller.effects.removeAll(removeList);
        
        g.dispose();
    }

    /**
     * Updates ball position based on speed and direction
     * 
     * @return returns true until ball runs into an obstacle and the game ends
     */
    public boolean runFrame() {
    	// Move Player
    	controller.player.move(screenDimension);
    	
        // Move Obstacles
    	controller.obstacles.forEach(o -> o.move(screenDimension)); // move all obstacles
        
        controller.allyObstacles.forEach(o -> o.move(screenDimension)); // move all allies
        
        // Check Collisions
        controller.player.checkCollisions(controller.obstacles);
        
        // Check Obstacle Collisions
        controller.obstacles.forEach(o -> {
        	o.checkObstacleCollisions(controller.obstacles); // check obstacle bounces with eachother
        });
        
        controller.allyObstacles.forEach(o -> {
        	o.checkObstacleCollisions(controller.obstacles); // check collision with other obstacles
        	o.checkHitboxCollision(controller.player.getHitbox()); // check collision with player
        });
        
        controller.powerups.forEach(p -> p.checkPlayerCollision(controller)); // check powerup collisions
        
        repaint();
        
        return controller.player.isAlive;
    }
 }