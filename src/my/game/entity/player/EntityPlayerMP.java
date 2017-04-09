package my.game.entity.player;

import gio.ddb.serial2.SEBlock;
import my.game.core.*;
import my.game.events.*;
import my.game.world.*;

public class EntityPlayerMP extends EntityPlayer {

	public EntityPlayerMP(Level currentLevel, String name, int x, int y, KeyHandler input) throws Exception {
		super(currentLevel, name, x, y, input);
	}
	
	public EntityPlayerMP(EntityPlayer base) throws Exception {
		this(base.level, base.name, (int)base.x, (int)base.y, base.input);
		this.playerInventory = base.playerInventory;
	}

	@Override
	public void move(double xDir, double yDir) {
		super.move(xDir, yDir);
		SEBlock data = new SEBlock();
		data.setValue("name", "C1");
		data.setValue("xMove", xDir);
		data.setValue("yMove", yDir);
		data.setValue("xPos", x);
		data.setValue("yPos", y);
        
		ConnectionManager.INSTANCE.send(data);
	}

	public void confirmMovement(SEBlock block) {
        double xPos = block.getDouble("xPos"), yPos = block.getDouble("yPos");
		if(this.x != xPos) this.x = xPos;
		if(this.y != yPos) this.y = yPos;
	}

}
