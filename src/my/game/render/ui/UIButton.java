package my.game.render.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import my.game.events.MouseHandler;
import my.game.util.Vector2i;

public class UIButton extends UIComponent {

	private UIButtonListener buttonListener;
	public UIEventListener eventListener;
	private Vector2i size;
	private UILabel label;
	private BufferedImage icon;
	
	private boolean inside = false;
	private boolean pressed = false;
	private boolean ignorePress = false;
	private boolean ignoreAction = false;

	public UIButton(Vector2i pos, Vector2i size) {
		super(pos);
		this.size = size;
		clr = 0x777777;
		buttonListener = new UIGenericButtonListener();
		
		eventListener = new UIEventListener() {public void preformAction(UIButton button) {}};
	}
	
	@Override
	public void update() {
		boolean mouseDown = MouseHandler.getMouseButton() == MouseEvent.BUTTON1;
		Rectangle bounds = new Rectangle(this.getAbsoulutePos().getX(), this.getAbsoulutePos().getY(), size.getX(), size.getY());
		
		if(bounds.contains(new Point(MouseHandler.getMouseX(), MouseHandler.getMouseY()))) {
			if(!inside) {
				if(mouseDown) ignorePress = true;
				else ignorePress = false;
				buttonListener.mouseEntered(this);
			}
			inside = true;
			if(!pressed && !ignorePress && mouseDown) {
				buttonListener.mousePressed(this);
				pressed = true;
			}else if(MouseHandler.getMouseButton() == MouseEvent.NOBUTTON) {
				if(pressed) {
					buttonListener.mouseReleased(this);
					pressed = false;
					if(!ignoreAction) eventListener.preformAction(this);
					else ignoreAction = false;
				}
				ignorePress = false;
			}
			
		}else {
			if(inside) {
				buttonListener.mouseExited(this);
				pressed = false;
			}
			inside = false;
		}
	}
	
	public UIButton setText(UILabel label) {
		this.label = label;
		return this;
	}
	
	@Override
	public void render(Graphics g) {
		if(icon != null) {
			g.drawImage(icon, pos.getX()+offset.getX(), pos.getY()+offset.getY(), size.getX(), size.getY(), null);
		}else {
			g.setColor(new Color(clr));
			g.fillRect(pos.getX()+offset.getX(), pos.getY()+offset.getY(), size.getX(), size.getY());
			
			if(label != null) {
				label.setOffset(new Vector2i(pos).add(offset));
				label.render(g);
			}
		}
	}
	
	public void setButtonListener(UIButtonListener buttonListener) {
		this.buttonListener = buttonListener;
	}

	public void setActionListener(UIEventListener eventListener) {
		this.eventListener = eventListener;
	}

	public Vector2i getSize() {
		return size;
	}

	public void preformAction() {
		eventListener.preformAction(this);
	}
	
	public void ignoreNextPress() {
		ignoreAction = true;
	}

	public UIButton setIcon(BufferedImage icon) {
		this.icon = icon;
		this.size = new Vector2i(icon.getWidth(), icon.getHeight());
		return this;
	}

}
