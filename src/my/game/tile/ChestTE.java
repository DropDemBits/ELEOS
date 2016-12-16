package my.game.tile;

import my.game.core.Items;
import my.game.inventory.*;

public class ChestTE extends TileEntity implements IInventory {

	public boolean isOpen = false;
	public int ourGuiID = 0;
	public Inventory inventory = new Inventory(8);
	private boolean init_main = false;
	
	public ChestTE() {
		
	}
	
	@Override
	public void update() {
		for(int slot = 0; slot < inventory.getSize(); slot++) {
			if(inventory.getStack(slot) != null && inventory.getStack(slot).quantity <= 0)
				inventory.setStack(slot, null);
		}
		if(init_main) return;
		inventory.setStack(0, new ItemStack(Items.fireStaff, 64));
		inventory.setStack(1, new ItemStack(Items.woodenBow, 64));
		inventory.setStack(2, new ItemStack(Items.swordBasic, 64));
		inventory.setStack(3, new ItemStack(Items.confiCannon, 64));
		inventory.setStack(4, new ItemStack(Items.healthPotion, 64));
		inventory.setStack(5, new ItemStack(Items.expPotion, 64));
		init_main = true;
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

	@Override
	public ItemStack getStack(int slot) {
		return inventory.getStack(slot);
	}

	@Override
	public void setStack(int slot, ItemStack item) {
		inventory.setStack(slot, item);
	}
	
}
