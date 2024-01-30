package playball.powerups;

import javax.swing.ImageIcon;

import playball.GameController;
import playball.hitboxes.Hitbox;

import java.awt.Graphics;

public abstract class Powerup {
	ImageIcon icon;
	public int width;
	public int height;
	public int x;
	public int y;
	public boolean queueRemove = false;
	
	public Hitbox hitbox;
	
	public Powerup(int x, int y, int width, int height, Hitbox hitbox, ImageIcon icon) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.hitbox = hitbox;
		this.icon = icon;
	}
	
	public void setIcon(ImageIcon icon) {
		this.icon = icon;
	}
	
	public void draw(Graphics g) {
		g.drawImage(icon.getImage(), x, y, null);
	}
	
	public void checkPlayerCollision(GameController g) {
		if (hitbox.checkCollision(g.player.getHitbox()) != null) {
			activate(g);
			remove();
		}
	}
	
	public void remove() {
		queueRemove = true;
	}
	
	public abstract void activate(GameController g);
}