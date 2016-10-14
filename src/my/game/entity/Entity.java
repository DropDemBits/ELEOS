package my.game.entity;

import java.util.*;

import my.game.render.*;
import my.game.util.*;
import my.game.world.*;

public abstract class Entity {

	public double x, y, xDir, yDir;
	protected boolean removed = false;
	public Level level;
	protected final Random rand = new Random();
	protected CollisionBox box;
	
	protected Entity(Level level) {
		this.level = level;
		box = new CollisionBox(0, 0, 16, 16);
	}
	
	public abstract void update();
	
	public void setMotion(double x, double y) {}
	public void setMotion(int x, int y) {setMotion((double)x, (double)y);}
	
	public void render(Screen screen) {
		if(Debug.DEBUG) {
			Debug.drawRect(screen, box.pos.getX(), box.pos.getY(), box.size.getX(), box.size.getY(), true);
			Font font = new Font();
			font.drawString(screen, (int)x/16, (int)y/16, this.getClass().getSimpleName(), false);
		}
	}
	
	public void remove() {
		//TODO: Remove from the level
		removed = true;
	}
	
	public boolean isRemoved() {
		return removed;
	}
	
	public CollisionBox getBox() {
		return box;
	}
	
}
