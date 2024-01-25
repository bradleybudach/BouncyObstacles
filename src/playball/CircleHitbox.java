package playball;

public class CircleHitbox extends Hitbox {
	private int radius;
	
	/**
	 * Defines a circle hitbox
	 * 
	 * @param x - x position of the hitbox
	 * @param y - y position of the hitbox
	 * @param diameter - diameter of the circle
	 * @param radius - radius of the circle
	 */
	public CircleHitbox(int x, int y, int diameter, int radius) {
		super(x, y, diameter, diameter);
		
		this.radius = radius;
	}

	public int getRadius() {
		return radius;
	}
	
	@Override
	public Direction checkCollision(Hitbox target) {
		if (target instanceof RectangleHitbox) {
			RectangleHitbox r = ((RectangleHitbox) target);
			int centerX = super.getCenterX();
			int centerY = super.getCenterY();
			Direction newDir = new Direction(false, false, false, false);
			
			int testX = centerX;
			int testY = centerY;
			
			if (centerX < r.getLeft()) {
				testX = r.getLeft();
				newDir.setLeft();
			} else if (centerX > r.getRight()) {
				testX = r.getRight();
				newDir.setRight();
			}
			
			if (centerY < r.getTop()) {
				testY = r.getTop();
				newDir.setUp();
			} else if (centerY > r.getBottom()) {
				testY = r.getBottom();
				newDir.setDown();
			}
			
			int distX = centerX-testX;
			int distY = centerY-testY;
			double distance = Math.sqrt((distX*distX) + (distY*distY));
			
			if (distance <= radius) {
				return newDir;
			}
			
			return null;
		}
		
		if (target instanceof CircleHitbox) {
			int distX = super.getCenterX()-target.getCenterX();
			int distY = super.getCenterY()-target.getCenterY();
			double distance = Math.sqrt((distX*distX) + (distY*distY));
			Direction newDir = new Direction(false, false, false, false);
			
			if (distX < 0) {
				newDir.setLeft();
			} else if (distX > 0) {
				newDir.setRight();
			}
			
			if (distY < 0) {
				newDir.setUp();
			} else if (distY > 0) {
				newDir.setDown();
			}
			
			if (distance <= radius+((CircleHitbox) target).getRadius()) {
				return newDir;
			}
		}
		
		return null;
	}

	@Override
	protected void updateHitbox() {} // no action needed here

}
