package my.game.tile;

import my.game.render.*;

public class BaseTiles {

	public static Tile air = new BlankTile(Sprite.air, "air");
	public static Tile grass = new BackgroundTile(Sprite.grass, "grass");
	public static Tile stone = new StoneTile(Sprite.stone, "stone");
	public static Tile dirt = new BackgroundTile(Sprite.dirt, "dirt");
	public static Tile sand = new BackgroundTile(Sprite.sand, "sand");
	public static Tile gravel = new BackgroundTile(Sprite.gravel, "gravel");
	public static Tile water = new BackgroundTile(Sprite.water, "water");
	public static Tile lava = new BackgroundTile(Sprite.lava, "lava")/*.setBrightness(25)*/;
	public static Tile flower = new ForegroundTile(Sprite.flower, "flower");
	public static Tile rocks = new ForegroundTile(Sprite.rocks, "rocks");
	
	public static Tile chest = new ChestTile(SpriteSheet.chestSheet_All);

}
