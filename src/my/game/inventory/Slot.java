package my.game.inventory;

public class Slot {

	public final int slotNum, slotX, slotY, width, height;
	public int edgeColor, innerColor;
	
	protected Slot() {
		slotNum = slotX = slotY = width = height = 0;
	}
	
	public Slot(int slotNum, int slotX, int slotY) {
		this(slotNum, slotX, slotY, 38);
	}
	
	public Slot(int slotNum,int slotX, int slotY, int size) {
		this(slotNum, slotX, slotY, size, size);
	}
	
	public Slot(int slotNum,int slotX, int slotY, int width, int height) {
		this(slotNum, slotX, slotY, width, height, 0xab50f0, 0xab50f0+0x111111);
	}
	
	public Slot(int slotNum, int slotX, int slotY, int width, int height, int edgeColor, int innerColour) {
		this.slotNum = slotNum;
		this.slotX = slotX;
		this.slotY = slotY;
		this.width = width;
		this.height = height;
		this.edgeColor = edgeColor;
		this.innerColor = innerColour;
	}
	
	public boolean isItemAppropriate(ItemStack stack) {
		return true;
	}
	
}
