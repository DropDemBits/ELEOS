package my.game.render.ui;

import java.awt.Graphics;

import my.game.util.Vector2i;

import java.awt.Color;
import java.awt.Font;

public class UILabel extends UIComponent {

	public String lable;
	public Font font;
	public boolean dropShadow;
	public int drop = 0;
	
	
	public UILabel(Vector2i pos, String text) {
		super(pos);
		this.lable = text;
		font = new Font("Comic Sans", 0, 50);
		clr = 0xFFFFFF;
	}
	
	@Override
	public void update() {
		super.update();
	}
	
	public UILabel setFont(Font font) {
		this.font = font;
		return this;
	}
	
	public void setLabel(String label) {
		this.lable = label;
	}
	
	@Override
	public void render(Graphics g) {
		//font.drawString(screen, pos.getX() + offset.getX(), pos.getY() + offset.getY(), lable, false);
		g.setFont(font);
		if(dropShadow) {
			g.setColor(new Color(clr-0xEEEEEE));
			g.drawString(lable, pos.getX() + offset.getX()+drop, pos.getY() + offset.getY()+drop);
		}
		g.setColor(new Color(clr));
		g.drawString(lable, pos.getX() + offset.getX(), pos.getY() + offset.getY());
	}

}
