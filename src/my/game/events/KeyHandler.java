package my.game.events;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

public class KeyHandler implements KeyListener {

	ArrayList<Integer> keysPressed = new ArrayList<Integer>();
	boolean finishedTyping = false;
	ArrayList<String> keyString = new ArrayList<String>();
	
	public boolean up,left,right,down,killAll,drop,toggleUi,special1,special2,heal;
	
	public void update() {
		up = keysPressed.contains(KeyEvent.VK_UP) || keysPressed.contains(KeyEvent.VK_W);
		left = keysPressed.contains(KeyEvent.VK_LEFT) || keysPressed.contains(KeyEvent.VK_A);
		right = keysPressed.contains(KeyEvent.VK_RIGHT) || keysPressed.contains(KeyEvent.VK_D);
		down = keysPressed.contains(KeyEvent.VK_DOWN) || keysPressed.contains(KeyEvent.VK_S);
		killAll = keysPressed.contains(KeyEvent.VK_K);
		drop = keysPressed.contains(KeyEvent.VK_Q);
		toggleUi = keysPressed.contains(KeyEvent.VK_E);
		special1 = keysPressed.contains(KeyEvent.VK_Z);
		special2 = keysPressed.contains(KeyEvent.VK_X);
		heal = keysPressed.contains(KeyEvent.VK_H);
	}
	
	@Override
	public void keyTyped(KeyEvent e) {
		/*if(e.getKeyCode() == KeyEvent.VK_ENTER && InputEvent.getModifiersExText(e.getModifiers()).contains(Toolkit.getProperty("AWT.control", "Ctrl"))) {
			finishedTyping = true;
			keyString.add(InputEvent.getModifiersExText(e.getModifiers()));
			e.consume();
		}
		
		keyString.add(""+e.getKeyChar());*/
	}
	
	public boolean isKeyPressed(int keycode) {
		return keysPressed.contains(keycode);
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() != KeyEvent.VK_UNDEFINED && !keysPressed.contains(e.getKeyCode())) {
			keysPressed.add(e.getKeyCode());
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode() != KeyEvent.VK_UNDEFINED && keysPressed.contains(e.getKeyCode())) {
			keysPressed.remove((Object)e.getKeyCode());
		}
	}

}
