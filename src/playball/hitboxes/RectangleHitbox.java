package playball.hitboxes;

import playball.Direction;

public class RectangleHitbox extends Hitbox {
	private int left, right, top, bottom;
	
	/**
	 * 
	 * @param x - x position of hitbox
	 * @param y - y position of hitbox
	 * @param width - width of hitbox
	 * @param height - height of hitbox
	 */
	public RectangleHitbox(int x, int y, int width, int height) {
		super(x, y, width, height);
		
		left = x;
		right = x+width;
		top = y;
		bottom = y+height;
	}
	
	
	public int getLeft() {
		return left;
	}

	public int getRight() {
		return right;
	}

	public int getTop() {
		return top;
	}

	public int getBottom() {
		return bottom;
	}

	@Override
	public Direction checkCollision(Hitbox target) {
		
		// rectangle collision handling
		if (target instanceof RectangleHitbox) {
			RectangleHitbox t = ((RectangleHitbox) target);
			
			if (t.getRight() >= left && t.getLeft() <= right && t.getTop() <= bottom && t.getBottom() >= top) {
				// returns minimum distance side to determine bounce-off direction
				int leftDistance = Math.abs(t.getRight()-left);
				int rightDistance = Math.abs(t.getLeft()-right);
				int bottomDistance = Math.abs(t.getTop()-bottom);
				int topDistance = Math.abs(t.getBottom()-top);
				
				int min = Math.min(leftDistance, Math.min(rightDistance, Math.min(topDistance, bottomDistance)));
				
				if (leftDistance == min) {
					return new Direction(false, true, false, false);
				} else if (rightDistance == min) {
					return new Direction(true, false, false, false);
				} else if (topDistance == min) {
					return new Direction(false, false, false, true);
				} else if (bottomDistance == min) {
					return new Direction(false, false, true, false);
				}
			}
			
			return null;
		}
		
		// circle collision handling
		if (target instanceof CircleHitbox) {
			int radius = ((CircleHitbox) target).getRadius();
			int centerX = target.getCenterX();
			int centerY = target.getCenterY();
			Direction newDir = new Direction(false, false, false, false);
			
			int testX = centerX;
			int testY = centerY;
			
			if (centerX < left) {
				testX = left;
				newDir.setRight();
			} else if (centerX > right) {
				testX = right;
				newDir.setLeft();
			}
			
			if (centerY < top) {
				testY = top;
				newDir.setDown();
			} else if (centerY > bottom) {
				testY = bottom;
				newDir.setUp();
			}
			
			int distX = centerX-testX;
			int distY = centerY-testY;
			double distance = Math.sqrt((distX*distX) + (distY*distY));
			
			if (distance <= radius) {
				return newDir;
			}
			
			return null;
		}
		
		return null;
	}


	@Override
	protected void updateHitbox() {
		int x = super.x;
		int y = super.y;
		
		left = x;
		right = x + super.getWidth();
		top = y;
		bottom = y + super.getHeight();
	}

}
