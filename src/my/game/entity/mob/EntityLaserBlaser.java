package my.game.entity.mob;

import java.util.List;

import my.game.entity.projectile.EntityLaser;
import my.game.render.AnimatedSprite;
import my.game.render.Screen;
import my.game.render.SpriteSheet;
import my.game.world.Level;

public class EntityLaserBlaser extends EntityMob {

	AnimatedSprite current = new AnimatedSprite(SpriteSheet.blaser, 11, 11, 2);
	int fireRate;
	EntityMob target;
	int selectionCooldown = 30;
	
	public EntityLaserBlaser(Level level, int x, int y) {
		super(level, x, y);
		sprite = current.getSprite();
		expGiven = 100;
	}
	
	int time1 = 0;
	int time = 0;
	double open = 0;
	public void update() {
		super.update();
		time++;
		if(time % ((rand.nextInt(5)+1)*60) == 0) {
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
		
		fireRate--;
		if(target != null && (target.isRemoved() || level.getDistance(this, target) >= 5*16)) {
			target = null;
			selectionCooldown = 30;
		}
		if(target == null && selectionCooldown <= 0) {
			List<EntityMob> ents = level.getEntitiesInRange(this, 5*16);
			EntityMob e = ents.get(rand.nextInt(ents.size()));
			if(e != this) {
				target = e;
			}
		}else{
			selectionCooldown--;
		}
		if(fireRate <= 0 && target != null) {
			double dx = target.x-x;
			double dy = target.y-y;
			double tan = Math.atan2(dy, dx);
			shoot(new EntityLaser(level, (int)x, (int)y, tan, this));
			/*for(double dir = 0; dir < Math.PI*2; dir+=0.05)
				shoot(new EntityLaser(level, (int)x, (int)y, dir, this));*/
			current.setFrame(0);
			time1+=60;
			fireRate = EntityLaser.fireRate;
		}else if(time1 > 0) { 
			current.setFrame(0);
			time1-=(EntityLaser.fireRate/2)+1;
		}
		else current.setFrame(1);
		if(time1 <= 0) time1 = 0;
		sprite = current.getSprite();
	}

	@Override
	public void render(Screen screen) {
		super.render(screen);
		screen.renderMob((int)x, (int)y, this);
	}

}
