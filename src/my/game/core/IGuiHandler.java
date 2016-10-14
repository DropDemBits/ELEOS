package my.game.core;

import my.game.entity.player.*;
import my.game.inventory.*;

/**
 * The interface used to implement custom Gui handlers
 * 
 * @version 0.0
 * @since build 0.0.0 alpha
 */
public interface IGuiHandler {
	
	public Object openGui(int x, int y, EntityPlayer player, int id, IInventory inv);

	public boolean getAppropriateGui(int id);

}
