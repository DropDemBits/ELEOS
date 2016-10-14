package my.game.gui;

import java.awt.*;

public class GuiButton {
	int x, y, width, height;
	String text;
	Color buttonColor, textColor;
	
	public GuiButton(int x, int y, int width, int height, String text, Color buttonColor, Color textColor) {
		super();
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.text = text;
		this.buttonColor = buttonColor;
		this.textColor = textColor;
	}

	public boolean isButtonPressed(int mouseX, int mouseY) {
		return mouseX >= x && mouseX <= x+width && mouseY >= y && mouseY <= y+height;
	}
	
	public void drawButton(Graphics g) {
		g.setColor(buttonColor);
		g.fillRect(x, y, width, height);
		
		g.setColor(textColor);
		g.setFont(new java.awt.Font("Default", java.awt.Font.BOLD, 30));
		g.drawString(text, x+(width/8), y+(int)(height/1.5d));
	}
	
	public static interface IGuiButtonAction {
		public void doAction();
	}
}
