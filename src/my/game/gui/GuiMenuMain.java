package my.game.gui;

import java.awt.*;
import java.awt.image.*;
import java.util.*;

import javax.imageio.*;

import my.game.core.*;
import my.game.core.GameCore.*;

public class GuiMenuMain extends Gui {

	private int xSelection = 1168-(GameCore.instance().getWidth()), ySelection = 1168-GameCore.instance().getHeight();
	private BufferedImage backgroundImage;
	private int xAdd = (new Random(System.currentTimeMillis()).nextInt(3)-1)*2, 
			yAdd = (new Random(System.currentTimeMillis()).nextInt(3)-1)*2;
	
	public GuiMenuMain() {
		super();
		
		try {
			backgroundImage = ImageIO.read(GameCore.class.getResourceAsStream("/textures/background.png"));
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		addButton(0, new GuiButton((int) GameCore.instance().getWidth()/2-(84*3/2), (int) (GameCore.instance().getHeight()/2-(16*3/2))+(16+2)*3*0, 84*3, 16*3, "Singleplayer", Color.lightGray.darker(), Color.white));
		addButton(1, new GuiButton((int) GameCore.instance().getWidth()/2-(84*3/2), (int) (GameCore.instance().getHeight()/2-(16*3/2))+(16+2)*3*1, 84*3, 16*3, "Multiplayer", Color.lightGray.darker(), Color.white));
		addButton(2, new GuiButton((int) GameCore.instance().getWidth()/2-(84*3/2), (int) (GameCore.instance().getHeight()/2-(16*3/2))+(16+2)*3*2, 84*3, 16*3, "Exit", Color.lightGray.darker(), Color.white));
	}
	
	@Override
	public void update() {
		if(xSelection+xAdd < backgroundImage.getWidth()-900 && xSelection+xAdd > 0) {
			xSelection+=xAdd;
		}else {
			xAdd = -xAdd;
		}
		
		if(ySelection+yAdd < backgroundImage.getHeight()-504 && ySelection+yAdd > 0) {
			ySelection+=yAdd;
		}else {
			yAdd = -yAdd;
		}
	}
	
	@Override
	public void onButtonPressed(int id, int x, int y, GuiButton guiButton) {
		switch(id) {
		case 0:
			GameCore.instance().currentState = GameState.GAMEPLAY;
			break;
		case 1:
			if(GameCore.DEV) GameCore.instance().currentState = GameState.MULTIPLAYER_SELECTION;
			break;
		case 2:
			Runtime.getRuntime().exit(0);
			break;
		}
	}

	@Override
	protected void renderBackground(Graphics g) {
		g.drawImage(backgroundImage, 0, 0, GameCore.instance().getWidth(), GameCore.instance().getHeight(), xSelection, ySelection, GameCore.instance().getWidth()+xSelection, GameCore.instance().getHeight()+ySelection, null);
	}
	
	@Override
	protected void renderForeground(Graphics g) {
		g.setColor(Color.black.brighter().brighter().brighter().brighter());
		g.setFont(new java.awt.Font("Default", java.awt.Font.BOLD, 50));
		g.drawString("ELEOS", GameCore.instance().getWidth()/2-(5*(17)), GameCore.instance().getHeight()/2-100);
		if(GameCore.DEV) g.drawString("Dev Version", 25, 50);
	}

}
