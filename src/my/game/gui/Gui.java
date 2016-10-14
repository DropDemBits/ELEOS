package my.game.gui;

import java.awt.*;
import java.util.*;
import java.util.List;

import my.game.core.*;
import my.game.events.*;

public class Gui {
	
	private boolean isActive = false;
	private int id;
	private List<GuiButton> buttons = new ArrayList<>();
	private boolean hasRequestedClose;
	
	public Gui() {
		GameCore.CORE_BUSSER.registerListener(this);
	}
	
	public void addButton(int id , GuiButton b) {
		if(!buttons.contains(b)) buttons.add(id, b);
	}
	
	public void addButton(int id) {
		if(buttons.get(id) != null) buttons.remove(id);
	}
	
	@EventHandler
	public void onClick(MouseEvent.MouseClicked e) {
		if(!isActive()) return;
		for(int id = 0; id < buttons.size(); id++) {
			if(buttons.get(id).isButtonPressed(e.x, e.y)) {
				onButtonPressed(id, e.x, e.y, buttons.get(id));
			}
		}
	}
	
	public void keyPressed() {
		
	}
	
	public void onButtonPressed(int id, int x, int y, GuiButton guiButton) {}

	public void update() {}
	
	public void render(Graphics g) {
		renderBackground(g);
		for(GuiButton button : buttons) {
			button.drawButton(g);
		}
		renderForeground(g);
	}
	
	protected void renderForeground(Graphics g) {}

	protected void renderBackground(Graphics g) {}

	public Gui setID(int id) {
		 this.id = id;
		 return this;
	}
	
	public void requestClose() {
		hasRequestedClose = true;
	}

	public boolean hasRequestedClose() {
		return hasRequestedClose;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive() {
		isActive = true;
	}
	
	public void setInactive() {
		isActive = false;
	}
	
}
