package my.game.render.ui;

import java.awt.Color;
import java.awt.Graphics;

import my.game.util.Vector2i;

public class UIProgressBar extends UIComponent {

	private double progress;
	public int foregroundClr;
	public boolean countdown = false;
	private Vector2i size;
	
	public UIProgressBar(Vector2i pos, Vector2i size) {
		super(pos);
		this.size = size;
	}
	
	public void setProgress(double progress) {
		if (progress < 0.0 || progress > 1.0) throw new IllegalArgumentException(
				String.format("Progress must be within 0.0-1.0. Progress is %f", progress));
		
		this.progress = progress;
	}
	
	public double getProgress() {
		return progress;
	}
	
	@Override
	public void render(Graphics g) {
		g.setColor(new Color(clr));
		g.fillRect(pos.getX()+offset.getX(), pos.getY()+offset.getY(), size.getX(), size.getY());
		g.setColor(new Color(foregroundClr));
		g.fillRect(pos.getX()+offset.getX(), pos.getY()+offset.getY(), (int)(progress*size.getX()), size.getY());
	}

	public Vector2i getSize() {
		return size;
	}


}
