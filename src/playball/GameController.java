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
import playball.obstacles.BombObstacle;
import playball.obstacles.CircleObstacle;
import playball.obstacles.Obstacle;
import playball.obstacles.ObstacleType;
import playball.obstacles.PowerupEaterObstacle;
import playball.obstacles.RectangleObstacle;
import playball.obstacles.SlowAbilityObstacle;
import playball.obstacles.SpeedAbilityObstacle;
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
    private JPanel topSection = new JPanel();
    private JPanel gameInfo = new JPanel();
    private JButton start = new JButton("START");
    private JButton stop = new JButton("STOP");
    private JLabel scoreLbl = new JLabel("Score: 0");
    private JLabel lvlLbl = new JLabel("Level: 1");
    private JLabel lblUpgrades = new JLabel("Select 2 Upgrades");
    
    JPanel cards = new JPanel();
    JPanel upgradePanel = new JPanel();
    UpgradePanel upgrade_one = new UpgradePanel();
    UpgradePanel upgrade_two = new UpgradePanel();
    UpgradePanel upgrade_three = new UpgradePanel();
    UpgradePanel upgrade_four = new UpgradePanel();
    CardLayout cl;
    
    public int score = 0;
    public int level = 1;
    public int enemyMaxSpeed = 2;
    public int spawnTimer = 500;
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
    private int bombRange = 200;
    private int itemSpawnThreshold = 1000;
    private boolean spawnFriendlyOnSurvive = false;
    private int spawnFirendlyThreshold = 2000;
    private boolean friendlyAlliesTrack = false;
    
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
    	BOMB_RANGE,
    	DROP_FREQUENCY,
    	PARRY_CONVERTS_ALLY,
    	SPAWN_FRIENDLY,
    	IMPROVE_SPAWN_FRIENDLY_TYPE,
    	IMPROVE_SPAWN_FRIENDLY_RATE
    }
    
    public ArrayList<Upgrade> remainingUpgrades = new ArrayList<Upgrade>(Arrays.asList(
    		Upgrade.PARRY,
    		Upgrade.TIME_SLOW,
    		Upgrade.SLOW_NEAR,
    		Upgrade.MAX_SHIELDS,
    		Upgrade.MAX_SHIELDS,
    		Upgrade.MAX_SHIELDS,
    		Upgrade.INCREASE_ALLY_BOUNCES,
    		Upgrade.BETTER_IMMORTALITY,
    		Upgrade.BOMB_RANGE,
    		Upgrade.BOMB_RANGE,
    		Upgrade.DROP_FREQUENCY,
    		Upgrade.DROP_FREQUENCY,
    		Upgrade.SPAWN_FRIENDLY
    ));
    
    public GameController() {
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        d = new DrawPanel(this);

        topSection.setPreferredSize(new Dimension(100,100));
        frame.getContentPane().add(BorderLayout.NORTH, topSection);
        
        gameInfo.add(start);
        gameInfo.add(stop);
        gameInfo.add(scoreLbl);
        gameInfo.add(lvlLbl);
        topSection.add(gameInfo);
        
        lblUpgrades.setFont(new Font("Tahoma", Font.BOLD, 26));;
        lblUpgrades.setVisible(false);
        topSection.add(lblUpgrades);
        
        upgradePanel.setLayout(new GridLayout(0, 4, 0, 0));
        upgradePanel.add(upgrade_one);
        upgradePanel.add(upgrade_two);
        upgradePanel.add(upgrade_three);
        upgradePanel.add(upgrade_four);
        
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
				player.setDirection("RIGHT");
			}
        });
        
        // Left:
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), "moveLeft");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_A, 0), "moveLeft");
        actionMap.put("moveLeft", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				player.setDirection("LEFT");
			}
        });
        
        // Up:
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), "moveUp");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_W, 0), "moveUp");
        actionMap.put("moveUp", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				player.setDirection("UP");
			}
        });
        
        // Down:
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), "moveDown");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_S, 0), "moveDown");
        actionMap.put("moveDown", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				player.setDirection("DOWN");
			}
        });
        
        
        // Parry:
        actionMap.put("parry", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				player.parry();
			}
        });
        
        addObstacle(ObstacleType.RECTANGLE, 1);
        player.setController(this);
        t = new Timer(15, this);
        t.start();
    }
    
    /**
     * Resets the game to its starting state
     */
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
        		Upgrade.BETTER_IMMORTALITY,
        		Upgrade.BOMB_RANGE,
        		Upgrade.BOMB_RANGE,
        		Upgrade.DROP_FREQUENCY,
        		Upgrade.DROP_FREQUENCY,
        		Upgrade.SPAWN_FRIENDLY
        ));
    	
    	// reset upgrades:
    	immortalityDuration = 300;
    	immortalitySpawnChance = 0.05;
    	allyBounces = 1;
    	itemSpawnThreshold = 1000;
    	spawnFriendlyOnSurvive = false;
    	spawnFirendlyThreshold = 2000;
    	friendlyAlliesTrack = false;
    	
    	inputMap.remove(KeyStroke.getKeyStroke(KeyEvent.VK_Q, 0)); // remove parry keybind
    	
    	addObstacle(ObstacleType.RECTANGLE, 1);
    	player.reset();
    	gameEnded = false;
		score = 0;
		enemyMaxSpeed = 2;
    	level = 1;
    	lvlLbl.setText("Level: " + 1);
    	spawnTimer = 500;
		t.start();
    }
    
    public void gameUpdate() {
    	// update score:
    	score++;
    	scoreLbl.setText("Score: " + score);
    	
    	double rand; // random number for spawning items
    	if (score % spawnTimer == 0) { // add obstacles with higher score
    		spawnObstacles();
    	} 
    	
    	if (score % itemSpawnThreshold == 0) {
    		if (score >= 8000) {
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
    		if (rand > 0.9) {
    			addPowerup(PowerupType.ALLY_BOMB);
    		}
    		
    		rand = Math.random();
    		if (rand > 0.9) {
    			addPowerup(PowerupType.BOMB);
    		}
    		
    		rand = Math.random();
    		if (rand > 0.4) {
    			addPowerup(PowerupType.SHIELD);
    		}
    	}
    	
    	
    	if (spawnFriendlyOnSurvive) {
    		if (player.timeSinceLastHit > 0 && player.timeSinceLastHit % spawnFirendlyThreshold == 0) { // if player has survived long enough for ally to spawn\
    			rand = Math.random();
        		if (rand > 0.5) {
        			if (friendlyAlliesTrack) {
        				addAllyObstacle(ObstacleType.ALLY_TRACKING_CIRCLE, 1);
        			} else {
        				addAllyObstacle(ObstacleType.ALLY_CIRCLE, 1);
        			}
        		} else {
        			if (friendlyAlliesTrack) {
        				addAllyObstacle(ObstacleType.ALLY_TRACKING_RECT, 1);
        			} else {
        				addAllyObstacle(ObstacleType.ALLY_RECT, 1);
        			}
        		}
    		}
    	}
    	
    	if (score % 10000 == 0) { // get upgrade
    		getUpgrade();
    		level++;
    		
    		if (level == 3) {
    			enemyMaxSpeed += 1;
    		}
    		
    		obstacles.clear();
    		lvlLbl.setText("Level: " + level);
    		
    		if (level <= 5) {
    			spawnTimer -= 50;
    		}
    		
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
    	
    	
    	// Remove any powerups that the player has picked up:
    	ArrayList<Powerup> removePowerupList = new ArrayList<Powerup>();
    	for (Powerup p : powerups) {
    		if (p.queueRemove) {
    			removePowerupList.add(p);
    		}
    	}
    	powerups.removeAll(removePowerupList);
    	
    	// run the next frame until game over:
    	if (!d.runFrame()) {
    		go = false;
    		gameEnded = true;
    		start.setVisible(true);
    		stop.setVisible(false);
    		t.stop();
    	}
    }
    
    /**
     * Spawns obstacles based on the current level
     */
    public void spawnObstacles() {
    	// random speed:
    	int max_s = ((int) score/8000) + 1;
    	int randSpeed = max_s;
    	if (max_s > 0) {
    		randSpeed = (int)(Math.random()*max_s) + 1;
    	}
    	
    	int speed = Math.min(randSpeed, enemyMaxSpeed); // max speed 3 increasing every 5000 score
    	double rand;
    	
    	//Level 1:
		rand = Math.random();
		if (rand >= 0.5) {
			addObstacle(ObstacleType.CIRCLE, speed);
		} else {
			addObstacle(ObstacleType.RECTANGLE, speed);
		}
		
		
		if (level >= 2) {
			rand = Math.random();
			
			if (rand > 0.6) {
				addObstacle(ObstacleType.BOMB, 0);
			}
			
			if (score > 15000) {
				rand = Math.random();
				if (rand < 0.1) {
					addObstacle(ObstacleType.SLOW_ABILITY, speed);
				}
			}
		}
		
		if (level >= 3) {
			rand = Math.random();
			if (rand < 0.15) {
				addObstacle(ObstacleType.SPEED_ABILITY, speed);
			}
		}
		
		if (level >= 4) {
			rand = Math.random();
			if (rand < 0.1) {
				addObstacle(ObstacleType.POWERUP_EATER, 0);
			}
		}
		
		if (level >= 5) {
			rand = Math.random();
			if (rand < 0.25) {
				rand = Math.random();
				
				if (rand < 0.5) {
					addObstacle(ObstacleType.TRACKING_RECT, Math.max(speed/2, 1));
				} else {
					addObstacle(ObstacleType.TRACKING_CIRCLE, Math.max(speed/2, 1));
				}
				
			}
			
		}
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
				case BOMB:
					ob = new BombObstacle(x, y, 5, this);
					break;
				case SLOW_ABILITY:
					ob = new SlowAbilityObstacle(x, y, 2, new Direction(), this, 250);
					break;
				case SPEED_ABILITY:
					ob = new SpeedAbilityObstacle(x, y, 2, new Direction(), this);
					break;
				case POWERUP_EATER:
					ob = new PowerupEaterObstacle(x, y, this);
					break;
				default:
					System.out.println("Invalid Obstacle Type");
					return;
			}
			
			// no obstacle within 50 px of ball:
			obstacleAdded = (ob.getHitbox().checkCollision(new RectangleHitbox(player.x-75, player.y-75, 150, 150)) == null); // = null if there is not a collision
			
			if (obstacleAdded) {
				obstacles.add(ob); // add the new obstacle to the list
			}
		}
    }
    
    /**
     * Adds an ally obstacle of the given type
     * 
     * @param type - OpstacleType
     * @param speed - move speed
     */
    public void addAllyObstacle(ObstacleType type, int speed) {
    	Obstacle ob;
    	int width = (int) Math.floor(Math.random()*100+5);
		int height = (int) Math.floor(Math.random()*100+5);
		int x = (int) Math.floor(Math.random()*screenDimension.width+1);
		int y = (int) Math.floor(Math.random()*screenDimension.height+1);
		
		switch (type) {
			case ALLY_RECT:
				ob = new AllyRectangleObstacle(width, height, x, y, speed, new Direction(), allyBounces);
				break;
			case ALLY_CIRCLE:
				ob = new AllyCircleObstacle(height, x, y, speed, new Direction(), allyBounces);
				break;
			case ALLY_TRACKING_RECT:
				ob = new AllyTrackingRectangleObstacle(width, height, x, y, speed, new Direction(), this, allyBounces);
				break;
			case ALLY_TRACKING_CIRCLE:
				ob = new AllyTrackingCircleObstacle(height, x, y, speed, new Direction(), this, allyBounces);
				break;
			default:
				System.out.println("Invalid Obstacle Type");
				return;
		}
		
		allyObstacles.add(ob); // add the new obstacle to the list
    
    }
    
    
    // Upgrades:
    /**
     * Pauses game and allows selection of 2 upgrades from a list of 4 random ones
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
    	Upgrade four = null;
    	
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
    	
    	if (remainingUpgrades.size() >= 1) {
    		upgradeSelection = (int)(Math.random()*remainingUpgrades.size());
    		
    		four = remainingUpgrades.get(upgradeSelection);
    		remainingUpgrades.remove(four);
    	}
    	
    	ActionListener upgradeListener = new ActionListener() {
    		private int count = 0;
    		private boolean one, two, three, four = false;
    		
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
				
				if (c == upgrade_four) {
					applyUpgrade(c.getUpgrade());
					upgrade_four.removeActionListener();
					four = true;
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
				
				if (upgrade_four.isVisible()) {
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
					
					if (!four && upgrade_four.isVisible()) {
						upgrade_four.removeActionListener();
						remainingUpgrades.add(upgrade_four.getUpgrade()); // re-ad not chosen upgrade
					}
					
					
					// UI updates:
					gameInfo.setVisible(true);
			    	lblUpgrades.setVisible(false);
					cl.show(cards, "GameScreen");
					
					// Continue Game:
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
    	
    	if (four != null) {
    		upgrade_four.setVisible(true);
    		upgrade_four.setValues(four);
        	upgrade_four.setActionListener(upgradeListener);
    	} else {
    		upgrade_four.setVisible(false);
    	}
    	
    	
    	// UI Changes:
    	gameInfo.setVisible(false);
    	lblUpgrades.setVisible(true);
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
            
            remainingUpgrades.add(Upgrade.INCREASE_PARRY_WINDOW);
            remainingUpgrades.add(Upgrade.INCREASE_PARRY_WINDOW);
            remainingUpgrades.add(Upgrade.PARRY_CONVERTS_ALLY);
			break;
    	case INCREASE_PARRY_WINDOW:
    		player.increaseParryWindow(2);
    		break;
    	case PARRY_CONVERTS_ALLY:
    		player.upgradeParryConvertsEnemy();
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
		case DROP_FREQUENCY:
			itemSpawnThreshold -= 100;
			break;
		case SPAWN_FRIENDLY:
			spawnFriendlyOnSurvive = true;
			spawnFirendlyThreshold = 2000;
			remainingUpgrades.add(Upgrade.IMPROVE_SPAWN_FRIENDLY_RATE);
			remainingUpgrades.add(Upgrade.IMPROVE_SPAWN_FRIENDLY_TYPE);
			break;
		case IMPROVE_SPAWN_FRIENDLY_RATE:
			spawnFirendlyThreshold -= 500;
			break;
		case IMPROVE_SPAWN_FRIENDLY_TYPE:
			friendlyAlliesTrack = true;
			break;
		default:
			System.out.println("Invalid powerup");
			break;
    	}
    }
    
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() instanceof Timer) {
			if (go) { // update each frame of the game
				gameUpdate();
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
	
	
	// Special Effects:
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
    
    /**
     * Adds a given effect to be rendered
     * @param e - effect
     */
    public void addEffect(Effect e) {
    	effects.add(e);
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
    			convertObstacleToAlly(o);
    		}
    	}
    }
    
    /**
     * Converts a given obstacle into an ally of the same type (if applicable)
     * @param o - obstacle to be converted
     */
    public void convertObstacleToAlly(Obstacle o) {
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
		// TODO: AllySlowObstacles
		
		o.remove();
    }
    
}
