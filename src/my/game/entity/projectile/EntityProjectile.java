package my.game.entity.projectile;

import my.game.core.*;
import my.game.entity.*;
import my.game.entity.mob.*;
import my.game.network.*;
import my.game.render.*;
import my.game.util.*;
import my.game.world.*;

public class EntityProjectile extends Entity {

	final int xOrigin, yOrigin;
	double angle;
	protected double x, y;
	public Sprite sprite;
	double newX, newY;
	protected double distance;
	double speed, dmg, range;
	public EntityMob owner;
	
	public EntityProjectile(Level level, int x, int y, double angle, EntityMob owner) {
		super(level);
		this.xOrigin = x;
		this.yOrigin = y;
		this.x = x;
		this.y = y;
		this.angle = angle;
		this.owner = owner;
		box.setLocation(x, y);
	}

	@Override
	public void update() {
		for(Entity e : level.entities) {
			if(e instanceof EntityMob && !(e instanceof EntityItem)) {
				if(((EntityMob)e) != owner && e.getBox().intersectBoth(box) && !(owner.getClass().isAssignableFrom(e.getClass()))) {
					((EntityMob)e).damage(new DamageSource(dmg, this));
					if(((EntityMob) e).health <= 0) {
						owner.currentExp += ((EntityMob)e).expGiven;
					}
					removeAnim();
					remove();
				}
			}
		}
		
		for(EntityMob e : level.players) {
			if(e != owner && e.getBox().intersectBoth(box)) {
				if(e instanceof NetPlayer && owner == GameCore.instance().getClientPlayer() || owner instanceof NetPlayer) continue;
				e.damage(new DamageSource(dmg, this));
				removeAnim();
				remove();
			}
		}
		box.setLocation((int)x, (int)y);
	}
	
	protected void move() {
		
	}
	
	public Sprite getSprite() {
		return sprite;
	}

	public void removeAnim() {}

}
