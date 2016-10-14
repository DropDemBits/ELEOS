package my.game.entity.mob;

import my.game.entity.Entity;
import my.game.entity.player.EntityPlayer;
import my.game.render.AnimatedSprite;
import my.game.render.Screen;
import my.game.render.SpriteSheet;
import my.game.world.Level;

public class EntityChase extends EntityMob {

	Entity target;
	AnimatedSprite current = null;
	
	AnimatedSprite dumbo = new AnimatedSprite(SpriteSheet.chase, 8, 8, 4);
	AnimatedSprite dumbo2 = new AnimatedSprite(SpriteSheet.dumbo, 16, 16, 2);
	int time = 0;
	
	public EntityChase(Level level, int x, int y) {
		super(level, x, y);
		current = dumbo2;
		current.setIntervals(6);
		movementSpeed = 0.9;
		expGiven = 10;
	}

	@Override
	public void update() {
		super.update();
		time++;
		if(target != null && target.isRemoved()) target = null;
		if(target == null) {
			for(Entity e : level.getEntitiesInRange(this, 32)) {
				if(e instanceof EntityPlayer) target = (Entity) e;
			}
		}
		if(time % ((rand.nextInt(5)+1)*60) == 0 && target == null) {
			xDir = 1-rand.nextInt(3);
			yDir = 1-rand.nextInt(3);
			if(rand.nextInt(8) == 0) {
				xDir = 0;
				yDir = 0;
			}
		}else if(target != null) {
			xDir = (target.x-x>0 ? movementSpeed : target.x-x<0 ? -movementSpeed : 0);
			yDir = (target.y-y>0 ? movementSpeed : target.y-y<0 ? -movementSpeed : 0);
		}
		
		//if(rand/*.nextBoolean()*/.nextInt(10) == 0) move((1-rand.nextInt(3))*4, (1-rand.nextInt(3))*4);
		move(xDir, yDir);
		
		if(moving) current.update();
		else current.setFrame(0);
		sprite = current.getSprite();
		//if(xDir == 0 && yDir == 0) remove();
		target = null;
	}
	
	@Override
	public void render(Screen screen) {
		super.render(screen);
		screen.renderMob((int)x, (int)y, this);
		moving = false;
	}


}
