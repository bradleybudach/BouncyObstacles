package playball;

import java.util.ArrayList;

public class Direction {
	public boolean left = false;
	public boolean right = false;
	public boolean up = false;
	public boolean down = false;
	
	public ArrayList<Dir> moveSteps = new ArrayList<Dir>(); // steps to move in this direction
	
	/**
	 * Sets up a defined direction with: left, right, up, down
	 */
	public Direction(boolean left, boolean right, boolean up, boolean down) {
		this.left = left;
		this.right = right;
		this.up = up;
		this.down = down;
		setMoveSteps();
	}
	
	
	private void setMoveSteps() {
		moveSteps.clear();
		
		if (left) {
			moveSteps.add(Dir.LEFT);
		} else if (right) {
			moveSteps.add(Dir.RIGHT);
		}
		
		if (up) {
			moveSteps.add(Dir.UP);
		} else if (down) {
			moveSteps.add(Dir.DOWN);
		}
	}
	
	/**
	 * Sets up a random direction
	 */
	public Direction() { // random direction
		while (!left && !right && !up && !down) {
			left = (Math.random() > 0.5) ? true : false;
			right = (left) ? false : (Math.random() > 0.5) ? true : false;
			up = (Math.random() > 0.5) ? true : false;
			down = (up) ? false : (Math.random() > 0.5) ? true : false;
		}
		
		setMoveSteps();
	}
	
	/**
	 * Combine direction d with this direction
	 * 
	 * @param d - the direction to be combined
	 */
	public void combineDirection(Direction d) {
		if (d.up) {
			setUp();
		} else if (d.down) {
			setDown();
		}
		
		if (d.right) {
			setRight();
		} else if (d.left) {
			setLeft();
		}
		
		setMoveSteps();
	}
	
	/**
	 * Change direction to up
	 */
	public void setUp() {
		up = true;
		down = false;
		setMoveSteps();
	}
	
	/**
	 * Change direction to down
	 */
	public void setDown() {
		up = false;
		down = true;
		setMoveSteps();
	}
	
	/**
	 * Change direction to left
	 */
	public void setLeft() {
		left = true;
		right = false;
		setMoveSteps();
	}
	
	/**
	 * Change direction to right
	 */
	public void setRight() {
		left = false;
		right = true;
		setMoveSteps();
	}
	
	/**
	 * Clear Direction
	 */
	public void setNone() {
		left = right = up = down = false;
		setMoveSteps();
	}
}

enum Dir {
	LEFT,
	RIGHT,
	UP,
	DOWN
}