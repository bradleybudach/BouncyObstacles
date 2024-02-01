package playball;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Tester implements ActionListener {

	JFrame frame;
	Timer t;;
	Timer movementTimer;
	Timer scoreTimer;

	DrawPanel d = new DrawPanel();
	boolean play = true;
	boolean go = false;
	JPanel c = new JPanel();
	JButton start = new JButton("START");
	JButton stop = new JButton("STOP");
//  JButton updn = new JButton("UP & DN");
//  JButton ltrt = new JButton("Lft & Dwn");

	public Tester() {
		frame = new JFrame("Test");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		frame.getContentPane().add(BorderLayout.CENTER, d);

		c.setPreferredSize(new Dimension(100, 100));
		frame.getContentPane().add(BorderLayout.NORTH, c);

		c.add(start);
		c.add(stop);
//      c.add(updn);
//      c.add(ltrt);

		frame.setVisible(true);
		frame.setResizable(false);
		frame.setSize(300, 400);
		frame.setLocation(375, 55);
		start.addActionListener(this);
		stop.addActionListener(this);
//      updn.addActionListener(this);
//      ltrt.addActionListener(this);

		setupKeyBindings();

		t = new Timer(5, this);
		scoreTimer = new Timer(1000, e -> d.increaseScore()); // Increment score every second
		t.start();
		scoreTimer.start();

	}

	private void setupKeyBindings() {
		int condition = JComponent.WHEN_IN_FOCUSED_WINDOW;
		InputMap inputMap = frame.getRootPane().getInputMap(condition);
		ActionMap actionMap = frame.getRootPane().getActionMap();

		inputMap.put(KeyStroke.getKeyStroke("LEFT"), "left");
		actionMap.put("left", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				d.left = true;
				d.right = false;
			}
		});

		inputMap.put(KeyStroke.getKeyStroke("RIGHT"), "right");
		actionMap.put("right", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				d.right = true;
				d.left = false;
			}
		});

		inputMap.put(KeyStroke.getKeyStroke("UP"), "up");
		actionMap.put("up", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				d.up = true;
				d.down = false;
			}
		});

		inputMap.put(KeyStroke.getKeyStroke("DOWN"), "down");
		actionMap.put("down", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				d.down = true;
				d.up = false;
			}
		});
	}

	public void doit() {

		System.out.println("moving it");
		if (go) {
			d.moveIt();
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (e.getSource() instanceof Timer) {
			if (go) {
				doit();
			}
		} else if (e.getSource() instanceof JButton) {
			if (e.getSource() == t) {
				doit();
			} else if (e.getSource() instanceof JButton) {
				JButton clicked = (JButton) e.getSource();
				if (clicked == start) {
					go = true;
				} else if (clicked == stop) {
					go = false;
				}
			}

		}
	}

	public static void main(String[] args) {
		new Tester();
	}
}
