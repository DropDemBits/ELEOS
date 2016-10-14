package my.game.world;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import my.game.tile.BaseTiles;
import my.game.tile.Tile;

public class LoadedLevel extends Level {
	
	public LoadedLevel(String path) {
		super(path);
	}
	
	@Override
	protected void loadLevel(String path) {
		try {
			BufferedImage img = ImageIO.read(LoadedLevel.class.getResource(path));
			int w = img.getWidth();
			int h = img.getHeight();
			width = w;
			height = h;
			tiles = new int[2][w*h];
			img.getRGB(0, 0, w, h, tiles[0], 0, w);
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Could Not Load Level...");
		}
	}

	/*
	 * Air:    0xFFFFFFFF
	 * Grass:  0xFF00FF00
	 * Stone:  0xFF777777
	 * Dirt:   0xFF7F3300
	 * Sand:   0xFF7F6A00
	 * Gravel: 0xFFBBBBBB
	 * Water:  0xFF0000FF
	 * Lava:   0xFFFF4400
	 * Flower: 0xFFFF0000
	 * Rocks:  0xFF555555
	 */
	protected void generateLevel() {}
	
	public Tile getTile(int x, int y) {
		if(x < 0 || y < 0 || x >= width || y >= height) return BaseTiles.air;
		//int tileId = rand.nextInt(10);
		if(tiles[0][x+y*width] == 0xFFFFFFFF) return BaseTiles.air;
		if(tiles[0][x+y*width] == 0xFF00FF00) return BaseTiles.grass;
		if(tiles[0][x+y*width] == 0xFF777777) return BaseTiles.stone;
		if(tiles[0][x+y*width] == 0xFF7F3300) return BaseTiles.gravel;
		if(tiles[0][x+y*width] == 0xFF7F6A00) return BaseTiles.sand;
		if(tiles[0][x+y*width] == 0xFF999999) return BaseTiles.gravel;
		if(tiles[0][x+y*width] == 0xFF0000FF) return BaseTiles.water;
		if(tiles[0][x+y*width] == 0xFFFF4400) return BaseTiles.lava;
		if(tiles[0][x+y*width] == 0xFFFF0000) return BaseTiles.flower;
		if(tiles[0][x+y*width] == 0xFF555555) return BaseTiles.rocks;
		return BaseTiles.air;
	}

}
