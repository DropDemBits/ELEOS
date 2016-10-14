package my.game.tile;

import my.game.render.Screen;
import my.game.render.Sprite;

public class StoneTile extends Tile {

	public StoneTile(Sprite sprite, String name) {
		super(sprite, name);
	}
	
	@Override
	public void render(int x, int y, Screen screen) {
		screen.renderTile(x*16, y*16, this);
	}

}
