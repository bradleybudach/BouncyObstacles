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

import playball.GameController.Upgrade;

public class UpgradePanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private JLabel title = new JLabel();
	private JTextArea descriptionTest = new JTextArea();
	private JButton submitButton = new JButton("Select");
	private Upgrade upgrade;
	
	public UpgradePanel() {
		super();
		setLayout(new BorderLayout(0, 10));
		setBackground(new Color(32, 32, 32));
		
		title.setForeground(Color.WHITE);
		title.setHorizontalAlignment(SwingConstants.CENTER);
		title.setVerticalAlignment(SwingConstants.CENTER);
		title.setFont(new Font("Tahoma", Font.BOLD, 16));
		add(BorderLayout.NORTH, this.title);
		
		descriptionTest.setMargin(new Insets(10, 10, 10, 10));
		descriptionTest.setBackground(new Color(32, 32, 32));
		descriptionTest.setForeground(Color.WHITE);
		descriptionTest.setEditable(false);
		descriptionTest.setLineWrap(true);
		add(BorderLayout.CENTER, this.descriptionTest);
		
		add(BorderLayout.SOUTH, submitButton);
	}
	
	public void setActionListener(ActionListener al) {
		submitButton.addActionListener(al);
		submitButton.setVisible(true);
	}
	
	public void removeActionListener() {
		if (submitButton.getActionListeners().length == 0) {
			return;
		}
		
		
		submitButton.removeActionListener(submitButton.getActionListeners()[0]);
		submitButton.setVisible(false);
	}
	
	public Upgrade getUpgrade() {
		return upgrade;
	}
	
	public void setValues(Upgrade upgrade) {
		this.upgrade = upgrade;
		
		switch (upgrade) {
		case PARRY:
			title.setText("<html>Parry</html>");
			descriptionTest.setText("Press [btn] within 0.2 seconds of getting hit to parry an enemy and survive the hit.");
			break;
		case TIME_SLOW:
			title.setText("<html>Time Slow</html>");
			descriptionTest.setText("Press [btn] to slow time briefly.");
			break;
		case INCREASE_TIME_SLOW_DURATION:
			title.setText("<html>Time Slow: Increase Duration</html>");
			descriptionTest.setText("Increase the maximum duration of the time slow by +50");
			break;
		case SLOW_NEAR:
			title.setText("<html>Slow Near</html>");
			descriptionTest.setText("Slow all enemies that get within a radius of 50 px.");
			break;
		case INCREASE_SLOW_NEAR_RANGE:
			title.setText("<html>Slow Near: Increase Range</html>");
			descriptionTest.setText("Increase the range of the slow near effect by 10.");
			break;
		case MAX_SHIELDS:
			title.setText("<html>Increase Max Shields</html>");
			descriptionTest.setText("+1 to max shields.");
			break;
		case INCREASE_ALLY_BOUNCES:
			title.setText("<html>Increase Ally Bounces</html>");
			descriptionTest.setText("+1 bounces to allied obstacles.");
			break;
		default:
			System.out.println("Invalid powerup");
			break;
		}
	}

}
