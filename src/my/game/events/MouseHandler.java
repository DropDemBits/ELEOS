package my.game.events;

import java.awt.event.MouseMotionListener;

import javax.swing.event.MouseInputListener;

import my.game.core.GameCore;

public class MouseHandler implements MouseMotionListener, MouseInputListener {

	private static int mouseX = -1, mouseY = -1, mouseButton = -1, mouseEntered = -1;
	
	public static int getMouseX() {
		return mouseX;
	}

	public static int getMouseY() {
		return mouseY;
	}

	public static int getMouseButton() {
		return mouseButton;
	}
	
	public static boolean hasMouseEntered() {
		return mouseEntered == 1;
	}

	@Override
	public void mouseClicked(java.awt.event.MouseEvent e) {
		GameCore.CORE_BUSSER.postToAllListeners(new MouseEvent.MouseClicked(e.getX(), e.getY(), e.getButton()));
	}

	@Override
	public void mousePressed(java.awt.event.MouseEvent e) {
		mouseButton = e.getButton();
		GameCore.CORE_BUSSER.postToAllListeners(new MouseEvent.MousePressed(e.getX(), e.getY(), e.getButton()));
	}

	@Override
	public void mouseReleased(java.awt.event.MouseEvent e) {
		mouseButton = java.awt.event.MouseEvent.NOBUTTON;
		GameCore.CORE_BUSSER.postToAllListeners(new MouseEvent.MouseReleased(e.getX(), e.getY(), java.awt.event.MouseEvent.NOBUTTON));
	}

	@Override
	public void mouseEntered(java.awt.event.MouseEvent e) {
		mouseEntered = 1;
	}

	@Override
	public void mouseExited(java.awt.event.MouseEvent e) {
		mouseEntered = 0;
	}

	@Override
	public void mouseDragged(java.awt.event.MouseEvent e) {
		mouseX = e.getX();
		mouseY = e.getY();
		GameCore.CORE_BUSSER.postToAllListeners(new MouseEvent.MouseMoved(e.getX(), e.getY(), true));
	}

	@Override
	public void mouseMoved(java.awt.event.MouseEvent e) {
		mouseX = e.getX();
		mouseY = e.getY();
		GameCore.CORE_BUSSER.postToAllListeners(new MouseEvent.MouseMoved(e.getX(), e.getY(), false));
	}

}
