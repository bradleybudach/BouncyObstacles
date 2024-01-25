package playball;
import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.InputMap;
import javax.swing.ActionMap;

public class GameController implements ActionListener {

    JFrame frame;
    Timer t;
    
    DrawPanel d;
    boolean play = true;
    boolean go = false;
    JPanel c = new JPanel();
    JButton start = new JButton("START");
    JButton stop = new JButton("STOP");
    JLabel scoreLbl = new JLabel("Score: 0");
    
    int score = 0;
    boolean gameEnded = false;
    
    private Dimension screenDimension = new Dimension(478, 456); // game dimensions
    
    public Player player = new Player(10, 10);
    
    public ArrayList<Powerup> powerups = new ArrayList<Powerup>();
    public ArrayList<Obstacle> obstacles = new ArrayList<Obstacle>();
    public ArrayList<Effect> effects = new ArrayList<Effect>();
    
    public GameController() {
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        d = new DrawPanel(this);
        frame.getContentPane().add(BorderLayout.CENTER, d);

        c.setPreferredSize(new Dimension(100,100));
        frame.getContentPane().add(BorderLayout.NORTH, c);
        
        c.add(start);
        c.add(stop);
        c.add(scoreLbl);
        
        frame.setVisible(true);
        frame.setResizable(false);
        frame.setSize(500, 600);
        frame.setLocation(375, 55);
        start.addActionListener(this);
        stop.addActionListener(this);
        stop.setVisible(false);
        
        // Key Bindings:
        InputMap inputMap = frame.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = frame.getRootPane().getActionMap();

        // Right:
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), "moveRight");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_D, 0), "moveRight");
        actionMap.put("moveRight", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setDirection("RIGHT");
			}
        });
        
        // Left:
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), "moveLeft");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_A, 0), "moveLeft");
        actionMap.put("moveLeft", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setDirection("LEFT");
			}
        });
        
        // Up:
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), "moveUp");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_W, 0), "moveUp");
        actionMap.put("moveUp", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setDirection("UP");
			}
        });
        
        // Down:
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), "moveDown");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_S, 0), "moveDown");
        actionMap.put("moveDown", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setDirection("DOWN");
			}
        });
        
        
        
        t = new Timer(15, this);
        t.start();
    }
    
    public void doit()
    {
    	//TODO: Handle Levels Here
    	
    	// update score:
    	score++;
    	scoreLbl.setText("Score: " + score);
    	
    	if (score % 500 == 0) { // add obstacles with higher score
    		spawnObstacles();
    	}
    	
    	if (score % 1500 == 0) { // add powerups
    		addPowerup(PowerupType.SHIELD);
    	}
    	
    	if (score % 4000 == 0) { // add powerups
    		addPowerup(PowerupType.BOMB);
    	}
    	
    	if (!d.runFrame()) { // move ball until game over
    		go = false;
    		gameEnded = true;
    		start.setVisible(true);
    		stop.setVisible(false);
    		t.stop();
    	}
    }
    
    public void spawnObstacles() {
    	int speed = Math.min(((int) score/4000) + 1, 3); // max speed 3 increasing every 4000 score
    	
    	if (score >= 1500) {
			double rand = Math.random();
			if (rand >= 0.5) {
				addObstacle("CIRCLE", speed);
				return; // prevent double spawns
			}
		}
		
		addObstacle("RECT", speed);
    }
    
    /**
     * Adds a new obstacle to the screen
     */
    public void addObstacle(String type, int speed) {
    	Obstacle ob = null;
    	boolean obstacleAdded = false;
		while (!obstacleAdded) { // only end loop when obstacle doesn't collide with ball
			int width = (int) Math.floor(Math.random()*100+5);
			int height = (int) Math.floor(Math.random()*100+5);
			int x = (int) Math.floor(Math.random()*screenDimension.width+1);
			int y = (int) Math.floor(Math.random()*screenDimension.height+1);
			
			
			//TODO: change type to enum
			if (type.equals("RECT")) {
				ob = new RectangleObstacle(width, height, x, y, speed, new Direction());
			} else if (type.equals("CIRCLE")) {
				ob = new CircleObstacle(width, x, y, speed, new Direction(), Color.ORANGE);
			} else {
				//TODO: throw error invalid obstacle type
				return;
			}
			
			// no obstacle within 50 px of ball:
			obstacleAdded = (ob.getHitbox().checkCollision(new RectangleHitbox(player.x-33, player.y-33, 66, 66)) == null); // = null if there is not a collision
			
			if (obstacleAdded) {
				obstacles.add(ob); // add the new obstacle to the list
			}
		}
    }
    
    /**
     * Removes all controller.obstacles from a given hitbox area
     * 
     * @param area - area to delete controller.obstacles from
     */
    public void clearObstaclesFromArea(Hitbox area) {
    	ArrayList<Obstacle> removeObstacles = new ArrayList<Obstacle>();
    	for (Obstacle o : obstacles) {
    		if (area.checkCollision(o.getHitbox()) != null) {
    			removeObstacles.add(o);
    		}
    	}
    	
    	obstacles.removeAll(removeObstacles);
    }
    
	/**
	 * Adds a powerup to the screen of a given type
	 * @param type - PowerupType
	 */
    public void addPowerup(PowerupType type) {
    	int x = (int) Math.floor(Math.random()*(screenDimension.width-10))+5;
		int y = (int) Math.floor(Math.random()*(screenDimension.height-10))+5;
    	
    	switch (type) {
	    	case SHIELD:
	    		powerups.add(new ShieldPowerup(x, y)); 
	    		break;
	    	case BOMB:
	    		powerups.add(new BombPowerup(x, y));
	    		break;
	    	case SLOW:
	    		powerups.add(new ObstacleSlowPowerup(x, y));
	    		break;
		default:
			break;
    	}
    }
    
    public void addEffect(Effect e) {
    	effects.add(e);
    }
    
    
    /**
     * @param direction - sets the direction of the ball
     */
    public void setDirection(String direction) {
    	switch (direction) {
    		case "UP":
    			player.orientation.setNone();
    			player.orientation.setUp();
    			break;
    		case "DOWN":
    			player.orientation.setNone();
    			player.orientation.setDown();
    			break;
    		case "LEFT":
    			player.orientation.setNone();
    			player.orientation.setLeft();
    			break;
    		case "RIGHT":
    			player.orientation.setNone();
    			player.orientation.setRight();
    			break;
    		default:
    			player.orientation.setNone();
    			player.orientation.setRight();
    			break;
    	}
    }

    public void resetGame() {
    	obstacles.clear();
    	powerups.clear();
    	addObstacle("RECT", 1);
    	addPowerup(PowerupType.SHIELD);
    	player.reset();
    	gameEnded = false;
		score = 0;
		t.start();
    }
    
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() instanceof Timer) {
			if (go) { // update each frame of the game
				doit();
			}
		}
		else if (e.getSource() instanceof JButton) {
			JButton clicked = (JButton) e.getSource();
			if (clicked.getText().equals("START")) {	
				if (gameEnded) {
					resetGame();
				}
				
				go = true;
				start.setVisible(false);
				stop.setVisible(true);
			} else if (clicked.getText().equals("STOP")) {
				go = false;
				start.setVisible(true);
				stop.setVisible(false);
			}
				
		}
	}
}
