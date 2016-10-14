package my.game.render.ui;

import java.awt.Graphics;

import my.game.util.Vector2i;

public class UIComponent {
	
	public Vector2i pos, offset;
	public int backgroundClr;
	protected int clr;
	
	public UIComponent(Vector2i pos) {
		this.pos = pos;
		offset = new Vector2i();
	}
	
	public UIComponent setColor(int clr) {
		this.clr = clr;
		return this;
	}
	
	void setOffset(Vector2i vec) {
		this.offset = vec;
	}
	
	public void render(Graphics g) {
		
	}
	
	public void update() {}
	
	public Vector2i getAbsoulutePos() {
		return new Vector2i(pos).add(offset);
	}

}
