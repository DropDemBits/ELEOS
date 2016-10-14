package my.game.world;

import java.util.Random;

import my.game.tile.BaseTiles;
import my.game.tile.Tile;

public class RandomLevel extends Level {

	static final Random rand = new Random(System.nanoTime());
	
	public RandomLevel(int width, int height) {
		super(width, height);
	}

	@Override
	protected void generateLevel() {
		for(int y = 0; y < height; y++) {
			for(int x = 0; x < width; x++) {
				tiles[0][x+y*width] = rand.nextInt(9);
			}
		}
	}
	
	public Tile getTile(int x, int y) {
		if(x < 0 || y < 0 || x >= width || y >= height) return BaseTiles.air;
		int tileId = tiles[0][x+y*width];
		switch(tileId) {
		case 0:
			return BaseTiles.grass;
		case 1:
			return BaseTiles.stone;
		case 2:
			return BaseTiles.dirt;
		case 3:
			return BaseTiles.sand;
		case 4:
			return BaseTiles.gravel;
		case 5:
			return BaseTiles.water;
		case 6:
			return BaseTiles.lava;
		case 7:
			return BaseTiles.flower;
		case 8:
			return BaseTiles.rocks;
		default:
			return BaseTiles.air;
		}
	}
	
}
