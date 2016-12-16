package my.game.render;

import java.util.Random;

public class Sprite {

	/**The linear size of the sprite*/
	public final int SIZE;
	private int width, height;
	/**The Sprite startX and startY*/
	int x, y;
	public int[] pixels;
	SpriteSheet parent;
	
	//Tiles
	public static Sprite air = new Sprite(16);
	public static Sprite grass = new Sprite(16, 0, 1, SpriteSheet.mainSheet);
	public static Sprite stone = new Sprite(16, 1, 0, SpriteSheet.mainSheet);
	public static Sprite dirt = new Sprite(16, 2, 0, SpriteSheet.mainSheet);
	public static Sprite sand = new Sprite(16, 3, 0, SpriteSheet.mainSheet);
	public static Sprite gravel = new Sprite(16, 4, 0, SpriteSheet.mainSheet);
	public static Sprite water = new Sprite(16, 5, 0, SpriteSheet.mainSheet);
	public static Sprite lava = new Sprite(16, 6, 0, SpriteSheet.mainSheet);
	public static Sprite flower = new Sprite(16, 7, 0, SpriteSheet.mainSheet);
	public static Sprite rocks = new Sprite(16, 8, 0, SpriteSheet.mainSheet);
	
	public static Sprite floorboards = new Sprite(16, 2, 1, SpriteSheet.mainSheet);
	public static Sprite walls = new Sprite(16, 1, 1, SpriteSheet.mainSheet);
	public static Sprite bricks = new Sprite(16, 1, 2, SpriteSheet.mainSheet);
	public static Sprite hedge = new Sprite(16, 2, 2, SpriteSheet.mainSheet);
	
	//Projectiles
	public static Sprite fireballSmall0 = new Sprite(16, 0, 0, SpriteSheet.fireballSmall);
	public static Sprite fireballSmall1 = new Sprite(16, 0, 1, SpriteSheet.fireballSmall);
	public static Sprite fireballSmall2 = new Sprite(16, 1, 0, SpriteSheet.fireballSmall);
	public static Sprite fireballSmall3 = new Sprite(16, 1, 1, SpriteSheet.fireballSmall);
	
	public static Sprite fireballSmallOther = new Sprite(8, 0, 0, SpriteSheet.fireballSmall2);
	public static Sprite arrow         = new Sprite(SpriteSheet.arrow.getPixels(), 16, 16);
	public static Sprite confiBall     = new Sprite(SpriteSheet.confiBall.getPixels(), 16, 16);
	
	//Particle
	public static Sprite fireParticle  = new Sprite(3, 0, 0, SpriteSheet.fireParticle);
	public static Sprite fireParticle1 = new Sprite(3, 1, 0, SpriteSheet.fireParticle);
	public static Sprite fireParticle2 = new Sprite(3, 1, 1, SpriteSheet.fireParticle);
	public static Sprite fireParticle3 = new Sprite(3, 0, 1, SpriteSheet.fireParticle);
	public static Sprite laser         = new Sprite(SpriteSheet.laser.getPixels(), 16, 16);
	public static Sprite dumboStill    = new Sprite(16, 0, 0, SpriteSheet.dumbo);
	
	//Items
	public static Sprite fireStaff     = new Sprite(8, 0, 0, SpriteSheet.items);
	public static Sprite woodenBow     = new Sprite(8, 1, 0, SpriteSheet.items);
	public static Sprite confiCannon   = new Sprite(8, 1, 1, SpriteSheet.items);
	public static Sprite swordBasic    = new Sprite(8, 0, 1, SpriteSheet.items);
	
	public static Sprite healthPotion  = new Sprite(8, 2, 0, SpriteSheet.items);
	public static Sprite expPotion     = new Sprite(8, 2, 1, SpriteSheet.items);
	
	public Sprite(int size) {
		SIZE = size;
		this.width = SIZE;
		this.height = SIZE;
		pixels = new int[SIZE*SIZE];
		setColour(new Random().nextInt(0xFFFFFF));
	}
	
	public Sprite(int width, int height, int clr) {
		SIZE = width;
		this.width = width;
		this.height = height;
		pixels = new int[width*height];
		setColour(clr);
	}
	
	
	public Sprite(int width, int height, SpriteSheet parent) {
		SIZE = width == height ? width : -1;
		this.parent = parent;
		this.width = width;
		this.height = height;
	}
	
