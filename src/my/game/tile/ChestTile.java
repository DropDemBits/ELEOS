package my.game.tile;

import my.game.core.*;
import my.game.core.GuiManager.*;
import my.game.entity.player.*;
import my.game.render.*;
import my.game.world.*;

public class ChestTile extends TileContainer {

	boolean opened = false;
	int dir = 0;
	AnimatedSprite allSprites;
	public ChestTile(SpriteSheet referenceSheet) {
		super(null, "chest");
		allSprites = new AnimatedSprite(referenceSheet, 16, 13, 8);
		sprite = allSprites.getSprite();
	}
	
	@Override
	public boolean onClick(int x, int y, Level level, EntityPlayer player, int button) {
		ChestTE te = (ChestTE) level.getTileEntity(x, y, ChestTE.class);
		if(te == null) return true;
		if(button == 1) { 
			if (!te.isOpen) { GameCore.instance().getGuiMgr().openGui(x, y, player, GuiConstants.CHEST, te); return false; }
			else { GameCore.instance().getGuiMgr().closeRecentGui(); return false;}
		}
		return true;
	}
	
	@Override
	public boolean isOverlay() {
		return true;
	}
	
	@Override
	public boolean isInFront() {
		return false;
	}
	
	@Override
	public void render(int x, int y, Screen screen) {
		ChestTE te = (ChestTE) GameCore.instance().getClientPlayer().level.getTileEntity(x, y, ChestTE.class);
		if(te == null) return;
		allSprites.setFrame(te.isOpen?4+dir:dir);
		sprite = allSprites.getSprite();
		screen.renderTile(x*16, y*16, this);
	}
	
	@Override
	public TileEntity getTileEntity() {
		return new ChestTE();
	}

}
