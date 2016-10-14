package my.game.events;

public class MouseEvent extends Event {
	
	public static class MouseMoved extends MouseEvent {
		public  int x, y;
		public boolean dragged;
		public MouseMoved(int x, int y, boolean dragged) {
			this.x = x;
			this.y = y;
			this.dragged = dragged;
		}
	}
	
	public static class MouseClicked extends MouseEvent {
		public int x, y, button;
		public MouseClicked(int x, int y, int button) {
			this.x = x;
			this.y = y;
			this.button = button;
		}
	}
	
	public static class MousePressed extends MouseClicked {
		public MousePressed(int x, int y, int button) {
			super(x, y, button);
		}
	}
	
	public static class MouseReleased extends MouseClicked {
		public MouseReleased(int x, int y, int button) {
			super(x, y, button);
		}
	}
	
}
