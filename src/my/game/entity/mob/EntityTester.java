package my.game.entity.mob;

import my.game.render.AnimatedSprite;
import my.game.render.Screen;
import my.game.render.SpriteSheet;
import my.game.world.Level;

public class EntityTester extends EntityMob {

	AnimatedSprite current = null;
	
	AnimatedSprite dumbo = new AnimatedSprite(SpriteSheet.dumbo, 16, 16, 2);
	int time = 0;
	
	public EntityTester(Level level, int x, int y) {
		super(level, x, y);
		current = dumbo;
		health = 10;
		expGiven = 5;
	}

	@Override
	public void update() {
		super.update();
		time++;
		if(time % ((rand.nextInt(5)+1)*60) == 0) {
			xDir = 1-rand.nextInt(3);
			yDir = 1-rand.nextInt(3);
			if(rand.nextInt(8) == 0) {
				xDir = 0;
				yDir = 0;
			}
		}
		current.setIntervals(8);
		current.update();
		
		move(xDir, yDir);
		
		sprite = current.getSprite();
	}
	
	@Override
	public void render(Screen screen) {
		super.render(screen);
		screen.renderMob((int)x, (int)y, this);
		moving = false;
	}


}
