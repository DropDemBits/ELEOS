package my.game.render;

public class AnimatedSprite extends Sprite {
	
	private int frame = 0;
	int time = 0;
	private int intervals = 0;
	private int length = -1;
	Sprite currentSprite;
	
	public AnimatedSprite(SpriteSheet sheet, int width, int height, int animLength) {
		super(width, height, sheet);
		if(length > sheet.getSprites().length) return;
		currentSprite = parent.getSprites()[0];
		length = animLength;
	}
	
	public void update() {
		time++;
		if(time % intervals == 0) {
			if(frame >= length-1) frame = 0;
			else frame++;
			currentSprite = parent.getSprites()[frame];
		}
	}
	
	public void setFrame(int index) {
		//if(index > parent.getSprites().length) return;
		currentSprite = parent.getSprites()[index%length];
	}
	
	public Sprite getSprite() {
		return currentSprite;
	}
	
	public void setIntervals(int inter) {
		this.intervals = inter;
	}

	public int getCurrentFrame() {
		return frame;
	}

}
