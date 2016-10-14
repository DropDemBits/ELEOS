package my.game.render.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import my.game.util.Vector2i;

public class UIPanel extends UIComponent {
	
	public Vector2i pos, size;
	private List<UIComponent> components = new ArrayList<UIComponent>();
	
	
	public UIPanel(Vector2i pos, Vector2i size) {
		super(pos);
		this.pos = pos;
		this.size = size;
		clr = 0x00FF00;
	}
	
	public UIPanel addComponent(UIComponent panel) {
		components.add(panel);
		return this;
	}
	
	public UIPanel removeComponent(UIComponent panel) {
		components.remove(panel);
		return this;
	}
	
	public void update() {
		for(UIComponent comp : components) {
			comp.setOffset(pos);
			comp.update();
		}
	}
	
	public void render(Graphics g) {
		int offset = 6;
		g.setColor(new Color(0x000000 | 0x99000000, true));
		g.fillRect(pos.getX()-offset, pos.getY()-offset, size.getX()+offset, size.getY()+offset);
		g.setColor(new Color(clr));
		g.fillRect(pos.getX(), pos.getY(), size.getX(), size.getY());
		g.setColor(new Color(0xFFFFFF));
		for(UIComponent comp : components) {
			comp.setOffset(pos);
			comp.render(g);
		}
	}
	
}
