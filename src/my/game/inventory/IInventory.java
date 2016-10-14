package my.game.inventory;

public interface IInventory {

	public int getSize();
	
	public void onChanged();

	public void onOpen();
	
	public void onClose();
	
}
