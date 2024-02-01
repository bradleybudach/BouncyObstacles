package playball;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import playball.GameController.Upgrade;

public class UpgradePanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	// Components:
	private JLabel title = new JLabel();
	private JTextArea descriptionText = new JTextArea();
	private JButton submitButton = new JButton("Select Upgrade");
	
	private Upgrade upgrade; // saved upgrade
	
	// Background colors:
	private Color greenColor = new Color(144, 238, 144);
	private Color whiteColor = new Color(240, 240, 240);
	
	/**
	 * Creates an upgrade panel to display upgrade choices
	 */
	public UpgradePanel() {
		super();
		setLayout(new BorderLayout(0, 10));
		title.setHorizontalAlignment(SwingConstants.CENTER);
		title.setVerticalAlignment(SwingConstants.CENTER);
		title.setFont(new Font("Tahoma", Font.BOLD, 16));
		title.setBorder(new EmptyBorder(10,10,10,10));
		add(BorderLayout.NORTH, this.title);
		
		descriptionText.setMargin(new Insets(10, 10, 10, 10));
		descriptionText.setEditable(false);
		descriptionText.setLineWrap(true);
		descriptionText.setWrapStyleWord(true);
		add(BorderLayout.CENTER, this.descriptionText);
		
		add(BorderLayout.SOUTH, submitButton);
		
		setBorder(new LineBorder(Color.BLACK));
	}
	
	public void setActionListener(ActionListener al) {
		submitButton.addActionListener(al);
	}
	
	/**
	 * Removes the action listener from the button in this panel if there is one.
	 */
	public void removeActionListener() {
		if (submitButton.getActionListeners().length == 0) {
			return;
		}
		
		
		submitButton.removeActionListener(submitButton.getActionListeners()[0]);
		//submitButton.setVisible(false);
		
		// UI Changes:
		submitButton.setText("Selected");
		setBackground(greenColor);
		descriptionText.setBackground(greenColor);
	}
	
	/**
	 * Returns the upgrade saved in this panel
	 * @return upgrade
	 */
	public Upgrade getUpgrade() {
		return upgrade;
	}
	
	
	/**
	 * Updates the panel to display a new upgrade
	 * @param upgrade - new upgrade to display
	 */
	public void setValues(Upgrade upgrade) {
		this.upgrade = upgrade;
		
		switch (upgrade) {
		case PARRY:
			title.setText("<html>Parry</html>");
			descriptionText.setText("Press Q within 2 frames of getting hit to parry an enemy and survive the hit, bouncing yourself in the opposite direction.");
			break;
		case INCREASE_PARRY_WINDOW:
			title.setText("<html>Parry: Increase Window</html>");
			descriptionText.setText("Increase the window you have to parry enemies by 1 frame.");
			break;
		case PARRY_CONVERTS_ALLY:
			title.setText("<html>Parry: Convert Ally</html>");
			descriptionText.setText("Instead of destroying an enemy on a parry, convert it to be an ally.");
			break;
		case TIME_SLOW:
			title.setText("<html>Time Slow</html>");
			descriptionText.setText("Press [btn] to slow time briefly.");
			break;
		case INCREASE_TIME_SLOW_DURATION:
			title.setText("<html>Time Slow: Increase Duration</html>");
			descriptionText.setText("Increase the maximum duration of the time slow by 50 frames");
			break;
		case SLOW_NEAR:
			title.setText("<html>Chilling Grasp</html>");
			descriptionText.setText("Slow all enemies that get within a radius of 50px.");
			break;
		case INCREASE_SLOW_NEAR_RANGE:
			title.setText("<html>Chilling Grasp: Increase Range</html>");
			descriptionText.setText("Increase the range of the slow near effect by 10px.");
			break;
		case MAX_SHIELDS:
			title.setText("<html>Increase Max Shields</html>");
			descriptionText.setText("+1 to max shields.");
			break;
		case INCREASE_ALLY_BOUNCES:
			title.setText("<html>Increase Ally Bounces</html>");
			descriptionText.setText("+1 bounces to allied obstacles.");
			break;
		case BETTER_IMMORTALITY:
			title.setText("<html>Immortal Aptitude</html>");
			descriptionText.setText("Immortality powerup is 10% more likely to spawn and has an its duration increased by 100 frames.");
			break;
		case BOMB_RANGE:
			title.setText("<html>Explosives Expert</html>");
			descriptionText.setText("Increase the range of all bombs by +50px.");
			break;
		case DROP_FREQUENCY:
			title.setText("<html>Lucky</html>");
			descriptionText.setText("Drops will spawn +100 frames more often.");
			break;
		case SPAWN_FRIENDLY:
			title.setText("<html>Friend Finder</html>");
			descriptionText.setText("Spawn a random frienly obstacle if you avoid taking damage for 2000 frames.");
			break;
		case IMPROVE_SPAWN_FRIENDLY_RATE:
			title.setText("<html>Friend Finder: Improve Rate</html>");
			descriptionText.setText("Improve the rate of friendly obstacle spawns if the player is not hit by 500 frames.");
			break;
		case IMPROVE_SPAWN_FRIENDLY_TYPE:
			title.setText("<html>Friend Finder: Bloodhounds</html>");
			descriptionText.setText("All allies spawned when the player avoids damage are now seek out nearby obstacles.");
			break;
		case SPRINT:
			title.setText("<html>Sprint</html>");
			descriptionText.setText("Hold shift to sprint, increasing speed by 1 for a limited duration.");
			break;
		case INCREASE_SPRINT_STAMINA:
			title.setText("<html>Sprint: Increase Stamina</html>");
			descriptionText.setText("Increase the maximum duration of a sprint by 50 frames.");
			break;
		case GAIN_SHEILD_ON_SURVIVE:
			title.setText("<html>Survival Expert</html>");
			descriptionText.setText("Gain 1 shield every 2000 score of not getting hit.");
			break;
		case IMPROVE_SHIELD_GAIN_RATE:
			title.setText("<html>Survival Expert: Improve Rate</html>");
			descriptionText.setText("Improve the rate shield regen if the player is not hit by 250 frames.");
			break;
		case PLAYER_EXPLODE_ON_HIT:
			title.setText("<html>Retaliation</html>");
			descriptionText.setText("Player explodes on taking damage in a radius of 100px, destroys all nearby enemies.");
			break;
		case IMPROVE_EXPLODE_ON_HIT_RANGE:
			title.setText("<html>Retaliation : Imrpove Range</html>");
			descriptionText.setText("Improve the range of the player explosion on hit by 50.");
			break;
		case GAIN_SHIELD_GIVES_IMMORTALITY:
			title.setText("<html>Strategic Survival</html>");
			descriptionText.setText("Picking up shield powerups gives the player immortality for 40 frames.");
			break;
		case BOMB_SPAWN_RATE:
			title.setText("<html>Detonator</html>");
			descriptionText.setText("Explosive Bombs are 10% more likely to spawn.");
			break;
		default:
			System.out.println("Invalid powerup");
			break;
		}
		

		// Update other UI:
		submitButton.setText("Select Upgrade"); 
		setBackground(whiteColor);
		descriptionText.setBackground(whiteColor);
	}

}
