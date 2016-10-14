package my.game.util;

import my.game.render.Screen;

public class Debug {

	public static final boolean DEBUG = false;

	private Debug() {}
	
	public static void drawRect(Screen screen, int xPos, int yPos, int width, int height, boolean fixed) {
		drawRect(screen, xPos, yPos, width, height, 0xF0FF0F, fixed);
	}
	
	public static void drawRect(Screen screen, int xPos, int yPos, int width, int height, int clr, boolean fixed) {
		screen.drawRect(xPos, yPos, width, height, clr, fixed);
	}

}
