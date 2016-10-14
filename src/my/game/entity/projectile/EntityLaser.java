package my.game.entity.projectile;

import my.game.entity.mob.EntityMob;
import my.game.render.Screen;
import my.game.render.Sprite;
import my.game.util.CollisionBox;
import my.game.world.Level;

public class EntityLaser extends EntityProjectile {

	public static final int fireRate = 1;
	int animTime = 0, intervals = 32;
	
	public EntityLaser(Level level, int x, int y, double angle, EntityMob owner) {
		super(level, x, y, angle, owner);
		range = 2000;
		dmg = 0.1;
		speed = 2;
		sprite = Sprite.rotate(Sprite.laser, angle);
		
		newX = speed * Math.cos(angle);
		newY = speed * Math.sin(angle);
		box = new CollisionBox(0, 0, 16, 1);
		box.setLocation(x, y);
	}
	
	public void update() {
		super.update();
		if(level.detectCollision((int)(x+newX), (int)(y+newY), 15, 6, 14, 1))
			remove();
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
