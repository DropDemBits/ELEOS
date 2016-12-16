package my.game.render.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import my.game.core.GameCore;
import my.game.entity.EntityItem;
import my.game.entity.player.EntityPlayer;
import my.game.events.EventHandler;
import my.game.events.MouseEvent;
import my.game.events.MouseHandler;
import my.game.inventory.Inventory;
import my.game.inventory.ItemStack;
import my.game.item.Item;
import my.game.render.Sprite;
import my.game.util.Vector2i;

/**
 * <p>This took way too long to make...
 * <br> 
 * <br> 
 * <br> 
 * <br> 
 * <br> 
 * <br> 
 * <br> 
 * <br> 
 * <br> 
 * <br>  
 * <br> 
 * <br> 
 * <br> 
 * <br> 
 * <br> 
 * <br> 
 * <br> 
 * <br> 
 * <br>  
 * <br> 
 * <br> 
 * <br> 
 * <br> 
 * <br> 
 * <br> 
 * <br> 
 * <br> 
 * <br>  
 * <br> 
 * <br> 
 * <br> 
 * <br> 
 * <br> 
 * <br> 
 * <br> 
 * <br> 
 * <br>  
 * <br> 
 * <br> 
 * <br> 
 * <br> 
 * <br> 
 * <br> 
 * <br> 
 * <br> 
 * <br>  
 * <br> 
 * <br> 
 * <br> 
 * <br> 
 * <br> 
 * <br> 
 * <br> 
 * <br> 
 * <br>  
 * <br> 
 * <br> 
 * <br> 
 * <br> 
 * <br> 
 * <br> 
 * <br> 
 * <br> 
 * <br> 
 * <p><b><i>WAY</i></b> too long...
 * <p>And it could of taken shorter to make</p>
 */

public class UIInventory extends UIComponent {

	Inventory inventory;
	EntityPlayer player;
	private ItemStack currentItem;
	
	public UIInventory(Vector2i pos, EntityPlayer player) {
		super(pos);
		this.player = player;
		this.inventory = player.playerInventory;
		GameCore.CORE_BUSSER.registerListener(this);
	}
	
	@Override
	public void update() {
		currentItem = player.currentItem;
		updateInventory();
	}
	
	@EventHandler
	public void mouseClicked(MouseEvent.MousePressed e) {
		if(e.x >= getAbsoulutePos().getX() && e.y >= getAbsoulutePos().getY() 
				&& e.x <= getAbsoulutePos().getX()+40*4 && e.y <= getAbsoulutePos().getY()+40) {
			int slot = (e.x-getAbsoulutePos().getX())/40;
			
			if (currentItem == null) {
				if (inventory.getStack(slot) != null) {
					ItemStack stack = inventory.getStack(slot);
					if (e.button == 1) {
						// multi
						if (currentItem != null) {
							inventory.setStack(slot, currentItem.combineStacks(stack));
						} else {
							currentItem = stack;
							inventory.setStack(slot, null);
						}
					} else if (e.button == 3) {
						// single
						if (currentItem != null) {
							inventory.setStack(slot, currentItem.combineStacks(stack));
						} else {
							currentItem = new ItemStack(inventory.getStack(slot).getItem(), 1);
							inventory.getStack(slot).quantity--;
						}
					}
				}
			}else {
				//Item in mouse
				if (inventory.getStack(slot) != null) {
					currentItem = inventory.getStack(slot).combineStacks(currentItem);
					if(currentItem != null) {
						ItemStack transfer = currentItem;
						currentItem = inventory.getStack(slot);
						inventory.setStack(slot, transfer);
					}
				}else {
					if(e.button == 1) {
						inventory.setStack(slot, currentItem);
						currentItem = null;
					}else if(e.button == 1) {
						ItemStack excess = inventory.getStack(slot).combineStacks(new ItemStack(currentItem.getItem(), 1));
						if(excess == null) currentItem.quantity--;
					}
				}
			}
			
		}else if(e.x >= getAbsoulutePos().getX() && e.y >= getAbsoulutePos().getY()+50 
				&& e.x <= getAbsoulutePos().getX()+40*4 && e.y <= getAbsoulutePos().getY()+40*3+10) {
			//116
			int row = (e.x-getAbsoulutePos().getX())/40;
			int coloum = (e.y-getAbsoulutePos().getY()+10)/40-1;
			int slot = row+coloum*4+4;
			
			if (currentItem == null) {
				if (inventory.getStack(slot) != null) {
					ItemStack stack = inventory.getStack(slot);
					if (e.button == 1) {
						// multi
						if (currentItem != null) {
							inventory.setStack(slot, currentItem.combineStacks(stack));
						} else {
							currentItem = stack;
							inventory.setStack(slot, null);
						}
					} else if (e.button == 3) {
						// single
						if (currentItem != null) {
							inventory.setStack(slot, currentItem.combineStacks(stack));
						} else {
							currentItem = new ItemStack(inventory.getStack(slot).getItem(), 1);
							inventory.getStack(slot).quantity--;
						}
					}
				}
			}else {
				//Item in mouse
				if (inventory.getStack(slot) != null) {
					currentItem = inventory.getStack(slot).combineStacks(currentItem);
					if(currentItem != null) {
						ItemStack transfer = currentItem;
						currentItem = inventory.getStack(slot);
						inventory.setStack(slot, transfer);
					}
				}else {
					if(e.button == 1) {
						inventory.setStack(slot, currentItem);
						currentItem = null;
					}else if(e.button == 1) {
						ItemStack excess = inventory.getStack(slot).combineStacks(new ItemStack(currentItem.getItem(), 1));
						if(excess == null) currentItem.quantity--;
					}
				}
			}
		}else if(currentItem != null) {
			/*if(GameCore.instance().getGuiMgr().mouseInGui(e.x, e.y)) return;
			if(e.button == 3) {
				//multi
				EntityItem item = new EntityItem(GameCore.instance().getClientPlayer().level,
						GameCore.instance().getClientPlayer().x, GameCore.instance().getClientPlayer().y, currentItem);
				GameCore.instance().getClientPlayer().level.spawnEntity(item);
				currentItem = null;
			}else if(e.button == 1) {
				//single
				EntityItem item = new EntityItem(GameCore.instance().getClientPlayer().level,
						GameCore.instance().getClientPlayer().x, GameCore.instance().getClientPlayer().y, new ItemStack(currentItem.getItem(), 1));
				GameCore.instance().getClientPlayer().level.spawnEntity(item);
				currentItem.quantity--;
				if(currentItem.quantity <= 0) {
					currentItem = null;
				}
			}*/
		}
		player.currentItem = currentItem;
	}
	
