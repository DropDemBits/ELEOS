package my.game.tile;

import gio.ddb.serial2.SEBlock;

public class TileEntity {

	public int xPos, yPos, layer;
	
	public TileEntity() {
		
	}
	
	//TODO: Add serialization stuff here
	public void writeData(SEBlock data) {
		data.setValue("xPos", xPos);
        data.setValue("yPos", xPos);
        data.setValue("type", getClass().getSimpleName());
	}
	
	public void readData(SEBlock data) {
		xPos = data.getInt("xPos");
		yPos = data.getInt("yPos");
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
