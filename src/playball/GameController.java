package playball;
import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.InputMap;
import playball.effects.Effect;
import playball.hitboxes.Hitbox;
import playball.hitboxes.RectangleHitbox;
import playball.obstacles.AllyCircleObstacle;
import playball.obstacles.AllyRectangleObstacle;
import playball.obstacles.AllyTrackingCircleObstacle;
import playball.obstacles.AllyTrackingRectangleObstacle;
import playball.obstacles.CircleObstacle;
import playball.obstacles.Obstacle;
import playball.obstacles.ObstacleType;
import playball.obstacles.RectangleObstacle;
import playball.obstacles.TrackingCircleObstacle;
import playball.obstacles.TrackingRectangleObstacle;
import playball.powerups.AllyBombPowerup;
import playball.powerups.BombPowerup;
import playball.powerups.ImmortalityPowerup;
import playball.powerups.ObstacleSlowPowerup;
import playball.powerups.Powerup;
import playball.powerups.PowerupType;
import playball.powerups.ShieldPowerup;

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
    JPanel cards = new JPanel();
    JPanel upgradePanel = new JPanel();
    UpgradePanel upgrade_one = new UpgradePanel();
    UpgradePanel upgrade_two = new UpgradePanel();
    UpgradePanel upgrade_three = new UpgradePanel();
    CardLayout cl;
    
    public int score = 0;
    boolean gameEnded = false;
    
    private Dimension screenDimension = new Dimension(478, 456); // game dimensions
    
    public Player player = new Player(10, 10);
    
    public ArrayList<Powerup> powerups = new ArrayList<Powerup>();
    public ArrayList<Obstacle> obstacles = new ArrayList<Obstacle>();
    public ArrayList<Obstacle> allyObstacles = new ArrayList<Obstacle>();
    public ArrayList<Effect> effects = new ArrayList<Effect>();
    
    // Key Bindings:
    InputMap inputMap;
    ActionMap actionMap;
    
    // Upgrades:
    private int allyBounces = 1;
    private double immortalitySpawnChance = 0.05;
    private int immortalityDuration = 300;
    private int bombRange = 300;
    
    // Upgrades:
    public enum Upgrade {
    	PARRY,
    	INCREASE_PARRY_WINDOW,
    	TIME_SLOW,
    	INCREASE_TIME_SLOW_DURATION,
    	SLOW_NEAR,
    	INCREASE_SLOW_NEAR_RANGE,
    	MAX_SHIELDS,
    	INCREASE_ALLY_BOUNCES,
    	BETTER_IMMORTALITY,
    	BOMB_RANGE
    }
    
    public ArrayList<Upgrade> remainingUpgrades = new ArrayList<Upgrade>(Arrays.asList(
    		Upgrade.PARRY,
    		Upgrade.TIME_SLOW,
    		Upgrade.SLOW_NEAR,
    		Upgrade.MAX_SHIELDS,
    		Upgrade.MAX_SHIELDS,
    		Upgrade.MAX_SHIELDS,
    		Upgrade.INCREASE_ALLY_BOUNCES,
    		Upgrade.INCREASE_ALLY_BOUNCES,
    		Upgrade.BETTER_IMMORTALITY,
    		Upgrade.BOMB_RANGE,
    		Upgrade.BOMB_RANGE
    ));
    
    public GameController() {
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        d = new DrawPanel(this);

        c.setPreferredSize(new Dimension(100,100));
        frame.getContentPane().add(BorderLayout.NORTH, c);
        
        c.add(start);
        c.add(stop);
        c.add(scoreLbl);
        
        upgradePanel.setLayout(new GridLayout(0, 3, 0, 0));
        upgradePanel.add(upgrade_one);
        upgradePanel.add(upgrade_two);
        upgradePanel.add(upgrade_three);
        
        cl = new CardLayout();
        cards.setLayout(cl);
        cards.add(d, "GameScreen");
        cards.add(upgradePanel, "UpgradeScreen");
        
        frame.getContentPane().add(BorderLayout.CENTER, cards);
        
        frame.setVisible(true);
        frame.setResizable(false);
        frame.setSize(500, 600);
        frame.setLocation(375, 55);
        start.addActionListener(this);
        stop.addActionListener(this);
        stop.setVisible(false);
        
        
        // Keybinds:
        inputMap = frame.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        actionMap = frame.getRootPane().getActionMap();

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
        
        addObstacle(ObstacleType.RECTANGLE, 1);
        player.setController(this);
        t = new Timer(15, this);
        t.start();
    }
    
    public void doit()
    {
    	// update score:
    	score++;
    	scoreLbl.setText("Score: " + score);
    	
    	double rand; // random number for spawning items
    	if (score % 500 == 0) { // add obstacles with higher score
    		spawnObstacles();
    	} 
    	
    	if (score % 1000 == 0) { 
    		if (score >= 4000) {
    			rand = Math.random();
        		if (rand > 0.85) {
        			addPowerup(PowerupType.SLOW);
        		}
    		}
    		
    		rand = Math.random();
    		if (rand < immortalitySpawnChance) {
    			addPowerup(PowerupType.IMMORTALITY);
    		}
    		
    		rand = Math.random();
    		if (rand > 0.85) {
    			addPowerup(PowerupType.ALLY_BOMB);
    		}
    	}
    	
    	if (score % 1500 == 0) { // add powerups
    		rand = Math.random();
    		if (rand > 0.25) {
    			addPowerup(PowerupType.SHIELD);
    		}
    	}
    	
    	if (score % 4000 == 0) { // add powerups
    		rand = Math.random();
    		if (rand > 0.5) {
    			addPowerup(PowerupType.BOMB);
    		}
    	}
    	
    	if (score % 100 == 0) { // get upgrade
    		getUpgrade();
    	}
    	
    	// Remove any obstacles that are were destroyed last frame:
    	ArrayList<Obstacle> removeList = new ArrayList<Obstacle>();
    	for (Obstacle o : obstacles) {
    		if (o.queueRemove) {
    			removeList.add(o);
    		}
    	}
    	obstacles.removeAll(removeList);
    	
    	removeList.clear();
    	for (Obstacle o : allyObstacles) {
    		if (o.queueRemove) {
    			removeList.add(o);
    		}
    	}
    	allyObstacles.removeAll(removeList);
    	
    	
    	// run the next frame until game over:
    	if (!d.runFrame()) {
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
				addObstacle(ObstacleType.CIRCLE, speed);
				return; // prevent double spawns
			}
		}
		
		addObstacle(ObstacleType.RECTANGLE, speed);
    }
    
    /**
     * Adds a new obstacle to the screen
     */
    public void addObstacle(ObstacleType type, int speed) {
    	Obstacle ob = null;
    	boolean obstacleAdded = false;
		while (!obstacleAdded) { // only end loop when obstacle doesn't collide with ball
			int width = (int) Math.floor(Math.random()*100+5);
			int height = (int) Math.floor(Math.random()*100+5);
			int x = (int) Math.floor(Math.random()*screenDimension.width+1);
			int y = (int) Math.floor(Math.random()*screenDimension.height+1);
			
			
			//TODO: change type to enum
			switch (type) {
				case RECTANGLE:
					ob = new RectangleObstacle(width, height, x, y, speed, new Direction());
					break;
				case CIRCLE:
					ob = new CircleObstacle(width, x, y, speed, new Direction(), Color.ORANGE);
					break;
				case TRACKING_RECT:
					ob = new TrackingRectangleObstacle(width, height, x, y, speed, new Direction(), this);
					break;
				case TRACKING_CIRCLE:
					ob = new TrackingCircleObstacle(width, x, y, speed, new Direction(), this);
					break;
				default:
					System.out.println("Invalid Obstacle Type");
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
     * @param area - area to delete obstacles from
     */
    public void clearObstaclesFromArea(Hitbox area) {
    	for (Obstacle o : obstacles) {
    		if (area.checkCollision(o.getHitbox()) != null) {
    			o.remove();
    		}
    	}
    }
    
    /**
     * Converts all obstacles in the given area to allies
     * 
     * @param area - area to convert obstacles
     */
    public void convertObstaclesInArea(Hitbox area) {
    	for (Obstacle o : obstacles) {
    		if (area.checkCollision(o.getHitbox()) != null) {
    			if (o instanceof CircleObstacle) {
    				if (o instanceof TrackingCircleObstacle) {
    					allyObstacles.add(new AllyTrackingCircleObstacle(o.width, o.x, o.y, o.speed, o.dir, this, allyBounces));
    				} else {
    					allyObstacles.add(new AllyCircleObstacle(o.width, o.x, o.y, o.speed, o.dir, allyBounces));
    				}
    			} else if (o instanceof RectangleObstacle) {
    				if (o instanceof TrackingRectangleObstacle) {
    					allyObstacles.add(new AllyTrackingRectangleObstacle(o.width, o.height, o.x, o.y, o.speed, o.dir, this, allyBounces));
    				} else {
    					allyObstacles.add(new AllyRectangleObstacle(o.width, o.height, o.x, o.y, o.speed, o.dir, allyBounces));
    				}
    			}
    			
    			o.remove();
    		}
    	}
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
	    		powerups.add(new BombPowerup(x, y, bombRange));
	    		break;
	    	case SLOW:
	    		powerups.add(new ObstacleSlowPowerup(x, y));
	    		break;
	    	case IMMORTALITY:
	    		powerups.add(new ImmortalityPowerup(x, y, immortalityDuration));
	    		break;
	    	case ALLY_BOMB:
	    		powerups.add(new AllyBombPowerup(x, y, bombRange));
		default:
			break;
    	}
    }
    
    public void addEffect(Effect e) {
    	effects.add(e);
    }
    
    /**
     * Pauses game and allows selection of 2 upgrades.
     */
    public void getUpgrade() {
    	if (remainingUpgrades.size() == 0) {
    		return;
    	}
    	
    	t.stop(); // pause game
    	
    	// Load random upgrades:
    	Upgrade one = null;
    	Upgrade two = null;
    	Upgrade three = null;
    	
    	int upgradeSelection = (int)(Math.random()*remainingUpgrades.size());
    	one = remainingUpgrades.get(upgradeSelection);
    	remainingUpgrades.remove(one);
    	
    	if (remainingUpgrades.size() >= 1) {
    		upgradeSelection = (int)(Math.random()*remainingUpgrades.size());
    		
    		two = remainingUpgrades.get(upgradeSelection);
    		remainingUpgrades.remove(two);
    	}
    	
    	if (remainingUpgrades.size() >= 1) {
    		upgradeSelection = (int)(Math.random()*remainingUpgrades.size());
    		
    		three = remainingUpgrades.get(upgradeSelection);
    		remainingUpgrades.remove(three);
    	}
    	
    	ActionListener upgradeListener = new ActionListener() {
    		private int count = 0;
    		private boolean one, two, three = false;
    		
			@Override
			public void actionPerformed(ActionEvent e) {
				count++;
				
				JButton b = ((JButton)e.getSource());
				UpgradePanel c = (UpgradePanel)b.getParent();
				if (c == upgrade_one) {
					applyUpgrade(c.getUpgrade());
					upgrade_one.removeActionListener();
					one = true;
				}
				
				if (c == upgrade_two) {
					applyUpgrade(c.getUpgrade());
					upgrade_two.removeActionListener();
					two = true;
				}
				
				if (c == upgrade_three) {
					applyUpgrade(c.getUpgrade());
					upgrade_three.removeActionListener();
					three = true;
				}
				
				
				int visibleCount = 0;
				if (upgrade_one.isVisible()) {
					visibleCount++;
				}
				
				if (upgrade_two.isVisible()) {
					visibleCount++;
				}
				
				if (upgrade_three.isVisible()) {
					visibleCount++;
				}
				
				if (count >= 2 || visibleCount < 2) {
					if (!one && upgrade_one.isVisible()) {
						upgrade_one.removeActionListener();
						remainingUpgrades.add(upgrade_one.getUpgrade()); // re-ad not chosen upgrade
					}
					
					if (!two && upgrade_two.isVisible()) {
						upgrade_two.removeActionListener();
						remainingUpgrades.add(upgrade_two.getUpgrade()); // re-ad not chosen upgrade
					}
					
					if (!three && upgrade_three.isVisible()) {
						upgrade_three.removeActionListener();
						remainingUpgrades.add(upgrade_three.getUpgrade()); // re-ad not chosen upgrade
					}
					
					cl.show(cards, "GameScreen");
					player.setImmortal(30);
					t.start();
				}
			}
    		
    	};
    
    	
    	upgrade_one.setValues(one);
    	upgrade_one.setActionListener(upgradeListener);
    	
    	if (two != null) { // only show if there is an upgrade to show here
    		upgrade_two.setVisible(true);
    		upgrade_two.setValues(two);
        	upgrade_two.setActionListener(upgradeListener);
    	} else {
    		upgrade_two.setVisible(false);
    	}
    	
    	if (three != null) { // only show if there is an upgrade to show here
    		upgrade_three.setVisible(true);
    		upgrade_three.setValues(three);
        	upgrade_three.setActionListener(upgradeListener);
    	} else {
    		upgrade_three.setVisible(false);
    	}
    	
    	
    	cl.show(cards, "UpgradeScreen"); // display upgrade screen with upgrade options
    }
    
    /**
     * Apply a chosen upgrade
     * 
     * @param u - the upgrade to apply
     */
    private void applyUpgrade(Upgrade u) {
    	switch (u) {
    	case PARRY:
    		player.enableParry();
    		
    		// Add Parry Keybind:
            inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_Q, 0), "parry");
            actionMap.put("parry", new AbstractAction() {
    			@Override
    			public void actionPerformed(ActionEvent e) {
    				player.parry();
    			}
            });
            
            remainingUpgrades.add(Upgrade.INCREASE_PARRY_WINDOW);
            remainingUpgrades.add(Upgrade.INCREASE_PARRY_WINDOW);
			break;
    	case INCREASE_PARRY_WINDOW:
    		player.increaseParryWindow(2);
    		break;
		case TIME_SLOW:
			remainingUpgrades.add(Upgrade.INCREASE_TIME_SLOW_DURATION);
			remainingUpgrades.add(Upgrade.INCREASE_TIME_SLOW_DURATION);
			remainingUpgrades.add(Upgrade.INCREASE_TIME_SLOW_DURATION);
			break;
		case INCREASE_TIME_SLOW_DURATION:
			
			break;
		case SLOW_NEAR:
			player.addColdAura();
			remainingUpgrades.add(Upgrade.INCREASE_SLOW_NEAR_RANGE);
			remainingUpgrades.add(Upgrade.INCREASE_SLOW_NEAR_RANGE);
			remainingUpgrades.add(Upgrade.INCREASE_SLOW_NEAR_RANGE);
			break;
		case INCREASE_SLOW_NEAR_RANGE:
			player.increaseColdAuraRange(10);
			break;
		case MAX_SHIELDS:
			player.increaseMaxShields();
			break;
		case INCREASE_ALLY_BOUNCES:
			allyBounces++;
			break;
		case BETTER_IMMORTALITY:
			immortalitySpawnChance += 0.1;
			immortalityDuration += 100;
			break;
		case BOMB_RANGE:
			bombRange += 50;
			break;
		default:
			System.out.println("Invalid powerup");
			break;
    	}
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
    	// Clear components:
    	obstacles.clear();
    	allyObstacles.clear();
    	powerups.clear();
    	effects.clear();
    	
    	// Reset upgrades:
    	remainingUpgrades = new ArrayList<Upgrade>(Arrays.asList(
        		Upgrade.PARRY,
        		Upgrade.TIME_SLOW,
        		Upgrade.SLOW_NEAR,
        		Upgrade.MAX_SHIELDS,
        		Upgrade.MAX_SHIELDS,
        		Upgrade.MAX_SHIELDS,
        		Upgrade.INCREASE_ALLY_BOUNCES,
        		Upgrade.INCREASE_ALLY_BOUNCES,
        		Upgrade.BETTER_IMMORTALITY,
        		Upgrade.BOMB_RANGE,
        		Upgrade.BOMB_RANGE
        ));
    	
    	
    	immortalityDuration = 300;
    	immortalitySpawnChance = 0.05;
    	allyBounces = 1;
    	
    	addObstacle(ObstacleType.RECTANGLE, 1);
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