	@Override
	public void render(Graphics g) {
		for(int y = 0; y < 1; y++) {
			for(int x = 0; x < 4; x++) {
				g.setColor(new Color(0xab50f0));
				g.drawRect(offset.getX() + pos.getX()+(x*38+2)-1, offset.getY() + pos.getY()+(y*38+2)-1, 37, 37);
				if(player.currentSlot != x + y)
					g.setColor(new Color(0xab50f0+0x111111));
				else
					g.setColor(new Color(0xffae00));
				g.fillRect(offset.getX() + pos.getX()+(x*38+2), offset.getY() + pos.getY()+(y*38+2), 108/3, 108/3);
				if (inventory.getStack(x+y) != null) {
					ItemStack stack = inventory.getStack(x+y);
					Item item = stack.getItem();
					Sprite itemSprite = item.getSprite();
					
					itemSprite.makeTrueTransparancy();
					BufferedImage image = new BufferedImage(itemSprite.getWidth(), itemSprite.getHeight(), BufferedImage.TYPE_INT_ARGB);
					image.setRGB(0, 0, itemSprite.getWidth(), itemSprite.getHeight(), itemSprite.pixels, 0, itemSprite.getWidth());
					g.drawImage(image, offset.getX() + pos.getX()+(x*38+2), offset.getY() + pos.getY()+(y*38+2), 108/3, 108/3, null);
					itemSprite.makeFalseTransparancy();
					
					g.setColor(new Color(0xFFFFFF));
					g.setFont(new Font("Snake", 0, 10));
					String text = ""+stack.quantity;
					g.drawString(text, offset.getX() + pos.getX()+(x*38+2)+(-4*text.length()+34), offset.getY() + pos.getY()+(y*38+2)+28+7);
				}
			}
		}
		
		for(int y = 0; y < 2; y++) {
			for(int x = 0; x < 4; x++) {
				int off=50;
				int slot = x+y*4+4;
				g.setColor(new Color(0xab50f0));
				g.drawRect(offset.getX() + pos.getX()+(x*38+2)-1, off+offset.getY() + pos.getY()+(y*38+2)-1, 37, 37);
				g.setColor(new Color(0xab50f0+0x111111));
				g.fillRect(offset.getX() + pos.getX()+(x*38+2), off+offset.getY() + pos.getY()+(y*38+2), 108/3, 108/3);
				if (inventory.getStack(slot) != null) {
					ItemStack stack = inventory.getStack(slot);
					Item item = stack.getItem();
					Sprite itemSprite = item.getSprite();
					
					itemSprite.makeTrueTransparancy();
					BufferedImage image = new BufferedImage(itemSprite.getWidth(), itemSprite.getHeight(), BufferedImage.TYPE_INT_ARGB);
					image.setRGB(0, 0, itemSprite.getWidth(), itemSprite.getHeight(), itemSprite.pixels, 0, itemSprite.getWidth());
					g.drawImage(image, offset.getX() + pos.getX()+(x*38+2), off+offset.getY() + pos.getY()+(y*38+2), 108/3, 108/3, null);
					itemSprite.makeFalseTransparancy();
					
					g.setColor(new Color(0xFFFFFF));
					g.setFont(new Font("Snake", 0, 10));
					String text = ""+stack.quantity;
					g.drawString(text, offset.getX() + pos.getX()+(x*38+2)+(-4*text.length()+34), offset.getY() + pos.getY()+(y*38+2)+28+7+off);
				}
			}
		}
		
		if(currentItem != null) {
			ItemStack stack = currentItem;
			Item item = stack.getItem();
			Sprite itemSprite = item.getSprite();
			
			itemSprite.makeTrueTransparancy();
			BufferedImage image = new BufferedImage(itemSprite.getWidth(), itemSprite.getHeight(), BufferedImage.TYPE_INT_ARGB);
			image.setRGB(0, 0, itemSprite.getWidth(), itemSprite.getHeight(), itemSprite.pixels, 0, itemSprite.getWidth());
			g.drawImage(image, MouseHandler.getMouseX()-itemSprite.getWidth()/2, MouseHandler.getMouseY()-itemSprite.getHeight()/2, 108/3, 108/3, null);
			
			g.setColor(new Color(0xFFFFFF));
			g.setFont(new Font("Snake", 0, 10));
			String text = ""+stack.quantity;
			g.drawString(text, MouseHandler.getMouseX()-itemSprite.getWidth()/2+108/3, MouseHandler.getMouseY()-itemSprite.getHeight()/2+108/3);
			itemSprite.makeFalseTransparancy();
		}
	}

	private void updateInventory() {
		for(int i = 0; i < inventory.getSize(); i++) {
			if(inventory.getStack(i) != null) {
				if(inventory.getStack(i).quantity <= 0) {
					inventory.setStack(i, null);
				}
			}
		}
	}

}
