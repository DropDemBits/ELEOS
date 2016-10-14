package my.game.tile;

import my.game.render.Sprite;

public class SpawnTiles {

	public static Tile floorboards = new BackgroundTile(Sprite.floorboards, "floorboards");
	public static Tile walls = new ForegroundTile(Sprite.walls, "walls");
	public static Tile bricks = new ForegroundTile(Sprite.bricks, "bricks");
	public static Tile hedge = new ForegroundTile(Sprite.hedge, "hedge").setBreakable(true);

}
