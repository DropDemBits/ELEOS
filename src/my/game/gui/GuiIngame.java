package my.game.gui;

import my.game.core.*;
import my.game.events.*;
import my.game.inventory.*;

public class GuiIngame extends Gui {

	protected IInventory inv;
	protected int screenX, screenY;
	protected int startTX, startTY;
	protected int width, height;
	
	public GuiIngame(IInventory inv) {
		this.inv = inv;
	}
	
	@EventHandler
	public void onClick(MouseEvent.MouseClicked e) {
		if(e.x >= screenX && e.x <= screenX+width && e.y >= screenY && e.y <= screenY+height) {
			
		}
	}
	
	public GuiIngame setScreenPosition(int screenX, int screenY) {
		this.screenX = screenX;
		this.screenY = screenY;
		return this;
	}
	
	public GuiIngame setOrigin(int tileX, int tileY) {
		this.startTX = tileX;
		this.startTY = tileY;
		return this;
	}
	
	public void update() {
		//Calculate translation
		int playerX = (int)Math.floor(GameCore.instance().getClientPlayer().x);
		int playerY = (int)Math.floor(GameCore.instance().getClientPlayer().y);
		
		
		int i = 3;
		
		if(startTX*i - playerX*i != 0) {
			screenX += startTX*i - playerX*i;
			startTX = playerX;
		}
		if(startTY*i - playerY*i != 0) {
			screenY += startTY*i - playerY*i;
			startTY = playerY;
		}
		
		if(screenX < 0 || screenY < 0 || screenX > GameCore.getWindowWidth() || screenY > GameCore.getWindowHeight()) {
			onClose();
			requestClose();
		}
	}
	
	public void onOpen() {
		setActive();
		inv.onOpen();
	}
	
	public void onClose() {
		setInactive();
		inv.onClose();
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
}
