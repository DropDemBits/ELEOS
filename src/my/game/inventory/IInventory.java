package my.game.inventory;

/**
 * The interface used for inventory related methods<br>
 * Must be olny implementd in TileEntity classes
 */
public interface IInventory {

	/**
	 * The number of slots in an inventory
	 * @return How big the inventory is
	 */
	public int getSize();
	
	public void onChanged();

	/**
	 * Called when the inventory is opened
	 */
	public void onOpen();
	
	/**
	 * Called when the inventory is closed
	 */
	public void onClose();
	
}
