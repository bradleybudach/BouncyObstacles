package playball;

public abstract class Hitbox {
	public int x, y;
	private int width, height;
	
	public Hitbox(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	public int getCenterX() {
		return x+(width/2);
	}
	
	public int getCenterY() {
		return y+(height/2);
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public void updatePosition(int x, int y) {
		this.x = x;
		this.y = y;
		
		updateHitbox();
	}
	
	public abstract Direction checkCollision(Hitbox target);
	protected abstract void updateHitbox();
}
