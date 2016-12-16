package my.game.inventory;

import my.game.item.Item;

public class ItemStack {

	public int quantity;
	Item item;
	
	public ItemStack(Item item) {
		this(item, 1);
	}
	
	public ItemStack(Item item, int amount) {
		this.item = item;
		this.quantity = amount;
	}
	
	public ItemStack combineStacks(ItemStack srcStack) {
		if(srcStack == null) return null;
		if(getItem() != null && srcStack.getItem() != null) {
			if(srcStack.getItem() == getItem() && getItem().getMaxStackSize() > 1) {
				int totalQ = this.quantity + srcStack.quantity;
				if(totalQ > getItem().getMaxStackSize()) {
					this.quantity = getItem().getMaxStackSize();
					srcStack.quantity = 0;
					return new ItemStack(getItem(), totalQ-getItem().getMaxStackSize());
				}else {
					this.quantity = totalQ;
					srcStack.quantity = 0;
					return null;
				}
			}else return srcStack;
		}
		return null;
	}
	
	public ItemStack splitStack(int size) {
		if(quantity - size < 0) {
			size = quantity;
		}
		int newQuantity = quantity - size;
		ItemStack ret = new ItemStack(item, newQuantity);
		quantity -= size;
		if(quantity <= 0) {
			quantity = 0;
			item = null;
		}
		return ret;
	}
	
	public Item getItem() {
		return item;
	}
	
	@Override
	public String toString() {
		return "Type: " + item.getName() + ", Quantity: " + quantity;
	}

}
