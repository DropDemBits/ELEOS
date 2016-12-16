package my.game.core;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.*;

import my.game.entity.player.*;
import my.game.events.*;
import my.game.gui.*;
import my.game.inventory.*;

/**
 * The Main Gui Manager
 * 
 * @version 0.0
 * @since build 0.0.0 alpha
 */
public class GuiManager {

	private static List<GuiIngame> openGuis = new CopyOnWriteArrayList<>();
	private static List<IGuiHandler> guiHandlers = new ArrayList<>();
	private static int currentGuiID;
	
	public GuiManager() {
		registerGuiHandler(new VanillaGuiHandler());
	}
	
	public void registerGuiHandler(IGuiHandler guiHandler) {
		guiHandlers.add(guiHandler);
	}

	/**
	 * Unmodded set of gui constants
	 * Ranges from 0-1023
	 */
	//TODO: Make some method to add more GuiConstants
	public enum GuiConstants {
		CHEST,
	}
	
	/**
	 * A method used to open a gui
	 * @param x X position in the level (if &lt 0 it defaults to Mouse X)
	 * @param y Y position in the level (if &lt 0 it defaults to Mouse Y)
	 * @param player The client player
	 * @param id The enum id of the gui that will be opened (ie Chest = 0)
	 * @param inv An object that implements the <a>IInventory</a> interface
	 */
	public int openGui(int x, int y, EntityPlayer player, GuiConstants id, IInventory inv) {
		return openGui(x, y, player, id.ordinal(), inv);
	}

	/**
	 * A method used to open a gui
	 * @param x X position in the level (if &lt 0 it defaults to 0)
	 * @param y Y position in the level (if &lt 0 it defaults to 0)
	 * @param player The client player
	 * @param id The integer id of the gui that will be opened (ie Chest = 0)
	 * @param inv An object that implements the <a>IInventory</a> interface
	 */
	public int openGui(int x, int y, EntityPlayer player, int id, IInventory inv) {
		if(x < 0) x = 0;
		if(y < 0) y = 0;
		int screenX = MouseHandler.getMouseX()/*-24*2-8*/, screenY = MouseHandler.getMouseY()/*-8*2-8-32-15*/;
		x *= 16;
		y *= 16;
		
		GuiIngame g;
		currentGuiID++;
		switch(id) {
		case 0: g = (GuiIngame) new ChestGui(inv, player).setID(currentGuiID)/*.setScreenPosition(screenX, screenY).setOrigin(x, y)*/; break;
		default:
			g = (findGuiManager(id) != null ? (GuiIngame) findGuiManager(id).openGui(x, y, player, id, inv) : new ExampleGui(inv, player).setScreenPosition(screenX, screenY).setOrigin(x, y)); break;
		}
		
		g.setOrigin(x, y).setScreenPosition(screenX-80, screenY-15);
		
		openGuis.add(g);
		g.onOpen();
		return g.getID();
	}
	
	private IGuiHandler findGuiManager(int id) {
		for(IGuiHandler guiHandle : guiHandlers) {
			if(guiHandle.getAppropriateGui(id)) return guiHandle;
		}
		return null;
	}

	public void update() {
		for(Gui gui : openGuis) {
			gui.update();
			if(gui.hasRequestedClose()) closeGui(gui);
		}
	}
	
	public void render(Graphics g) {
		for(Gui gui : openGuis) {
			gui.render(g);
		}
	}
	
	/**
	 * Close all open GUIs
	 */
	public void closeAllGuis() {
		for(GuiIngame gui : openGuis) {
			gui.onClose();
		}
		openGuis.clear();
	}
	
	/**
	 * Close a GUI based on the instance of that GUI
	 * @param g The instance of the GUI
	 */
	public void closeGui(Gui g) {
		openGuis.remove(g);
	}
	
	/**
	 * Closes a GUI using the id
	 */
	public boolean closeGui(int id) {
		for(int i = 0; i < openGuis.size(); i++) {
			Gui g = openGuis.get(i);
			if(g.getID() == id) {
				if(g instanceof GuiIngame)
					((GuiIngame)g).onClose();
				openGuis.remove(g);
			}
		}
		return false;
	}
	
	/**
	 * Close the most recently opened GUI
	 */
	public void closeRecentGui() {
		if(openGuis.isEmpty()) return;
		GuiIngame g = openGuis.get(openGuis.size()-1);
		g.onClose();
		openGuis.remove(g);
	}
	
	/**
	 * Checks if the mouse is in a GUI
	 * @param x The X of the mouse
	 * @param y The Y of the mouse
	 * @return If true, the mouse is in a GUI
	 */
	public boolean mouseInGui(int x, int y) {
		for(Gui g : openGuis) {
			if(g.getClass() != GuiIngame.class) continue;
			GuiIngame gui = (GuiIngame) g;
			if(x >= gui.screenX && x <= gui.getWidth() + gui.screenX && y >= gui.screenY && y <= gui.getHeight() + gui.screenY) return true;
		}
		return false;
	}
	
	private static class VanillaGuiHandler implements IGuiHandler {

		@Override
		public Object openGui(int x, int y, EntityPlayer player, int id, IInventory inv) {
			return null;
		}

		@Override
		public boolean getAppropriateGui(int id) {
			if(id <= 1023 && id >= 0) return true;
			return false;
		}
		
	}
	
}
