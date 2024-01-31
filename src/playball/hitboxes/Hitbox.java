package playball.hitboxes;

import playball.Direction;

public abstract class Hitbox {
	public int x, y;
	private int width, height;
	 
	/**
	 * Creates a hitbox
	 * @param x - int x position
	 * @param y - int y position
	 * @param width - int width
	 * @param height - int height
	 */
	public Hitbox(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	/**
	 * @return the center x position of this hitbox
	 */
	public int getCenterX() {
		return x+(width/2);
	}
	
	/**
	 * @return the center y position of this hitbox
	 */
	public int getCenterY() {
		return y+(height/2);
	}
	
	
	/**
	 * @return width
	 */
	public int getWidth() {
		return width;
	}
	
	/**
	 * @return height
	 */
	public int getHeight() {
		return height;
	}
	
	/**
	 * Moves the hitbox to a new position
	 * @param x - x position
	 * @param y - y position
	 */
	public void updatePosition(int x, int y) {
		this.x = x;
		this.y = y;
		
		updateHitbox();
	}
	
	/**
	 * update hitbox width and height
	 * @param width 
	 * @param height
	 */
	public void updateSize(int width, int height) {
		this.width = width;
		this.height = height;
	}
	
	/**
	 * Implement collision detection for hitbox type
	 * @param target - target hitbox
	 * @return direction of the hit
	 */
	public abstract Direction checkCollision(Hitbox target);
	
	/**
	 * Implement additional hitbox update functionality
	 */
	protected abstract void updateHitbox();
}
