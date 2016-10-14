package my.game.world;

public class TilePos {

	int x,y;
	private final int TILE_SIZE = 16;
	
	public TilePos(int x, int y) {
		super();
		this.x = x * TILE_SIZE;
		this.y = y * TILE_SIZE;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public int[] getCoordPair() {
		return new int[] {x, y};
	}
	
}
