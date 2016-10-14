package my.game.world;

import java.awt.image.*;

import javax.imageio.*;

import my.game.entity.*;
import my.game.entity.EntitySpawner.*;
import my.game.entity.mob.*;
import my.game.tile.*;

public class CentralLevel extends Level {
	
	boolean init = false;
	
	public CentralLevel(String path) {
		super(path);
	}
	
	@Override
	protected void loadLevel(String path) {
		try {
			boolean hasOverlay = true;
			BufferedImage img = ImageIO.read(getClass().getResource(path+".png"));
			BufferedImage overlay = null;
			try {
				overlay = ImageIO.read(LoadedLevel.class.getResource(path+"_overlay.png"));
			}catch(Exception e) {
				hasOverlay = false;
			}
			int w = width = img.getWidth();
			int h = height = img.getHeight();
			tiles = new int[2][w*h];
			img.getRGB(0, 0, w, h, tiles[0], 0, w);
			tileList = new Tile[layers][w*h];
			for(int y = 0; y < height; y++) {	
				for(int x = 0; x < width; x++) {
					setTile(x, y, 0, getTileFrmRGB(x, y, tiles[0]));
				}
			}
			if(hasOverlay) {
				if(overlay != null) {
					if(overlay.getWidth() != width || height != overlay.getHeight()) throw new RuntimeException("Overlay is not the same dimensions as the level image");
					overlay.getRGB(0, 0, w, h, tiles[1], 0, w);
					for(int y = 0; y < height; y++) {
						for(int x = 0; x < width; x++) {
							setTile(x, y, 1, getTileFrmRGB(x, y, tiles[1]));
						}
					}
				}
			}
			init = true;
		} catch (Exception e) {
			System.err.println("ERR: Could Not Load Level...");
			e.printStackTrace();
		}
		EntitySpawner item = new EntitySpawner(this, 60*16, 60*16, Type.CHASER, 18, true);
		spawnEntity(item);
		EntityLaserBlaser blazor = new EntityLaserBlaser(this, (29*2+5)*16, (29*2+5)*16);
		spawnEntity(blazor);
		EntityStarboard star = new EntityStarboard(this, (29*2+6)*16, (29*2+6)*16);
		spawnEntity(star);
	}
	
	public Tile getTileFrmRGB(int x, int y, int[] tiles) {
		if(x < 0 || y < 0 || x >= width || y >= height) return BaseTiles.air;
		if(tiles[x+y*width] == 0xFFFFFFFF) return BaseTiles.air;
		if(tiles[x+y*width] == 0xFF00FF00) return BaseTiles.grass;
		if(tiles[x+y*width] == 0xFF7F3300) return SpawnTiles.floorboards;
		if(tiles[x+y*width] == 0xFF7F6A00) return BaseTiles.sand;
		if(tiles[x+y*width] == 0xFF999999) return BaseTiles.gravel;
		if(tiles[x+y*width] == 0xFF0000FF) return BaseTiles.water;
		if(tiles[x+y*width] == 0xFFFF4400) return BaseTiles.lava;
		if(tiles[x+y*width] == 0xFFFF0000) return SpawnTiles.bricks;
		if(tiles[x+y*width] == 0xFF555555) return SpawnTiles.walls;
		if(tiles[x+y*width] == 0xFFE5DD00) return BaseTiles.chest;
		return BaseTiles.air;
	}
	
	@Override
	public boolean setTile(int x, int y, int layer, Tile tile) {
		if(x < 0 || y < 0 || x >= width || y >= height || tile == null) return false;
		if(tile == BaseTiles.air)          tiles[layer][x+y*width] = 0xFFFFFFFF;
		if(tile == BaseTiles.grass)        tiles[layer][x+y*width] = 0xFF00FF00;
		if(tile == SpawnTiles.floorboards) tiles[layer][x+y*width] = 0xFF7F3300;
		if(tile == BaseTiles.sand)         tiles[layer][x+y*width] = 0xFF7F6A00;
		if(tile == BaseTiles.gravel)       tiles[layer][x+y*width] = 0xFF999999;
		if(tile == BaseTiles.water)        tiles[layer][x+y*width] = 0xFF0000FF;
		if(tile == BaseTiles.lava)         tiles[layer][x+y*width] = 0xFFFF4400;
		if(tile == SpawnTiles.bricks)      tiles[layer][x+y*width] = 0xFFFF0000;
		if(tile == SpawnTiles.walls)       tiles[layer][x+y*width] = 0xFF555555;
		if(tile == BaseTiles.chest)        tiles[layer][x+y*width] = 0xFFE5DD00;
		return super.setTile(x, y, layer, tile);
	}

}
