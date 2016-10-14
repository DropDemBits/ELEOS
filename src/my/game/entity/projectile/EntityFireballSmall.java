package my.game.entity.projectile;

import my.game.entity.mob.*;
import my.game.entity.particle.*;
import my.game.render.*;
import my.game.world.*;

public class EntityFireballSmall extends EntityProjectile {

	int animTime = 0, intervals = 32;
	private Sprite spriteOrg;
	public static final int fireRate = 30;
	
	public EntityFireballSmall(Level level, int x, int y, double angle, EntityMob owner, Sprite sprite2) {
		super(level, x, y, angle, owner);
		range = 200;
		dmg = 20;
		speed = 4;
		sprite = Sprite.rotate(sprite2, angle);
		spriteOrg = sprite2;
		
		newX = speed * Math.cos(angle);
		newY = speed * Math.sin(angle);
	}
	
	public void update() {
		super.update();
		if(level.detectCollision((int)(x+newX), (int)(y+newY), 5, 4, 7)) {
			removeAnim();
			remove(); 
		}
		move();
		animTime++;
		if(animTime == intervals) animTime = 0;
		if(calcDistance() >= range) remove();
	}
	
	private double calcDistance() {
		double dist = 0;
		dist = Math.sqrt(Math.abs(Math.pow(xOrigin-x, 2)) + Math.abs(Math.pow(yOrigin-y, 2)));
		return dist;
	}
	
	@Override
	protected void move() {
		x += newX;
		y += newY;
	}
	
	@Override
	public void removeAnim() {
		if(spriteOrg != Sprite.fireballSmall0) return;
		for(int i = 0; i < 5/2; i++) {
			level.spawnParticle(new EntityParticle(level, (int)x, (int)y, rand.nextInt(300), Sprite.fireParticle));
		}
	}
	
	@Override
	public void render(Screen screen) {
		super.render(screen);
		screen.renderProjectile((int)x, (int)y, this);
	}
	
	@Override
	public Sprite getSprite() {
		/*if(animTime <= intervals/4){return Sprite.fireballSmall1;}
		else if(animTime <= intervals/4*2 && animTime > intervals/4){return Sprite.fireballSmall2;}
		else if(animTime <= intervals/4*3 && animTime > intervals/4*2){return Sprite.fireballSmall3;}
		else if(animTime > intervals/4*3){return Sprite.fireballSmall0;}*/
		return super.getSprite();
	}

}
