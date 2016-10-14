package my.game.tile;

import my.game.render.Screen;
import my.game.render.Sprite;

public class BlankTile extends Tile {

	public BlankTile(Sprite sprite, String name) {
		super(sprite, name);
	}
	
	@Override
	public boolean isSolid() {
		return true;
	}
	
	@Override
	public void render(int x, int y, Screen screen) {
		screen.renderTile(x*16, y*16, this);
	}

}
