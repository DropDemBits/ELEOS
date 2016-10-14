package my.game.render.ui;

public interface UIButtonListener {
	
	public default void mouseEntered(UIButton button) {
		button.setColor(0x999999);
	}
	
	public default void mouseExited(UIButton button) {
		button.setColor(0x777777);
	}
	
	
	public default void mousePressed(UIButton button) {
		button.setColor(0x555555);
		button.preformAction();
		button.ignoreNextPress();
	}
	
	public default void mouseReleased(UIButton button){
		button.setColor(0x999999);
	}
	
}
