package my.game.entity.mob;

import my.game.entity.projectile.EntityLaser;
import my.game.render.AnimatedSprite;
import my.game.render.Screen;
import my.game.render.SpriteSheet;
import my.game.world.Level;

public class EntityLaserPulser extends EntityMob {

	AnimatedSprite current = new AnimatedSprite(SpriteSheet.blaser, 11, 11, 2);
	private int cooldown = 0;
	private int life;
	private boolean shooting;
	private double rad = 0;
	
	public EntityLaserPulser(Level level, int x, int y) {
		super(level, x, y);
		sprite = current.getSprite();
		expGiven = 9000;
	}
	
	public void update() {
		life++;
		if(life % ((rand.nextInt(5)+1)*60) == 0) {
			int x1 = 1-rand.nextInt(3);
			int y1 = 1-rand.nextInt(3);
			
			if(x1 == -1) xDir = -movementSpeed;
			else if (x1 == 1) xDir = movementSpeed;
			else xDir = 0;
			
			if(y1 == -1) yDir = -movementSpeed;
			else if (y1 == 1) yDir = movementSpeed;
			else yDir= 0;
			
			if(rand.nextInt(8) == 0) {
				xDir = 0;
				yDir = 0;
			}
		}
		move(xDir, yDir);
		
		if(cooldown < 0) {
			cooldown = 0;
			shooting = true;
		}
		if(shooting) {
			current.setFrame(0);
			if(rad < 2*Math.PI && rad > -(2*Math.PI)) {
				shoot(new EntityLaser(level, (int)x, (int)y, rad, this));
				rad += Math.PI/24;
			}else{
				rad = 0;
				shooting = false;
			}
		}else {
			current.setFrame(1);
			cooldown--;
		}
		
		sprite = current.getSprite();
	}
	
	@Override
	public void render(Screen screen) {
		super.render(screen);
		screen.renderMob((int)x, (int)y, this);
	}

}
