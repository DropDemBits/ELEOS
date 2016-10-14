package my.game.util;

public class Vector2i {

	private int x, y;
	
	public Vector2i(Vector2i base) {
		set(base.x, base.y);
	}
	
	public Vector2i(int x, int y) {
		set(x,y);
	}
	
	public Vector2i() {
		set(0,0);
	}
	
	public Vector2i set(int x, int y) {
		this.x = x;
		this.y = y;
		return this;
	}
	
	public Vector2i add(Vector2i v) {
		this.x += v.x;
		this.y += v.y;
		return this;
	}
	
	public void sub(Vector2i v) {
		this.x -= v.x;
		this.y -= v.y;
	}

	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}

	public Vector2i setX(int x) {
		this.x = x;
		return this;
	}

	public Vector2i setY(int y) {
		this.y = y;
		return this;
	}
	
	public static double distance(Vector2i one, Vector2i other) {
		int x = one.x-other.x;
		int y = one.y-other.y;
		return Math.sqrt(x*x + y*y);
	}
	
	@Override
	public boolean equals(Object object) {
		if(!(object instanceof Vector2i)) return false;
		Vector2i vec = (Vector2i) object;
		if(vec.getX() == this.getX() && vec.getY() == this.getY()) return true;
		return false;
	}

}
