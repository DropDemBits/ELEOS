package my.game.gui;

import java.awt.*;

import my.game.core.GameCore;
import my.game.entity.player.*;
import my.game.inventory.*;
import my.game.util.Debug;

public class ChestGui extends GuiIngame {

	private int distance;
	private int lineX, lineY;
	private int mouseX, mouseY;
	
	public ChestGui(IInventory inv, EntityPlayer player) {
		super(inv);
		height = 48*2;
		width = 5*2+30*4;
		for(int y = 0; y < 2; y++) {
			for(int x = 0; x < 4; x++)
				slots.add(new Slot(x+y*4, 5+(28*x), 20+(28*y), 28, 28));
		}
	}
	
	@Override
	protected void onMousePressed(int mouseX, int mouseY, int button) {
		
	}
	
	@Override
	protected void onMouseReleased(int mouseX, int mouseY, int button) {
		
	}
	
	@Override
	protected void onMouseMoved(int mouseX, int mouseY, boolean dragged) {
		if(dragged) {
			if(mouseY < screenY+20) {
				//Move GUI
				screenX += mouseX-this.mouseX;
				screenY += mouseY-this.mouseY;
			}
		}
		this.mouseX = mouseX;
		this.mouseY = mouseY;
	}
	
	@Override
	public ItemStack onSlotClicked(ItemStack currentItem, int slot, int button) {
		ItemStack stack = inv.getStack(slot);
		System.out.printf("%d\n", slot);
		
		if(currentItem != null) {
			//Item in cursor
			if(stack != null) {
				//Stack in selected slot
				if(button == 1) {
					//Put in single item
					ItemStack singleItem = new ItemStack(currentItem.getItem(), 1);
					ItemStack resultStack = stack.combineStacks(singleItem);
					if(resultStack == null) {
						//Same stack, add Item
						//Decrement currentItem
						currentItem.quantity--;
						if(currentItem.quantity <= 0) currentItem = null; 
					} else {
						//Not the same stack, or slot stack is full
						if(singleItem == resultStack) {
							//Not the same stack, swap stacks
							ItemStack temp = stack;
							stack = currentItem;
							currentItem = temp;
						} else {
							//Slot stack full, so set remainder to currentItem
							currentItem = resultStack;
						}
					}
				} else if(button == 3) {
					//Put entire stack in
					ItemStack resultStack = stack.combineStacks(currentItem);
					if(resultStack == null) {
						//Same stack, combine stack
						//Set currentItem to null as process is done
						currentItem = null;
					} else {
						//Not the same stack, or slot stack is full
						if(currentItem == resultStack) {
							//Not the same stack, swap stacks
							ItemStack temp = stack;
							stack = currentItem;
							currentItem = temp;
						} else {
							//Slot stack full, so set remainder to currentItem
							currentItem = resultStack;
						}
					}
				}
			} else {
				//Stack not in the selected slot
				if(button == 1) {
					//Put single item
					ItemStack singleStack = new ItemStack(currentItem.getItem(), 1);
					stack = singleStack;
					currentItem.quantity--;
				} else if(button == 3) {
					//Put entire stack in
					stack = currentItem;
					currentItem = null;
				}
			}
		} else {
			//No item in cursor
			if(stack != null) {
				//Take item from slot
				if(button == 1) {
					//Take single item
					ItemStack singleStack = new ItemStack(stack.getItem(), 1);
					currentItem = singleStack;
					stack.quantity--;
				} else if(button == 3) {
					//Take entire stack
					currentItem = stack;
					stack = null;
				}
			} else {
				//Do nothing, no point in putting null in null
			}
		}
		if(stack != inv.getStack(slot)) {
			inv.setStack(slot, stack);
		}
		System.out.println(currentItem);
		System.out.println(stack);
		return currentItem;
	}
	
	@Override
	public void render(Graphics g) {
		super.render(g);
		
		g.setColor(new Color(0xEEFFFFFF, true));
		g.drawLine(lineX, lineY, lineX, screenY); //Y Line
		g.drawLine(lineX, screenY, screenX, screenY); //X Line
		
		g.setColor(new Color(0x33FFFFFF, true));
		drawTexturedBox(g, screenX, screenY, width, height);
		g.fillRoundRect(screenX, screenY, width, 10, 10, 10);
		g.setColor(Color.white);
		g.setFont(new Font("Dep", 0, 12));
		g.drawString("Chest | Distance: "+distance+"", screenX+2, screenY+10);
		
		if(Debug.DEBUG) {
			g.setColor(Color.cyan);
			g.drawLine(screenX, screenY, 0, screenY); //Y Line
			g.drawLine(screenX, screenY, screenX, 0); //X Line
			
			g.setColor(Color.red);
			g.drawLine(screenX, screenY, mouseX, screenY); //X Line
			g.drawLine(screenX, screenY, screenX, mouseY); //Y Line
			
			g.setColor(Color.green);
			g.drawLine(screenX+(screenX-mouseX), screenY, 0, screenY); //X Line
			g.drawLine(screenX, screenY+(screenY-mouseY), screenX, 0); //Y Line
		}
		
		drawInventorySlots(g, 4, 2, 0, 0, 28);
		//g.drawRect(screenX-20, screenY-20, width+40, height+40);
	}
	
	@Override
	public void update() {
		//super.update();
		//Calculate translation
		double playerX = (double)Math.floor(GameCore.instance().getClientPlayer().x);
		double playerY = (double)Math.floor(GameCore.instance().getClientPlayer().y);
		double distX = (double)Math.floor(GameCore.instance().getClientPlayer().x)/16 - startTX/16;
		double distY = (double)Math.floor(GameCore.instance().getClientPlayer().y)/16 - startTY/16;
		
		distance = (int)Math.sqrt(distX*distX+distY*distY);
		int i = 3;
		
		if(lineX*i - playerX*i != 0) {
			lineX = startTX*i - (int)playerX*i + GameCore.getWindowWidth()/2+16;
			//lineX = playerX;
		}
		if(lineY*i - playerY*i != 0) {
			lineY = startTY*i - (int)playerY*i + GameCore.getWindowHeight()/2+16;
			//lineY = playerY;
		}
		
	}
	
	@Override
	public void onOpen() {
		super.onOpen();
	}
	
	@Override
	public void onClose() {
		super.onClose();
	}

}
