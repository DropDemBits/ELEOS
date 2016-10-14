package my.game.inventory;

public class Inventory {

	ItemStack[] items;
	
	public Inventory(int size) {
		items = new ItemStack[size];
	}
	
	public void setStack(int slot, ItemStack item) {
		if(slot < 0 || slot >= items.length) return;
		items[slot] = item;
		//return items[slot];
	}
	
	public ItemStack getStack(int slot) {
		if(slot < 0 || slot >= items.length) return null;
		return items[slot];
	}

	public int getSize() {
		return items.length;
	}

}
