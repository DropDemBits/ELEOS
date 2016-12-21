package my.game.gui;

import java.awt.*;
import java.util.*;
import java.util.List;

import my.game.core.*;
import my.game.events.*;

public class Gui {
	
	private boolean isActive = false;
	private int id;
	private List<GuiButton> buttons = new ArrayList<GuiButton>();
	private boolean hasRequestedClose;
	
	public Gui() {
		GameCore.CORE_BUSSER.registerListener(this);
	}
	
	/**
	 * Adds a button
	 * 
	 * @param id The id of the button
	 * @param b The instance of the button
	 */
	public void addButton(int id , GuiButton b) {
		if(!buttons.contains(b)) buttons.add(id, b);
	}
	
	/**
	 * Removes a button
	 * 
	 * @param id The id of the button
	 */
	public void removeButton(int id) {
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
	
	/**
	 * Called when a button has been pressed and the mouse button is being held
	 * 
	 * @param id The id of the button
	 * @param x The x position of the mouse
	 * @param y The y position of the mouse
	 * @param guiButton The instance of the pressed button
	 */
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
	
	public int getID() {
		return id;
	}
	
	/**
	 * Requests to close a gui
	 */
	public void requestClose() {
		hasRequestedClose = true;
	}

	/**
	 * Checks to see wether this gui has requested a close
	 * 
	 * @return Returns true if the gui has requested a close
	 */
	public boolean hasRequestedClose() {
		return hasRequestedClose;
	}

	/**
	 * Checks to see whether this gui is focused
	 * 
	 * @return Returns ture if the gui is active
	 */
	public boolean isActive() {
		return isActive;
	}

	/**
	 * Sets this current gui to be active
	 */
	public void setActive() {
		isActive = true;
	}
	
	/**
	 * Sets this current gui to be inactive
	 */
	public void setInactive() {
		isActive = false;
	}
	
}
