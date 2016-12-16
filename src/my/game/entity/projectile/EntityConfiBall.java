package my.game.entity.projectile;

import my.game.entity.mob.*;
import my.game.entity.particle.*;
import my.game.render.*;
import my.game.util.*;
import my.game.world.*;

public class EntityConfiBall extends EntityProjectile {

	public EntityConfiBall(Level level, int x, int y, double angle, EntityMob owner) {
		super(level, x, y, angle, owner);
		range = Integer.MAX_VALUE;
		dmg = 0.25;
		speed = 4;
		sprite = Sprite.rotate(Sprite.confiBall, angle);
		
		newX = speed * Math.cos(angle);
		newY = speed * Math.sin(angle);
	}
	
	public void update() {
		super.update();
		if(level.detectCollision((int)(x+newX), (int)(y+newY), 16, 16, 16)) {
			removeAnim();
			remove(); 
		}
		move();
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
		Utilities.playSound("confi_Spread.wav", -20);
		for(int i = 0; i < 128; i++) {
			Sprite s = new Sprite(2, 2, rand.nextInt());
			level.spawnParticle(new EntityParticle(level, (int)x, (int)y, rand.nextInt(300), s));
		}
		for(int i = 0; i < 1; i ++) {
			level.spawnEntity(new EntityConfiBall(level, (int)x, (int)y, 180-angle, owner));
		}
	}
	
	@Override
	public void render(Screen screen) {
		super.render(screen);
		screen.renderProjectile((int)x, (int)y, this);
	}

}
