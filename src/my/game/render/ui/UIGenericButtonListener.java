package my.game.render.ui;

public class UIGenericButtonListener implements UIButtonListener {

	public void mouseEntered(UIButton button) {
		button.setColor(0x999999);
	}

	public void mouseExited(UIButton button) {
		button.setColor(0x777777);
	}

	public void mousePressed(UIButton button) {
		button.setColor(0x555555);
		button.preformAction();
		button.ignoreNextPress();
	}

	public void mouseReleased(UIButton button) {
		button.setColor(0x999999);
	}

}
