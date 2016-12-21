package my.game.render;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import my.game.core.GameCore;
import my.game.util.Resource;

public class SpriteSheet {

	String location;
	/**Size of the spritesheet, linear*/
	final int SIZE;
	public final int spriteWidth, spriteHeight;
	private int width, height;
	private int[] pixels;
	Sprite[] sprites;
	
	public static SpriteSheet laser = new SpriteSheet("/textures/projectiles/LASERRRRRR.png", 16);
	public static SpriteSheet dumbo = new SpriteSheet(new SpriteSheet("/textures/mob.png", 32, 16), 0, 0, 2, 1, 16);
	public static SpriteSheet chase = new SpriteSheet(new SpriteSheet("/textures/chaser.png", 32, 8), 0, 0, 4, 1, 8);
	public static SpriteSheet blaser = new SpriteSheet(new SpriteSheet("/textures/mob/pewpew/IMAFIRINGMA.png", 22, 11), 0, 0, 2, 1, 11);
	
	public static SpriteSheet mainSheet = new SpriteSheet("/textures/spritesheets/spriteSheet.png", 256);
	public static SpriteSheet playerSheet = new SpriteSheet("/textures/playerSprite.png", 64);
	public static SpriteSheet fireParticle = new SpriteSheet("/textures/particles/fire.png", 6);
	
	public static SpriteSheet fireballSmall = new SpriteSheet("/textures/projectiles/fireball.png", 32);
	public static SpriteSheet confiBall = new SpriteSheet("/textures/projectiles/confiBall.png", 16);
	public static SpriteSheet fireballSmall2 = new SpriteSheet("/textures/projectiles/fireball2.png", 8);
	public static SpriteSheet arrow = new SpriteSheet("/textures/projectiles/arrow.png", 16, 16);
	
	public static SpriteSheet player2Sheet_Main = new SpriteSheet("/textures/playerSprite4.png", 64, 48);
	public static SpriteSheet player2Sheet_N = new SpriteSheet(player2Sheet_Main, 2, 0, 1, 3, 16);
	public static SpriteSheet player2Sheet_S = new SpriteSheet(player2Sheet_Main, 0, 0, 1, 3, 16);
	public static SpriteSheet player2Sheet_E = new SpriteSheet(player2Sheet_Main, 1, 0, 1, 3, 16);
	public static SpriteSheet player2Sheet_W = new SpriteSheet(player2Sheet_Main, 3, 0, 1, 3, 16);
	
	public static SpriteSheet chestSheet_Main = new SpriteSheet("/textures/spritesheets/chestSprites.png", 64, 26);
	public static SpriteSheet chestSheet_All = new SpriteSheet(chestSheet_Main, 0, 0, 4, 2, 16, 13);
	public static SpriteSheet items = new SpriteSheet("/textures/spritesheets/items.png", 24, 24);
	
	public SpriteSheet(SpriteSheet sheet, int x, int y, int width, int height, int spriteSize) {
		this(sheet, x, y, width, height, spriteSize, spriteSize);
	}
	
	public SpriteSheet(SpriteSheet sheet, int x, int y, int width, int height, int spriteSizeX, int spriteSizeY) {
		int xSprite = x*spriteSizeX;
		int ySprite = y*spriteSizeY;
		int w = width * spriteSizeX;
		int h = height * spriteSizeY;
		if (width == height) SIZE = w;
		else SIZE = -1;
		spriteWidth = w;
		spriteHeight = h;
		pixels = new int[w*h];
		for(int yScan = 0; yScan < h; yScan++) {
			int yPos = ySprite + yScan;
			for(int xScan = 0; xScan < w; xScan++) {
				int xPos = xSprite + xScan;
				pixels[xScan + yScan * w] = sheet.pixels[xPos + yPos * sheet.spriteWidth];
			}
		}
		//TP
		int frm = 0;
		sprites = new Sprite[width * height];
		for(int yTile = 0; yTile < height; yTile++) {
			for(int xTile = 0; xTile < width; xTile++) {
				int[] spritePixels = new int[spriteSizeX*spriteSizeY];
				//PP
				for(int yPixel = 0; yPixel < spriteSizeY; yPixel++) {
					for(int xPixel = 0; xPixel < spriteSizeX; xPixel++) {
						spritePixels[xPixel + yPixel * spriteSizeX] = pixels[(xPixel + xTile * spriteSizeX) + (yPixel + yTile * spriteSizeY) * spriteWidth];
					}
				}
				Sprite sprite = new Sprite(spritePixels, spriteSizeX, spriteSizeY);
				sprites[frm++] = sprite;
			}
		}
	}
	
	public SpriteSheet(String location, int width, int height) {
		this.location = location;
		SIZE = -1;
		spriteWidth = width;
		spriteHeight = height;
		init();
	}
	
	public SpriteSheet(String location, int size) {
		this.location = location;
		SIZE = size;
		spriteWidth = size;
		spriteHeight = size;
		pixels = new int[SIZE*SIZE];
		init();
	}
	
	public Sprite[] getSprites() {
		return sprites;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	private void init() {
		BufferedImage img = GameCore.instance().getTextureManager().get(new Resource("eleos", location));
		if(img == null) return;
		height = img.getHeight();
		width = img.getWidth();
		pixels = new int[this.width*this.height];
		img.getRGB(0, 0, width, height, pixels, 0, img.getWidth());
	}

	public int[] getPixels() {
		return pixels;
	}

	
}
