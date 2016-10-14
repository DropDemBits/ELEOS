package my.game.tile;

import my.game.render.*;

public abstract class TileContainer extends Tile {

	public TileContainer(Sprite sprite, String name) {
		super(sprite, name);
	}
	
	public abstract TileEntity getTileEntity();

}
