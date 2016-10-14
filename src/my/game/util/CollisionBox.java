package my.game.util;

import java.awt.Rectangle;

public class CollisionBox {

	public Vector2i pos, size;
	
	public CollisionBox(int x, int y, int width, int height) {
		pos = new Vector2i(x, y);
		size = new Vector2i(width, height);
	}
	
	public void setLocation(int x, int y) {
		pos.set(x, y);
	}
	
	public void move(int dx, int dy) {
		pos.add(new Vector2i(dx, dy));
	}
	
	public void grow(int addWid, int addHei) {
		size.add(new Vector2i(addWid, addHei));
	}
	
	public boolean intersectX(CollisionBox otherBox, double dX) {
		return false;
	}
	
	public boolean intersectY(CollisionBox otherBox, double dY) {
		return false;
	}
	
	public boolean intersectBoth(CollisionBox otherBox) {
		Rectangle box1 = new Rectangle(otherBox.pos.getX(), otherBox.pos.getY(), otherBox.size.getX(), otherBox.size.getY());
		Rectangle box2 = new Rectangle(this.pos.getX(), this.pos.getY(), this.size.getX(), this.size.getY());
		return box1.intersects(box2);
	}

}
