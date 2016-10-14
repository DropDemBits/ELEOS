package my.game.entity;

import my.game.entity.mob.*;
import my.game.entity.player.*;
import my.game.inventory.*;
//import my.game.network.*;
import my.game.render.*;
import my.game.util.*;
import my.game.world.*;

public class EntityItem extends EntityMob {

	int lifespan = 0;
	int maxLifespan = 6000;
	double zDir = 1.0;
	int pickupDelay = 60;
	ItemStack item;
	
	public EntityItem(Level level, double x, double y, ItemStack item) {
		super(level, (int)x, (int)y);
		this.x = x;
		this.y = y;
		this.item = item;
		this.sprite = item.getItem().getSprite();
		box = new CollisionBox((int)this.x, (int)this.y, sprite.getWidth(), sprite.getHeight());
		expGiven = 0;
	}
	
	boolean flip = false;
	int time = 0;
	@Override
	public void update() {
		super.update();
		if(xDir < 0) {
			xDir+=0.1;
		}else if(xDir > 0) {
			xDir-=0.1;
		}else{
			xDir=0;
		}
		lifespan++;
		time++;
			if(flip) zDir-=0.5;
			else zDir+=0.5;
		
		if(zDir >= 5 || zDir <= 0) flip = !flip;
		
		for(EntityMob player : level.players) {
			if(player.getBox().intersectBoth(box)) {
				if(pickupDelay <= 0) {
					for(int i = 4; i < 12; i++) {
						if (player instanceof EntityPlayer) {
							ItemStack stack = ((EntityPlayer) player).playerInventory.getStack(i);
							if (stack != null && stack.getItem() == item.getItem() && stack.quantity + item.quantity <= stack.getItem().getMaxStackSize()) {
								stack.quantity += item.quantity;
								if(stack.quantity > stack.getItem().getMaxStackSize())
								
								((EntityPlayer) player).playerInventory.setStack(i, stack);
								remove();
								break;
							}else if (stack == null) {
								((EntityPlayer) player).playerInventory.setStack(i, item);
								remove();
								break;
							}
						}
						/*else if (player instanceof NetPlayer
								&& ((NetPlayer) player).inv.getStack(i) == null) {
							((NetPlayer) player).inv.setStack(i, item);
							break;
						}*/
					}
				}
				else pickupDelay--;
			}else pickupDelay = 60;
		}
		if(lifespan >= maxLifespan) remove();
	}

	@Override
	public void render(Screen screen) {
		screen.renderMob((int)x, (int)y+(int)zDir, this);
	}

}
