package my.game.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import my.game.core.GameCore;
import my.game.events.EventHandler;
import my.game.events.MouseEvent;
import my.game.inventory.IInventory;
import my.game.inventory.ItemStack;
import my.game.inventory.Slot;
import my.game.item.Item;
import my.game.render.Sprite;
import my.game.render.SpriteSheet;

public class GuiIngame extends Gui {

	protected IInventory inv;
	public int screenX, screenY;
	protected int startTX, startTY;
	protected int width, height;
	protected List<Slot> slots = new ArrayList<Slot>();
	private static BufferedImage bigBox;
	
	static {
		Sprite boxSprite = new Sprite(64, 0, 0, new SpriteSheet("/textures/spritesheets/bigBox.png", 64, 64));
		bigBox = new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB);
		bigBox.setRGB(0, 0, 64, 64, boxSprite.pixels, 0, 64);
	}
	
	/**
	 * Creates an inventory
	 * It is a good idea to initialize slots here (slot positions are already adjusted to the location on the screen)
	*/
	public GuiIngame(IInventory inv) {
		this.inv = inv;
	}
	
	//Mouse Handlers
	@EventHandler
	public void mouseClick(MouseEvent.MouseClicked e) {
		if(!isActive()) return;
		if(e.x >= screenX && e.x <= screenX+width && e.y >= screenY && e.y <= screenY+height) {
			System.out.println(GameCore.instance().getClientPlayer().currentItem);
			onMouseClicked(e.x, e.y, e.button);
			e.setCancelled();
		}
	}
	
	public void mousePressed(MouseEvent.MousePressed e) {
		if(!isActive()) return;
		if(e.x >= screenX && e.x <= screenX+width && e.y >= screenY && e.y <= screenY+height) {
			onMousePressed(e.x, e.y, e.button);
			e.setCancelled();
		}
	}
	
	public void mouseReleased(MouseEvent.MouseReleased e) {
		if(!isActive()) return;
		if(e.x >= screenX && e.x <= screenX+width && e.y >= screenY && e.y <= screenY+height) {
			onMouseReleased(e.x, e.y, e.button);
			e.setCancelled();
		}
	}
	
	@EventHandler
	public void mouseMoved(MouseEvent.MouseMoved e) {
		if(!isActive()) return;
		if(e.x >= screenX-80 && e.x <= screenX+width+80 && e.y >= screenY-80 && e.y <= screenY+height+80) {
			onMouseMoved(e.x, e.y, e.dragged);
			e.setCancelled();
		}
	}
	
	//GUI Usable methods
	protected void onMouseMoved(int mouseX, int mouseY, boolean dragged) {
		
	}
	
	protected void onMouseClicked(int mouseX, int mouseY, int button) {
		for(Slot slot : slots) {
			int slotWidth  = slot.slotX + slot.width  + screenX, 
				slotX      = slot.slotX + 0           + screenX, 
				slotHeight = slot.slotY + slot.height + screenY, 
				slotY      = slot.slotY + 0           + screenY;
			if(mouseX <= slotWidth && mouseX >= slotX && mouseY <= slotHeight && mouseY >= slotY) {
				GameCore.instance().getClientPlayer().currentItem = onSlotClicked(GameCore.instance().getClientPlayer().currentItem, slot.slotNum, button);
				break;
			}
		}
	}
	
	protected void onMousePressed(int mouseX, int mouseY, int button) {
		
	}
	
	protected void onMouseReleased(int mouseX, int mouseY, int button) {
		
	}
	
	public void drawInventorySlots(Graphics g, int width, int height, int xOff, int yOff, int slotSize) {
		for(Slot slot : slots) {
			int slotId = slot.slotNum;
			
			g.setColor(new Color(slot.innerColor));
			g.fillRect(xOff + screenX+(slot.slotX+2), yOff + screenY+(slot.slotY+2), slot.width, slot.height);
			if (inv.getStack(slotId) != null) {
				ItemStack stack = inv.getStack(slotId);
				Item item = stack.getItem();
				Sprite itemSprite = item.getSprite();
				
				itemSprite.makeTrueTransparancy();
				BufferedImage image = new BufferedImage(itemSprite.getWidth(), itemSprite.getHeight(), BufferedImage.TYPE_INT_ARGB);
				image.setRGB(0, 0, itemSprite.getWidth(), itemSprite.getHeight(), itemSprite.pixels, 0, itemSprite.getWidth());
				g.drawImage(image, xOff + screenX+(slot.slotX+2), yOff + screenY+(slot.slotY+2), slot.width, slot.height, null);
				itemSprite.makeFalseTransparancy();
				
				g.setColor(new Color(0xFFFFFF));
				g.setFont(new Font("Snake", 0, 10));
				String text = ""+stack.quantity;
				g.drawString(text, xOff + screenX+(slot.slotX+2)+(-4*text.length()+slot.width-6), yOff + screenY+(slot.slotY+2)+27);
			}
			g.setColor(new Color(slot.edgeColor));
			g.drawRect(xOff + screenX+(slot.slotX+2)-1, yOff + screenY+(slot.slotY+2)-1, slot.width, slot.height);
		}
	}
	
	public ItemStack onSlotClicked(ItemStack currentItem, int slotNum, int button) {
		return null;
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
	
	public void drawTexturedBox(Graphics g, int x, int y, int width, int height) {
		g.drawImage(bigBox, x, y, width, height, null);
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
			//onClose();
			//requestClose();
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
