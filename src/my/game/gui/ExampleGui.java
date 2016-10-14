package my.game.gui;

import java.awt.*;

import my.game.entity.player.*;
import my.game.inventory.*;

public class ExampleGui extends GuiIngame {
	
	public ExampleGui(IInventory inv, EntityPlayer player) {
		super(inv);
		width = 48*2;
		height = 16*2;
	}
	
	@Override
	public void update() {
		super.update();
	}
	
	@Override
	public void render(Graphics g) {
		super.render(g);
		g.setColor(Color.RED);
		g.fillRect(screenX, screenY, width, height);
	}
	
	@Override
	public void onOpen() {
		super.onOpen();
	}
	
	@Override
	public void onClose() {
		super.onClose();
	}

}
