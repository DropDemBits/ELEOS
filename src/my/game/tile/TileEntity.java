package my.game.tile;

import util.serialization.*;
import util.serialization.types.*;

public class TileEntity {

	public int xPos, yPos, layer;
	
	public TileEntity() {
		
	}
	
	//TODO: Add serialization stuff here
	public void writeData(SEObject data) {
		data.pushField(SEField.createField("xPos", DataType.INT, xPos));
		data.pushField(SEField.createField("yPos", DataType.INT, yPos));
		data.pushString(SEString.createString("type", getClass().getName()));
	}
	
	public void readData(SEObject data) {
		xPos = (int) data.pullField("xPos").getData();
		yPos = (int) data.pullField("yPos").getData();
	}
	//END OF TODO
	
	public void onRemoved() {}
	
	public TileEntity setPos(int x, int y, int layer) {
		this.xPos = x;
		this.yPos = y;
		this.layer = layer;
		return this;
	}
	
	public void update() {
		
	}
	
}