	public Sprite(int size, int x, int y, SpriteSheet parent) {
		this(size, size, x, y, parent);
	}
	
	public Sprite(int width, int height, int x, int y, SpriteSheet parent) {
		SIZE = width==height?width:-1;
		this.width = width;
		this.height = height;
		pixels = new int[this.width*this.height];
		this.x = x*width;
		this.y = y*height;
		this.parent = parent;
		init();
	}
	
	public Sprite(int[] pixels, int width, int height) {
		SIZE = width == height ? width : -1;
		this.width = width;
		this.height = height;
		this.pixels = pixels;
	}
	
	public static Sprite rotate(Sprite sprite, double angle) {
		return new Sprite(rotate(sprite.pixels, sprite.width, sprite.height, angle), sprite.width, sprite.height);
	}
	
	private static int[] rotate(int[] pixels, int width, int height, double angle) {
		int[] result = new int[width * height];
		
		double newX_X = rotX(-angle, 1.0, 0.0);
		double newX_Y = rotY(-angle, 1.0, 0.0);
		double newY_X = rotX(-angle, 0.0, 1.0);
		double newY_Y = rotY(-angle, 0.0, 1.0);
		
		double x0 = rotX(-angle, -width/2.0, -height/2.0) + width/2.0;
		double y0 = rotY(-angle, -width/2.0, -height/2.0) + height/2.0;
		
		for(int y = 0; y < height; y++) {
			double x1 = x0;
			double y1 = y0;
			for(int x = 0; x < width; x++) {
				int xx = (int) x1;
				int yy = (int) y1;
				int clr = 0;
				if(xx < 0 || xx >= width || yy < 0 || yy >= height) clr = 0xFFFF00FF;
				else clr = pixels[xx + yy * width];
				result[x+y*width] = clr;
				x1+=newX_X;
				y1+=newX_Y;
			}
			x0+=newY_X;
			y0+=newY_Y;
		}
		
		return result;
	}
	
	//X
	private static double rotX(double angle, double x, double y) {
		double cos = Math.cos(angle - Math.PI / 2);
		double sin = Math.sin(angle - Math.PI / 2);
		return x*cos + y*-sin;
	}
	//Y
	private static double rotY(double angle, double x, double y) {
		double cos = Math.cos(angle - Math.PI / 2);
		double sin = Math.sin(angle - Math.PI / 2);
		return x*sin + y*cos;
	}
	
	public static Sprite[] split(SpriteSheet sheet) {
		int amount = (sheet.getWidth() * sheet.getHeight()) / (sheet.spriteWidth * sheet.spriteHeight);
		Sprite[] sprites = new Sprite[amount];
		int current = 0;
		
		for(int yPos = 0; yPos < sheet.getHeight()/ sheet.spriteHeight; yPos++) {
			for(int xPos = 0; xPos < sheet.getWidth()/ sheet.spriteWidth; xPos++) {
				int[] pixels = new int[sheet.spriteWidth * sheet.spriteHeight];
				for(int y = 0; y < sheet.spriteHeight; y++) {
					for(int x = 0; x < sheet.spriteWidth; x++) {
						int xOff = x + xPos * sheet.spriteWidth;
						int yOff = y + yPos * sheet.spriteHeight;
						pixels[x+y*sheet.spriteWidth] = sheet.getPixels()[xOff + yOff * sheet.getWidth()];
					}
				}
				sprites[current] = new Sprite(pixels, sheet.spriteWidth, sheet.spriteHeight);
				current++;
			}
		}
		
		return sprites;
	}

	public Sprite getSprite() {
		return this;
	}
	
	private void setColour(int clr) {
		for(int i = 0; i < pixels.length; i++) {
			pixels[i] = clr;
		}
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	
	
	private void init() {
		for(int y = 0; y < height; y++) {
			for(int x = 0; x < width; x++) {
				pixels[x+y*width] = parent.getPixels()[(x+this.x)+(y+this.y)*parent.spriteWidth];
			}
		}
	}

	public void makeTrueTransparancy() {
		for(int i = 0; i < pixels.length; i++) {
			if(pixels[i] == 0xFFFF00FF) pixels[i] = 0x00FFFFFF;
		}
	}

	public void makeFalseTransparancy() {
		for(int i = 0; i < pixels.length; i++) {
			if(pixels[i] == 0x00FFFFFF) pixels[i] = 0xFFFF00FF;
		}
	}
	
}
