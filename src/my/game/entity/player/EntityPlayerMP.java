package my.game.entity.player;

import my.game.core.*;
import my.game.events.*;
import my.game.world.*;
import util.serialization.*;
import util.serialization.types.*;

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
		SEDatabase db = new SEDatabase("Packet");
		SEObject data = new SEObject("data");
		SEString name = SEString.createString("name", "C1");
		SEField xMove = SEField.createField("xMove", DataType.DOUBLE, xDir);
		SEField yMove = SEField.createField("yMove", DataType.DOUBLE, yDir);
		SEField xPos = SEField.createField("xPos", DataType.DOUBLE, x);
		SEField yPos = SEField.createField("yPos", DataType.DOUBLE, y);
		
		data.pushString(name);
		data.pushField(xMove);
		data.pushField(yMove);
		data.pushField(xPos);
		data.pushField(yPos);
		
		db.pushObject(data);
		ConnectionManager.INSTANCE.send(db);
	}

	public void confirmMovement(SEDatabase db) {
		SEObject data = db.pullObject("data");
		double xPos = (Double)data.pullField("xPos").getData(), yPos = (Double)data.pullField("yPos").getData();
		if(this.x != xPos) this.x = xPos;
		if(this.y != yPos) this.y = yPos;
	}

}
