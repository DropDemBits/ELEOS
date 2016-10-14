package my.game.render;

import java.util.Random;

import my.game.core.*;
import my.game.entity.mob.EntityChase;
import my.game.entity.mob.EntityMob;
import my.game.entity.mob.EntityStarboard;
import my.game.entity.mob.EntityTester;
import my.game.entity.projectile.EntityProjectile;
import my.game.tile.Tile;

public class Screen {

	public int width, height;
	public int[] pixels;
	
	//Map
	private int mapSize = 128;
	
	public int[] tiles = new int[mapSize * mapSize];
	
	public int xOffset, yOffset;
	private Random rand = new Random(System.nanoTime());
	
	public Screen(int width, int height) {
		this.width = width;
		this.height = height;
		pixels = new int[width * height];
		
		for(int i = 0; i < mapSize*mapSize; i++) {
			tiles[i] = rand.nextInt(0xffffff);
		}
	}
	
	public void renderTile(int xPos, int yPos, Tile tile) {
		xPos -= xOffset;
		yPos -= yOffset;
		for(int y = 0; y < tile.sprite.getHeight(); y++) {
			int yAbs = y + (yPos);
			for(int x = 0; x < tile.sprite.getWidth(); x++) {
				int xAbs = x + (xPos);
				if(yAbs < 0 || yAbs >= height || xAbs < -tile.sprite.getWidth()|| xAbs >= width) break;
				if(xAbs < 0) xAbs=0;
				int colour = tile.sprite.pixels[x+y*16];
				if(colour != 0xFFFF00FF) {
					colour = ColorUtil.changeBrightness(colour, GameCore.instance().getCurrentLevel().brightness+tile.getBrightness());
					pixels[xAbs+yAbs*width] = colour;
				}
				//if(x%16 == 15 || y%16 == 15) pixels[xAbs+yAbs*width] = 0xFFFFFFFF;
				//pixels[xAbs+yAbs*width] = tile.sprite.pixels[x+y*tile.sprite.SIZE];
			}
		}
	}
	
	public void renderMob(int xPos, int yPos, EntityMob mob) {
		xPos -= xOffset;
		yPos -= yOffset;
		for(int y = 0; y < mob.getSprite().getHeight(); y++) {
			int yAbs = y + (yPos);
			for(int x = 0; x < mob.getSprite().getWidth(); x++) {
				int xAbs = x + (xPos);
				if(yAbs < 0 || yAbs >= height || xAbs < -mob.getSprite().getWidth() || xAbs >= width) break;
				if(xAbs < 0) xAbs=0;
				int colour = mob.getSprite().pixels[x+y*mob.getSprite().getWidth()];
				
				if(colour == 0xFF727272 && mob instanceof EntityChase) colour = 0xFFA51300;
				if(colour == 0xFF727272 && mob instanceof EntityTester) colour = 0xFF0013A5;
				if(colour == 0xFF727272 && mob instanceof EntityStarboard) colour = 0xFFF6FF00;
				
				if(colour != 0xFFFF00FF) pixels[xAbs+yAbs*width] = colour;
			}
		}
	}
	
	public void renderProjectile(int xPos, int yPos, EntityProjectile projectile) {
		xPos -= xOffset;
		yPos -= yOffset;
		for(int y = 0; y < projectile.getSprite().getHeight(); y++) {
			int yAbs = y + (yPos);
			for(int x = 0; x < projectile.getSprite().getWidth(); x++) {
				int xAbs = x + (xPos);
				if(yAbs < 0 || yAbs >= height || xAbs < -projectile.getSprite().getWidth() || xAbs >= width) break;
				if(xAbs < 0) xAbs=0;
				int colour = projectile.getSprite().pixels[x+y*projectile.getSprite().getWidth()];
				if(colour != 0xFFFF00FF) pixels[xAbs+yAbs*width] = colour;
			}
		}
	}
	
	public void renderSprite(int xPos, int yPos, Sprite sprite, boolean fixed) {
		renderSprite(xPos, yPos, sprite, fixed, false);
	}
	
	public void renderSprite(int xPos, int yPos, Sprite sprite, boolean fixed, boolean flip) {
		renderSprite(xPos, yPos, sprite, 0x00FFFFFF, fixed, flip);
	}
	
	public void renderSprite(int xPos, int yPos, Sprite sprite, int clr, boolean fixed, boolean flip) {
		if(fixed) {
			xPos -= xOffset;
			yPos -= yOffset;
		}
		
		for(int y = 0; y < sprite.getHeight(); y++) {
			int yAbs = y + (yPos);
			for(int x = 0; x < sprite.getWidth(); x++) {
				int xAbs = !flip ? x + (xPos) : (xPos) + sprite.getWidth() - x;
				if(xAbs < 0 || xAbs >= width || yAbs < 0 || yAbs >= height) continue;
				int colour = sprite.pixels[x+y*sprite.getWidth()];
				if(colour != 0xFFFF00FF) pixels[xAbs+yAbs*width] = ColorUtil.blend(colour, clr, (clr >> (28)&0xFF));
			}
		}
		
	}
	
	public void renderSheet(int xPos, int yPos, SpriteSheet sprite, boolean fixed) {
		if(fixed) {
			xPos -= xOffset;
			yPos -= yOffset;
		}
		
		for(int y = 0; y < sprite.spriteHeight; y++) {
			int yAbs = y + (yPos);
			for(int x = 0; x < sprite.spriteWidth; x++) {
				int xAbs = x + (xPos);
				if(xAbs < 0 || xAbs >= width || yAbs < 0 || yAbs >= height) continue;
				int colour = sprite.getPixels()[x+y*sprite.spriteWidth];
				if(colour != 0xFFFF00FF) pixels[xAbs+yAbs*width] = colour;
			}
		}
		
	}
	
	public void setOffset(int xOffset, int yOffset) {
		this.xOffset = xOffset;
		this.yOffset = yOffset;
	}
	
	public void clear() {
		for(int i = 0; i < pixels.length; i++) {
			pixels[i] = 0;
		}
	}

	public void drawRect(int xPos, int yPos, int width, int height, int clr, boolean fixed) {
		if (fixed) {
			xPos -= xOffset;
			yPos -= yOffset;
		}

		for (int x = xPos; x <= xPos + width; x++) {
			if (x < 0 || x >= this.width || yPos >= this.height) continue;
			if (yPos > 0) pixels[x + yPos * this.width] = clr;
			if (yPos + height >= this.height) continue;
			if (yPos+height > 0) pixels[x + (yPos + height) * this.width] = clr;
		}
		for (int y = yPos; y < yPos + height; y++) {
			if (xPos >= this.width || y >= this.height || y < 0) continue;
			if (xPos > 0) pixels[xPos + y * this.width] = clr;
			if (xPos + width >= this.width) continue;
			if (xPos+width > 0) pixels[(xPos + width) + y * this.width] = clr;
		}
	}
	
	public void fillRect(int xPos, int yPos, int width, int height, int clr, boolean fixed) {
		if (fixed) {
			xPos -= xOffset;
			yPos -= yOffset;
		}
		
		for (int y = 0; y < height; y++) {
			int yAbs = y + yPos;
			if(yAbs < 0 || yAbs >= this.height) continue;
			for (int x = 0; x < width; x++) {
				int xAbs = x + xPos;
				if(xAbs < 0 || xAbs >= this.width) continue;
				pixels[xAbs+yAbs*this.width] = clr;
			}
		}
	}

	
}
