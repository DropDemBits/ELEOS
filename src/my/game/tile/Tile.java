package my.game.tile;

import my.game.entity.player.*;
import my.game.render.*;
import my.game.world.*;

public class Tile {

	public int x,y;
	private int brightness = 0;
	private int opacity = 0;
	public Sprite sprite;
	private String name;
	private boolean isBreakable;
	
	public Tile(Sprite sprite, String name) {
		this.sprite = sprite;
		this.name = name;
	}
	
	public void render(int x, int y, Screen screen) {}
	
	public boolean isSolid() {
		return false;
	}
	
	public boolean onClick(int x, int y, Level level, EntityPlayer player, int button) {return false;}

	public boolean isOverlay() {
		return false;
	}
	
	public boolean isInFront() {
		return false;
	}
	
	public boolean isBreakable() {
		return isBreakable;
	}
	
	/**
	 * Max 100, min -100
	 */
	public int getBrightness() {
		return brightness;
	}
	
	public Tile setBrightness(int level) {
		if(level < -100) level = -100;
		if(level > 100) level = 100;
		brightness = level;
		return this;
	}
	
	/**
	 * Max 100, min 0
	 */
	public int getOpacity() {
		return opacity;
	}
	
	public Tile setOpacity(int level) {
		if(level < -100) level = -100;
		if(level > 100) level = 100;
		opacity = level;
		return this;
	}

	public Tile setBreakable(boolean isBreakable) {
		this.isBreakable = isBreakable;
		return this;
	}
	
	@Override
	public String toString() {
		return name;
	}
	
}
