package my.game.network;

import my.game.core.*;
import my.game.entity.mob.*;
import my.game.inventory.*;
import my.game.world.*;

public class NetPlayer extends EntityMob {

	public Inventory inv;
	
	public NetPlayer(Level level, int x, int y) {
		super(level, x, y);
		box.setLocation(x, y);
	}
	
	@Override
	public void update() {
		this.x = GameCore.instance().getClientPlayer().x;
		this.y = GameCore.instance().getClientPlayer().y;
		this.inv = GameCore.instance().getClientPlayer().playerInventory;
		//TODO:(PROPERLY) SYNC CLIENT INVENTORY WITH SERVER INVENTORY
		for(int i = 0; i < 12; i++) {
			this.inv.setStack(i, GameCore.instance().getClientPlayer().playerInventory.getStack(i));
		}
		box.setLocation((int)x, (int)y);
	}
	

}
