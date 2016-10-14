package my.game.tile;

import my.game.inventory.*;

public class ChestTE extends TileEntity implements IInventory {

	public boolean isOpen = false;
	
	@Override
	public void update() {
		
	}

	@Override
	public void onChanged() {
		System.out.println(isOpen);
	}

	@Override
	public void onOpen() {
		isOpen = true;
	}

	@Override
	public void onClose() {
		isOpen = false;
	}

	@Override
	public int getSize() {
		return 8;
	}
	
}
